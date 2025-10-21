package com.blockeditor.mod;

import com.blockeditor.mod.commands.DebugCommand;
import com.blockeditor.mod.commands.TranslateCommand;
import com.blockeditor.mod.commands.WorldEditProxyCommand;
import com.blockeditor.mod.network.ModNetworking;
import com.blockeditor.mod.registry.ModBlockEntities;
import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.ModCreativeModeTabs;
import com.blockeditor.mod.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlockEditorMod.MOD_ID)
public class BlockEditorMod {
    public static final String MOD_ID = "be";

    public BlockEditorMod() {
        System.out.println("BLOCKEDITOR MOD CONSTRUCTOR CALLED!");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all deferred registers
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        System.out.println("BLOCKEDITOR: Registries registered, adding common setup listener");

        // Register common setup event for networking
        modEventBus.addListener(this::commonSetup);
        
        // Register for server events (commands)
        MinecraftForge.EVENT_BUS.register(this);

        System.out.println("BLOCKEDITOR: Constructor complete");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        System.out.println("========================================");
        System.out.println("BLOCKEDITOR COMMON SETUP EVENT FIRED");
        System.out.println("========================================");

        event.enqueueWork(() -> {
            System.out.println("BLOCKEDITOR: enqueueWork - about to call ModNetworking.register()");
            ModNetworking.register();
            System.out.println("BLOCKEDITOR: ModNetworking.register() completed");
        });

        System.out.println("BLOCKEDITOR: commonSetup method complete");
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        System.out.println("BLOCKEDITOR: Registering debug command");
        DebugCommand.register(event.getDispatcher());
        System.out.println("BLOCKEDITOR: Registering translate command");
        TranslateCommand.register(event.getDispatcher());
        System.out.println("BLOCKEDITOR: Registering WorldEdit proxy command");
        WorldEditProxyCommand.register(event.getDispatcher());
    }
}