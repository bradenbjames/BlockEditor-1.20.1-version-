package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCreativeModeTabs {

    public static final ItemGroup BLOCK_EDITOR_TAB = Registry.register(
        Registries.ITEM_GROUP,
        new Identifier(BlockEditorMod.MOD_ID, "block_editor_tab"),
        FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.DYNAMIC_BLOCK_ITEM))
            .displayName(Text.translatable("itemGroup.blockeditor"))
            .entries((ctx, entries) -> {
                entries.add(ModItems.DYNAMIC_BLOCK_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_DIRT_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_SAND_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_WOOL_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_CONCRETE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_DEEPSLATE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_WOOD_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_STONE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_COBBLESTONE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_SMOOTH_STONE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_TERRACOTTA_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_CONCRETE_POWDER_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_GLASS_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_TINTED_GLASS_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_STAINED_GLASS_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_DIORITE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_CALCITE_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_MUSHROOM_STEM_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_DEAD_TUBE_CORAL_ITEM);
                entries.add(ModItems.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT_ITEM);

                // USER_WOOL blocks (1-20)
                entries.add(ModItems.USER_WOOL_1_ITEM);
                entries.add(ModItems.USER_WOOL_2_ITEM);
                entries.add(ModItems.USER_WOOL_3_ITEM);
                entries.add(ModItems.USER_WOOL_4_ITEM);
                entries.add(ModItems.USER_WOOL_5_ITEM);
                entries.add(ModItems.USER_WOOL_6_ITEM);
                entries.add(ModItems.USER_WOOL_7_ITEM);
                entries.add(ModItems.USER_WOOL_8_ITEM);
                entries.add(ModItems.USER_WOOL_9_ITEM);
                entries.add(ModItems.USER_WOOL_10_ITEM);
                entries.add(ModItems.USER_WOOL_11_ITEM);
                entries.add(ModItems.USER_WOOL_12_ITEM);
                entries.add(ModItems.USER_WOOL_13_ITEM);
                entries.add(ModItems.USER_WOOL_14_ITEM);
                entries.add(ModItems.USER_WOOL_15_ITEM);
                entries.add(ModItems.USER_WOOL_16_ITEM);
                entries.add(ModItems.USER_WOOL_17_ITEM);
                entries.add(ModItems.USER_WOOL_18_ITEM);
                entries.add(ModItems.USER_WOOL_19_ITEM);
                entries.add(ModItems.USER_WOOL_20_ITEM);

                // USER_STONE blocks (1-20)
                entries.add(ModItems.USER_STONE_1_ITEM);
                entries.add(ModItems.USER_STONE_2_ITEM);
                entries.add(ModItems.USER_STONE_3_ITEM);
                entries.add(ModItems.USER_STONE_4_ITEM);
                entries.add(ModItems.USER_STONE_5_ITEM);
                entries.add(ModItems.USER_STONE_6_ITEM);
                entries.add(ModItems.USER_STONE_7_ITEM);
                entries.add(ModItems.USER_STONE_8_ITEM);
                entries.add(ModItems.USER_STONE_9_ITEM);
                entries.add(ModItems.USER_STONE_10_ITEM);
                entries.add(ModItems.USER_STONE_11_ITEM);
                entries.add(ModItems.USER_STONE_12_ITEM);
                entries.add(ModItems.USER_STONE_13_ITEM);
                entries.add(ModItems.USER_STONE_14_ITEM);
                entries.add(ModItems.USER_STONE_15_ITEM);
                entries.add(ModItems.USER_STONE_16_ITEM);
                entries.add(ModItems.USER_STONE_17_ITEM);
                entries.add(ModItems.USER_STONE_18_ITEM);
                entries.add(ModItems.USER_STONE_19_ITEM);
                entries.add(ModItems.USER_STONE_20_ITEM);

                // USER_CONCRETE blocks (1-20)
                entries.add(ModItems.USER_CONCRETE_1_ITEM);
                entries.add(ModItems.USER_CONCRETE_2_ITEM);
                entries.add(ModItems.USER_CONCRETE_3_ITEM);
                entries.add(ModItems.USER_CONCRETE_4_ITEM);
                entries.add(ModItems.USER_CONCRETE_5_ITEM);
                entries.add(ModItems.USER_CONCRETE_6_ITEM);
                entries.add(ModItems.USER_CONCRETE_7_ITEM);
                entries.add(ModItems.USER_CONCRETE_8_ITEM);
                entries.add(ModItems.USER_CONCRETE_9_ITEM);
                entries.add(ModItems.USER_CONCRETE_10_ITEM);
                entries.add(ModItems.USER_CONCRETE_11_ITEM);
                entries.add(ModItems.USER_CONCRETE_12_ITEM);
                entries.add(ModItems.USER_CONCRETE_13_ITEM);
                entries.add(ModItems.USER_CONCRETE_14_ITEM);
                entries.add(ModItems.USER_CONCRETE_15_ITEM);
                entries.add(ModItems.USER_CONCRETE_16_ITEM);
                entries.add(ModItems.USER_CONCRETE_17_ITEM);
                entries.add(ModItems.USER_CONCRETE_18_ITEM);
                entries.add(ModItems.USER_CONCRETE_19_ITEM);
                entries.add(ModItems.USER_CONCRETE_20_ITEM);

                // USER_WOOD blocks (1-20)
                entries.add(ModItems.USER_WOOD_1_ITEM);
                entries.add(ModItems.USER_WOOD_2_ITEM);
                entries.add(ModItems.USER_WOOD_3_ITEM);
                entries.add(ModItems.USER_WOOD_4_ITEM);
                entries.add(ModItems.USER_WOOD_5_ITEM);
                entries.add(ModItems.USER_WOOD_6_ITEM);
                entries.add(ModItems.USER_WOOD_7_ITEM);
                entries.add(ModItems.USER_WOOD_8_ITEM);
                entries.add(ModItems.USER_WOOD_9_ITEM);
                entries.add(ModItems.USER_WOOD_10_ITEM);
                entries.add(ModItems.USER_WOOD_11_ITEM);
                entries.add(ModItems.USER_WOOD_12_ITEM);
                entries.add(ModItems.USER_WOOD_13_ITEM);
                entries.add(ModItems.USER_WOOD_14_ITEM);
                entries.add(ModItems.USER_WOOD_15_ITEM);
                entries.add(ModItems.USER_WOOD_16_ITEM);
                entries.add(ModItems.USER_WOOD_17_ITEM);
                entries.add(ModItems.USER_WOOD_18_ITEM);
                entries.add(ModItems.USER_WOOD_19_ITEM);
                entries.add(ModItems.USER_WOOD_20_ITEM);

                // USER_DIRT blocks (1-20)
                entries.add(ModItems.USER_DIRT_1_ITEM);
                entries.add(ModItems.USER_DIRT_2_ITEM);
                entries.add(ModItems.USER_DIRT_3_ITEM);
                entries.add(ModItems.USER_DIRT_4_ITEM);
                entries.add(ModItems.USER_DIRT_5_ITEM);
                entries.add(ModItems.USER_DIRT_6_ITEM);
                entries.add(ModItems.USER_DIRT_7_ITEM);
                entries.add(ModItems.USER_DIRT_8_ITEM);
                entries.add(ModItems.USER_DIRT_9_ITEM);
                entries.add(ModItems.USER_DIRT_10_ITEM);
                entries.add(ModItems.USER_DIRT_11_ITEM);
                entries.add(ModItems.USER_DIRT_12_ITEM);
                entries.add(ModItems.USER_DIRT_13_ITEM);
                entries.add(ModItems.USER_DIRT_14_ITEM);
                entries.add(ModItems.USER_DIRT_15_ITEM);
                entries.add(ModItems.USER_DIRT_16_ITEM);
                entries.add(ModItems.USER_DIRT_17_ITEM);
                entries.add(ModItems.USER_DIRT_18_ITEM);
                entries.add(ModItems.USER_DIRT_19_ITEM);
                entries.add(ModItems.USER_DIRT_20_ITEM);

                // USER_SAND blocks (1-20)
                entries.add(ModItems.USER_SAND_1_ITEM);
                entries.add(ModItems.USER_SAND_2_ITEM);
                entries.add(ModItems.USER_SAND_3_ITEM);
                entries.add(ModItems.USER_SAND_4_ITEM);
                entries.add(ModItems.USER_SAND_5_ITEM);
                entries.add(ModItems.USER_SAND_6_ITEM);
                entries.add(ModItems.USER_SAND_7_ITEM);
                entries.add(ModItems.USER_SAND_8_ITEM);
                entries.add(ModItems.USER_SAND_9_ITEM);
                entries.add(ModItems.USER_SAND_10_ITEM);
                entries.add(ModItems.USER_SAND_11_ITEM);
                entries.add(ModItems.USER_SAND_12_ITEM);
                entries.add(ModItems.USER_SAND_13_ITEM);
                entries.add(ModItems.USER_SAND_14_ITEM);
                entries.add(ModItems.USER_SAND_15_ITEM);
                entries.add(ModItems.USER_SAND_16_ITEM);
                entries.add(ModItems.USER_SAND_17_ITEM);
                entries.add(ModItems.USER_SAND_18_ITEM);
                entries.add(ModItems.USER_SAND_19_ITEM);
                entries.add(ModItems.USER_SAND_20_ITEM);

                // USER_DEEPSLATE blocks (1-20)
                entries.add(ModItems.USER_DEEPSLATE_1_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_2_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_3_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_4_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_5_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_6_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_7_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_8_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_9_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_10_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_11_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_12_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_13_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_14_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_15_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_16_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_17_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_18_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_19_ITEM);
                entries.add(ModItems.USER_DEEPSLATE_20_ITEM);

                // USER_COBBLESTONE blocks (1-20)
                entries.add(ModItems.USER_COBBLESTONE_1_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_2_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_3_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_4_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_5_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_6_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_7_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_8_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_9_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_10_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_11_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_12_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_13_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_14_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_15_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_16_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_17_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_18_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_19_ITEM);
                entries.add(ModItems.USER_COBBLESTONE_20_ITEM);

                // USER_SMOOTH_STONE blocks (1-20)
                entries.add(ModItems.USER_SMOOTH_STONE_1_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_2_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_3_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_4_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_5_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_6_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_7_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_8_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_9_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_10_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_11_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_12_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_13_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_14_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_15_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_16_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_17_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_18_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_19_ITEM);
                entries.add(ModItems.USER_SMOOTH_STONE_20_ITEM);

                // USER_TERRACOTTA blocks (1-20)
                entries.add(ModItems.USER_TERRACOTTA_1_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_2_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_3_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_4_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_5_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_6_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_7_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_8_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_9_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_10_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_11_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_12_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_13_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_14_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_15_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_16_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_17_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_18_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_19_ITEM);
                entries.add(ModItems.USER_TERRACOTTA_20_ITEM);

                // USER_CONCRETE_POWDER blocks (1-20)
                entries.add(ModItems.USER_CONCRETE_POWDER_1_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_2_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_3_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_4_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_5_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_6_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_7_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_8_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_9_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_10_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_11_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_12_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_13_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_14_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_15_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_16_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_17_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_18_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_19_ITEM);
                entries.add(ModItems.USER_CONCRETE_POWDER_20_ITEM);

                // USER_GLASS blocks (1-20)
                entries.add(ModItems.USER_GLASS_1_ITEM);
                entries.add(ModItems.USER_GLASS_2_ITEM);
                entries.add(ModItems.USER_GLASS_3_ITEM);
                entries.add(ModItems.USER_GLASS_4_ITEM);
                entries.add(ModItems.USER_GLASS_5_ITEM);
                entries.add(ModItems.USER_GLASS_6_ITEM);
                entries.add(ModItems.USER_GLASS_7_ITEM);
                entries.add(ModItems.USER_GLASS_8_ITEM);
                entries.add(ModItems.USER_GLASS_9_ITEM);
                entries.add(ModItems.USER_GLASS_10_ITEM);
                entries.add(ModItems.USER_GLASS_11_ITEM);
                entries.add(ModItems.USER_GLASS_12_ITEM);
                entries.add(ModItems.USER_GLASS_13_ITEM);
                entries.add(ModItems.USER_GLASS_14_ITEM);
                entries.add(ModItems.USER_GLASS_15_ITEM);
                entries.add(ModItems.USER_GLASS_16_ITEM);
                entries.add(ModItems.USER_GLASS_17_ITEM);
                entries.add(ModItems.USER_GLASS_18_ITEM);
                entries.add(ModItems.USER_GLASS_19_ITEM);
                entries.add(ModItems.USER_GLASS_20_ITEM);

                // USER_TINTED_GLASS blocks (1-20)
                entries.add(ModItems.USER_TINTED_GLASS_1_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_2_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_3_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_4_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_5_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_6_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_7_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_8_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_9_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_10_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_11_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_12_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_13_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_14_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_15_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_16_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_17_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_18_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_19_ITEM);
                entries.add(ModItems.USER_TINTED_GLASS_20_ITEM);

                // USER_STAINED_GLASS blocks (1-20)
                entries.add(ModItems.USER_STAINED_GLASS_1_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_2_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_3_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_4_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_5_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_6_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_7_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_8_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_9_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_10_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_11_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_12_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_13_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_14_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_15_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_16_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_17_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_18_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_19_ITEM);
                entries.add(ModItems.USER_STAINED_GLASS_20_ITEM);

                // USER_DIORITE blocks (1-20)
                entries.add(ModItems.USER_DIORITE_1_ITEM);
                entries.add(ModItems.USER_DIORITE_2_ITEM);
                entries.add(ModItems.USER_DIORITE_3_ITEM);
                entries.add(ModItems.USER_DIORITE_4_ITEM);
                entries.add(ModItems.USER_DIORITE_5_ITEM);
                entries.add(ModItems.USER_DIORITE_6_ITEM);
                entries.add(ModItems.USER_DIORITE_7_ITEM);
                entries.add(ModItems.USER_DIORITE_8_ITEM);
                entries.add(ModItems.USER_DIORITE_9_ITEM);
                entries.add(ModItems.USER_DIORITE_10_ITEM);
                entries.add(ModItems.USER_DIORITE_11_ITEM);
                entries.add(ModItems.USER_DIORITE_12_ITEM);
                entries.add(ModItems.USER_DIORITE_13_ITEM);
                entries.add(ModItems.USER_DIORITE_14_ITEM);
                entries.add(ModItems.USER_DIORITE_15_ITEM);
                entries.add(ModItems.USER_DIORITE_16_ITEM);
                entries.add(ModItems.USER_DIORITE_17_ITEM);
                entries.add(ModItems.USER_DIORITE_18_ITEM);
                entries.add(ModItems.USER_DIORITE_19_ITEM);
                entries.add(ModItems.USER_DIORITE_20_ITEM);

                // USER_CALCITE blocks (1-20)
                entries.add(ModItems.USER_CALCITE_1_ITEM);
                entries.add(ModItems.USER_CALCITE_2_ITEM);
                entries.add(ModItems.USER_CALCITE_3_ITEM);
                entries.add(ModItems.USER_CALCITE_4_ITEM);
                entries.add(ModItems.USER_CALCITE_5_ITEM);
                entries.add(ModItems.USER_CALCITE_6_ITEM);
                entries.add(ModItems.USER_CALCITE_7_ITEM);
                entries.add(ModItems.USER_CALCITE_8_ITEM);
                entries.add(ModItems.USER_CALCITE_9_ITEM);
                entries.add(ModItems.USER_CALCITE_10_ITEM);
                entries.add(ModItems.USER_CALCITE_11_ITEM);
                entries.add(ModItems.USER_CALCITE_12_ITEM);
                entries.add(ModItems.USER_CALCITE_13_ITEM);
                entries.add(ModItems.USER_CALCITE_14_ITEM);
                entries.add(ModItems.USER_CALCITE_15_ITEM);
                entries.add(ModItems.USER_CALCITE_16_ITEM);
                entries.add(ModItems.USER_CALCITE_17_ITEM);
                entries.add(ModItems.USER_CALCITE_18_ITEM);
                entries.add(ModItems.USER_CALCITE_19_ITEM);
                entries.add(ModItems.USER_CALCITE_20_ITEM);

                // USER_MUSHROOM_STEM blocks (1-20)
                entries.add(ModItems.USER_MUSHROOM_STEM_1_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_2_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_3_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_4_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_5_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_6_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_7_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_8_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_9_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_10_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_11_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_12_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_13_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_14_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_15_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_16_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_17_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_18_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_19_ITEM);
                entries.add(ModItems.USER_MUSHROOM_STEM_20_ITEM);

                // USER_DEAD_TUBE_CORAL blocks (1-20)
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_1_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_2_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_3_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_4_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_5_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_6_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_7_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_8_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_9_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_10_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_11_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_12_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_13_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_14_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_15_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_16_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_17_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_18_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_19_ITEM);
                entries.add(ModItems.USER_DEAD_TUBE_CORAL_20_ITEM);

                // USER_PEARLESCENT_FROGLIGHT blocks (1-20)
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_1_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_2_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_3_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_4_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_5_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_6_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_7_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_8_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_9_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_10_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_11_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_12_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_13_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_14_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_15_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_16_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_17_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_18_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_19_ITEM);
                entries.add(ModItems.USER_PEARLESCENT_FROGLIGHT_20_ITEM);

            })
            .build()
    );

    public static void register() {
        // Triggers class loading and static field initialization
    }
}
