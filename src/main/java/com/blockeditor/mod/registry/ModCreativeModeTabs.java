package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
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

    public static final RegistryObject<CreativeModeTab> BLOCK_EDITOR_TAB = CREATIVE_MODE_TABS.register("block_editor_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.blockeditor"))
        .icon(() -> new ItemStack(ModItems.DYNAMIC_BLOCK_ITEM.get()))
        .displayItems((parameters, output) -> {
            // Main dynamic blocks
            output.accept(ModItems.DYNAMIC_BLOCK_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_DIRT_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_SAND_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_WOOL_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_CONCRETE_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_DEEPSLATE_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_WOOD_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_STONE_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_COBBLESTONE_ITEM.get());
            output.accept(ModItems.DYNAMIC_BLOCK_SMOOTH_STONE_ITEM.get());
            
            // User customizable blocks for WorldEdit integration (20 slots each)
            // Wool blocks (1-20) - Users can name these for WorldEdit commands like //set be:a
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_WOOL_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_WOOL_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_WOOL_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_WOOL_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_WOOL_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_WOOL_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_WOOL_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_WOOL_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_WOOL_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_WOOL_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_WOOL_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_WOOL_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_WOOL_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_WOOL_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_WOOL_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_WOOL_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_WOOL_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_WOOL_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_WOOL_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_WOOL_20_ITEM.get()); break;
                }
            }
            
            // Stone blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_STONE_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_STONE_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_STONE_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_STONE_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_STONE_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_STONE_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_STONE_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_STONE_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_STONE_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_STONE_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_STONE_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_STONE_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_STONE_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_STONE_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_STONE_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_STONE_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_STONE_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_STONE_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_STONE_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_STONE_20_ITEM.get()); break;
                }
            }
            
            // Concrete blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_CONCRETE_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_CONCRETE_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_CONCRETE_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_CONCRETE_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_CONCRETE_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_CONCRETE_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_CONCRETE_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_CONCRETE_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_CONCRETE_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_CONCRETE_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_CONCRETE_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_CONCRETE_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_CONCRETE_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_CONCRETE_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_CONCRETE_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_CONCRETE_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_CONCRETE_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_CONCRETE_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_CONCRETE_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_CONCRETE_20_ITEM.get()); break;
                }
            }
            
            // Wood blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_WOOD_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_WOOD_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_WOOD_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_WOOD_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_WOOD_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_WOOD_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_WOOD_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_WOOD_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_WOOD_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_WOOD_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_WOOD_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_WOOD_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_WOOD_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_WOOD_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_WOOD_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_WOOD_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_WOOD_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_WOOD_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_WOOD_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_WOOD_20_ITEM.get()); break;
                }
            }
            
            // Dirt blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_DIRT_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_DIRT_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_DIRT_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_DIRT_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_DIRT_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_DIRT_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_DIRT_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_DIRT_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_DIRT_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_DIRT_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_DIRT_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_DIRT_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_DIRT_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_DIRT_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_DIRT_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_DIRT_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_DIRT_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_DIRT_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_DIRT_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_DIRT_20_ITEM.get()); break;
                }
            }
            
            // Sand blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_SAND_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_SAND_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_SAND_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_SAND_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_SAND_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_SAND_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_SAND_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_SAND_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_SAND_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_SAND_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_SAND_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_SAND_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_SAND_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_SAND_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_SAND_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_SAND_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_SAND_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_SAND_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_SAND_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_SAND_20_ITEM.get()); break;
                }
            }
            
            // Deepslate blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_DEEPSLATE_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_DEEPSLATE_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_DEEPSLATE_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_DEEPSLATE_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_DEEPSLATE_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_DEEPSLATE_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_DEEPSLATE_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_DEEPSLATE_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_DEEPSLATE_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_DEEPSLATE_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_DEEPSLATE_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_DEEPSLATE_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_DEEPSLATE_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_DEEPSLATE_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_DEEPSLATE_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_DEEPSLATE_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_DEEPSLATE_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_DEEPSLATE_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_DEEPSLATE_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_DEEPSLATE_20_ITEM.get()); break;
                }
            }
            
            // Cobblestone blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_COBBLESTONE_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_COBBLESTONE_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_COBBLESTONE_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_COBBLESTONE_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_COBBLESTONE_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_COBBLESTONE_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_COBBLESTONE_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_COBBLESTONE_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_COBBLESTONE_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_COBBLESTONE_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_COBBLESTONE_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_COBBLESTONE_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_COBBLESTONE_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_COBBLESTONE_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_COBBLESTONE_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_COBBLESTONE_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_COBBLESTONE_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_COBBLESTONE_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_COBBLESTONE_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_COBBLESTONE_20_ITEM.get()); break;
                }
            }
            
            // Smooth Stone blocks (1-20)
            for (int i = 1; i <= 20; i++) {
                switch (i) {
                    case 1: output.accept(ModItems.USER_SMOOTH_STONE_1_ITEM.get()); break;
                    case 2: output.accept(ModItems.USER_SMOOTH_STONE_2_ITEM.get()); break;
                    case 3: output.accept(ModItems.USER_SMOOTH_STONE_3_ITEM.get()); break;
                    case 4: output.accept(ModItems.USER_SMOOTH_STONE_4_ITEM.get()); break;
                    case 5: output.accept(ModItems.USER_SMOOTH_STONE_5_ITEM.get()); break;
                    case 6: output.accept(ModItems.USER_SMOOTH_STONE_6_ITEM.get()); break;
                    case 7: output.accept(ModItems.USER_SMOOTH_STONE_7_ITEM.get()); break;
                    case 8: output.accept(ModItems.USER_SMOOTH_STONE_8_ITEM.get()); break;
                    case 9: output.accept(ModItems.USER_SMOOTH_STONE_9_ITEM.get()); break;
                    case 10: output.accept(ModItems.USER_SMOOTH_STONE_10_ITEM.get()); break;
                    case 11: output.accept(ModItems.USER_SMOOTH_STONE_11_ITEM.get()); break;
                    case 12: output.accept(ModItems.USER_SMOOTH_STONE_12_ITEM.get()); break;
                    case 13: output.accept(ModItems.USER_SMOOTH_STONE_13_ITEM.get()); break;
                    case 14: output.accept(ModItems.USER_SMOOTH_STONE_14_ITEM.get()); break;
                    case 15: output.accept(ModItems.USER_SMOOTH_STONE_15_ITEM.get()); break;
                    case 16: output.accept(ModItems.USER_SMOOTH_STONE_16_ITEM.get()); break;
                    case 17: output.accept(ModItems.USER_SMOOTH_STONE_17_ITEM.get()); break;
                    case 18: output.accept(ModItems.USER_SMOOTH_STONE_18_ITEM.get()); break;
                    case 19: output.accept(ModItems.USER_SMOOTH_STONE_19_ITEM.get()); break;
                    case 20: output.accept(ModItems.USER_SMOOTH_STONE_20_ITEM.get()); break;
                }
            }
        })
        .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}