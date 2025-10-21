package com.blockeditor.mod.commands;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.integration.WorldEditIntegration;
import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TranslateCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("translate")
            .then(Commands.argument("blockname", StringArgumentType.word())
                .executes(TranslateCommand::translateBlock))
            .executes(TranslateCommand::listAllMappings)
        );
    }
    
    private static int translateBlock(CommandContext<CommandSourceStack> context) {
        String blockName = StringArgumentType.getString(context, "blockname");
        CommandSourceStack source = context.getSource();
        
        if (source.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Check in our mapping cache
            Map<String, String> mappings = WorldEditIntegration.getCustomNameMappings();
            String internalId = mappings.get(blockName.toLowerCase());
            
            if (internalId != null) {
                String translatedName = "be:u_" + internalId;
                source.sendSuccess(() -> Component.literal("§a'" + blockName + "' translates to: §e" + translatedName), false);
                source.sendSuccess(() -> Component.literal("§7Try using: §b//set " + translatedName), false);
                return 1;
            } else {
                // Try the registry directly
                String foundInternalId = registry.getInternalIdentifier(blockName);
                if (foundInternalId != null) {
                    String translatedName = "be:u_" + foundInternalId;
                    source.sendSuccess(() -> Component.literal("§a'" + blockName + "' translates to: §e" + translatedName), false);
                    source.sendSuccess(() -> Component.literal("§7Try using: §b//set " + translatedName), false);
                    return 1;
                } else {
                    // Don't show error message, just return 0
                    return 0;
                }
            }
        }
        
        source.sendSuccess(() -> Component.literal("§cCommand must be used by a player"), false);
        return 0;
    }
    
    private static int listAllMappings(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        if (source.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            Map<String, String> allMappings = new HashMap<>();
            Set<String> allNames = registry.getAllCustomNames();
            
            for (String customName : allNames) {
                String internalId = registry.getInternalIdentifier(customName);
                if (internalId != null) {
                    allMappings.put(customName, internalId);
                }
            }
            
            if (allMappings.isEmpty()) {
                source.sendSuccess(() -> Component.literal("§cNo custom block mappings found"), false);
                return 0;
            }
            
            source.sendSuccess(() -> Component.literal("§6Custom Block Mappings:"), false);
            allMappings.forEach((customName, internalId) -> {
                String translatedName = "be:u_" + internalId;
                source.sendSuccess(() -> Component.literal("§7'" + customName + "' -> §e" + translatedName), false);
            });
            
            source.sendSuccess(() -> Component.literal("§aTotal mappings: " + allMappings.size()), false);
            return allMappings.size();
        }
        
        source.sendSuccess(() -> Component.literal("§cCommand must be used by a player"), false);
        return 0;
    }
}