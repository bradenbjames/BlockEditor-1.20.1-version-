package com.blockeditor.mod.worldedit;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves custom block names to internal block registry names
 * Allows WorldEdit commands like //set be:customname to work
 */
public class BlockNameResolver {
    
    /**
     * Resolves a custom block name to the actual registered block
     * @param customName The custom name (e.g., "d", "myblock")
     * @return The Block instance if found, null otherwise
     */
    public static Block resolveCustomName(String customName) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return null;
        }
        
        // Get the overworld to access the registry
        ServerLevel level = server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (level == null) {
            return null;
        }
        
        // Get the user block registry
        UserBlockRegistry registry = UserBlockRegistry.get(level);
        
        // Resolve custom name to internal identifier
        String internalId = registry.getInternalIdentifier(customName);
        if (internalId == null) {
            return null;
        }
        
        // Convert internal identifier to registry name (e.g., "wool1" -> "u_wool1")
        String registryName = "u_" + internalId;
        
        // Get the block from the registry
        ResourceLocation blockLocation = new ResourceLocation(BlockEditorMod.MOD_ID, registryName);
        return BuiltInRegistries.BLOCK.get(blockLocation);
    }
    
    /**
     * Gets all custom name mappings as a map
     * @return Map of custom names to internal identifiers
     */
    public static Map<String, String> getAllCustomMappings() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return new HashMap<>();
        }
        
        ServerLevel level = server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (level == null) {
            return new HashMap<>();
        }
        
        UserBlockRegistry registry = UserBlockRegistry.get(level);
        Map<String, String> mappings = new HashMap<>();
        
        // Get all custom names and convert internal IDs to registry names
        for (String customName : registry.getAllCustomNames()) {
            String internalId = registry.getInternalIdentifier(customName);
            if (internalId != null) {
                mappings.put(customName, "u_" + internalId);
            }
        }
        
        return mappings;
    }
    
    /**
     * Checks if a custom name exists
     */
    public static boolean hasCustomName(String customName) {
        return resolveCustomName(customName) != null;
    }
    
    /**
     * Gets the full registry name for a custom name
     * @param customName The custom name
     * @return The full registry name (e.g., "be:u_wool1") or null if not found
     */
    public static String getRegistryName(String customName) {
        Block block = resolveCustomName(customName);
        if (block == null) {
            return null;
        }
        
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
        return location.toString();
    }
}