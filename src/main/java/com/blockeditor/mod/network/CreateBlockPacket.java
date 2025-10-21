package com.blockeditor.mod.network;

import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

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
        System.out.println("NETWORK ENCODE: *** SENDING NAME: '" + packet.customName + "' ***");
        buf.writeUtf(packet.hexColor);
        buf.writeUtf(packet.mimicBlockId);
        buf.writeUtf(packet.blockType);
        buf.writeUtf(packet.customName);
    }

    public static CreateBlockPacket decode(FriendlyByteBuf buf) {
        System.out.println("NETWORK DECODE: Starting decode...");
        CreateBlockPacket packet = new CreateBlockPacket(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
        System.out.println("NETWORK DECODE: *** RECEIVED NAME: '" + packet.customName + "' ***");
        return packet;
    }

    public static void handle(CreateBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        System.out.println("=== SERVER: CreateBlockPacket.handle called! ===");
        System.out.println("SERVER:   Color: " + packet.hexColor);
        System.out.println("SERVER:   Mimic: " + packet.mimicBlockId);
        System.out.println("SERVER:   Block type: " + packet.blockType);
        System.out.println("SERVER:   Custom name: " + packet.customName);

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                System.out.println("SERVER ERROR: No sender player!");
                return;
            }

            System.out.println("SERVER:   Player: " + player.getName().getString());

            // Get the user block registry
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Determine block type from the block ID
            ResourceLocation blockId = ResourceLocation.tryParse(packet.blockType);
            if (blockId == null) {
                System.out.println("ERROR: Invalid block ID: " + packet.blockType);
                return;
            }
            
            String blockPath = blockId.getPath();
            String blockType = "stone"; // default
            if (blockPath.contains("wool")) blockType = "wool";
            else if (blockPath.contains("concrete")) blockType = "concrete";
            else if (blockPath.contains("wood")) blockType = "wood";
            else if (blockPath.contains("dirt")) blockType = "dirt";
            else if (blockPath.contains("sand")) blockType = "sand";
            else if (blockPath.contains("deepslate")) blockType = "deepslate";
            else if (blockPath.contains("cobblestone")) blockType = "cobblestone";
            else if (blockPath.contains("smooth_stone")) blockType = "smooth_stone";
            
            System.out.println("SERVER: Block detection - Path: '" + blockPath + "' -> Type: '" + blockType + "'");
            
            // Parse color
            int color;
            try {
                color = Integer.parseInt(packet.hexColor, 16);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid hex color: " + packet.hexColor);
                return;
            }
            
            // Clean up custom name (make it command-safe)
            String cleanCustomName = packet.customName.toLowerCase()
                .replaceAll("[^a-z0-9_]", "_") // Replace invalid chars with underscore
                .replaceAll("_{2,}", "_") // Replace multiple underscores with single
                .replaceAll("^_|_$", ""); // Remove leading/trailing underscores
            
            if (cleanCustomName.isEmpty()) {
                cleanCustomName = "block"; // fallback name
            }
            
            // Try to assign the custom block
            String internalId = registry.assignUserBlockWithCustomName(blockType, color, packet.mimicBlockId, cleanCustomName);
            if (internalId == null) {
                // Check if it's a duplicate name or no slots available
                if (registry.getAllCustomNames().contains(cleanCustomName)) {
                    player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§cError: Block name '" + cleanCustomName + "' already exists! Try clearing the registry or use a different name."),
                        false
                    );
                } else {
                    player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§cError: No more slots available for " + blockType + " blocks!"),
                        false
                    );
                }
                return;
            }
            
            // Get the actual user block from ModBlocks
            Block userBlock = getUserBlockByIdentifier(internalId);
            if (userBlock == null) {
                System.out.println("ERROR: Could not find user block for identifier: " + internalId);
                return;
            }

            System.out.println("Creating item stack for user block: " + userBlock + " with custom name: " + cleanCustomName);

            // Create the item stack
            ItemStack coloredBlock = new ItemStack(userBlock);

            // Create NBT data
            CompoundTag customData = new CompoundTag();
            customData.putString("Color", packet.hexColor);
            customData.putString("OriginalBlock", packet.mimicBlockId);
            customData.putString("CustomName", cleanCustomName);

            // Parse color to RGB (reuse the already parsed color variable)
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;
            customData.putInt("Red", red);
            customData.putInt("Green", green);
            customData.putInt("Blue", blue);

            // Set NBT tag for 1.20.1
            coloredBlock.setTag(customData);

            // Set display name using custom name
            coloredBlock.setHoverName(
                net.minecraft.network.chat.Component.literal("§r" + cleanCustomName + " §7(#" + packet.hexColor + ")")
            );

            System.out.println("Adding item to player inventory...");

            // Try to add to the current selected slot first, then hotbar, then inventory
            boolean itemPlaced = false;
            
            // Get the currently selected hotbar slot
            int selectedSlot = player.getInventory().selected;
            
            // If the selected slot is empty, place it there
            if (player.getInventory().getItem(selectedSlot).isEmpty()) {
                player.getInventory().setItem(selectedSlot, coloredBlock);
                itemPlaced = true;
                System.out.println("Item placed in selected hotbar slot: " + selectedSlot);
            } else {
                // Try to add to inventory normally
                if (player.getInventory().add(coloredBlock)) {
                    itemPlaced = true;
                    System.out.println("Item added to inventory successfully!");
                } else {
                    // If inventory is full, drop the item
                    player.drop(coloredBlock, false);
                    System.out.println("Inventory full, dropping item");
                }
            }
            
            // Send a more prominent notification
            if (itemPlaced) {
                player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§a§lBlock Created! §r§7Check your hotbar or inventory"), 
                    true // Show as action bar message
                );
            }
        });

        context.setPacketHandled(true);
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
            
            default -> null;
        };
    }
}