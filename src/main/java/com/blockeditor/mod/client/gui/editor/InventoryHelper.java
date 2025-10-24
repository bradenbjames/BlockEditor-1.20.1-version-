package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class InventoryHelper {
    private InventoryHelper() {}

    public static void findAndEquipBlock(BlockEditorHistory.CreatedBlockInfo blockInfo) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        String targetColor = blockInfo.hexColor.toUpperCase();
        String targetCustomName = blockInfo.blockName;

        net.minecraft.world.entity.player.Inventory inventory = minecraft.player.getInventory();

        int foundSlot = -1;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) continue;

            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Color") && tag.contains("CustomName")) {
                String stackColor = tag.getString("Color").toUpperCase();
                String stackCustomName = tag.getString("CustomName");

                if (stackColor.equals(targetColor) && stackCustomName.equals(targetCustomName)) {
                    foundSlot = slot;
                    break;
                }
            }
        }

        if (foundSlot != -1) {
            // If the item exists in the hotbar, just select it. Do NOT mutate inventory contents client-side.
            if (foundSlot < 9) {
                inventory.selected = foundSlot;
                minecraft.player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§a✓ Equipped: §f" + targetCustomName + " §7(#" + targetColor + ")"),
                    true
                );
                if (minecraft.screen != null) {
                    minecraft.screen.onClose();
                }
                return;
            }
            // If it's in the main inventory (slot >= 9), avoid client-only swaps.
            // Fall through to request the server to provide an item in hand instead.
        }

        // Not found in hotbar (or only found in main inventory): request server to recreate and give the item based on history entry
        try {
            // Determine mimic block id and dynamic block type id
            var mimicKey = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(blockInfo.originalBlock);
            if (mimicKey == null) {
                minecraft.player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§cInternal error: Missing block id for history entry"),
                    true
                );
                return;
            }
            var dynamicBlock = TextureBlockResolver.resolve(blockInfo.originalBlock);
            var dynamicKey = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(dynamicBlock);
            if (dynamicKey == null) {
                minecraft.player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§cInternal error: Could not resolve block type"),
                    true
                );
                return;
            }

            // Only treat blockInfo.blockName as a custom name if it differs from the auto-generated default
            String computedDefaultName;
            {
                var rl = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(blockInfo.originalBlock);
                String path = rl.getPath();
                // Mirror BlockEditorHistory default naming
                if (path.startsWith("dynamic_block_")) {
                    computedDefaultName = path.substring(14).replace('_', ' ') + " #" + targetColor;
                } else if ("dynamic_block".equals(path)) {
                    computedDefaultName = "stone #" + targetColor;
                } else {
                    computedDefaultName = path.replace('_', ' ') + " #" + targetColor;
                }
            }
            String customNameToUse = blockInfo.blockName != null && !blockInfo.blockName.isBlank()
                && !blockInfo.blockName.equalsIgnoreCase(computedDefaultName)
                ? blockInfo.blockName
                : ""; // no explicit custom name

            // Send packet to create the item on server
            com.blockeditor.mod.network.ModNetworking.sendToServer(
                new com.blockeditor.mod.network.CreateBlockPacket(
                    targetColor,
                    mimicKey.toString(),
                    dynamicKey.toString(),
                    customNameToUse
                )
            );

            minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§7Requesting picked block from server..."),
                true
            );

            // Optionally close the screen so the item appears in hand
            if (minecraft.screen != null) {
                minecraft.screen.onClose();
            }
        } catch (Exception ex) {
            minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cFailed to request block from server: " + ex.getMessage()),
                true
            );
        }
    }

    private static int findBestHotbarSlot(net.minecraft.world.entity.player.Inventory inventory) {
        int currentSlot = inventory.selected;
        if (inventory.getItem(currentSlot).isEmpty()) {
            return currentSlot;
        }
        for (int slot = 0; slot < 9; slot++) {
            if (inventory.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return currentSlot;
    }
}
