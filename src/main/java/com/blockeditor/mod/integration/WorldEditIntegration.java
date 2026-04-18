package com.blockeditor.mod.integration;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldEditIntegration {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, String> customBlockMappings = new HashMap<>();
    private static final Pattern WORLDEDIT_PATTERN = Pattern.compile("\\bbe:([a-zA-Z0-9_]+)\\b");

    /**
     * Call from BlockEditorMod.onInitialize() to register all Fabric event callbacks.
     */
    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.debug("WorldEdit Integration: Server started");
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            ServerWorld world = player.getServerWorld();
            UserBlockRegistry registry = UserBlockRegistry.get(world);

            Set<String> existingNames = registry.getAllCustomNames();
            for (String customName : existingNames) {
                String internalId = registry.getInternalIdentifier(customName);
                if (internalId != null) {
                    customBlockMappings.put(customName.toLowerCase(), internalId);
                    LOGGER.debug("Stored custom block mapping: {} -> {}", customName, internalId);
                }
            }
            LOGGER.debug("Loaded {} existing custom block aliases", existingNames.size());
        });

        // Intercept chat messages to translate WorldEdit commands
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, senderPlayer, params) -> {
            String msgText = message.getContent().getString();

            if (msgText.startsWith("//") && msgText.contains("be:")) {
                LOGGER.debug("WorldEdit Integration: Intercepted WorldEdit command: {}", msgText);

                Matcher matcher = WORLDEDIT_PATTERN.matcher(msgText);
                StringBuffer modifiedMessage = new StringBuffer();
                boolean foundReplacement = false;

                while (matcher.find()) {
                    String customName = matcher.group(1).toLowerCase();

                    String internalId = customBlockMappings.get(customName);

                    if (internalId != null) {
                        String replacement = "be:u_" + internalId;
                        matcher.appendReplacement(modifiedMessage, replacement);
                        foundReplacement = true;
                    } else {
                        ServerWorld world = senderPlayer.getServerWorld();
                        UserBlockRegistry registry = UserBlockRegistry.get(world);
                        String foundInternalId = registry.getInternalIdentifier(customName);

                        if (foundInternalId != null) {
                            customBlockMappings.put(customName, foundInternalId);
                            String replacement = "be:u_" + foundInternalId;
                            matcher.appendReplacement(modifiedMessage, replacement);
                            foundReplacement = true;
                        } else {
                            matcher.appendReplacement(modifiedMessage, matcher.group());
                        }
                    }
                }

                if (foundReplacement) {
                    matcher.appendTail(modifiedMessage);
                    String translatedCommand = modifiedMessage.toString();

                    senderPlayer.server.execute(() -> {
                        try {
                            senderPlayer.server.getCommandManager().executeWithPrefix(
                                senderPlayer.getCommandSource(),
                                translatedCommand
                            );
                        } catch (Exception e) {
                            LOGGER.error("WorldEdit Integration: Failed to execute: {}", e.getMessage());
                            senderPlayer.sendMessage(Text.literal(
                                "\u00A7cWorldEdit Integration Error: " + e.getMessage()
                            ));
                        }
                    });

                    return false; // Cancel original chat message
                }
            }

            return true; // Allow normal messages
        });
    }

    public static void updateCustomBlockMapping(String customName, String internalIdentifier) {
        customBlockMappings.put(customName.toLowerCase(), internalIdentifier);
        LOGGER.debug("WorldEdit Integration: Mapped '{}' -> '{}'", customName, internalIdentifier);
        registerWorldEditAlias(customName, internalIdentifier);
    }

    @SuppressWarnings("unused")
    public static void removeCustomBlockMapping(String customName) {
        customBlockMappings.remove(customName.toLowerCase());
    }

    public static void clearAllMappings() {
        customBlockMappings.clear();
    }

    public static Map<String, String> getCustomNameMappings() {
        return new HashMap<>(customBlockMappings);
    }

    private static void registerWorldEditAlias(String customName, String internalId) {
        try {
            Identifier blockLocation = new Identifier(BlockEditorMod.MOD_ID, "u_" + internalId);
            Block block = Registries.BLOCK.get(blockLocation);
            if (block != null) {
                LOGGER.debug("WorldEdit Integration: Block '{}' available for '{}'", blockLocation, customName);
            }
        } catch (Exception e) {
            LOGGER.error("WorldEdit Integration: Error processing alias: {}", e.getMessage());
        }
    }
}
