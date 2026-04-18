package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;

public final class InventoryHelper {
    private InventoryHelper() {}

    public static void findAndEquipBlock(BlockEditorHistory.CreatedBlockInfo blockInfo) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null) {
            return;
        }

        String targetColor = blockInfo.hexColor.toUpperCase();
        String targetCustomName = blockInfo.blockName;

        net.minecraft.entity.player.PlayerInventory inventory = minecraft.player.getInventory();

        int foundSlot = -1;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty()) continue;

            NbtCompound tag = stack.getNbt();
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
            // If the item exists in the hotbar, just select it.
            if (foundSlot < 9) {
                inventory.selectedSlot = foundSlot;
                minecraft.player.sendMessage(
                    net.minecraft.text.Text.literal("§a✓ Equipped: §f" + targetCustomName + " §7(#" + targetColor + ")"),
                    true
                );
                if (minecraft.currentScreen != null) {
                    minecraft.currentScreen.close();
                }
                return;
            }
            // Found in main inventory: request server-authoritative move into the hotbar (swaps if hotbar is full)
            if (minecraft.interactionManager != null) {
                minecraft.interactionManager.pickFromInventory(foundSlot);
                minecraft.player.sendMessage(
                    net.minecraft.text.Text.literal("§a✓ Equipped from inventory: §f" + targetCustomName + " §7(#" + targetColor + ")"),
                    true
                );
                if (minecraft.currentScreen != null) {
                    minecraft.currentScreen.close();
                }
                return;
            }
            // If gameMode is unexpectedly null, fall through to server creation as a safe fallback.
        }

        // Not found in hotbar or gameMode unavailable: request server to recreate and give the item based on history entry
        try {
            // Determine mimic block id and dynamic block type id
            var mimicKey = net.minecraft.registry.Registries.BLOCK.getId(blockInfo.originalBlock);
            if (mimicKey == null) {
                minecraft.player.sendMessage(
                    net.minecraft.text.Text.literal("§cInternal error: Missing block id for history entry"),
                    true
                );
                return;
            }
            var dynamicBlock = TextureBlockResolver.resolve(blockInfo.originalBlock);
            var dynamicKey = net.minecraft.registry.Registries.BLOCK.getId(dynamicBlock);
            if (dynamicKey == null) {
                minecraft.player.sendMessage(
                    net.minecraft.text.Text.literal("§cInternal error: Could not resolve block type"),
                    true
                );
                return;
            }

            // Only treat blockInfo.blockName as a custom name if it differs from the auto-generated default
            String computedDefaultName;
            {
                var rl = net.minecraft.registry.Registries.BLOCK.getId(blockInfo.originalBlock);
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

            minecraft.player.sendMessage(
                net.minecraft.text.Text.literal("§7Requesting picked block from server..."),
                true
            );

            // Optionally close the screen so the item appears in hand
            if (minecraft.currentScreen != null) {
                minecraft.currentScreen.close();
            }
        } catch (Exception ex) {
            minecraft.player.sendMessage(
                net.minecraft.text.Text.literal("§cFailed to request block from server: " + ex.getMessage()),
                true
            );
        }
    }

    private static int findBestHotbarSlot(net.minecraft.entity.player.PlayerInventory inventory) {
        int currentSlot = inventory.selectedSlot;
        if (inventory.getStack(currentSlot).isEmpty()) {
            return currentSlot;
        }
        for (int slot = 0; slot < 9; slot++) {
            if (inventory.getStack(slot).isEmpty()) {
                return slot;
            }
        }
        return currentSlot;
    }
}
