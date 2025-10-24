package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class TextureBlockResolver {
    private TextureBlockResolver() {}

    public static Block resolve(Block selectedBlock) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);
        String blockName = blockId.getPath().toLowerCase();

        if (blockName.contains("dirt") || blockName.contains("coarse")) {
            return ModBlocks.DYNAMIC_BLOCK_DIRT.get();
        } else if (blockName.contains("planks") || blockName.contains("wood")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOD.get();
        } else if (blockName.contains("wool")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOL.get();
        } else if (blockName.contains("concrete") && !blockName.contains("powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE.get();
        } else if (blockName.contains("concrete_powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.get();
        } else if (blockName.contains("terracotta")) {
            return ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.get();
        } else if (blockName.contains("glass") && !blockName.contains("pane")) {
            return ModBlocks.DYNAMIC_BLOCK_GLASS.get();
        } else if (blockName.contains("diorite")) {
            return ModBlocks.DYNAMIC_BLOCK_DIORITE.get();
        } else if (blockName.contains("calcite")) {
            return ModBlocks.DYNAMIC_BLOCK_CALCITE.get();
        } else if (blockName.contains("mushroom_stem")) {
            return ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get();
        } else if (blockName.contains("dead_tube_coral")) {
            return ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get();
        } else if (blockName.contains("pearlescent_froglight")) {
            return ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get();
        } else if (blockName.contains("cobblestone")) {
            return ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get();
        } else if (blockName.contains("deepslate")) {
            return ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get();
        } else if (blockName.contains("sand")) {
            return ModBlocks.DYNAMIC_BLOCK_SAND.get();
        } else if (blockName.contains("smooth_stone")) {
            return ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get();
        } else if (blockName.contains("stone")) {
            return ModBlocks.DYNAMIC_BLOCK.get();
        } else {
            return ModBlocks.DYNAMIC_BLOCK.get();
        }
    }
}

