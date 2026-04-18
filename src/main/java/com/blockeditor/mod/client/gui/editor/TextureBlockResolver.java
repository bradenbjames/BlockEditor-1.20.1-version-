package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.registry.ModBlocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

public final class TextureBlockResolver {
    private TextureBlockResolver() {}

    public static Block resolve(Block selectedBlock) {
        Identifier blockId = Registries.BLOCK.getId(selectedBlock);
        String blockName = blockId.getPath().toLowerCase();

        if (blockName.contains("dirt") || blockName.contains("coarse")) {
            return ModBlocks.DYNAMIC_BLOCK_DIRT;
        } else if (blockName.contains("planks") || blockName.contains("wood")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOD;
        } else if (blockName.contains("wool")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOL;
        } else if (blockName.contains("concrete") && !blockName.contains("powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE;
        } else if (blockName.contains("concrete_powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER;
        } else if (blockName.contains("terracotta")) {
            return ModBlocks.DYNAMIC_BLOCK_TERRACOTTA;
        } else if (blockName.contains("tinted_glass")) {
            return ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS;
        } else if (blockName.contains("white_stained_glass") || blockName.contains("stained_glass")) {
            return ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS;
        } else if (blockName.contains("glass") && !blockName.contains("pane")) {
            return ModBlocks.DYNAMIC_BLOCK_GLASS;
        } else if (blockName.contains("diorite")) {
            return ModBlocks.DYNAMIC_BLOCK_DIORITE;
        } else if (blockName.contains("calcite")) {
            return ModBlocks.DYNAMIC_BLOCK_CALCITE;
        } else if (blockName.contains("mushroom_stem")) {
            return ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM;
        } else if (blockName.contains("dead_tube_coral")) {
            return ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL;
        } else if (blockName.contains("pearlescent_froglight")) {
            return ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT;
        } else if (blockName.contains("cobblestone")) {
            return ModBlocks.DYNAMIC_BLOCK_COBBLESTONE;
        } else if (blockName.contains("deepslate")) {
            return ModBlocks.DYNAMIC_BLOCK_DEEPSLATE;
        } else if (blockName.contains("sand")) {
            return ModBlocks.DYNAMIC_BLOCK_SAND;
        } else if (blockName.contains("smooth_stone")) {
            return ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE;
        } else if (blockName.contains("stone")) {
            return ModBlocks.DYNAMIC_BLOCK;
        } else {
            return ModBlocks.DYNAMIC_BLOCK;
        }
    }
}

