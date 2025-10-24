package com.blockeditor.mod.client.gui.editor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public final class BlockAllowList {
    private BlockAllowList() {}

    private static final String[] ALLOWED = new String[] {
        "white_wool",
        "sand",
        "stone",
        "smooth_stone",
        "white_concrete",
        "oak_planks",
        "bamboo_planks",
        "cobblestone",
        "deepslate",
        "white_terracotta",
        "white_concrete_powder",
        "glass",
        "diorite",
        "calcite",
        "mushroom_stem",
        "dead_tube_coral_block",
        "pearlescent_froglight"
    };

    public static boolean isAllowed(Block block) {
        String id = BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase();
        for (String allow : ALLOWED) {
            if (id.equals(allow)) return true;
        }
        return false;
    }
}

