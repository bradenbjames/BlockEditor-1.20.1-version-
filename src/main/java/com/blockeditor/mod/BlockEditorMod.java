package com.blockeditor.mod;

import com.blockeditor.mod.commands.BlockEditorCommands;
import com.blockeditor.mod.commands.DebugCommand;
import com.blockeditor.mod.commands.TranslateCommand;
import com.blockeditor.mod.commands.WorldEditProxyCommand;
import com.blockeditor.mod.integration.WorldEditBlockAliasManager;
import com.blockeditor.mod.integration.WorldEditIntegration;
import com.blockeditor.mod.network.ModNetworking;
import com.blockeditor.mod.registry.ModBlockEntities;
import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.ModCreativeModeTabs;
import com.blockeditor.mod.registry.ModItems;
import com.blockeditor.mod.worldedit.BlockNameResolver;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;

public class BlockEditorMod implements ModInitializer {
    public static final String MOD_ID = "be";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.debug("BlockEditor mod initializing");

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModCreativeModeTabs.register();

        LOGGER.debug("Registries registered, setting up networking");
        ModNetworking.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DebugCommand.register(dispatcher);
            TranslateCommand.register(dispatcher);
            WorldEditProxyCommand.register(dispatcher);
            BlockEditorCommands.register(dispatcher);
        });

        // Server lifecycle events
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            BlockNameResolver.setServer(server);
            WorldEditBlockAliasManager.onServerStarted(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            BlockNameResolver.clearServer();
        });

        // WorldEdit integration events (chat interception, player join)
        WorldEditIntegration.registerEvents();

        LOGGER.debug("BlockEditor initialization complete");
    }
}