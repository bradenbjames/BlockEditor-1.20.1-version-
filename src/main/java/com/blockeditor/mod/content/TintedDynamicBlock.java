package com.blockeditor.mod.content;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;

/**
 * A DynamicBlock variant for tinted glass which blocks light but remains transparent.
 * Unlike regular glass, tinted glass blocks light propagation (getLightBlock = 15)
 * while still allowing visual transparency.
 */
public class TintedDynamicBlock extends DynamicBlock {

    public TintedDynamicBlock() {
        super(AbstractBlock.Settings.create()
            .nonOpaque()
            .strength(0.3f)
            .sounds(BlockSoundGroup.GLASS));
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 15; // Tinted glass blocks all light
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F; // Still visually bright for rendering
    }
}