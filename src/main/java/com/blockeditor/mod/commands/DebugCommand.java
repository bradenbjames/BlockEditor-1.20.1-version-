package com.blockeditor.mod.commands;

import com.blockeditor.mod.integration.WorldEditIntegration;
import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class DebugCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("bedebug")
            .requires(source -> source.hasPermission(2)) // Requires OP
            .executes(DebugCommand::execute)
        );
    }
    
    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        try {
            // Get the overworld level
            ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
            if (overworld == null) {
                source.sendSuccess(() -> Component.literal("§cError: Could not get overworld level"), false);
                return 0;
            }
            
            // Get the user block registry
            UserBlockRegistry registry = UserBlockRegistry.get(overworld);
            
            // Show all custom names
            source.sendSuccess(() -> Component.literal("§a=== Block Editor Debug Info ==="), false);
            source.sendSuccess(() -> Component.literal("§6Custom blocks in registry:"), false);
            
            if (registry.getAllCustomNames().isEmpty()) {
                source.sendSuccess(() -> Component.literal("§7  No custom blocks found"), false);
            } else {
                for (String customName : registry.getAllCustomNames()) {
                    String internalId = registry.getInternalIdentifier(customName);
                    source.sendSuccess(() -> Component.literal("§7  " + customName + " -> " + internalId), false);
                }
            }
            
            // Show WorldEdit mappings
            source.sendSuccess(() -> Component.literal("§6WorldEdit mappings:"), false);
            var mappings = WorldEditIntegration.getCustomNameMappings();
            if (mappings.isEmpty()) {
                source.sendSuccess(() -> Component.literal("§7  No WorldEdit mappings found"), false);
            } else {
                for (var entry : mappings.entrySet()) {
                    source.sendSuccess(() -> Component.literal("§7  " + entry.getKey() + " -> " + entry.getValue()), false);
                }
            }
            
            return 1;
            
        } catch (Exception e) {
            source.sendSuccess(() -> Component.literal("§cError: " + e.getMessage()), false);
            e.printStackTrace();
            return 0;
        }
    }
}