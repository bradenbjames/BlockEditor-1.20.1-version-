package com.blockeditor.mod.worldedit;

import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.content.UserBlock;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = "blockeditor")
public class WorldEditIntegration {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof Level level) {
            handleBlockPlacement(level, event.getPos(), event.getPlacedBlock());
        }
    }

    @SubscribeEvent 
    public static void onBlockChange(BlockEvent.NeighborNotifyEvent event) {
        // This catches block changes that might not trigger EntityPlaceEvent
        if (event.getLevel() instanceof Level level) {
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            
            if (state.getBlock() instanceof UserBlock) {
                LOGGER.info("WorldEditIntegration: NeighborNotifyEvent detected UserBlock at {}", pos);
                handleBlockPlacement(level, pos, state);
            }
        }
    }
    
    private static void handleBlockPlacement(Level level, BlockPos pos, BlockState newState) {
        Block block = newState.getBlock();
        
        // Only handle our UserBlock instances
        if (!(block instanceof UserBlock userBlock)) {
            return;
        }
        
        LOGGER.info("=== WorldEditIntegration: UserBlock placed at {} ===", pos);
        
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            // Get the block entity
            if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);
                
                // Extract the identifier from the block name
                String blockName = userBlock.getBlockType(); // e.g., "wool", "stone", etc.
                String blockId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath();
                
                LOGGER.info("WorldEditIntegration: Block ID: {}, Type: {}", blockId, blockName);
                
                // Parse identifier (e.g., "wool1" from "user_wool1")
                if (blockId.startsWith("user_")) {
                    String identifier = blockId.substring(5); // Remove "user_" prefix
                    LOGGER.info("WorldEditIntegration: Looking up identifier: {}", identifier);
                    
                    UserBlockRegistry.UserBlockData data = registry.getUserBlockData(identifier);
                    if (data != null) {
                        blockEntity.setColor(data.color());
                        blockEntity.setMimicBlock(data.mimicBlock());
                        blockEntity.setChanged();
                        
                        LOGGER.info("WorldEditIntegration: Applied color {} to {} block at {}", 
                            String.format("%06X", data.color()), identifier, pos);
                        
                        // Force block update to clients
                        level.sendBlockUpdated(pos, newState, newState, 3);
                    } else {
                        LOGGER.warn("WorldEditIntegration: No registry data for identifier: {}", identifier);
                    }
                }
            }
        }
    }
}