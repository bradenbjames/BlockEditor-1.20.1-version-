package com.blockeditor.mod.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A DynamicBlock variant intended for transparent blocks like glass.
 * - noOcclusion so neighboring faces (e.g., ground under glass) are not culled
 * - lighter strength and glass sound
 * - skylight propagates and bright shade for proper appearance
 */
public class TransparentDynamicBlock extends DynamicBlock {

    public TransparentDynamicBlock() {
        super(BlockBehaviour.Properties.of()
            .noOcclusion()
            .strength(0.3f)
            .sound(SoundType.GLASS));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    // No explicit overrides for suffocation/view blocking are needed here for 1.20.1; noOcclusion prevents unwanted occlusion.
}
