package com.blockeditor.mod.events;

import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.content.UserBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles periodic refresh of user blocks to ensure colors are applied
 */
@Mod.EventBusSubscriber(modid = "blockeditor")
public class UserBlockRefreshHandler {
    private static int tickCounter = 0;
    
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        // Run every 20 ticks (1 second)
        if (tickCounter % 20 != 0) {
            return;
        }
        
        // Check all loaded chunks for user blocks that need color refresh
        for (ServerLevel level : event.getServer().getAllLevels()) {
            if (level.players().isEmpty()) {
                continue; // Skip if no players
            }
            
            // Check chunks around each player
            for (var player : level.players()) {
                int playerChunkX = player.chunkPosition().x;
                int playerChunkZ = player.chunkPosition().z;
                
                // Check 3x3 chunks around player
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        int chunkX = playerChunkX + dx;
                        int chunkZ = playerChunkZ + dz;
                        
                        if (level.hasChunk(chunkX, chunkZ)) {
                            LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                            refreshUserBlocksInChunk(chunk, level);
                        }
                    }
                }
            }
        }
    }
    
    private static void refreshUserBlocksInChunk(LevelChunk chunk, ServerLevel level) {
        // Check all block entities in the chunk
        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
            if (blockEntity instanceof DynamicBlockEntity dynamicBE) {
                // Check if this is a user block with default color (needs refresh)
                if (dynamicBE.getBlockState().getBlock() instanceof UserBlock) {
                    if (dynamicBE.getColor() == 0xFFFFFF) { // Default white color
                        // Force a color check
                        dynamicBE.forceUserBlockDataCheck();
                        
                        // Force immediate client sync after color application
                        if (dynamicBE.getColor() != 0xFFFFFF) {
                            // Color was applied, now force aggressive client update
                            level.sendBlockUpdated(dynamicBE.getBlockPos(), 
                                dynamicBE.getBlockState(), dynamicBE.getBlockState(), 3);
                            
                            // Send block entity update packet to all nearby players
                            var packet = dynamicBE.getUpdatePacket();
                            if (packet != null) {
                                for (var player : level.players()) {
                                    if (player.distanceToSqr(dynamicBE.getBlockPos().getX(), 
                                            dynamicBE.getBlockPos().getY(), dynamicBE.getBlockPos().getZ()) < 64 * 64) {
                                        player.connection.send(packet);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}