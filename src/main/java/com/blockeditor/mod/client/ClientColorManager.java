package com.blockeditor.mod.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import com.blockeditor.mod.registry.ModBlocks;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom colors for user blocks on the client side
 */
public class ClientColorManager {
    private static final Map<String, Integer> SAVED_USER_COLORS = new HashMap<>();
    private static final Map<String, String> SAVED_USER_MIMIC_BLOCKS = new HashMap<>();
    
    /**
     * Saves a custom color and mimic block for a user block type
     */
    public static void saveUserBlockData(String blockType, int color, String mimicBlock) {
        SAVED_USER_COLORS.put(blockType, color);
        SAVED_USER_MIMIC_BLOCKS.put(blockType, mimicBlock);
    }
    
    /**
     * Gets the saved color for a user block type
     */
    public static Integer getSavedColor(String blockType) {
        return SAVED_USER_COLORS.get(blockType);
    }
    
    /**
     * Gets the saved mimic block for a user block type
     */
    public static String getSavedMimicBlock(String blockType) {
        return SAVED_USER_MIMIC_BLOCKS.get(blockType);
    }
    
    /**
     * Creates a user block item with the saved color, mimic block, and custom name
     */
    public static ItemStack createUserBlockItem(Block userBlock, String blockType, String customName) {
        ItemStack stack = new ItemStack(userBlock);
        
        Integer savedColor = getSavedColor(blockType);
        String savedMimicBlock = getSavedMimicBlock(blockType);
        CompoundTag tag = new CompoundTag();
        if (savedColor != null) {
            tag.putString("Color", String.format("%06X", savedColor));

            // Parse color to RGB for backwards compatibility
            int red = (savedColor >> 16) & 0xFF;
            int green = (savedColor >> 8) & 0xFF;
            int blue = savedColor & 0xFF;
            tag.putInt("Red", red);
            tag.putInt("Green", green);
            tag.putInt("Blue", blue);
        }
        if (savedMimicBlock != null) {
            tag.putString("OriginalBlock", savedMimicBlock);
        }
        // Store custom name in NBT and set display name
        if (customName != null && !customName.isEmpty()) {
            tag.putString("CustomName", customName);
            stack.setHoverName(net.minecraft.network.chat.Component.literal(customName));
        }
        stack.setTag(tag);

        return stack;
    }
    
    /**
     * Registers a new custom block creation and updates the corresponding user block
     */
    public static void registerCustomBlockCreation(ItemStack customBlock) {
        CompoundTag tag = customBlock.getTag();
        if (tag == null) return;
        
        String mimicBlock = tag.getString("OriginalBlock");
        String hexColor = tag.getString("Color");
        
        if (mimicBlock.isEmpty() || hexColor.isEmpty()) return;
        
        try {
            int color = Integer.parseInt(hexColor, 16);
            String blockType = getUserBlockTypeFromMimic(mimicBlock);
            
            if (blockType != null) {
                saveUserBlockData(blockType, color, mimicBlock);
            }
        } catch (NumberFormatException e) {
            // Ignore invalid color
        }
    }
    
    /**
     * Determines the user block type based on the mimic block
     */
    private static String getUserBlockTypeFromMimic(String mimicBlock) {
        if (mimicBlock.contains("wool")) return "wool";
        if (mimicBlock.contains("stone") && !mimicBlock.contains("deepslate") && !mimicBlock.contains("smooth")) return "stone";
        if (mimicBlock.contains("concrete")) return "concrete";
        if (mimicBlock.contains("planks") || mimicBlock.contains("wood") || mimicBlock.contains("log")) return "wood";
        if (mimicBlock.contains("dirt")) return "dirt";
        if (mimicBlock.contains("sand")) return "sand";
        if (mimicBlock.contains("deepslate")) return "deepslate";
        if (mimicBlock.contains("cobblestone")) return "cobblestone";
        if (mimicBlock.contains("smooth_stone")) return "smooth_stone";
        return null;
    }
}