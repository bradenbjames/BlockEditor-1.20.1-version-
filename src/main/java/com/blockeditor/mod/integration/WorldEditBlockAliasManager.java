package com.blockeditor.mod.integration;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WorldEdit Integration that creates command aliases for custom blocks
 * Since we can't modify the block registry after registration, we'll use a different approach
 */
@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID)
public class WorldEditBlockAliasManager {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, String> customAliases = new ConcurrentHashMap<>();
    
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("WorldEdit Block Alias Manager: Server started, loading existing custom block mappings");
        loadExistingMappings(event.getServer().getLevel(Level.OVERWORLD));
    }
    
    /**
     * Registers a custom block name mapping (no longer tries to modify registry)
     */
    public static void registerCustomBlockAlias(String customName, String internalIdentifier) {
        try {
            String actualBlockName = "u_" + internalIdentifier;
            ResourceLocation actualId = new ResourceLocation(BlockEditorMod.MOD_ID, actualBlockName);
            
            // Verify the actual block exists
            Block actualBlock = ForgeRegistries.BLOCKS.getValue(actualId);
            if (actualBlock != null) {
                customAliases.put(customName, actualBlockName);
                LOGGER.info("Stored custom block mapping: {} -> {}", customName, actualBlockName);
                
                // Log the full registry name that WorldEdit should use
                String fullRegistryName = BlockEditorMod.MOD_ID + ":" + actualBlockName;
                LOGGER.info("WorldEdit should recognize: {} as {}", fullRegistryName, actualBlock.getClass().getSimpleName());
            } else {
                LOGGER.warn("Could not find actual block for: {}", actualBlockName);
            }
        } catch (Exception e) {
            LOGGER.error("Error registering custom block alias: {}", e.getMessage());
        }
    }
    
    /**
     * Removes a custom block alias
     */
    public static void removeCustomBlockAlias(String customName) {
        customAliases.remove(customName);
        LOGGER.info("Removed custom block mapping: {}", customName);
    }
    
    /**
     * Clears all custom block aliases
     */
    public static void clearAllAliases() {
        customAliases.clear();
        LOGGER.info("Cleared all custom block aliases");
    }
    
    /**
     * Gets the internal identifier for a custom name
     */
    public static String getInternalIdentifier(String customName) {
        return customAliases.get(customName);
    }
    
    /**
     * Gets the full registry name for a custom name
     */
    public static String getFullRegistryName(String customName) {
        String internalId = customAliases.get(customName);
        if (internalId != null) {
            return BlockEditorMod.MOD_ID + ":" + internalId;
        }
        return null;
    }
    
    /**
     * Gets all registered custom aliases
     */
    public static Map<String, String> getAllAliases() {
        return new ConcurrentHashMap<>(customAliases);
    }
    
    /**
     * Loads existing custom name mappings from the server's UserBlockRegistry
     */
    private static void loadExistingMappings(ServerLevel level) {
        if (level == null) return;
        
        try {
            UserBlockRegistry registry = UserBlockRegistry.get(level);
            
            // Load all existing custom names
            for (String customName : registry.getAllCustomNames()) {
                String internalId = registry.getInternalIdentifier(customName);
                if (internalId != null) {
                    registerCustomBlockAlias(customName, internalId);
                }
            }
            
            LOGGER.info("Loaded {} existing custom block aliases", registry.getAllCustomNames().size());
            
            // Log some examples for debugging
            if (!customAliases.isEmpty()) {
                LOGGER.info("Example mappings loaded:");
                int count = 0;
                for (Map.Entry<String, String> entry : customAliases.entrySet()) {
                    if (count++ < 3) {
                        String fullName = BlockEditorMod.MOD_ID + ":" + entry.getValue();
                        LOGGER.info("  {} -> {} (full: {})", entry.getKey(), entry.getValue(), fullName);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load existing custom block mappings: {}", e.getMessage());
        }
    }
}