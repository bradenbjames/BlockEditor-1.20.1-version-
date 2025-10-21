package com.blockeditor.mod.commands;

import com.blockeditor.mod.worldedit.BlockNameResolver;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

/**
 * Provides debug commands for the block editor
 */
@Mod.EventBusSubscriber(modid = "be")
public class BlockEditorCommands {
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(
            Commands.literal("blockeditor")
                .then(Commands.literal("list")
                    .executes(BlockEditorCommands::listCustomBlocks))
                .then(Commands.literal("clear")
                    .executes(BlockEditorCommands::clearRegistry))
                .then(Commands.literal("test")
                    .then(Commands.argument("blockname", StringArgumentType.word())
                        .executes(BlockEditorCommands::testBlockName)))
        );
    }
    
    private static int listCustomBlocks(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        Map<String, String> mappings = BlockNameResolver.getAllCustomMappings();
        
        if (mappings.isEmpty()) {
            source.sendSuccess(() -> Component.literal("§eNo custom blocks found."), false);
        } else {
            source.sendSuccess(() -> Component.literal("§eCustom Block Mappings:"), false);
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                String customName = entry.getKey();
                String registryName = entry.getValue();
                source.sendSuccess(() -> Component.literal("§7  " + customName + " §f→ §7be:" + registryName), false);
                source.sendSuccess(() -> Component.literal("§a    Use: §f//set be:" + customName), false);
            }
        }
        
        return mappings.size();
    }
    
    private static int clearRegistry(CommandContext<CommandSourceStack> context) {
        // This would be handled by the existing clear registry functionality
        context.getSource().sendSuccess(() -> Component.literal("§eUse the 'Clear Registry' button in the Block Editor GUI to clear all blocks."), false);
        return 1;
    }
    
    private static int testBlockName(CommandContext<CommandSourceStack> context) {
        String blockName = context.getArgument("blockname", String.class);
        CommandSourceStack source = context.getSource();
        
        source.sendSuccess(() -> Component.literal("§eTesting block name: " + blockName), false);
        
        String registryName = BlockNameResolver.getRegistryName(blockName);
        if (registryName != null) {
            source.sendSuccess(() -> Component.literal("§aFound! Registry name: " + registryName), false);
            
            // Try to get the actual block
            net.minecraft.world.level.block.Block block = BlockNameResolver.resolveCustomName(blockName);
            if (block != null) {
                source.sendSuccess(() -> Component.literal("§aBlock object: " + block.getClass().getSimpleName()), false);
                source.sendSuccess(() -> Component.literal("§aTry this command: /setblock ~ ~ ~ " + registryName), false);
            } else {
                source.sendSuccess(() -> Component.literal("§cBlock object is null!"), false);
            }
        } else {
            source.sendSuccess(() -> Component.literal("§cNot found! Block name '" + blockName + "' doesn't exist."), false);
        }
        
        return 1;
    }
}