package com.blockeditor.mod.commands;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;

/**
 * Custom commands for the BlockEditor mod
 */
public class ModCommands {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("be")
            .then(CommandManager.literal("clear")
                .requires(source -> source.hasPermissionLevel(2)) // Requires op level 2
                .executes(ModCommands::clearUserBlocks)
            )
            .then(CommandManager.literal("refresh")
                .requires(source -> source.hasPermissionLevel(2)) // Requires op level 2
                .executes(ModCommands::refreshUserBlocks)
            )
        );
    }

    private static int clearUserBlocks(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            ServerWorld serverLevel = source.getWorld();
            if (serverLevel != null) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);
                int clearedCount = registry.clearAllUserBlocks();
                
                // Send success message to command sender
                source.sendFeedback(() -> Text.literal("§aCleared " + clearedCount + " custom user blocks from registry"), true);
                
                // Also send to all players
                for (ServerPlayerEntity player : serverLevel.getServer().getPlayerManager().getPlayerList()) {
                    player.sendMessage(Text.literal("§6[BlockEditor] §aAll custom user blocks have been cleared by " + source.getName()), false);
                }
                
                LOGGER.info("User {} cleared {} custom user blocks", source.getName(), clearedCount);
                return clearedCount;
            } else {
                source.sendError(Text.literal("§cCommand can only be used in a server world"));
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error clearing user blocks", e);
            source.sendError(Text.literal("§cError clearing user blocks: " + e.getMessage()));
            return 0;
        }
    }
    
    private static int refreshUserBlocks(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            ServerWorld serverLevel = source.getWorld();
            if (serverLevel != null) {
                // Get player position for area refresh
                net.minecraft.util.math.Vec3d pos = source.getPosition();
                int centerX = (int) pos.x;
                int centerY = (int) pos.y;
                int centerZ = (int) pos.z;
                
                int refreshed = 0;
                int radius = 16; // Reduced to 16 block radius to prevent lag
                
                // Scan area around command source
                for (int x = centerX - radius; x <= centerX + radius; x++) {
                    for (int y = Math.max(serverLevel.getBottomY(), centerY - radius); 
                         y <= Math.min(serverLevel.getTopY(), centerY + radius); y++) {
                        for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                            net.minecraft.util.math.BlockPos blockPos = new net.minecraft.util.math.BlockPos(x, y, z);
                            
                            if (serverLevel.getBlockEntity(blockPos) instanceof com.blockeditor.mod.content.DynamicBlockEntity blockEntity) {
                                if (blockEntity.getCachedState().getBlock() instanceof com.blockeditor.mod.content.UserBlock) {
                                    // Simple refresh - just trigger the auto-apply
                                    blockEntity.forceUserBlockDataCheck();
                                    refreshed++;
                                }
                            }
                        }
                    }
                }
                
                final int finalRefreshed = refreshed;
                source.sendFeedback(() -> Text.literal("§aRefreshed " + finalRefreshed + " user blocks in 16-block radius"), true);
                LOGGER.info("User {} refreshed {} user blocks", source.getName(), refreshed);
                return refreshed;
            } else {
                source.sendError(Text.literal("§cCommand can only be used in a server world"));
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error refreshing user blocks", e);
            source.sendError(Text.literal("§cError refreshing user blocks: " + e.getMessage()));
            return 0;
        }
    }
}