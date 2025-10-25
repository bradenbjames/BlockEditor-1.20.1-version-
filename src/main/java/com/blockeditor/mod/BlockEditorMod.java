package com.blockeditor.mod;

import com.blockeditor.mod.commands.DebugCommand;
import com.blockeditor.mod.commands.TranslateCommand;
import com.blockeditor.mod.commands.WorldEditProxyCommand;
import com.blockeditor.mod.network.ModNetworking;
import com.blockeditor.mod.registry.ModBlockEntities;
import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.ModCreativeModeTabs;
import com.blockeditor.mod.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BlockEditorMod.MOD_ID)
public class BlockEditorMod {
    public static final String MOD_ID = "be";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BlockEditorMod() {
        LOGGER.debug("BlockEditor mod constructor called");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all deferred registers
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        LOGGER.debug("Registries registered, adding common setup listener");

        // Register common setup event for networking
        modEventBus.addListener(this::commonSetup);
        
        // Register for server events (commands)
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.debug("Constructor complete");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup event fired");

        event.enqueueWork(() -> {
            LOGGER.debug("Enqueue work: registering network packets");
            ModNetworking.register();
            LOGGER.debug("Network packet registration completed");
        });

        LOGGER.debug("Common setup complete");
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        DebugCommand.register(event.getDispatcher());
        TranslateCommand.register(event.getDispatcher());
        WorldEditProxyCommand.register(event.getDispatcher());
    }
}