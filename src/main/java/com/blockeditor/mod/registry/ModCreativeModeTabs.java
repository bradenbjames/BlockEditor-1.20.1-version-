package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.client.ClientColorManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BlockEditorMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCK_EDITOR_TAB =
        CREATIVE_MODE_TABS.register("block_editor_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.blockeditor"))
                .icon(() -> new ItemStack(ModBlocks.DYNAMIC_BLOCK.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.DYNAMIC_BLOCK_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_DIRT_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_SAND_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_WOOL_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_CONCRETE_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_DEEPSLATE_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_WOOD_ITEM.get());
                    output.accept(ModItems.DYNAMIC_BLOCK_STONE_ITEM.get());
                    
                    // Numbered user blocks for WorldEdit integration
                    output.accept(ModItems.USER_WOOL_1_ITEM.get());
                    output.accept(ModItems.USER_STONE_1_ITEM.get());
                    output.accept(ModItems.USER_CONCRETE_1_ITEM.get());
                    output.accept(ModItems.USER_WOOD_1_ITEM.get());
                    output.accept(ModItems.USER_DIRT_1_ITEM.get());
                    output.accept(ModItems.USER_SAND_1_ITEM.get());
                    output.accept(ModItems.USER_DEEPSLATE_1_ITEM.get());
                })
                .build()
        );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}