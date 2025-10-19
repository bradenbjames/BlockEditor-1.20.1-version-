package com.blockeditor.mod.commands;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

/**
 * Custom commands for the BlockEditor mod
 */
public class ModCommands {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("be")
            .then(Commands.literal("clear")
                .requires(source -> source.hasPermission(2)) // Requires op level 2
                .executes(ModCommands::clearUserBlocks)
            )
            .then(Commands.literal("refresh")
                .requires(source -> source.hasPermission(2)) // Requires op level 2
                .executes(ModCommands::refreshUserBlocks)
            )
        );
    }

    private static int clearUserBlocks(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        try {
            ServerLevel serverLevel = source.getLevel();
            if (serverLevel != null) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);
                int clearedCount = registry.clearAllUserBlocks();
                
                // Send success message to command sender
                source.sendSuccess(() -> Component.literal("§aCleared " + clearedCount + " custom user blocks from registry"), true);
                
                // Also send to all players
                for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
                    player.displayClientMessage(Component.literal("§6[BlockEditor] §aAll custom user blocks have been cleared by " + source.getTextName()), false);
                }
                
                LOGGER.info("User {} cleared {} custom user blocks", source.getTextName(), clearedCount);
                return clearedCount;
            } else {
                source.sendFailure(Component.literal("§cCommand can only be used in a server world"));
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error clearing user blocks", e);
            source.sendFailure(Component.literal("§cError clearing user blocks: " + e.getMessage()));
            return 0;
        }
    }
    
    private static int refreshUserBlocks(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        try {
            ServerLevel serverLevel = source.getLevel();
            if (serverLevel != null) {
                // Get player position for area refresh
                net.minecraft.world.phys.Vec3 pos = source.getPosition();
                int centerX = (int) pos.x;
                int centerY = (int) pos.y;
                int centerZ = (int) pos.z;
                
                int refreshed = 0;
                int radius = 16; // Reduced to 16 block radius to prevent lag
                
                // Scan area around command source
                for (int x = centerX - radius; x <= centerX + radius; x++) {
                    for (int y = Math.max(serverLevel.getMinBuildHeight(), centerY - radius); 
                         y <= Math.min(serverLevel.getMaxBuildHeight(), centerY + radius); y++) {
                        for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                            net.minecraft.core.BlockPos blockPos = new net.minecraft.core.BlockPos(x, y, z);
                            
                            if (serverLevel.getBlockEntity(blockPos) instanceof com.blockeditor.mod.content.DynamicBlockEntity blockEntity) {
                                if (blockEntity.getBlockState().getBlock() instanceof com.blockeditor.mod.content.UserBlock) {
                                    // Simple refresh - just trigger the auto-apply
                                    blockEntity.forceUserBlockDataCheck();
                                    refreshed++;
                                }
                            }
                        }
                    }
                }
                
                final int finalRefreshed = refreshed;
                source.sendSuccess(() -> Component.literal("§aRefreshed " + finalRefreshed + " user blocks in 16-block radius"), true);
                LOGGER.info("User {} refreshed {} user blocks", source.getTextName(), refreshed);
                return refreshed;
            } else {
                source.sendFailure(Component.literal("§cCommand can only be used in a server world"));
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("Error refreshing user blocks", e);
            source.sendFailure(Component.literal("§cError refreshing user blocks: " + e.getMessage()));
            return 0;
        }
    }
}