package com.blockeditor.mod.worldedit;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.registry.UserBlockRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves custom block names to internal block registry names.
 * In Fabric, we store the server reference from ServerLifecycleEvents.SERVER_STARTED.
 */
public class BlockNameResolver {

    private static MinecraftServer currentServer;

    /**
     * Called from BlockEditorMod.onInitialize() via ServerLifecycleEvents.SERVER_STARTED.
     */
    public static void setServer(MinecraftServer server) {
        currentServer = server;
    }

    /**
     * Called from ServerLifecycleEvents.SERVER_STOPPED to clear reference.
     */
    public static void clearServer() {
        currentServer = null;
    }

    public static Block resolveCustomName(String customName) {
        MinecraftServer server = currentServer;
        if (server == null) {
            return null;
        }

        ServerWorld world = server.getWorld(net.minecraft.world.World.OVERWORLD);
        if (world == null) {
            return null;
        }

        UserBlockRegistry registry = UserBlockRegistry.get(world);

        String internalId = registry.getInternalIdentifier(customName);
        if (internalId == null) {
            return null;
        }

        String registryName = "u_" + internalId;
        Identifier blockLocation = new Identifier(BlockEditorMod.MOD_ID, registryName);
        return Registries.BLOCK.get(blockLocation);
    }

    public static Map<String, String> getAllCustomMappings() {
        MinecraftServer server = currentServer;
        if (server == null) {
            return new HashMap<>();
        }

        ServerWorld world = server.getWorld(net.minecraft.world.World.OVERWORLD);
        if (world == null) {
            return new HashMap<>();
        }

        UserBlockRegistry registry = UserBlockRegistry.get(world);
        Map<String, String> mappings = new HashMap<>();

        for (String customName : registry.getAllCustomNames()) {
            String internalId = registry.getInternalIdentifier(customName);
            if (internalId != null) {
                mappings.put(customName, "u_" + internalId);
            }
        }

        return mappings;
    }

    public static boolean hasCustomName(String customName) {
        return resolveCustomName(customName) != null;
    }

    public static String getRegistryName(String customName) {
        Block block = resolveCustomName(customName);
        if (block == null) {
            return null;
        }
        
        Identifier location = Registries.BLOCK.getId(block);
        String result = location.toString();
        return result;
    }
}