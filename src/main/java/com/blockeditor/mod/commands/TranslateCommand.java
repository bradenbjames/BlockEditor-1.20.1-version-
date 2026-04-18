package com.blockeditor.mod.commands;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.integration.WorldEditIntegration;
import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TranslateCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("translate")
            .then(CommandManager.argument("blockname", StringArgumentType.word())
                .executes(TranslateCommand::translateBlock))
            .executes(TranslateCommand::listAllMappings)
        );
    }
    
    private static int translateBlock(CommandContext<ServerCommandSource> context) {
        String blockName = StringArgumentType.getString(context, "blockname");
        ServerCommandSource source = context.getSource();
        
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            ServerWorld level = player.getServerWorld();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Check in our mapping cache
            Map<String, String> mappings = WorldEditIntegration.getCustomNameMappings();
            String internalId = mappings.get(blockName.toLowerCase());
            
            if (internalId != null) {
                String translatedName = "be:u_" + internalId;
                source.sendFeedback(() -> Text.literal("§a'" + blockName + "' translates to: §e" + translatedName), false);
                source.sendFeedback(() -> Text.literal("§7Try using: §b//set " + translatedName), false);
                return 1;
            } else {
                // Try the registry directly
                String foundInternalId = registry.getInternalIdentifier(blockName);
                if (foundInternalId != null) {
                    String translatedName = "be:u_" + foundInternalId;
                    source.sendFeedback(() -> Text.literal("§a'" + blockName + "' translates to: §e" + translatedName), false);
                    source.sendFeedback(() -> Text.literal("§7Try using: §b//set " + translatedName), false);
                    return 1;
                } else {
                    // Don't show error message, just return 0
                    return 0;
                }
            }
        }
        
        source.sendFeedback(() -> Text.literal("§cCommand must be used by a player"), false);
        return 0;
    }
    
    private static int listAllMappings(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            ServerWorld level = player.getServerWorld();
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
                source.sendFeedback(() -> Text.literal("§cNo custom block mappings found"), false);
                return 0;
            }
            
            source.sendFeedback(() -> Text.literal("§6Custom Block Mappings:"), false);
            allMappings.forEach((customName, internalId) -> {
                String translatedName = "be:u_" + internalId;
                source.sendFeedback(() -> Text.literal("§7'" + customName + "' -> §e" + translatedName), false);
            });
            
            source.sendFeedback(() -> Text.literal("§aTotal mappings: " + allMappings.size()), false);
            return allMappings.size();
        }
        
        source.sendFeedback(() -> Text.literal("§cCommand must be used by a player"), false);
        return 0;
    }
}