package com.blockeditor.mod.content;

import net.minecraft.util.math.BlockPos;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;

/**
 * A DynamicBlock variant intended for transparent blocks like glass.
 * - noOcclusion so neighboring faces (e.g., ground under glass) are not culled
 * - lighter strength and glass sound
 * - skylight propagates and bright shade for proper appearance
 */
public class TransparentDynamicBlock extends DynamicBlock {

    public TransparentDynamicBlock() {
        super(AbstractBlock.Settings.create()
            .nonOpaque()
            .strength(0.3f)
            .sounds(BlockSoundGroup.GLASS));
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, net.minecraft.world.BlockView world, BlockPos pos) {
        return 1.0F;
    }

    // No explicit overrides for suffocation/view blocking are needed here for 1.20.1; noOcclusion prevents unwanted occlusion.
}
