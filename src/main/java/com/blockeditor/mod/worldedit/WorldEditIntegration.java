package com.blockeditor.mod.worldedit;

import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.content.UserBlock;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles applying stored UserBlock data (color, mimic) when blocks are placed
 * programmatically (e.g. by WorldEdit). In Fabric, there is no global block-place
 * event, so this logic is called from UserBlock.onPlaced() and from
 * DynamicBlockEntity when it first loads with no data.
 */
public class WorldEditIntegration {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Call this when a UserBlock is placed at a position to apply stored registry data.
     */
    public static void handleBlockPlacement(World world, BlockPos pos, BlockState newState) {
        Block block = newState.getBlock();

        if (!(block instanceof UserBlock userBlock)) {
            return;
        }

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            if (world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverWorld);

                String blockId = net.minecraft.registry.Registries.BLOCK.getId(block).getPath();

                if (blockId.startsWith("u_")) {
                    String identifier = blockId.substring(2);

                    UserBlockRegistry.UserBlockData data = registry.getUserBlockData(identifier);
                    if (data != null) {
                        blockEntity.setColor(data.color());
                        blockEntity.setMimicBlock(data.mimicBlock());
                        blockEntity.markDirty();

                        world.updateListeners(pos, newState, newState, 3);
                    } else {
                        LOGGER.warn("WorldEditIntegration: No registry data for identifier: {}", identifier);
                    }
                }
            }
        }
    }
}