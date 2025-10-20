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
        System.out.println("CreateBlockPacket.encode called");
        buf.writeUtf(packet.hexColor);
        buf.writeUtf(packet.mimicBlockId);
        buf.writeUtf(packet.blockType);
        buf.writeUtf(packet.customName);
    }

    public static CreateBlockPacket decode(FriendlyByteBuf buf) {
        System.out.println("CreateBlockPacket.decode called");
        return new CreateBlockPacket(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(CreateBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        System.out.println("CreateBlockPacket.handle called!");
        System.out.println("  Color: " + packet.hexColor);
        System.out.println("  Mimic: " + packet.mimicBlockId);
        System.out.println("  Block type: " + packet.blockType);
        System.out.println("  Custom name: " + packet.customName);

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                System.out.println("ERROR: No sender player!");
                return;
            }

            System.out.println("  Player: " + player.getName().getString());

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
                // Custom name already exists or no slots available
                player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§cError: Block name '" + cleanCustomName + "' already exists or no slots available!"),
                    false
                );
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
            case "wool1" -> ModBlocks.USER_WOOL_1.get();
            case "wool2" -> ModBlocks.USER_WOOL_2.get();
            case "wool3" -> ModBlocks.USER_WOOL_3.get();
            case "wool4" -> ModBlocks.USER_WOOL_4.get();
            case "wool5" -> ModBlocks.USER_WOOL_5.get();
            case "stone1" -> ModBlocks.USER_STONE_1.get();
            case "stone2" -> ModBlocks.USER_STONE_2.get();
            case "stone3" -> ModBlocks.USER_STONE_3.get();
            case "stone4" -> ModBlocks.USER_STONE_4.get();
            case "stone5" -> ModBlocks.USER_STONE_5.get();
            case "concrete1" -> ModBlocks.USER_CONCRETE_1.get();
            case "concrete2" -> ModBlocks.USER_CONCRETE_2.get();
            case "concrete3" -> ModBlocks.USER_CONCRETE_3.get();
            case "concrete4" -> ModBlocks.USER_CONCRETE_4.get();
            case "concrete5" -> ModBlocks.USER_CONCRETE_5.get();
            default -> null;
        };
    }
}