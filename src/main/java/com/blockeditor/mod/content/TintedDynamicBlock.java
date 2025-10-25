package com.blockeditor.mod.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A DynamicBlock variant for tinted glass which blocks light but remains transparent.
 * Unlike regular glass, tinted glass blocks light propagation (getLightBlock = 15)
 * while still allowing visual transparency.
 */
public class TintedDynamicBlock extends DynamicBlock {

    public TintedDynamicBlock() {
        super(BlockBehaviour.Properties.of()
            .noOcclusion()
            .strength(0.3f)
            .sound(SoundType.GLASS));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false; // Tinted glass blocks skylight propagation
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15; // Tinted glass blocks all light
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F; // Still visually bright for rendering
    }
}