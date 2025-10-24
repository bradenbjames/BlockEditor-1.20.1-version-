package com.blockeditor.mod.client.gui.editor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEditorUtils {
    public static boolean isFullSolidBlock(Block block) {
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
        String blockId = key != null ? key.getPath().toLowerCase() : "";
        String[] allowedBlocks = {
            "white_wool", "sand", "stone", "smooth_stone", "white_concrete", "oak_planks", "bamboo_planks",
            "cobblestone", "deepslate", "white_terracotta", "white_concrete_powder", "glass", "diorite",
            "calcite", "mushroom_stem", "dead_tube_coral_block", "pearlescent_froglight"
        };
        for (String allowedBlock : allowedBlocks) {
            if (blockId.equals(allowedBlock)) {
                return true;
            }
        }
        return false;
    }

    public static int parseHexColor(String hex) {
        try {
            hex = hex.replace("#", "").trim();
            while (hex.length() < 6) {
                hex = "0" + hex;
            }
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFF;
        }
    }

    public static boolean isValidBlockName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Allow letters, numbers, space, underscore, apostrophe, dot, parentheses, and hyphen
        return name.matches("^[a-zA-Z0-9 _'.()-]+$") && name.trim().length() >= 1 && name.trim().length() <= 32;
    }
}
