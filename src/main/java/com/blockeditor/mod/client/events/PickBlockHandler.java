package com.blockeditor.mod.client.events;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory;
import com.blockeditor.mod.network.ModNetworking;
import com.blockeditor.mod.network.GivePickedBlockPacket;
import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PickBlockHandler {

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton event) {
        // Check for middle click (button 2) on press
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            // Check if player is looking at a block
            HitResult hitResult = mc.hitResult;
            if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) return;

            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = mc.level.getBlockState(pos);
            Block block = state.getBlock();

            // Check if this is one of our custom blocks (DynamicBlock or UserBlock)
            var blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (blockId == null || !blockId.getNamespace().equals(BlockEditorMod.MOD_ID)) return;

            String blockPath = blockId.getPath();
            if (!blockPath.startsWith("dynamic_block") && !blockPath.startsWith("user_") && !blockPath.startsWith("u_")) return;

            // Get the block entity to extract color and mimic information
            BlockEntity be = mc.level.getBlockEntity(pos);
            if (be == null) return;

            CompoundTag beTag = be.saveWithoutMetadata();
            if (!beTag.contains("MimicBlock")) return;

            // Robustly read color from BE: it may be stored as int or string
            int color;
            String hexColor;
            if (beTag.contains("Color", Tag.TAG_INT)) {
                color = beTag.getInt("Color");
                hexColor = String.format("%06X", color);
            } else if (beTag.contains("Color", Tag.TAG_STRING)) {
                String raw = beTag.getString("Color");
                try {
                    hexColor = normalizeHex(raw);
                    color = Integer.parseInt(hexColor, 16);
                } catch (NumberFormatException ex) {
                    // Warn and fall back to default pick behavior (event not canceled)
                    com.blockeditor.mod.BlockEditorMod.LOGGER.warn("Malformed color on picked block entity: '{}'", raw);
                    return;
                }
            } else {
                return;
            }

            String mimicBlockId = beTag.getString("MimicBlock");
            String customName = beTag.contains("CustomName", Tag.TAG_STRING) ? beTag.getString("CustomName") : "";

            // (Debug logging removed: extracted BE data no longer logged to reduce noise.)

            // Search inventory for matching block using the same logic as Recent Blocks
            ResourceLocation mimicBlockRL = new ResourceLocation(mimicBlockId);
            Block mimicBlock = ForgeRegistries.BLOCKS.getValue(mimicBlockRL);
            if (mimicBlock == null) mimicBlock = net.minecraft.world.level.block.Blocks.STONE; // fallback
            
            BlockEditorHistory.CreatedBlockInfo blockInfo = new BlockEditorHistory.CreatedBlockInfo(
                mimicBlock, hexColor, color, customName
            );
            
            // First try to find and equip existing block
            boolean foundExisting = tryFindAndEquipExisting(blockInfo, mimicBlockId, !customName.isEmpty());

            if (!foundExisting) {
                // Not found in inventory: ask server to give the correct item reconstructed from BE
                ModNetworking.sendToServer(new GivePickedBlockPacket(pos));
                mc.player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§7Requesting picked block from server..."),
                    true
                );
            }

            // Cancel the event to prevent default pick block behavior
            event.setCanceled(true);
        }
    }
    
    /**
     * Try to find and equip an existing block in the player's inventory.
     * Match by Color + OriginalBlock, and if a custom name is present, also match that.
     * Uses gameMode.handlePickItem to avoid client-side desync.
     */
    private static boolean tryFindAndEquipExisting(BlockEditorHistory.CreatedBlockInfo blockInfo, String targetOriginalBlockId, boolean requireCustomName) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return false;
        }
        
        String targetColor = blockInfo.hexColor.toUpperCase();
        String targetCustomName = blockInfo.blockName != null ? blockInfo.blockName : "";

        net.minecraft.world.entity.player.Inventory inventory = minecraft.player.getInventory();
        
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            
            if (stack.isEmpty()) continue;
            
            // Check NBT tags for our custom data
            CompoundTag tag = stack.getTag();
            if (tag == null) continue;

            boolean hasColor = tag.contains("Color", Tag.TAG_STRING);
            boolean hasOrig = tag.contains("OriginalBlock", Tag.TAG_STRING);
            if (!hasColor || !hasOrig) continue;

            String stackColor = tag.getString("Color").toUpperCase();
            String stackOriginal = tag.getString("OriginalBlock");

            if (!stackColor.equals(targetColor)) continue;
            if (!stackOriginal.equals(targetOriginalBlockId)) continue;

            if (requireCustomName) {
                if (!tag.contains("CustomName", Tag.TAG_STRING)) continue;
                String stackCustomName = tag.getString("CustomName");
                if (!stackCustomName.equals(targetCustomName)) continue;
            }

            // Found the matching block! Ask server to move/select it properly
            if (minecraft.gameMode != null) {
                minecraft.gameMode.handlePickItem(slot);
            } else {
                inventory.selected = Math.min(slot, 8);
            }

            minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§a✓ Equipped from inventory: §f#" + targetColor),
                true
            );

            return true;
        }
        
        return false; // Not found
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
