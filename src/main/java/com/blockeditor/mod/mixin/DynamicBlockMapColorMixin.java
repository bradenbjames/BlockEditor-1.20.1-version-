package com.blockeditor.mod.mixin;

import com.blockeditor.mod.content.DynamicBlock;
import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.util.MapColorHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class DynamicBlockMapColorMixin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static int logCounter = 0;

    @Inject(method = "getMapColor", at = @At("HEAD"), cancellable = true)
    private void onGetMapColor(BlockView world, BlockPos pos, CallbackInfoReturnable<MapColor> cir) {
        AbstractBlock.AbstractBlockState self = (AbstractBlock.AbstractBlockState) (Object) this;
        Block block = self.getBlock();
        if (block instanceof DynamicBlock) {
            if (pos == null) return;

            // Use the thread-safe cache instead of getBlockEntity (which isn't safe from map threads)
            int color = DynamicBlockEntity.getCachedColor(pos);

            // Log first 50 calls to help debug
            boolean shouldLog = logCounter < 50;
            if (shouldLog) logCounter++;

            if (color >= 0) {
                MapColor result = MapColorHelper.nearest(color);
                if (shouldLog) {
                    LOGGER.info("[MapColor] DynamicBlock at {} - cachedColor=#{}, mapColor=#{} (id={})",
                        pos, String.format("%06X", color), String.format("%06X", result.color), result.id);
                }
                cir.setReturnValue(result);
            } else {
                if (shouldLog) {
                    LOGGER.info("[MapColor] DynamicBlock at {} - no cached color, thread={}",
                        pos, Thread.currentThread().getName());
                }
            }
        }
    }
}
