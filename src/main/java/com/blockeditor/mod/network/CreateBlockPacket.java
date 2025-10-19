package com.blockeditor.mod.network;

import com.blockeditor.mod.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CreateBlockPacket {

    private final String hexColor;
    private final String mimicBlockId;
    private final String blockType;

    public CreateBlockPacket(String hexColor, String mimicBlockId, String blockType) {
        this.hexColor = hexColor;
        this.mimicBlockId = mimicBlockId;
        this.blockType = blockType;
    }

    public static void encode(CreateBlockPacket packet, FriendlyByteBuf buf) {
        System.out.println("CreateBlockPacket.encode called");
        buf.writeUtf(packet.hexColor);
        buf.writeUtf(packet.mimicBlockId);
        buf.writeUtf(packet.blockType);
    }

    public static CreateBlockPacket decode(FriendlyByteBuf buf) {
        System.out.println("CreateBlockPacket.decode called");
        return new CreateBlockPacket(buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(CreateBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        System.out.println("CreateBlockPacket.handle called!");
        System.out.println("  Color: " + packet.hexColor);
        System.out.println("  Mimic: " + packet.mimicBlockId);
        System.out.println("  Block type: " + packet.blockType);

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                System.out.println("ERROR: No sender player!");
                return;
            }

            System.out.println("  Player: " + player.getName().getString());

            // Parse block type
            ResourceLocation blockId = ResourceLocation.tryParse(packet.blockType);
            if (blockId == null) {
                System.out.println("ERROR: Invalid block ID: " + packet.blockType);
                return;
            }

            Block block = BuiltInRegistries.BLOCK.get(blockId);
            if (block == null) {
                System.out.println("ERROR: Block not found: " + blockId);
                return;
            }

            System.out.println("Creating item stack for block: " + block);

            // Create the item stack
            ItemStack coloredBlock = new ItemStack(block);

            // Create NBT data
            CompoundTag customData = new CompoundTag();
            customData.putString("Color", packet.hexColor);
            customData.putString("OriginalBlock", packet.mimicBlockId);

            // Parse color to RGB
            try {
                int color = Integer.parseInt(packet.hexColor, 16);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;
                customData.putInt("Red", red);
                customData.putInt("Green", green);
                customData.putInt("Blue", blue);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid hex color: " + packet.hexColor);
                return;
            }

            // Set NBT tag for 1.20.1
            coloredBlock.setTag(customData);

            // Set display name
            ResourceLocation mimicId = ResourceLocation.tryParse(packet.mimicBlockId);
            if (mimicId != null) {
                String blockName = mimicId.getPath().replace("_", " ");
                coloredBlock.setHoverName(
                    net.minecraft.network.chat.Component.literal("§r" + blockName + " §7(#" + packet.hexColor + ")"));
            }

            System.out.println("Adding item to player inventory...");

            // Add to player inventory or drop
            if (!player.getInventory().add(coloredBlock)) {
                System.out.println("Inventory full, dropping item");
                player.drop(coloredBlock, false);
            } else {
                System.out.println("Item added to inventory successfully!");
            }
        });

        context.setPacketHandled(true);
    }
}