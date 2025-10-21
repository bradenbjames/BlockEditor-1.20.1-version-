package com.blockeditor.mod.integration;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID)
public class WorldEditIntegration {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    // Maps custom names to internal block identifiers (e.g., "mycustomblock" -> "wool1")
    private static final Map<String, String> customBlockMappings = new HashMap<>();
    
    // Pattern to match WorldEdit commands with our custom blocks
    private static final Pattern WORLDEDIT_PATTERN = Pattern.compile("\\bbe:([a-zA-Z0-9_]+)\\b");
    
    /**
     * Updates the custom block mapping when a new block is created
     */
    public static void updateCustomBlockMapping(String customName, String internalIdentifier) {
        customBlockMappings.put(customName.toLowerCase(), internalIdentifier);
        LOGGER.info("WorldEdit Integration: Mapped '{}' -> '{}'", customName, internalIdentifier);
        
        // Register with WorldEdit's block alias system
        registerWorldEditAlias(customName, internalIdentifier);
    }
    
    /**
     * Removes a custom block mapping
     */
    public static void removeCustomBlockMapping(String customName) {
        customBlockMappings.remove(customName.toLowerCase());
        LOGGER.info("WorldEdit Integration: Removed mapping for '{}'", customName);
    }
    
    /**
     * Clears all custom block mappings
     */
    public static void clearAllMappings() {
        customBlockMappings.clear();
        LOGGER.info("WorldEdit Integration: Cleared all custom block mappings");
    }
    
    /**
     * Gets all current mappings
     */
    public static Map<String, String> getCustomNameMappings() {
        return new HashMap<>(customBlockMappings);
    }
    
    /**
     * Intercepts chat messages to translate WorldEdit commands with custom block names
     * Using HIGHEST priority to catch commands before anything else processes them
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerChat(ServerChatEvent event) {
        String message = event.getMessage().getString();
        ServerPlayer player = event.getPlayer();
        
        LOGGER.info("WorldEdit Integration: Chat message received: '{}'", message);
        
        // Check if this is a WorldEdit command with our custom blocks
        if (message.startsWith("//") && message.contains("be:")) {
            LOGGER.info("WorldEdit Integration: Intercepted WorldEdit command: {}", message);
            
            // Check if the command contains any of our custom block patterns
            Matcher matcher = WORLDEDIT_PATTERN.matcher(message);
            StringBuffer modifiedMessage = new StringBuffer();
            boolean foundReplacement = false;
            
            while (matcher.find()) {
                String customName = matcher.group(1).toLowerCase();
                LOGGER.info("WorldEdit Integration: Found custom block reference: {}", customName);
                
                String internalId = customBlockMappings.get(customName);
                
                if (internalId != null) {
                    // Replace be:customname with be:u_blocktype#
                    String replacement = "be:u_" + internalId;
                    matcher.appendReplacement(modifiedMessage, replacement);
                    foundReplacement = true;
                    LOGGER.info("WorldEdit Integration: Translated '{}' -> '{}'", customName, replacement);
                } else {
                    // Check if we can find the mapping in the registry
                    ServerLevel level = player.serverLevel();
                    UserBlockRegistry registry = UserBlockRegistry.get(level);
                    String foundInternalId = registry.getInternalIdentifier(customName);
                    
                    if (foundInternalId != null) {
                        // Update our cache and replace
                        customBlockMappings.put(customName, foundInternalId);
                        String replacement = "be:u_" + foundInternalId;
                        matcher.appendReplacement(modifiedMessage, replacement);
                        foundReplacement = true;
                        LOGGER.info("WorldEdit Integration: Found in registry and translated '{}' -> '{}'", customName, replacement);
                    } else {
                        // No replacement found, keep original
                        matcher.appendReplacement(modifiedMessage, matcher.group());
                        LOGGER.warn("WorldEdit Integration: No mapping found for custom name: {}", customName);
                    }
                }
            }
            
            if (foundReplacement) {
                matcher.appendTail(modifiedMessage);
                String translatedCommand = modifiedMessage.toString();
                LOGGER.info("WorldEdit Integration: Final translated command: {}", translatedCommand);
                
                // Cancel the original event
                event.setCanceled(true);
                
                // Execute the translated command
                player.server.execute(() -> {
                    try {
                        LOGGER.info("WorldEdit Integration: Executing translated command: {}", translatedCommand);
                        player.server.getCommands().performPrefixedCommand(
                            player.createCommandSourceStack(),
                            translatedCommand
                        );
                    } catch (Exception e) {
                        LOGGER.error("WorldEdit Integration: Failed to execute translated command: {}", e.getMessage());
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                            "§cWorldEdit Integration Error: " + e.getMessage()
                        ));
                    }
                });
            }
        }
    }
    
    /**
     * Registers a custom block alias with WorldEdit
     */
    private static void registerWorldEditAlias(String customName, String internalId) {
        try {
            // Simple logging for now - we'll focus on the translate command to help users
            ResourceLocation blockLocation = new ResourceLocation(BlockEditorMod.MOD_ID, "u_" + internalId);
            Block block = BuiltInRegistries.BLOCK.get(blockLocation);
            
            if (block != null) {
                LOGGER.info("WorldEdit Integration: Block '{}' is available for custom name '{}'", blockLocation, customName);
                LOGGER.info("WorldEdit Integration: Users should use 'be:u_{}' in WorldEdit commands", internalId);
            } else {
                LOGGER.warn("WorldEdit Integration: Block not found for location: {}", blockLocation);
            }
        } catch (Exception e) {
            LOGGER.error("WorldEdit Integration: Error processing alias: {}", e.getMessage());
        }
    }
    
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("WorldEdit Block Alias Manager: Server started, loading existing custom block mappings");
        
        // Load existing mappings from the UserBlockRegistry
        // This will be called when the server starts
        try {
            // We'll load the mappings when players join instead since we need a ServerLevel
            LOGGER.info("WorldEdit Integration: Ready to load custom block mappings when players join");
        } catch (Exception e) {
            LOGGER.error("WorldEdit Integration: Error during server startup: {}", e.getMessage());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Load all existing custom block mappings
            Set<String> existingNames = registry.getAllCustomNames();
            for (String customName : existingNames) {
                String internalId = registry.getInternalIdentifier(customName);
                if (internalId != null) {
                    customBlockMappings.put(customName.toLowerCase(), internalId);
                    LOGGER.info("Stored custom block mapping: {} -> {}", customName, internalId);
                    
                    // Try to make WorldEdit recognize this block
                    ResourceLocation blockLocation = new ResourceLocation(BlockEditorMod.MOD_ID, "u_" + internalId);
                    LOGGER.info("WorldEdit should recognize: {} as UserBlock", blockLocation);
                }
            }
            
            LOGGER.info("Loaded {} existing custom block aliases", existingNames.size());
            if (!existingNames.isEmpty()) {
                LOGGER.info("Example mappings loaded:");
                existingNames.stream().limit(5).forEach(name -> {
                    String internalId = registry.getInternalIdentifier(name);
                    if (internalId != null) {
                        LOGGER.info("  '{}' -> 'be:u_{}'", name, internalId);
                    }
                });
            }
        }
    }
}