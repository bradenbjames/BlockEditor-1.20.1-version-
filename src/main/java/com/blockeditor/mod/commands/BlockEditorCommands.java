package com.blockeditor.mod.commands;

import com.blockeditor.mod.worldedit.BlockNameResolver;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import java.util.Map;

/**
 * Provides debug commands for the block editor
 */
public class BlockEditorCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        
        dispatcher.register(
            CommandManager.literal("blockeditor")
                .then(CommandManager.literal("list")
                    .executes(BlockEditorCommands::listCustomBlocks))
                .then(CommandManager.literal("clear")
                    .executes(BlockEditorCommands::clearRegistry))
                .then(CommandManager.literal("test")
                    .then(CommandManager.argument("blockname", StringArgumentType.word())
                        .executes(BlockEditorCommands::testBlockName)))
        );
    }
    
    private static int listCustomBlocks(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        Map<String, String> mappings = BlockNameResolver.getAllCustomMappings();
        
        if (mappings.isEmpty()) {
            source.sendFeedback(() -> Text.literal("§eNo custom blocks found."), false);
        } else {
            source.sendFeedback(() -> Text.literal("§eCustom Block Mappings:"), false);
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                String customName = entry.getKey();
                String registryName = entry.getValue();
                source.sendFeedback(() -> Text.literal("§7  " + customName + " §f→ §7be:" + registryName), false);
                source.sendFeedback(() -> Text.literal("§a    Use: §f//set be:" + customName), false);
            }
        }
        
        return mappings.size();
    }
    
    private static int clearRegistry(CommandContext<ServerCommandSource> context) {
        // This would be handled by the existing clear registry functionality
        context.getSource().sendFeedback(() -> Text.literal("§eUse the 'Clear Registry' button in the Block Editor GUI to clear all blocks."), false);
        return 1;
    }
    
    private static int testBlockName(CommandContext<ServerCommandSource> context) {
        String blockName = context.getArgument("blockname", String.class);
        ServerCommandSource source = context.getSource();
        
        source.sendFeedback(() -> Text.literal("§eTesting block name: " + blockName), false);
        
        String registryName = BlockNameResolver.getRegistryName(blockName);
        if (registryName != null) {
            source.sendFeedback(() -> Text.literal("§aFound! Registry name: " + registryName), false);
            
            // Try to get the actual block
            net.minecraft.block.Block block = BlockNameResolver.resolveCustomName(blockName);
            if (block != null) {
                source.sendFeedback(() -> Text.literal("§aBlock object: " + block.getClass().getSimpleName()), false);
                source.sendFeedback(() -> Text.literal("§aTry this command: /setblock ~ ~ ~ " + registryName), false);
            } else {
                source.sendFeedback(() -> Text.literal("§cBlock object is null!"), false);
            }
        } else {
            source.sendFeedback(() -> Text.literal("§cNot found! Block name '" + blockName + "' doesn't exist."), false);
        }
        
        return 1;
    }
}