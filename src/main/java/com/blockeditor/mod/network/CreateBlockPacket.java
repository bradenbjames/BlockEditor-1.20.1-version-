package com.blockeditor.mod.network;

import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

import org.jetbrains.annotations.NotNull;

public class CreateBlockPacket {

    private final String hexColor;
    private final String mimicBlockId;
    private final String blockType;
    private final String customName;
    private final int stackSize;

    public CreateBlockPacket(String hexColor, String mimicBlockId, String blockType, String customName, int stackSize) {
        this.hexColor = hexColor;
        this.mimicBlockId = mimicBlockId;
        this.blockType = blockType;
        this.customName = customName;
        this.stackSize = stackSize;
    }

    // Legacy constructor for backward compatibility
    public CreateBlockPacket(String hexColor, String mimicBlockId, String blockType, String customName) {
        this(hexColor, mimicBlockId, blockType, customName, 1);
    }

    public static void encode(CreateBlockPacket packet, PacketByteBuf buf) {
        buf.writeString(packet.hexColor);
        buf.writeString(packet.mimicBlockId);
        buf.writeString(packet.blockType);
        buf.writeString(packet.customName);
        buf.writeInt(packet.stackSize);
    }

    public static CreateBlockPacket decode(PacketByteBuf buf) {
        return new CreateBlockPacket(buf.readString(), buf.readString(), buf.readString(), buf.readString(), buf.readInt());
    }

    public static void handle(CreateBlockPacket packet, ServerPlayerEntity player) {
        if (player == null) {
            return;
        }

            // Get the user block registry
            ServerWorld level = player.getServerWorld();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Determine block type from the block ID
            Identifier blockId = Identifier.tryParse(packet.blockType);
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
                            player.sendMessage(
                                net.minecraft.text.Text.literal("§cError: No more slots available for " + blockType + " blocks!"),
                                false
                            );
                            return;
                        } else {
                            internalId = autoId;
                            effectiveName = candidate;
                            player.sendMessage(
                                net.minecraft.text.Text.literal("§eName in use. Using '" + effectiveName + "'"),
                                false
                            );
                        }
                    } else {
                        // No more slots available
                        player.sendMessage(
                            net.minecraft.text.Text.literal("§cError: No more slots available for " + blockType + " blocks!"),
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

            ItemStack coloredBlock = new ItemStack(userBlock, Math.max(1, Math.min(64, packet.stackSize)));
            NbtCompound tag = new NbtCompound();
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
            coloredBlock.setNbt(tag);
            if (!effectiveName.isBlank()) {
                coloredBlock.setCustomName(net.minecraft.text.Text.literal(effectiveName));
            }

            // === Inventory placement policy ===
            // Always place the new block in the player's hand (selected hotbar slot).
            // If the hand slot is occupied, move the existing item to an empty inventory slot.
            // If no empty slots, drop the displaced item on the ground.
            boolean placed = false;
            int containerSize = player.getInventory().size();
            int handSlot = player.getInventory().selectedSlot;

            ItemStack currentInHand = player.getInventory().getStack(handSlot);
            if (currentInHand.isEmpty()) {
                // Hand is empty, just place it
                player.getInventory().setStack(handSlot, coloredBlock);
                placed = true;
            } else {
                // Move current hand item to an empty inventory slot (prefer main inventory 9+, then other hotbar)
                boolean moved = false;
                for (int i = 9; i < containerSize; i++) {
                    if (player.getInventory().getStack(i).isEmpty()) {
                        player.getInventory().setStack(i, currentInHand);
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    // No empty main slots, try other hotbar slots
                    for (int i = 0; i < 9; i++) {
                        if (i != handSlot && player.getInventory().getStack(i).isEmpty()) {
                            player.getInventory().setStack(i, currentInHand);
                            moved = true;
                            break;
                        }
                    }
                }
                if (!moved) {
                    // Inventory completely full, drop the hand item
                    player.dropItem(currentInHand, false);
                }
                // Place new block in hand
                player.getInventory().setStack(handSlot, coloredBlock);
                placed = true;
            }

            if (placed) {
                String shownName = !effectiveName.isBlank() ? effectiveName : ("#" + storedHex);
                String action = reusedExisting ? "Ready" : "Created";
                player.sendMessage(
                    net.minecraft.text.Text.literal("§a§lBlock " + action + "! §r§f" + shownName + " §7(in your hand)"),
                    true
                );
            } else {
                // This should never happen, but add a safe fallback
                player.dropItem(coloredBlock, false);
                player.sendMessage(
                    net.minecraft.text.Text.literal("§c§lInventory Full! §r§7Block dropped on ground"),
                    true
                );
            }
    }

    private static @NotNull String getString(Identifier blockId) {
        String blockPath = blockId.getPath();
        String blockType = "stone"; // default
        if (blockPath.contains("wool")) blockType = "wool";
        else if (blockPath.contains("concrete_powder")) blockType = "concrete_powder";
        else if (blockPath.contains("concrete")) blockType = "concrete";
        else if (blockPath.contains("terracotta")) blockType = "terracotta";
        else if (blockPath.contains("tinted_glass")) blockType = "tinted_glass";
        else if (blockPath.contains("stained_glass")) blockType = "stained_glass";
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
            case "wool1" -> ModBlocks.USER_WOOL_1;
            case "wool2" -> ModBlocks.USER_WOOL_2;
            case "wool3" -> ModBlocks.USER_WOOL_3;
            case "wool4" -> ModBlocks.USER_WOOL_4;
            case "wool5" -> ModBlocks.USER_WOOL_5;
            
            // Stone blocks
            case "stone1" -> ModBlocks.USER_STONE_1;
            case "stone2" -> ModBlocks.USER_STONE_2;
            case "stone3" -> ModBlocks.USER_STONE_3;
            case "stone4" -> ModBlocks.USER_STONE_4;
            case "stone5" -> ModBlocks.USER_STONE_5;
            
            // Concrete blocks
            case "concrete1" -> ModBlocks.USER_CONCRETE_1;
            case "concrete2" -> ModBlocks.USER_CONCRETE_2;
            case "concrete3" -> ModBlocks.USER_CONCRETE_3;
            case "concrete4" -> ModBlocks.USER_CONCRETE_4;
            case "concrete5" -> ModBlocks.USER_CONCRETE_5;
            
            // Sand blocks
            case "sand1" -> ModBlocks.USER_SAND_1;
            case "sand2" -> ModBlocks.USER_SAND_2;
            case "sand3" -> ModBlocks.USER_SAND_3;
            case "sand4" -> ModBlocks.USER_SAND_4;
            case "sand5" -> ModBlocks.USER_SAND_5;
            
            // Deepslate blocks
            case "deepslate1" -> ModBlocks.USER_DEEPSLATE_1;
            case "deepslate2" -> ModBlocks.USER_DEEPSLATE_2;
            case "deepslate3" -> ModBlocks.USER_DEEPSLATE_3;
            case "deepslate4" -> ModBlocks.USER_DEEPSLATE_4;
            case "deepslate5" -> ModBlocks.USER_DEEPSLATE_5;
            
            // Wood blocks
            case "wood1" -> ModBlocks.USER_WOOD_1;
            case "wood2" -> ModBlocks.USER_WOOD_2;
            case "wood3" -> ModBlocks.USER_WOOD_3;
            case "wood4" -> ModBlocks.USER_WOOD_4;
            case "wood5" -> ModBlocks.USER_WOOD_5;
            
            // Dirt blocks
            case "dirt1" -> ModBlocks.USER_DIRT_1;
            case "dirt2" -> ModBlocks.USER_DIRT_2;
            case "dirt3" -> ModBlocks.USER_DIRT_3;
            case "dirt4" -> ModBlocks.USER_DIRT_4;
            case "dirt5" -> ModBlocks.USER_DIRT_5;
            
            // Cobblestone blocks
            case "cobblestone1" -> ModBlocks.USER_COBBLESTONE_1;
            case "cobblestone2" -> ModBlocks.USER_COBBLESTONE_2;
            case "cobblestone3" -> ModBlocks.USER_COBBLESTONE_3;
            case "cobblestone4" -> ModBlocks.USER_COBBLESTONE_4;
            case "cobblestone5" -> ModBlocks.USER_COBBLESTONE_5;
            case "cobblestone6" -> ModBlocks.USER_COBBLESTONE_6;
            case "cobblestone7" -> ModBlocks.USER_COBBLESTONE_7;
            case "cobblestone8" -> ModBlocks.USER_COBBLESTONE_8;
            case "cobblestone9" -> ModBlocks.USER_COBBLESTONE_9;
            case "cobblestone10" -> ModBlocks.USER_COBBLESTONE_10;
            case "cobblestone11" -> ModBlocks.USER_COBBLESTONE_11;
            case "cobblestone12" -> ModBlocks.USER_COBBLESTONE_12;
            case "cobblestone13" -> ModBlocks.USER_COBBLESTONE_13;
            case "cobblestone14" -> ModBlocks.USER_COBBLESTONE_14;
            case "cobblestone15" -> ModBlocks.USER_COBBLESTONE_15;
            case "cobblestone16" -> ModBlocks.USER_COBBLESTONE_16;
            case "cobblestone17" -> ModBlocks.USER_COBBLESTONE_17;
            case "cobblestone18" -> ModBlocks.USER_COBBLESTONE_18;
            case "cobblestone19" -> ModBlocks.USER_COBBLESTONE_19;
            case "cobblestone20" -> ModBlocks.USER_COBBLESTONE_20;
            
            // Smooth Stone blocks
            case "smooth_stone1" -> ModBlocks.USER_SMOOTH_STONE_1;
            case "smooth_stone2" -> ModBlocks.USER_SMOOTH_STONE_2;
            case "smooth_stone3" -> ModBlocks.USER_SMOOTH_STONE_3;
            case "smooth_stone4" -> ModBlocks.USER_SMOOTH_STONE_4;
            case "smooth_stone5" -> ModBlocks.USER_SMOOTH_STONE_5;
            case "smooth_stone6" -> ModBlocks.USER_SMOOTH_STONE_6;
            case "smooth_stone7" -> ModBlocks.USER_SMOOTH_STONE_7;
            case "smooth_stone8" -> ModBlocks.USER_SMOOTH_STONE_8;
            case "smooth_stone9" -> ModBlocks.USER_SMOOTH_STONE_9;
            case "smooth_stone10" -> ModBlocks.USER_SMOOTH_STONE_10;
            case "smooth_stone11" -> ModBlocks.USER_SMOOTH_STONE_11;
            case "smooth_stone12" -> ModBlocks.USER_SMOOTH_STONE_12;
            case "smooth_stone13" -> ModBlocks.USER_SMOOTH_STONE_13;
            case "smooth_stone14" -> ModBlocks.USER_SMOOTH_STONE_14;
            case "smooth_stone15" -> ModBlocks.USER_SMOOTH_STONE_15;
            case "smooth_stone16" -> ModBlocks.USER_SMOOTH_STONE_16;
            case "smooth_stone17" -> ModBlocks.USER_SMOOTH_STONE_17;
            case "smooth_stone18" -> ModBlocks.USER_SMOOTH_STONE_18;
            case "smooth_stone19" -> ModBlocks.USER_SMOOTH_STONE_19;
            case "smooth_stone20" -> ModBlocks.USER_SMOOTH_STONE_20;
            
            // Terracotta blocks
            case "terracotta1" -> ModBlocks.USER_TERRACOTTA_1;
            case "terracotta2" -> ModBlocks.USER_TERRACOTTA_2;
            case "terracotta3" -> ModBlocks.USER_TERRACOTTA_3;
            case "terracotta4" -> ModBlocks.USER_TERRACOTTA_4;
            case "terracotta5" -> ModBlocks.USER_TERRACOTTA_5;
            case "terracotta6" -> ModBlocks.USER_TERRACOTTA_6;
            case "terracotta7" -> ModBlocks.USER_TERRACOTTA_7;
            case "terracotta8" -> ModBlocks.USER_TERRACOTTA_8;
            case "terracotta9" -> ModBlocks.USER_TERRACOTTA_9;
            case "terracotta10" -> ModBlocks.USER_TERRACOTTA_10;
            case "terracotta11" -> ModBlocks.USER_TERRACOTTA_11;
            case "terracotta12" -> ModBlocks.USER_TERRACOTTA_12;
            case "terracotta13" -> ModBlocks.USER_TERRACOTTA_13;
            case "terracotta14" -> ModBlocks.USER_TERRACOTTA_14;
            case "terracotta15" -> ModBlocks.USER_TERRACOTTA_15;
            case "terracotta16" -> ModBlocks.USER_TERRACOTTA_16;
            case "terracotta17" -> ModBlocks.USER_TERRACOTTA_17;
            case "terracotta18" -> ModBlocks.USER_TERRACOTTA_18;
            case "terracotta19" -> ModBlocks.USER_TERRACOTTA_19;
            case "terracotta20" -> ModBlocks.USER_TERRACOTTA_20;
            
            // Concrete Powder blocks
            case "concrete_powder1" -> ModBlocks.USER_CONCRETE_POWDER_1;
            case "concrete_powder2" -> ModBlocks.USER_CONCRETE_POWDER_2;
            case "concrete_powder3" -> ModBlocks.USER_CONCRETE_POWDER_3;
            case "concrete_powder4" -> ModBlocks.USER_CONCRETE_POWDER_4;
            case "concrete_powder5" -> ModBlocks.USER_CONCRETE_POWDER_5;
            case "concrete_powder6" -> ModBlocks.USER_CONCRETE_POWDER_6;
            case "concrete_powder7" -> ModBlocks.USER_CONCRETE_POWDER_7;
            case "concrete_powder8" -> ModBlocks.USER_CONCRETE_POWDER_8;
            case "concrete_powder9" -> ModBlocks.USER_CONCRETE_POWDER_9;
            case "concrete_powder10" -> ModBlocks.USER_CONCRETE_POWDER_10;
            case "concrete_powder11" -> ModBlocks.USER_CONCRETE_POWDER_11;
            case "concrete_powder12" -> ModBlocks.USER_CONCRETE_POWDER_12;
            case "concrete_powder13" -> ModBlocks.USER_CONCRETE_POWDER_13;
            case "concrete_powder14" -> ModBlocks.USER_CONCRETE_POWDER_14;
            case "concrete_powder15" -> ModBlocks.USER_CONCRETE_POWDER_15;
            case "concrete_powder16" -> ModBlocks.USER_CONCRETE_POWDER_16;
            case "concrete_powder17" -> ModBlocks.USER_CONCRETE_POWDER_17;
            case "concrete_powder18" -> ModBlocks.USER_CONCRETE_POWDER_18;
            case "concrete_powder19" -> ModBlocks.USER_CONCRETE_POWDER_19;
            case "concrete_powder20" -> ModBlocks.USER_CONCRETE_POWDER_20;
            
            // Glass blocks
            case "glass1" -> ModBlocks.USER_GLASS_1;
            case "glass2" -> ModBlocks.USER_GLASS_2;
            case "glass3" -> ModBlocks.USER_GLASS_3;
            case "glass4" -> ModBlocks.USER_GLASS_4;
            case "glass5" -> ModBlocks.USER_GLASS_5;
            case "glass6" -> ModBlocks.USER_GLASS_6;
            case "glass7" -> ModBlocks.USER_GLASS_7;
            case "glass8" -> ModBlocks.USER_GLASS_8;
            case "glass9" -> ModBlocks.USER_GLASS_9;
            case "glass10" -> ModBlocks.USER_GLASS_10;
            case "glass11" -> ModBlocks.USER_GLASS_11;
            case "glass12" -> ModBlocks.USER_GLASS_12;
            case "glass13" -> ModBlocks.USER_GLASS_13;
            case "glass14" -> ModBlocks.USER_GLASS_14;
            case "glass15" -> ModBlocks.USER_GLASS_15;
            case "glass16" -> ModBlocks.USER_GLASS_16;
            case "glass17" -> ModBlocks.USER_GLASS_17;
            case "glass18" -> ModBlocks.USER_GLASS_18;
            case "glass19" -> ModBlocks.USER_GLASS_19;
            case "glass20" -> ModBlocks.USER_GLASS_20;
            
            // Tinted Glass blocks
            case "tinted_glass1" -> ModBlocks.USER_TINTED_GLASS_1;
            case "tinted_glass2" -> ModBlocks.USER_TINTED_GLASS_2;
            case "tinted_glass3" -> ModBlocks.USER_TINTED_GLASS_3;
            case "tinted_glass4" -> ModBlocks.USER_TINTED_GLASS_4;
            case "tinted_glass5" -> ModBlocks.USER_TINTED_GLASS_5;
            case "tinted_glass6" -> ModBlocks.USER_TINTED_GLASS_6;
            case "tinted_glass7" -> ModBlocks.USER_TINTED_GLASS_7;
            case "tinted_glass8" -> ModBlocks.USER_TINTED_GLASS_8;
            case "tinted_glass9" -> ModBlocks.USER_TINTED_GLASS_9;
            case "tinted_glass10" -> ModBlocks.USER_TINTED_GLASS_10;
            case "tinted_glass11" -> ModBlocks.USER_TINTED_GLASS_11;
            case "tinted_glass12" -> ModBlocks.USER_TINTED_GLASS_12;
            case "tinted_glass13" -> ModBlocks.USER_TINTED_GLASS_13;
            case "tinted_glass14" -> ModBlocks.USER_TINTED_GLASS_14;
            case "tinted_glass15" -> ModBlocks.USER_TINTED_GLASS_15;
            case "tinted_glass16" -> ModBlocks.USER_TINTED_GLASS_16;
            case "tinted_glass17" -> ModBlocks.USER_TINTED_GLASS_17;
            case "tinted_glass18" -> ModBlocks.USER_TINTED_GLASS_18;
            case "tinted_glass19" -> ModBlocks.USER_TINTED_GLASS_19;
            case "tinted_glass20" -> ModBlocks.USER_TINTED_GLASS_20;

            // Stained Glass blocks
            case "stained_glass1" -> ModBlocks.USER_STAINED_GLASS_1;
            case "stained_glass2" -> ModBlocks.USER_STAINED_GLASS_2;
            case "stained_glass3" -> ModBlocks.USER_STAINED_GLASS_3;
            case "stained_glass4" -> ModBlocks.USER_STAINED_GLASS_4;
            case "stained_glass5" -> ModBlocks.USER_STAINED_GLASS_5;
            case "stained_glass6" -> ModBlocks.USER_STAINED_GLASS_6;
            case "stained_glass7" -> ModBlocks.USER_STAINED_GLASS_7;
            case "stained_glass8" -> ModBlocks.USER_STAINED_GLASS_8;
            case "stained_glass9" -> ModBlocks.USER_STAINED_GLASS_9;
            case "stained_glass10" -> ModBlocks.USER_STAINED_GLASS_10;
            case "stained_glass11" -> ModBlocks.USER_STAINED_GLASS_11;
            case "stained_glass12" -> ModBlocks.USER_STAINED_GLASS_12;
            case "stained_glass13" -> ModBlocks.USER_STAINED_GLASS_13;
            case "stained_glass14" -> ModBlocks.USER_STAINED_GLASS_14;
            case "stained_glass15" -> ModBlocks.USER_STAINED_GLASS_15;
            case "stained_glass16" -> ModBlocks.USER_STAINED_GLASS_16;
            case "stained_glass17" -> ModBlocks.USER_STAINED_GLASS_17;
            case "stained_glass18" -> ModBlocks.USER_STAINED_GLASS_18;
            case "stained_glass19" -> ModBlocks.USER_STAINED_GLASS_19;
            case "stained_glass20" -> ModBlocks.USER_STAINED_GLASS_20;

            // Diorite blocks
            case "diorite1" -> ModBlocks.USER_DIORITE_1;
            case "diorite2" -> ModBlocks.USER_DIORITE_2;
            case "diorite3" -> ModBlocks.USER_DIORITE_3;
            case "diorite4" -> ModBlocks.USER_DIORITE_4;
            case "diorite5" -> ModBlocks.USER_DIORITE_5;
            case "diorite6" -> ModBlocks.USER_DIORITE_6;
            case "diorite7" -> ModBlocks.USER_DIORITE_7;
            case "diorite8" -> ModBlocks.USER_DIORITE_8;
            case "diorite9" -> ModBlocks.USER_DIORITE_9;
            case "diorite10" -> ModBlocks.USER_DIORITE_10;
            case "diorite11" -> ModBlocks.USER_DIORITE_11;
            case "diorite12" -> ModBlocks.USER_DIORITE_12;
            case "diorite13" -> ModBlocks.USER_DIORITE_13;
            case "diorite14" -> ModBlocks.USER_DIORITE_14;
            case "diorite15" -> ModBlocks.USER_DIORITE_15;
            case "diorite16" -> ModBlocks.USER_DIORITE_16;
            case "diorite17" -> ModBlocks.USER_DIORITE_17;
            case "diorite18" -> ModBlocks.USER_DIORITE_18;
            case "diorite19" -> ModBlocks.USER_DIORITE_19;
            case "diorite20" -> ModBlocks.USER_DIORITE_20;
            
            // Calcite blocks
            case "calcite1" -> ModBlocks.USER_CALCITE_1;
            case "calcite2" -> ModBlocks.USER_CALCITE_2;
            case "calcite3" -> ModBlocks.USER_CALCITE_3;
            case "calcite4" -> ModBlocks.USER_CALCITE_4;
            case "calcite5" -> ModBlocks.USER_CALCITE_5;
            case "calcite6" -> ModBlocks.USER_CALCITE_6;
            case "calcite7" -> ModBlocks.USER_CALCITE_7;
            case "calcite8" -> ModBlocks.USER_CALCITE_8;
            case "calcite9" -> ModBlocks.USER_CALCITE_9;
            case "calcite10" -> ModBlocks.USER_CALCITE_10;
            case "calcite11" -> ModBlocks.USER_CALCITE_11;
            case "calcite12" -> ModBlocks.USER_CALCITE_12;
            case "calcite13" -> ModBlocks.USER_CALCITE_13;
            case "calcite14" -> ModBlocks.USER_CALCITE_14;
            case "calcite15" -> ModBlocks.USER_CALCITE_15;
            case "calcite16" -> ModBlocks.USER_CALCITE_16;
            case "calcite17" -> ModBlocks.USER_CALCITE_17;
            case "calcite18" -> ModBlocks.USER_CALCITE_18;
            case "calcite19" -> ModBlocks.USER_CALCITE_19;
            case "calcite20" -> ModBlocks.USER_CALCITE_20;
            
            // Mushroom Stem blocks
            case "mushroom_stem1" -> ModBlocks.USER_MUSHROOM_STEM_1;
            case "mushroom_stem2" -> ModBlocks.USER_MUSHROOM_STEM_2;
            case "mushroom_stem3" -> ModBlocks.USER_MUSHROOM_STEM_3;
            case "mushroom_stem4" -> ModBlocks.USER_MUSHROOM_STEM_4;
            case "mushroom_stem5" -> ModBlocks.USER_MUSHROOM_STEM_5;
            case "mushroom_stem6" -> ModBlocks.USER_MUSHROOM_STEM_6;
            case "mushroom_stem7" -> ModBlocks.USER_MUSHROOM_STEM_7;
            case "mushroom_stem8" -> ModBlocks.USER_MUSHROOM_STEM_8;
            case "mushroom_stem9" -> ModBlocks.USER_MUSHROOM_STEM_9;
            case "mushroom_stem10" -> ModBlocks.USER_MUSHROOM_STEM_10;
            case "mushroom_stem11" -> ModBlocks.USER_MUSHROOM_STEM_11;
            case "mushroom_stem12" -> ModBlocks.USER_MUSHROOM_STEM_12;
            case "mushroom_stem13" -> ModBlocks.USER_MUSHROOM_STEM_13;
            case "mushroom_stem14" -> ModBlocks.USER_MUSHROOM_STEM_14;
            case "mushroom_stem15" -> ModBlocks.USER_MUSHROOM_STEM_15;
            case "mushroom_stem16" -> ModBlocks.USER_MUSHROOM_STEM_16;
            case "mushroom_stem17" -> ModBlocks.USER_MUSHROOM_STEM_17;
            case "mushroom_stem18" -> ModBlocks.USER_MUSHROOM_STEM_18;
            case "mushroom_stem19" -> ModBlocks.USER_MUSHROOM_STEM_19;
            case "mushroom_stem20" -> ModBlocks.USER_MUSHROOM_STEM_20;
            
            // Dead Tube Coral blocks
            case "dead_tube_coral1" -> ModBlocks.USER_DEAD_TUBE_CORAL_1;
            case "dead_tube_coral2" -> ModBlocks.USER_DEAD_TUBE_CORAL_2;
            case "dead_tube_coral3" -> ModBlocks.USER_DEAD_TUBE_CORAL_3;
            case "dead_tube_coral4" -> ModBlocks.USER_DEAD_TUBE_CORAL_4;
            case "dead_tube_coral5" -> ModBlocks.USER_DEAD_TUBE_CORAL_5;
            case "dead_tube_coral6" -> ModBlocks.USER_DEAD_TUBE_CORAL_6;
            case "dead_tube_coral7" -> ModBlocks.USER_DEAD_TUBE_CORAL_7;
            case "dead_tube_coral8" -> ModBlocks.USER_DEAD_TUBE_CORAL_8;
            case "dead_tube_coral9" -> ModBlocks.USER_DEAD_TUBE_CORAL_9;
            case "dead_tube_coral10" -> ModBlocks.USER_DEAD_TUBE_CORAL_10;
            case "dead_tube_coral11" -> ModBlocks.USER_DEAD_TUBE_CORAL_11;
            case "dead_tube_coral12" -> ModBlocks.USER_DEAD_TUBE_CORAL_12;
            case "dead_tube_coral13" -> ModBlocks.USER_DEAD_TUBE_CORAL_13;
            case "dead_tube_coral14" -> ModBlocks.USER_DEAD_TUBE_CORAL_14;
            case "dead_tube_coral15" -> ModBlocks.USER_DEAD_TUBE_CORAL_15;
            case "dead_tube_coral16" -> ModBlocks.USER_DEAD_TUBE_CORAL_16;
            case "dead_tube_coral17" -> ModBlocks.USER_DEAD_TUBE_CORAL_17;
            case "dead_tube_coral18" -> ModBlocks.USER_DEAD_TUBE_CORAL_18;
            case "dead_tube_coral19" -> ModBlocks.USER_DEAD_TUBE_CORAL_19;
            case "dead_tube_coral20" -> ModBlocks.USER_DEAD_TUBE_CORAL_20;
            
            // Pearlescent Froglight blocks
            case "pearlescent_froglight1" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_1;
            case "pearlescent_froglight2" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_2;
            case "pearlescent_froglight3" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_3;
            case "pearlescent_froglight4" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_4;
            case "pearlescent_froglight5" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_5;
            case "pearlescent_froglight6" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_6;
            case "pearlescent_froglight7" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_7;
            case "pearlescent_froglight8" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_8;
            case "pearlescent_froglight9" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_9;
            case "pearlescent_froglight10" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_10;
            case "pearlescent_froglight11" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_11;
            case "pearlescent_froglight12" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_12;
            case "pearlescent_froglight13" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_13;
            case "pearlescent_froglight14" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_14;
            case "pearlescent_froglight15" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_15;
            case "pearlescent_froglight16" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_16;
            case "pearlescent_froglight17" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_17;
            case "pearlescent_froglight18" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_18;
            case "pearlescent_froglight19" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_19;
            case "pearlescent_froglight20" -> ModBlocks.USER_PEARLESCENT_FROGLIGHT_20;
            
            default -> null;
        };
    }
}

