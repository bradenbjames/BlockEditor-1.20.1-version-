package com.blockeditor.mod.commands;

import com.blockeditor.mod.integration.WorldEditIntegration;
import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DebugCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("bedebug")
            .requires(source -> source.hasPermissionLevel(2)) // Requires OP
            .executes(DebugCommand::execute)
        );
    }
    
    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            // Get the overworld level
            ServerWorld overworld = source.getServer().getWorld(World.OVERWORLD);
            if (overworld == null) {
                source.sendFeedback(() -> Text.literal("§cError: Could not get overworld level"), false);
                return 0;
            }
            
            // Get the user block registry
            UserBlockRegistry registry = UserBlockRegistry.get(overworld);
            
            // Show all custom names
            source.sendFeedback(() -> Text.literal("§a=== Block Editor Debug Info ==="), false);
            source.sendFeedback(() -> Text.literal("§6Custom blocks in registry:"), false);
            
            if (registry.getAllCustomNames().isEmpty()) {
                source.sendFeedback(() -> Text.literal("§7  No custom blocks found"), false);
            } else {
                for (String customName : registry.getAllCustomNames()) {
                    String internalId = registry.getInternalIdentifier(customName);
                    source.sendFeedback(() -> Text.literal("§7  " + customName + " -> " + internalId), false);
                }
            }
            
            // Show WorldEdit mappings
            source.sendFeedback(() -> Text.literal("§6WorldEdit mappings:"), false);
            var mappings = WorldEditIntegration.getCustomNameMappings();
            if (mappings.isEmpty()) {
                source.sendFeedback(() -> Text.literal("§7  No WorldEdit mappings found"), false);
            } else {
                for (var entry : mappings.entrySet()) {
                    source.sendFeedback(() -> Text.literal("§7  " + entry.getKey() + " -> " + entry.getValue()), false);
                }
            }
            
            return 1;
            
        } catch (Exception e) {
            source.sendFeedback(() -> Text.literal("§cError: " + e.getMessage()), false);
            e.printStackTrace();
            return 0;
        }
    }
}