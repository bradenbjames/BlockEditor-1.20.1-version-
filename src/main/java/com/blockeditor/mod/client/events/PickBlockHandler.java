package com.blockeditor.mod.client.events;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory;
import com.blockeditor.mod.network.ModNetworking;
import com.blockeditor.mod.network.GivePickedBlockPacket;
import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.registry.Registries;

public class PickBlockHandler {

    /**
     * Called from DoItemPickMixin to intercept middle-click pick block.
     * Returns true if we handled it (cancelling vanilla behavior).
     */
    public static boolean onPickBlock() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return false;

        HitResult hitResult = mc.crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) return false;

        BlockHitResult blockHit = (BlockHitResult) hitResult;
        BlockPos pos = blockHit.getBlockPos();
        BlockState state = mc.world.getBlockState(pos);
        Block block = state.getBlock();

        var blockId = Registries.BLOCK.getId(block);
        if (blockId == null || !blockId.getNamespace().equals(BlockEditorMod.MOD_ID)) return false;

        String blockPath = blockId.getPath();
        if (!blockPath.startsWith("dynamic_block") && !blockPath.startsWith("user_") && !blockPath.startsWith("u_")) return false;

        BlockEntity be = mc.world.getBlockEntity(pos);
        if (be == null) return false;

        NbtCompound beTag = be.createNbt();
        if (!beTag.contains("MimicBlock")) return false;

        int color;
        String hexColor;
        if (beTag.contains("Color", NbtElement.INT_TYPE)) {
            color = beTag.getInt("Color");
            hexColor = String.format("%06X", color);
        } else if (beTag.contains("Color", NbtElement.STRING_TYPE)) {
            String raw = beTag.getString("Color");
            try {
                hexColor = normalizeHex(raw);
                color = Integer.parseInt(hexColor, 16);
            } catch (NumberFormatException ex) {
                BlockEditorMod.LOGGER.warn("Malformed color on picked block entity: '{}'", raw);
                return false;
            }
        } else {
            return false;
        }

        String mimicBlockId = beTag.getString("MimicBlock");
        String customName = beTag.contains("CustomName", NbtElement.STRING_TYPE) ? beTag.getString("CustomName") : "";

        Identifier mimicBlockRL = new Identifier(mimicBlockId);
        Block mimicBlock = Registries.BLOCK.get(mimicBlockRL);
        if (mimicBlock == null) mimicBlock = net.minecraft.block.Blocks.STONE;

        BlockEditorHistory.CreatedBlockInfo blockInfo = new BlockEditorHistory.CreatedBlockInfo(
            mimicBlock, hexColor, color, customName
        );

        boolean foundExisting = tryFindAndEquipExisting(blockInfo, mimicBlockId, !customName.isEmpty());

        if (!foundExisting) {
            ModNetworking.sendToServer(new GivePickedBlockPacket(pos));
            mc.player.sendMessage(
                net.minecraft.text.Text.literal("\u00A77Requesting picked block from server..."),
                true
            );
        }

        return true; // We handled it
    }

    private static boolean tryFindAndEquipExisting(BlockEditorHistory.CreatedBlockInfo blockInfo, String targetOriginalBlockId, boolean requireCustomName) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null) return false;

        String targetColor = blockInfo.hexColor.toUpperCase();
        String targetCustomName = blockInfo.blockName != null ? blockInfo.blockName : "";

        net.minecraft.entity.player.PlayerInventory inventory = minecraft.player.getInventory();

        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty()) continue;

            NbtCompound tag = stack.getNbt();
            if (tag == null) continue;

            boolean hasColor = tag.contains("Color", NbtElement.STRING_TYPE);
            boolean hasOrig = tag.contains("OriginalBlock", NbtElement.STRING_TYPE);
            if (!hasColor || !hasOrig) continue;

            String stackColor = tag.getString("Color").toUpperCase();
            String stackOriginal = tag.getString("OriginalBlock");

            if (!stackColor.equals(targetColor)) continue;
            if (!stackOriginal.equals(targetOriginalBlockId)) continue;

            if (requireCustomName) {
                if (!tag.contains("CustomName", NbtElement.STRING_TYPE)) continue;
                String stackCustomName = tag.getString("CustomName");
                if (!stackCustomName.equals(targetCustomName)) continue;
            }

            if (minecraft.interactionManager != null) {
                minecraft.interactionManager.pickFromInventory(slot);
            } else {
                inventory.selectedSlot = Math.min(slot, 8);
            }

            minecraft.player.sendMessage(
                net.minecraft.text.Text.literal("\u00A7a\u2713 Equipped from inventory: \u00A7f#" + targetColor),
                true
            );

            return true;
        }

        return false;
    }

    private static String normalizeHex(String raw) {
        if (raw == null) return "FFFFFF";
        String s = raw.trim();
        if (s.startsWith("#")) s = s.substring(1);
        if (s.startsWith("0x") || s.startsWith("0X")) s = s.substring(2);
        s = s.replaceAll("[^0-9A-Fa-f]", "");
        if (s.isEmpty()) return "FFFFFF";
        if (s.length() > 6) s = s.substring(s.length() - 6);
        if (s.length() < 6) s = ("000000" + s).substring(s.length());
        return s.toUpperCase();
    }
}
