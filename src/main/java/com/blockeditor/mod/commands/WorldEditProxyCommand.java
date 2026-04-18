package com.blockeditor.mod.commands;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldEditProxyCommand {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern CUSTOM_BLOCK_PATTERN = Pattern.compile("\\bbe:([a-zA-Z0-9_]+)\\b");
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Register bset command as proxy for //set
        dispatcher.register(CommandManager.literal("bset")
            .then(CommandManager.argument("pattern", StringArgumentType.greedyString())
                .executes(WorldEditProxyCommand::handleBSetCommand)
            )
        );
        
        // Register breplace command as proxy for //replace
        dispatcher.register(CommandManager.literal("breplace")
            .then(CommandManager.argument("from_to_pattern", StringArgumentType.greedyString())
                .executes(WorldEditProxyCommand::handleBReplaceCommand)
            )
        );
    }
    
    private static int handleBSetCommand(CommandContext<ServerCommandSource> context) {
        String pattern = StringArgumentType.getString(context, "pattern");
        return executeTranslatedWorldEditCommand(context, "//set " + pattern);
    }
    
    private static int handleBReplaceCommand(CommandContext<ServerCommandSource> context) {
        String fromToPattern = StringArgumentType.getString(context, "from_to_pattern");
        return executeTranslatedWorldEditCommand(context, "//replace " + fromToPattern);
    }
    
    private static int executeTranslatedWorldEditCommand(CommandContext<ServerCommandSource> context, String originalCommand) {
        ServerCommandSource source = context.getSource();
        
        LOGGER.info("WorldEdit Proxy: Processing command: {}", originalCommand);
        
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("§cCommand must be used by a player"));
            return 0;
        }
        
        ServerWorld level = player.getServerWorld();
        UserBlockRegistry registry = UserBlockRegistry.get(level);
        
        // First, handle explicit be: prefixed references
        String command = originalCommand;
        if (command.contains("be:")) {
            Matcher matcher = CUSTOM_BLOCK_PATTERN.matcher(command);
            StringBuffer sb = new StringBuffer();
            
            while (matcher.find()) {
                String customName = matcher.group(1).toLowerCase();
                LOGGER.info("WorldEdit Proxy: Found be: reference: {}", customName);
                
                String internalId = registry.getInternalIdentifier(customName);
                if (internalId != null) {
                    String replacement = "be:u_" + internalId;
                    matcher.appendReplacement(sb, replacement);
                    LOGGER.info("WorldEdit Proxy: Translated be:{} -> {}", customName, replacement);
                } else {
                    matcher.appendReplacement(sb, matcher.group());
                    LOGGER.warn("WorldEdit Proxy: No mapping for be:{}", customName);
                }
            }
            matcher.appendTail(sb);
            command = sb.toString();
        }
        
        // Then, translate bare custom block names (e.g. "blue" -> "be:u_calcite1")
        // Split command into parts: "//set blue" -> ["//set", "blue"]
        // or "//replace stone blue" -> ["//replace", "stone", "blue"]
        String[] parts = command.split(" ");
        StringBuilder translated = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) translated.append(" ");
            String part = parts[i];
            
            // Skip the command itself (//set, //replace, etc.)
            if (i == 0 || part.startsWith("//") || part.startsWith("-")) {
                translated.append(part);
                continue;
            }
            
            // If it already has a namespace (e.g. be:, minecraft:), leave it alone
            if (part.contains(":")) {
                translated.append(part);
                continue;
            }
            
            // Try to look up as a custom block name
            String internalId = registry.getInternalIdentifier(part.toLowerCase());
            if (internalId != null) {
                String replacement = "be:u_" + internalId;
                LOGGER.info("WorldEdit Proxy: Translated bare name '{}' -> '{}'", part, replacement);
                translated.append(replacement);
            } else {
                // Not a custom block, pass through (could be vanilla like "stone", "dirt")
                translated.append(part);
            }
        }
        
        String finalCommand = translated.toString();
        if (!finalCommand.equals(originalCommand)) {
            LOGGER.info("WorldEdit Proxy: Final translated command: {}", finalCommand);
        }
        
        // Execute the command
        try {
            player.server.getCommandManager().executeWithPrefix(
                player.getCommandSource(),
                finalCommand
            );
            return 1;
        } catch (Exception e) {
            LOGGER.error("WorldEdit Proxy: Failed to execute command: {}", e.getMessage());
            player.sendMessage(Text.literal("§cFailed to execute command: " + e.getMessage()));
            return 0;
        }
    }
}