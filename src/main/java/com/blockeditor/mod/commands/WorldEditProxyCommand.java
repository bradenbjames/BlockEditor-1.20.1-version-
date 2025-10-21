package com.blockeditor.mod.commands;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldEditProxyCommand {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern CUSTOM_BLOCK_PATTERN = Pattern.compile("\\bbe:([a-zA-Z0-9_]+)\\b");
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Register bset command as proxy for //set
        dispatcher.register(Commands.literal("bset")
            .then(Commands.argument("pattern", StringArgumentType.greedyString())
                .executes(WorldEditProxyCommand::handleBSetCommand)
            )
        );
        
        // Register breplace command as proxy for //replace
        dispatcher.register(Commands.literal("breplace")
            .then(Commands.argument("from_to_pattern", StringArgumentType.greedyString())
                .executes(WorldEditProxyCommand::handleBReplaceCommand)
            )
        );
    }
    
    private static int handleBSetCommand(CommandContext<CommandSourceStack> context) {
        String pattern = StringArgumentType.getString(context, "pattern");
        return executeTranslatedWorldEditCommand(context, "//set " + pattern);
    }
    
    private static int handleBReplaceCommand(CommandContext<CommandSourceStack> context) {
        String fromToPattern = StringArgumentType.getString(context, "from_to_pattern");
        return executeTranslatedWorldEditCommand(context, "//replace " + fromToPattern);
    }
    
    private static int executeTranslatedWorldEditCommand(CommandContext<CommandSourceStack> context, String originalCommand) {
        CommandSourceStack source = context.getSource();
        
        LOGGER.info("WorldEdit Proxy: Processing command: {}", originalCommand);
        
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("§cCommand must be used by a player"));
            return 0;
        }
        
        // Check if the command contains custom block references
        if (originalCommand.contains("be:")) {
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            Matcher matcher = CUSTOM_BLOCK_PATTERN.matcher(originalCommand);
            StringBuffer translatedCommand = new StringBuffer();
            boolean foundReplacement = false;
            
            while (matcher.find()) {
                String customName = matcher.group(1).toLowerCase();
                LOGGER.info("WorldEdit Proxy: Found custom block reference: {}", customName);
                
                String internalId = registry.getInternalIdentifier(customName);
                
                if (internalId != null) {
                    String replacement = "be:u_" + internalId;
                    matcher.appendReplacement(translatedCommand, replacement);
                    foundReplacement = true;
                    LOGGER.info("WorldEdit Proxy: Translated '{}' -> '{}'", customName, replacement);
                    
                    // Send feedback to player
                    player.sendSystemMessage(Component.literal("§7Translated §e" + customName + " §7-> §a" + replacement));
                } else {
                    // No mapping found, keep original
                    matcher.appendReplacement(translatedCommand, matcher.group());
                    LOGGER.warn("WorldEdit Proxy: No mapping found for custom name: {}", customName);
                }
            }
            
            if (foundReplacement) {
                matcher.appendTail(translatedCommand);
                String finalCommand = translatedCommand.toString();
                LOGGER.info("WorldEdit Proxy: Final translated command: {}", finalCommand);
                
                // Execute the translated WorldEdit command
                try {
                    player.server.getCommands().performPrefixedCommand(
                        player.createCommandSourceStack(),
                        finalCommand
                    );
                    return 1;
                } catch (Exception e) {
                    LOGGER.error("WorldEdit Proxy: Failed to execute command: {}", e.getMessage());
                    player.sendSystemMessage(Component.literal("§cFailed to execute command: " + e.getMessage()));
                    return 0;
                }
            }
        }
        
        // No translation needed, execute original command
        try {
            player.server.getCommands().performPrefixedCommand(
                player.createCommandSourceStack(),
                originalCommand
            );
            return 1;
        } catch (Exception e) {
            LOGGER.error("WorldEdit Proxy: Failed to execute original command: {}", e.getMessage());
            player.sendSystemMessage(Component.literal("§cFailed to execute command: " + e.getMessage()));
            return 0;
        }
    }
}