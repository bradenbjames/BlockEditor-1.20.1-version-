package com.blockeditor.mod.network;

import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CreateBlockPacket {

    private final String hexColor;
    private final String mimicBlockId;
    private final String blockType;
    private final String customName;

    public CreateBlockPacket(String hexColor, String mimicBlockId, String blockType, String customName) {
        this.hexColor = hexColor;
        this.mimicBlockId = mimicBlockId;
        this.blockType = blockType;
        this.customName = customName;
    }

    public static void encode(CreateBlockPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.hexColor);
        buf.writeUtf(packet.mimicBlockId);
        buf.writeUtf(packet.blockType);
        buf.writeUtf(packet.customName);
    }

    public static CreateBlockPacket decode(FriendlyByteBuf buf) {
        return new CreateBlockPacket(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(CreateBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            // Get the user block registry
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Determine block type from the block ID
            ResourceLocation blockId = ResourceLocation.tryParse(packet.blockType);
            if (blockId == null) {
                return;
            }

            String blockType = getString(blockId);

            // Parse and normalize color (used if creating a new mapping)
            String normalizedHex = normalizeHex(packet.hexColor);
            int color;
            try {
                color = Integer.parseInt(normalizedHex, 16);
            } catch (NumberFormatException e) {
                return;
            }
            
            // Clean up custom name for internal registry/aliases only
            String cleanCustomName = packet.customName.toLowerCase()
                .replaceAll("[^a-z0-9_]", "_")
                .replaceAll("_{2,}", "_")
                .replaceAll("^_|_$", "");

            // If a non-empty custom name already exists, REUSE it instead of creating a new mapping
            String internalId = null;
            String effectiveName = cleanCustomName;
            boolean reusedExisting = false;
            if (!cleanCustomName.isBlank()) {
                String existing = registry.getInternalIdentifier(cleanCustomName);
                if (existing != null) {
                    internalId = existing; // reuse
                    reusedExisting = true;
                }
            }

            if (internalId == null) {
                if (cleanCustomName.isEmpty()) {
                    cleanCustomName = "block"; // fallback name
                }
                // Try to assign the custom block internally
                internalId = registry.assignUserBlockWithCustomName(blockType, color, packet.mimicBlockId, cleanCustomName);
                effectiveName = cleanCustomName;
                if (internalId == null) {
                    // If duplicate name, auto-increment deterministically: base, base_2, base_3, ...
                    if (registry.getAllCustomNames().contains(cleanCustomName)) {
                        String base = cleanCustomName.replaceAll("_(?:\\d+)$", "");
                        java.util.Set<String> names = registry.getAllCustomNames();
                        int n = 2;
                        String candidate = base + "_" + n;
                        // Find next available suffix
                        while (names.contains(candidate) && n < 1000) {
                            n++;
                            candidate = base + "_" + n;
                        }
                        // Try assigning with the new name
                        String autoId = registry.assignUserBlockWithCustomName(blockType, color, packet.mimicBlockId, candidate);
                        if (autoId == null) {
                            player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§cError: No more slots available for " + blockType + " blocks!"),
                                false
                            );
                            return;
                        } else {
                            internalId = autoId;
                            effectiveName = candidate;
                            player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal("§eName in use. Using '" + effectiveName + "'"),
                                false
                            );
                        }
                    } else {
                        // No more slots available
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("§cError: No more slots available for " + blockType + " blocks!"),
                            false
                        );
                        return;
                    }
                }
            } else {
                // We are reusing an existing mapping. Keep effectiveName as cleanCustomName.
                effectiveName = cleanCustomName;
            }
            
            // Update WorldEdit integration with the custom block mapping (idempotent if already mapped)
            try {
                com.blockeditor.mod.integration.WorldEditIntegration.updateCustomBlockMapping(effectiveName, internalId);
            } catch (Exception e) {
                System.out.println("[BlockEditor] WorldEdit mapping update error: " + e.getMessage());
            }
            
            // Get the actual user block from ModBlocks
            Block userBlock = getUserBlockByIdentifier(internalId);
            if (userBlock == null) {
                return;
            }

            // === Build the ItemStack using registry-stored data to ensure consistency ===
            UserBlockRegistry.UserBlockData data = registry.getUserBlockData(internalId);
            if (data == null) {
                return;
            }
            int storedColor = data.color();
            String storedHex = String.format("%06X", storedColor);
            String storedMimic = data.mimicBlock();

            ItemStack coloredBlock = new ItemStack(userBlock);
            CompoundTag tag = new CompoundTag();
            tag.putString("Color", storedHex);
            tag.putString("OriginalBlock", storedMimic);
            // Also store RGB for backwards compatibility with any client logic
            int red = (storedColor >> 16) & 0xFF;
            int green = (storedColor >> 8) & 0xFF;
            int blue = storedColor & 0xFF;
            tag.putInt("Red", red);
            tag.putInt("Green", green);
            tag.putInt("Blue", blue);

            // Store the pretty/custom name for later reads
            if (!effectiveName.isBlank()) {
                tag.putString("CustomName", effectiveName);
            }
            // IMPORTANT: set the tag first, then apply hover name to avoid it being wiped by setTag
            coloredBlock.setTag(tag);
            if (!effectiveName.isBlank()) {
                coloredBlock.setHoverName(net.minecraft.network.chat.Component.literal(effectiveName));
            }

            // === Inventory placement policy ===
            // 1) Try to place in an open hotbar slot (0..8) and select it
            // 2) If none, try to place in an empty main inventory slot (9..containerSize-1)
            // 3) If none, replace a non-hotbar inventory slot (drop the replaced stack) and do not touch the hotbar
            boolean placed = false;
            int containerSize = player.getInventory().getContainerSize();

            // Step 1: empty hotbar slot
            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getItem(i).isEmpty()) {
                    player.getInventory().setItem(i, coloredBlock);
                    player.getInventory().selected = i;
                    placed = true;
                    break;
                }
            }

            // Step 2: empty main inventory slot
            if (!placed) {
                for (int i = 9; i < containerSize; i++) {
                    if (player.getInventory().getItem(i).isEmpty()) {
                        player.getInventory().setItem(i, coloredBlock);
                        placed = true;
                        break;
                    }
                }
            }

            // Step 3: replace a non-hotbar slot (drop previous stack)
            if (!placed) {
                // Prefer the last slot (from end to start) to minimize disruption
                for (int i = containerSize - 1; i >= 9; i--) {
                    ItemStack toReplace = player.getInventory().getItem(i);
                    if (!toReplace.isEmpty()) {
                        // Drop the replaced stack near the player to avoid silent loss
                        ItemStack dropCopy = toReplace.copy();
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                        player.drop(dropCopy, false);

                        player.getInventory().setItem(i, coloredBlock);
                        placed = true;
                        break;
                    }
                }
            }

            if (placed) {
                String shownName = !effectiveName.isBlank() ? effectiveName : ("#" + storedHex);
                String action = reusedExisting ? "Ready" : "Created";
                player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§a§lBlock " + action + "! §r§f" + shownName + (player.getInventory().selected < 9 && player.getInventory().getItem(player.getInventory().selected) == coloredBlock ? " §7(in your hand)" : " §7(in inventory)")),
                    true
                );
            } else {
                // This should never happen, but add a safe fallback
                player.drop(coloredBlock, false);
                player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§c§lInventory Full! §r§7Block dropped on ground"),
                    true
                );
            }
        });

        context.setPacketHandled(true);
    }

    private static @NotNull String getString(ResourceLocation blockId) {
        String blockPath = blockId.getPath();
        String blockType = "stone"; // default
        if (blockPath.contains("wool")) blockType = "wool";
        else if (blockPath.contains("concrete_powder")) blockType = "concrete_powder";
        else if (blockPath.contains("concrete")) blockType = "concrete";
        else if (blockPath.contains("terracotta")) blockType = "terracotta";
        else if (blockPath.contains("glass")) blockType = "glass";
        else if (blockPath.contains("diorite")) blockType = "diorite";
        else if (blockPath.contains("calcite")) blockType = "calcite";
        else if (blockPath.contains("mushroom_stem")) blockType = "mushroom_stem";
        else if (blockPath.contains("dead_tube_coral")) blockType = "dead_tube_coral";
        else if (blockPath.contains("pearlescent_froglight")) blockType = "pearlescent_froglight";
        else if (blockPath.contains("wood")) blockType = "wood";
        else if (blockPath.contains("dirt")) blockType = "dirt";
        else if (blockPath.contains("sand")) blockType = "sand";
        else if (blockPath.contains("deepslate")) blockType = "deepslate";
        else if (blockPath.contains("cobblestone")) blockType = "cobblestone";
        else if (blockPath.contains("smooth_stone")) blockType = "smooth_stone";
        return blockType;
    }

    private static String normalizeHex(String raw) {
        if (raw == null) return "FFFFFF";
        String s = raw.trim();
        if (s.startsWith("#")) s = s.substring(1);
        if (s.startsWith("0x") || s.startsWith("0X")) s = s.substring(2);
        // Keep only hex chars
        s = s.replaceAll("[^0-9A-Fa-f]", "");
        if (s.isEmpty()) return "FFFFFF";
        // Trim or pad to 6 chars
        if (s.length() > 6) s = s.substring(s.length() - 6);
        if (s.length() < 6) s = ("000000" + s).substring(s.length());
        return s.toUpperCase();
    }

    /**
     * Gets a user block by its internal identifier (e.g., "wool1" -> USER_WOOL_1)
     */
    private static Block getUserBlockByIdentifier(String identifier) {
        return switch (identifier) {
            // Wool blocks
            case "wool1" -> ModBlocks.USER_WOOL_1.get();
            case "wool2" -> ModBlocks.USER_WOOL_2.get();
            case "wool3" -> ModBlocks.USER_WOOL_3.get();
            case "wool4" -> ModBlocks.USER_WOOL_4.get();
            case "wool5" -> ModBlocks.USER_WOOL_5.get();
            
            // Stone blocks
            case "stone1" -> ModBlocks.USER_STONE_1.get();
            case "stone2" -> ModBlocks.USER_STONE_2.get();
            case "stone3" -> ModBlocks.USER_STONE_3.get();
            case "stone4" -> ModBlocks.USER_STONE_4.get();
            case "stone5" -> ModBlocks.USER_STONE_5.get();
            
            // Concrete blocks
            case "concrete1" -> ModBlocks.USER_CONCRETE_1.get();
            case "concrete2" -> ModBlocks.USER_CONCRETE_2.get();
            case "concrete3" -> ModBlocks.USER_CONCRETE_3.get();
            case "concrete4" -> ModBlocks.USER_CONCRETE_4.get();
            case "concrete5" -> ModBlocks.USER_CONCRETE_5.get();
            
            // Sand blocks
            case "sand1" -> ModBlocks.USER_SAND_1.get();
            case "sand2" -> ModBlocks.USER_SAND_2.get();
            case "sand3" -> ModBlocks.USER_SAND_3.get();
            case "sand4" -> ModBlocks.USER_SAND_4.get();
            case "sand5" -> ModBlocks.USER_SAND_5.get();
            
            // Deepslate blocks
            case "deepslate1" -> ModBlocks.USER_DEEPSLATE_1.get();
            case "deepslate2" -> ModBlocks.USER_DEEPSLATE_2.get();
            case "deepslate3" -> ModBlocks.USER_DEEPSLATE_3.get();
            case "deepslate4" -> ModBlocks.USER_DEEPSLATE_4.get();
            case "deepslate5" -> ModBlocks.USER_DEEPSLATE_5.get();
            
            // Wood blocks
            case "wood1" -> ModBlocks.USER_WOOD_1.get();
            case "wood2" -> ModBlocks.USER_WOOD_2.get();
            case "wood3" -> ModBlocks.USER_WOOD_3.get();
            case "wood4" -> ModBlocks.USER_WOOD_4.get();
            case "wood5" -> ModBlocks.USER_WOOD_5.get();
            
            // Dirt blocks
            case "dirt1" -> ModBlocks.USER_DIRT_1.get();
            case "dirt2" -> ModBlocks.USER_DIRT_2.get();
            case "dirt3" -> ModBlocks.USER_DIRT_3.get();
            case "dirt4" -> ModBlocks.USER_DIRT_4.get();
            case "dirt5" -> ModBlocks.USER_DIRT_5.get();
            
            // Cobblestone blocks
            case "cobblestone1" -> ModBlocks.USER_COBBLESTONE_1.get();
            case "cobblestone2" -> ModBlocks.USER_COBBLESTONE_2.get();
            case "cobblestone3" -> ModBlocks.USER_COBBLESTONE_3.get();
            case "cobblestone4" -> ModBlocks.USER_COBBLESTONE_4.get();
            case "cobblestone5" -> ModBlocks.USER_COBBLESTONE_5.get();
            case "cobblestone6" -> ModBlocks.USER_COBBLESTONE_6.get();
            case "cobblestone7" -> ModBlocks.USER_COBBLESTONE_7.get();
            case "cobblestone8" -> ModBlocks.USER_COBBLESTONE_8.get();
            case "cobblestone9" -> ModBlocks.USER_COBBLESTONE_9.get();
            case "cobblestone10" -> ModBlocks.USER_COBBLESTONE_10.get();
            case "cobblestone11" -> ModBlocks.USER_COBBLESTONE_11.get();
            case "cobblestone12" -> ModBlocks.USER_COBBLESTONE_12.get();
            case "cobblestone13" -> ModBlocks.USER_COBBLESTONE_13.get();
            case "cobblestone14" -> ModBlocks.USER_COBBLESTONE_14.get();
            case "cobblestone15" -> ModBlocks.USER_COBBLESTONE_15.get();
            case "cobblestone16" -> ModBlocks.USER_COBBLESTONE_16.get();
            case "cobblestone17" -> ModBlocks.USER_COBBLESTONE_17.get();
            case "cobblestone18" -> ModBlocks.USER_COBBLESTONE_18.get();
            case "cobblestone19" -> ModBlocks.USER_COBBLESTONE_19.get();
            case "cobblestone20" -> ModBlocks.USER_COBBLESTONE_20.get();
            
            // Smooth Stone blocks
            case "smooth_stone1" -> ModBlocks.USER_SMOOTH_STONE_1.get();
            case "smooth_stone2" -> ModBlocks.USER_SMOOTH_STONE_2.get();
            case "smooth_stone3" -> ModBlocks.USER_SMOOTH_STONE_3.get();
            case "smooth_stone4" -> ModBlocks.USER_SMOOTH_STONE_4.get();
            case "smooth_stone5" -> ModBlocks.USER_SMOOTH_STONE_5.get();
            case "smooth_stone6" -> ModBlocks.USER_SMOOTH_STONE_6.get();
            case "smooth_stone7" -> ModBlocks.USER_SMOOTH_STONE_7.get();
            case "smooth_stone8" -> ModBlocks.USER_SMOOTH_STONE_8.get();
            case "smooth_stone9" -> ModBlocks.USER_SMOOTH_STONE_9.get();
            case "smooth_stone10" -> ModBlocks.USER_SMOOTH_STONE_10.get();
            case "smooth_stone11" -> ModBlocks.USER_SMOOTH_STONE_11.get();
            case "smooth_stone12" -> ModBlocks.USER_SMOOTH_STONE_12.get();
            case "smooth_stone13" -> ModBlocks.USER_SMOOTH_STONE_13.get();
            case "smooth_stone14" -> ModBlocks.USER_SMOOTH_STONE_14.get();
            case "smooth_stone15" -> ModBlocks.USER_SMOOTH_STONE_15.get();
            case "smooth_stone16" -> ModBlocks.USER_SMOOTH_STONE_16.get();
            case "smooth_stone17" -> ModBlocks.USER_SMOOTH_STONE_17.get();
            case "smooth_stone18" -> ModBlocks.USER_SMOOTH_STONE_18.get();
            case "smooth_stone19" -> ModBlocks.USER_SMOOTH_STONE_19.get();
            case "smooth_stone20" -> ModBlocks.USER_SMOOTH_STONE_20.get();
            
            // Terracotta blocks
            case "terracotta1" -> ModBlocks.USER_TERRACOTTA_1.get();
            case "terracotta2" -> ModBlocks.USER_TERRACOTTA_2.get();
            case "terracotta3" -> ModBlocks.USER_TERRACOTTA_3.get();
            case "terracotta4" -> ModBlocks.USER_TERRACOTTA_4.get();
            case "terracotta5" -> ModBlocks.USER_TERRACOTTA_5.get();
            case "terracotta6" -> ModBlocks.USER_TERRACOTTA_6.get();
            case "terracotta7" -> ModBlocks.USER_TERRACOTTA_7.get();
            case "terracotta8" -> ModBlocks.USER_TERRACOTTA_8.get();
            case "terracotta9" -> ModBlocks.USER_TERRACOTTA_9.get();
            case "terracotta10" -> ModBlocks.USER_TERRACOTTA_10.get();
            case "terracotta11" -> ModBlocks.USER_TERRACOTTA_11.get();
            case "terracotta12" -> ModBlocks.USER_TERRACOTTA_12.get();
            case "terracotta13" -> ModBlocks.USER_TERRACOTTA_13.get();
            case "terracotta14" -> ModBlocks.USER_TERRACOTTA_14.get();
            case "terracotta15" -> ModBlocks.USER_TERRACOTTA_15.get();
            case "terracotta16" -> ModBlocks.USER_TERRACOTTA_16.get();
            case "terracotta17" -> ModBlocks.USER_TERRACOTTA_17.get();
            case "terracotta18" -> ModBlocks.USER_TERRACOTTA_18.get();
            case "terracotta19" -> ModBlocks.USER_TERRACOTTA_19.get();
            case "terracotta20" -> ModBlocks.USER_TERRACOTTA_20.get();
            
            // Concrete Powder blocks
            case "concrete_powder1" -> ModBlocks.USER_CONCRETE_POWDER_1.get();
            case "concrete_powder2" -> ModBlocks.USER_CONCRETE_POWDER_2.get();
            case "concrete_powder3" -> ModBlocks.USER_CONCRETE_POWDER_3.get();
            case "concrete_powder4" -> ModBlocks.USER_CONCRETE_POWDER_4.get();
            case "concrete_powder5" -> ModBlocks.USER_CONCRETE_POWDER_5.get();
            case "concrete_powder6" -> ModBlocks.USER_CONCRETE_POWDER_6.get();
            case "concrete_powder7" -> ModBlocks.USER_CONCRETE_POWDER_7.get();
            case "concrete_powder8" -> ModBlocks.USER_CONCRETE_POWDER_8.get();
            case "concrete_powder9" -> ModBlocks.USER_CONCRETE_POWDER_9.get();
            case "concrete_powder10" -> ModBlocks.USER_CONCRETE_POWDER_10.get();
            case "concrete_powder11" -> ModBlocks.USER_CONCRETE_POWDER_11.get();
            case "concrete_powder12" -> ModBlocks.USER_CONCRETE_POWDER_12.get();
            case "concrete_powder13" -> ModBlocks.USER_CONCRETE_POWDER_13.get();
            case "concrete_powder14" -> ModBlocks.USER_CONCRETE_POWDER_14.get();
            case "concrete_powder15" -> ModBlocks.USER_CONCRETE_POWDER_15.get();
            case "concrete_powder16" -> ModBlocks.USER_CONCRETE_POWDER_16.get();
            case "concrete_powder17" -> ModBlocks.USER_CONCRETE_POWDER_17.get();
            case "concrete_powder18" -> ModBlocks.USER_CONCRETE_POWDER_18.get();
            case "concrete_powder19" -> ModBlocks.USER_CONCRETE_POWDER_19.get();
            case "concrete_powder20" -> ModBlocks.USER_CONCRETE_POWDER_20.get();
            
            // Glass blocks
            case "glass1" -> ModBlocks.USER_GLASS_1.get();
            case "glass2" -> ModBlocks.USER_GLASS_2.get();
            case "glass3" -> ModBlocks.USER_GLASS_3.get();
            case "glass4" -> ModBlocks.USER_GLASS_4.get();
            case "glass5" -> ModBlocks.USER_GLASS_5.get();
            case "glass6" -> ModBlocks.USER_GLASS_6.get();
            case "glass7" -> ModBlocks.USER_GLASS_7.get();
            case "glass8" -> ModBlocks.USER_GLASS_8.get();
            case "glass9" -> ModBlocks.USER_GLASS_9.get();
            case "glass10" -> ModBlocks.USER_GLASS_10.get();
            case "glass11" -> ModBlocks.USER_GLASS_11.get();
            case "glass12" -> ModBlocks.USER_GLASS_12.get();
            case "glass13" -> ModBlocks.USER_GLASS_13.get();
            case "glass14" -> ModBlocks.USER_GLASS_14.get();
            case "glass15" -> ModBlocks.USER_GLASS_15.get();
            case "glass16" -> ModBlocks.USER_GLASS_16.get();
            case "glass17" -> ModBlocks.USER_GLASS_17.get();
            case "glass18" -> ModBlocks.USER_GLASS_18.get();
            case "glass19" -> ModBlocks.USER_GLASS_19.get();
            case "glass20" -> ModBlocks.USER_GLASS_20.get();
            
            // Diorite blocks
            case "diorite1" -> ModBlocks.USER_DIORITE_1.get();
            case "diorite2" -> ModBlocks.USER_DIORITE_2.get();
            case "diorite3" -> ModBlocks.USER_DIORITE_3.get();
            case "diorite4" -> ModBlocks.USER_DIORITE_4.get();
            case "diorite5" -> ModBlocks.USER_DIORITE_5.get();
            case "diorite6" -> ModBlocks.USER_DIORITE_6.get();
            case "diorite7" -> ModBlocks.USER_DIORITE_7.get();
            case "diorite8" -> ModBlocks.USER_DIORITE_8.get();
            case "diorite9" -> ModBlocks.USER_DIORITE_9.get();
            case "diorite10" -> ModBlocks.USER_DIORITE_10.get();
            case "diorite11" -> ModBlocks.USER_DIORITE_11.get();
            case "diorite12" -> ModBlocks.USER_DIORITE_12.get();
            case "diorite13" -> ModBlocks.USER_DIORITE_13.get();
            case "diorite14" -> ModBlocks.USER_DIORITE_14.get();
            case "diorite15" -> ModBlocks.USER_DIORITE_15.get();
            case "diorite16" -> ModBlocks.USER_DIORITE_16.get();
            case "diorite17" -> ModBlocks.USER_DIORITE_17.get();
            case "diorite18" -> ModBlocks.USER_DIORITE_18.get();
            case "diorite19" -> ModBlocks.USER_DIORITE_19.get();
            case "diorite20" -> ModBlocks.USER_DIORITE_20.get();
            
            // Calcite blocks
            case "calcite1" -> ModBlocks.USER_CALCITE_1.get();
            case "calcite2" -> ModBlocks.USER_CALCITE_2.get();
            case "calcite3" -> ModBlocks.USER_CALCITE_3.get();
            case "calcite4" -> ModBlocks.USER_CALCITE_4.get();
            case "calcite5" -> ModBlocks.USER_CALCITE_5.get();
            case "calcite6" -> ModBlocks.USER_CALCITE_6.get();
            case "calcite7" -> ModBlocks.USER_CALCITE_7.get();
            case "calcite8" -> ModBlocks.USER_CALCITE_8.get();
            case "calcite9" -> ModBlocks.USER_CALCITE_9.get();
            case "calcite10" -> ModBlocks.USER_CALCITE_10.get();
            case "calcite11" -> ModBlocks.USER_CALCITE_11.get();
            case "calcite12" -> ModBlocks.USER_CALCITE_12.get();
            case "calcite13" -> ModBlocks.USER_CALCITE_13.get();
            case "calcite14" -> ModBlocks.USER_CALCITE_14.get();
            case "calcite15" -> ModBlocks.USER_CALCITE_15.get();
            case "calcite16" -> ModBlocks.USER_CALCITE_16.get();
            case "calcite17" -> ModBlocks.USER_CALCITE_17.get();
            case "calcite18" -> ModBlocks.USER_CALCITE_18.get();
            case "calcite19" -> ModBlocks.USER_CALCITE_19.get();
            case "calcite20" -> ModBlocks.USER_CALCITE_20.get();
            
            // Mushroom Stem blocks
            case "mushroom_stem1" -> ModBlocks.USER_MUSHROOM_STEM_1.get();
            case "mushroom_stem2" -> ModBlocks.USER_MUSHROOM_STEM_2.get();
            case "mushroom_stem3" -> ModBlocks.USER_MUSHROOM_STEM_3.get();
            case "mushroom_stem4" -> ModBlocks.USER_MUSHROOM_STEM_4.get();
            case "mushroom_stem5" -> ModBlocks.USER_MUSHROOM_STEM_5.get();
            case "mushroom_stem6" -> ModBlocks.USER_MUSHROOM_STEM_6.get();
            case "mushroom_stem7" -> ModBlocks.USER_MUSHROOM_STEM_7.get();
            case "mushroom_stem8" -> ModBlocks.USER_MUSHROOM_STEM_8.get();
            case "mushroom_stem9" -> ModBlocks.USER_MUSHROOM_STEM_9.get();
            case "mushroom_stem10" -> ModBlocks.USER_MUSHROOM_STEM_10.get();
            case "mushroom_stem11" -> ModBlocks.USER_MUSHROOM_STEM_11.get();
            case "mushroom_stem12" -> ModBlocks.USER_MUSHROOM_STEM_12.get();
            case "mushroom_stem13" -> ModBlocks.USER_MUSHROOM_STEM_13.get();
            case "mushroom_stem14" -> ModBlocks.USER_MUSHROOM_STEM_14.get();
            case "mushroom_stem15" -> ModBlocks.USER_MUSHROOM_STEM_15.get();
            case "mushroom_stem16" -> ModBlocks.USER_MUSHROOM_STEM_16.get();
            case "mushroom_stem17" -> ModBlocks.USER_MUSHROOM_STEM_17.get();
            case "mushroom_stem18" -> ModBlocks.USER_MUSHROOM_STEM_18.get();
            case "mushroom_stem19" -> ModBlocks.USER_MUSHROOM_STEM_19.get();
            case "mushroom_stem20" -> ModBlocks.USER_MUSHROOM_STEM_20.get();
            
            // Dead Tube Coral blocks
            case "dead_tube_coral1" -> ModBlocks.USER_DEAD_TUBE_CORAL_1.get();
            case "dead_tube_coral2" -> ModBlocks.USER_DEAD_TUBE_CORAL_2.get();
            case "dead_tube_coral3" -> ModBlocks.USER_DEAD_TUBE_CORAL_3.get();
            case "dead_tube_coral4" -> ModBlocks.USER_DEAD_TUBE_CORAL_4.get();
            case "dead_tube_coral5" -> ModBlocks.USER_DEAD_TUBE_CORAL_5.get();
            case "dead_tube_coral6" -> ModBlocks.USER_DEAD_TUBE_CORAL_6.get();
            case "dead_tube_coral7" -> ModBlocks.USER_DEAD_TUBE_CORAL_7.get();
            case "dead_tube_coral8" -> ModBlocks.USER_DEAD_TUBE_CORAL_8.get();
            case "dead_tube_coral9" -> ModBlocks.USER_DEAD_TUBE_CORAL_9.get();
            case "dead_tube_coral10" -> ModBlocks.USER_DEAD_TUBE_CORAL_10.get();
            case "dead_tube_coral11" -> ModBlocks.USER_DEAD_TUBE_CORAL_11.get();
            case "dead_tube_coral12" -> ModBlocks.USER_DEAD_TUBE_CORAL_12.get();
            case "dead_tube_coral13" -> ModBlocks.USER_DEAD_TUBE_CORAL_13.get();
            case "dead_tube_coral14" -> ModBlocks.USER_DEAD_TUBE_CORAL_14.get();
            case "dead_tube_coral15" -> ModBlocks.USER_DEAD_TUBE_CORAL_15.get();
            case "dead_tube_coral16" -> ModBlocks.USER_DEAD_TUBE_CORAL_16.get();
            case "dead_tube_coral17" -> ModBlocks.USER_DEAD_TUBE_CORAL_17.get();
            case "dead_tube_coral18" -> ModBlocks.USER_DEAD_TUBE_CORAL_18.get();
            case "dead_tube_coral19" -> ModBlocks.USER_DEAD_TUBE_CORAL_19.get();
            case "dead_tube_coral20" -> ModBlocks.USER_DEAD_TUBE_CORAL_20.get();
            
            // Pearlescent Froglight blocks
            case "pearlescent_froglight1" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_1.get();
            case "pearlescent_froglight2" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_2.get();
            case "pearlescent_froglight3" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_3.get();
            case "pearlescent_froglight4" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_4.get();
            case "pearlescent_froglight5" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_5.get();
            case "pearlescent_froglight6" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_6.get();
            case "pearlescent_froglight7" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_7.get();
            case "pearlescent_froglight8" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_8.get();
            case "pearlescent_froglight9" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_9.get();
            case "pearlescent_froglight10" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_10.get();
            case "pearlescent_froglight11" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_11.get();
            case "pearlescent_froglight12" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_12.get();
            case "pearlescent_froglight13" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_13.get();
            case "pearlescent_froglight14" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_14.get();
            case "pearlescent_froglight15" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_15.get();
            case "pearlescent_froglight16" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_16.get();
            case "pearlescent_froglight17" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_17.get();
            case "pearlescent_froglight18" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_18.get();
            case "pearlescent_froglight19" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_19.get();
            case "pearlescent_froglight20" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_20.get();
            
            default -> null;
        };
    }
}

