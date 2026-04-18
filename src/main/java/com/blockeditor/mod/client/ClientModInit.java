package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.client.gui.BlockEditorScreen;
import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;

public class ClientModInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register key bindings
        KeyBindingHelper.registerKeyBinding(ModKeyMappings.OPEN_BLOCK_EDITOR);

        // Register client tick event for key binding
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeyMappings.OPEN_BLOCK_EDITOR.wasPressed() && client.currentScreen == null) {
                client.setScreen(new BlockEditorScreen());
            }
        });

        // Register render layers for glass blocks
        registerRenderLayers();

        // Register block color providers
        registerBlockColors();

        // Register item color providers
        registerItemColors();
    }

    private void registerRenderLayers() {
        // Dynamic glass blocks
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYNAMIC_BLOCK_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS, RenderLayer.getTranslucent());

        // User glass blocks (1-20)
        for (var block : new net.minecraft.block.Block[]{
            ModBlocks.USER_GLASS_1, ModBlocks.USER_GLASS_2, ModBlocks.USER_GLASS_3, ModBlocks.USER_GLASS_4, ModBlocks.USER_GLASS_5,
            ModBlocks.USER_GLASS_6, ModBlocks.USER_GLASS_7, ModBlocks.USER_GLASS_8, ModBlocks.USER_GLASS_9, ModBlocks.USER_GLASS_10,
            ModBlocks.USER_GLASS_11, ModBlocks.USER_GLASS_12, ModBlocks.USER_GLASS_13, ModBlocks.USER_GLASS_14, ModBlocks.USER_GLASS_15,
            ModBlocks.USER_GLASS_16, ModBlocks.USER_GLASS_17, ModBlocks.USER_GLASS_18, ModBlocks.USER_GLASS_19, ModBlocks.USER_GLASS_20,
            ModBlocks.USER_TINTED_GLASS_1, ModBlocks.USER_TINTED_GLASS_2, ModBlocks.USER_TINTED_GLASS_3, ModBlocks.USER_TINTED_GLASS_4, ModBlocks.USER_TINTED_GLASS_5,
            ModBlocks.USER_TINTED_GLASS_6, ModBlocks.USER_TINTED_GLASS_7, ModBlocks.USER_TINTED_GLASS_8, ModBlocks.USER_TINTED_GLASS_9, ModBlocks.USER_TINTED_GLASS_10,
            ModBlocks.USER_TINTED_GLASS_11, ModBlocks.USER_TINTED_GLASS_12, ModBlocks.USER_TINTED_GLASS_13, ModBlocks.USER_TINTED_GLASS_14, ModBlocks.USER_TINTED_GLASS_15,
            ModBlocks.USER_TINTED_GLASS_16, ModBlocks.USER_TINTED_GLASS_17, ModBlocks.USER_TINTED_GLASS_18, ModBlocks.USER_TINTED_GLASS_19, ModBlocks.USER_TINTED_GLASS_20,
            ModBlocks.USER_STAINED_GLASS_1, ModBlocks.USER_STAINED_GLASS_2, ModBlocks.USER_STAINED_GLASS_3, ModBlocks.USER_STAINED_GLASS_4, ModBlocks.USER_STAINED_GLASS_5,
            ModBlocks.USER_STAINED_GLASS_6, ModBlocks.USER_STAINED_GLASS_7, ModBlocks.USER_STAINED_GLASS_8, ModBlocks.USER_STAINED_GLASS_9, ModBlocks.USER_STAINED_GLASS_10,
            ModBlocks.USER_STAINED_GLASS_11, ModBlocks.USER_STAINED_GLASS_12, ModBlocks.USER_STAINED_GLASS_13, ModBlocks.USER_STAINED_GLASS_14, ModBlocks.USER_STAINED_GLASS_15,
            ModBlocks.USER_STAINED_GLASS_16, ModBlocks.USER_STAINED_GLASS_17, ModBlocks.USER_STAINED_GLASS_18, ModBlocks.USER_STAINED_GLASS_19, ModBlocks.USER_STAINED_GLASS_20
        }) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
        }
    }

    @SuppressWarnings("deprecation")
    private void registerBlockColors() {
        net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry.BLOCK.register(
            (state, world, pos, tintIndex) -> {
                if (tintIndex != 0) return 0xFFFFFF;
                if (world != null && pos != null && world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                    return blockEntity.getColor();
                }
                return 0xFFFFFF;
            },
            // Regular dynamic blocks
            ModBlocks.DYNAMIC_BLOCK,
            ModBlocks.DYNAMIC_BLOCK_DIRT,
            ModBlocks.DYNAMIC_BLOCK_SAND,
            ModBlocks.DYNAMIC_BLOCK_WOOL,
            ModBlocks.DYNAMIC_BLOCK_CONCRETE,
            ModBlocks.DYNAMIC_BLOCK_DEEPSLATE,
            ModBlocks.DYNAMIC_BLOCK_WOOD,
            ModBlocks.DYNAMIC_BLOCK_STONE,
            ModBlocks.DYNAMIC_BLOCK_COBBLESTONE,
            ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE,
            ModBlocks.DYNAMIC_BLOCK_TERRACOTTA,
            ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER,
            ModBlocks.DYNAMIC_BLOCK_GLASS,
            ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS,
            ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS,
            ModBlocks.DYNAMIC_BLOCK_DIORITE,
            ModBlocks.DYNAMIC_BLOCK_CALCITE,
            ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM,
            ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL,
            ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT,
            // User blocks - Wool (1-20)
            ModBlocks.USER_WOOL_1, ModBlocks.USER_WOOL_2, ModBlocks.USER_WOOL_3, ModBlocks.USER_WOOL_4, ModBlocks.USER_WOOL_5,
            ModBlocks.USER_WOOL_6, ModBlocks.USER_WOOL_7, ModBlocks.USER_WOOL_8, ModBlocks.USER_WOOL_9, ModBlocks.USER_WOOL_10,
            ModBlocks.USER_WOOL_11, ModBlocks.USER_WOOL_12, ModBlocks.USER_WOOL_13, ModBlocks.USER_WOOL_14, ModBlocks.USER_WOOL_15,
            ModBlocks.USER_WOOL_16, ModBlocks.USER_WOOL_17, ModBlocks.USER_WOOL_18, ModBlocks.USER_WOOL_19, ModBlocks.USER_WOOL_20,
            // Stone (1-20)
            ModBlocks.USER_STONE_1, ModBlocks.USER_STONE_2, ModBlocks.USER_STONE_3, ModBlocks.USER_STONE_4, ModBlocks.USER_STONE_5,
            ModBlocks.USER_STONE_6, ModBlocks.USER_STONE_7, ModBlocks.USER_STONE_8, ModBlocks.USER_STONE_9, ModBlocks.USER_STONE_10,
            ModBlocks.USER_STONE_11, ModBlocks.USER_STONE_12, ModBlocks.USER_STONE_13, ModBlocks.USER_STONE_14, ModBlocks.USER_STONE_15,
            ModBlocks.USER_STONE_16, ModBlocks.USER_STONE_17, ModBlocks.USER_STONE_18, ModBlocks.USER_STONE_19, ModBlocks.USER_STONE_20,
            // Concrete (1-20)
            ModBlocks.USER_CONCRETE_1, ModBlocks.USER_CONCRETE_2, ModBlocks.USER_CONCRETE_3, ModBlocks.USER_CONCRETE_4, ModBlocks.USER_CONCRETE_5,
            ModBlocks.USER_CONCRETE_6, ModBlocks.USER_CONCRETE_7, ModBlocks.USER_CONCRETE_8, ModBlocks.USER_CONCRETE_9, ModBlocks.USER_CONCRETE_10,
            ModBlocks.USER_CONCRETE_11, ModBlocks.USER_CONCRETE_12, ModBlocks.USER_CONCRETE_13, ModBlocks.USER_CONCRETE_14, ModBlocks.USER_CONCRETE_15,
            ModBlocks.USER_CONCRETE_16, ModBlocks.USER_CONCRETE_17, ModBlocks.USER_CONCRETE_18, ModBlocks.USER_CONCRETE_19, ModBlocks.USER_CONCRETE_20,
            // Wood (1-20)
            ModBlocks.USER_WOOD_1, ModBlocks.USER_WOOD_2, ModBlocks.USER_WOOD_3, ModBlocks.USER_WOOD_4, ModBlocks.USER_WOOD_5,
            ModBlocks.USER_WOOD_6, ModBlocks.USER_WOOD_7, ModBlocks.USER_WOOD_8, ModBlocks.USER_WOOD_9, ModBlocks.USER_WOOD_10,
            ModBlocks.USER_WOOD_11, ModBlocks.USER_WOOD_12, ModBlocks.USER_WOOD_13, ModBlocks.USER_WOOD_14, ModBlocks.USER_WOOD_15,
            ModBlocks.USER_WOOD_16, ModBlocks.USER_WOOD_17, ModBlocks.USER_WOOD_18, ModBlocks.USER_WOOD_19, ModBlocks.USER_WOOD_20,
            // Dirt (1-20)
            ModBlocks.USER_DIRT_1, ModBlocks.USER_DIRT_2, ModBlocks.USER_DIRT_3, ModBlocks.USER_DIRT_4, ModBlocks.USER_DIRT_5,
            ModBlocks.USER_DIRT_6, ModBlocks.USER_DIRT_7, ModBlocks.USER_DIRT_8, ModBlocks.USER_DIRT_9, ModBlocks.USER_DIRT_10,
            ModBlocks.USER_DIRT_11, ModBlocks.USER_DIRT_12, ModBlocks.USER_DIRT_13, ModBlocks.USER_DIRT_14, ModBlocks.USER_DIRT_15,
            ModBlocks.USER_DIRT_16, ModBlocks.USER_DIRT_17, ModBlocks.USER_DIRT_18, ModBlocks.USER_DIRT_19, ModBlocks.USER_DIRT_20,
            // Sand (1-20)
            ModBlocks.USER_SAND_1, ModBlocks.USER_SAND_2, ModBlocks.USER_SAND_3, ModBlocks.USER_SAND_4, ModBlocks.USER_SAND_5,
            ModBlocks.USER_SAND_6, ModBlocks.USER_SAND_7, ModBlocks.USER_SAND_8, ModBlocks.USER_SAND_9, ModBlocks.USER_SAND_10,
            ModBlocks.USER_SAND_11, ModBlocks.USER_SAND_12, ModBlocks.USER_SAND_13, ModBlocks.USER_SAND_14, ModBlocks.USER_SAND_15,
            ModBlocks.USER_SAND_16, ModBlocks.USER_SAND_17, ModBlocks.USER_SAND_18, ModBlocks.USER_SAND_19, ModBlocks.USER_SAND_20,
            // Deepslate (1-20)
            ModBlocks.USER_DEEPSLATE_1, ModBlocks.USER_DEEPSLATE_2, ModBlocks.USER_DEEPSLATE_3, ModBlocks.USER_DEEPSLATE_4, ModBlocks.USER_DEEPSLATE_5,
            ModBlocks.USER_DEEPSLATE_6, ModBlocks.USER_DEEPSLATE_7, ModBlocks.USER_DEEPSLATE_8, ModBlocks.USER_DEEPSLATE_9, ModBlocks.USER_DEEPSLATE_10,
            ModBlocks.USER_DEEPSLATE_11, ModBlocks.USER_DEEPSLATE_12, ModBlocks.USER_DEEPSLATE_13, ModBlocks.USER_DEEPSLATE_14, ModBlocks.USER_DEEPSLATE_15,
            ModBlocks.USER_DEEPSLATE_16, ModBlocks.USER_DEEPSLATE_17, ModBlocks.USER_DEEPSLATE_18, ModBlocks.USER_DEEPSLATE_19, ModBlocks.USER_DEEPSLATE_20,
            // Cobblestone (1-20)
            ModBlocks.USER_COBBLESTONE_1, ModBlocks.USER_COBBLESTONE_2, ModBlocks.USER_COBBLESTONE_3, ModBlocks.USER_COBBLESTONE_4, ModBlocks.USER_COBBLESTONE_5,
            ModBlocks.USER_COBBLESTONE_6, ModBlocks.USER_COBBLESTONE_7, ModBlocks.USER_COBBLESTONE_8, ModBlocks.USER_COBBLESTONE_9, ModBlocks.USER_COBBLESTONE_10,
            ModBlocks.USER_COBBLESTONE_11, ModBlocks.USER_COBBLESTONE_12, ModBlocks.USER_COBBLESTONE_13, ModBlocks.USER_COBBLESTONE_14, ModBlocks.USER_COBBLESTONE_15,
            ModBlocks.USER_COBBLESTONE_16, ModBlocks.USER_COBBLESTONE_17, ModBlocks.USER_COBBLESTONE_18, ModBlocks.USER_COBBLESTONE_19, ModBlocks.USER_COBBLESTONE_20,
            // Smooth Stone (1-20)
            ModBlocks.USER_SMOOTH_STONE_1, ModBlocks.USER_SMOOTH_STONE_2, ModBlocks.USER_SMOOTH_STONE_3, ModBlocks.USER_SMOOTH_STONE_4, ModBlocks.USER_SMOOTH_STONE_5,
            ModBlocks.USER_SMOOTH_STONE_6, ModBlocks.USER_SMOOTH_STONE_7, ModBlocks.USER_SMOOTH_STONE_8, ModBlocks.USER_SMOOTH_STONE_9, ModBlocks.USER_SMOOTH_STONE_10,
            ModBlocks.USER_SMOOTH_STONE_11, ModBlocks.USER_SMOOTH_STONE_12, ModBlocks.USER_SMOOTH_STONE_13, ModBlocks.USER_SMOOTH_STONE_14, ModBlocks.USER_SMOOTH_STONE_15,
            ModBlocks.USER_SMOOTH_STONE_16, ModBlocks.USER_SMOOTH_STONE_17, ModBlocks.USER_SMOOTH_STONE_18, ModBlocks.USER_SMOOTH_STONE_19, ModBlocks.USER_SMOOTH_STONE_20,
            // Terracotta (1-20)
            ModBlocks.USER_TERRACOTTA_1, ModBlocks.USER_TERRACOTTA_2, ModBlocks.USER_TERRACOTTA_3, ModBlocks.USER_TERRACOTTA_4, ModBlocks.USER_TERRACOTTA_5,
            ModBlocks.USER_TERRACOTTA_6, ModBlocks.USER_TERRACOTTA_7, ModBlocks.USER_TERRACOTTA_8, ModBlocks.USER_TERRACOTTA_9, ModBlocks.USER_TERRACOTTA_10,
            ModBlocks.USER_TERRACOTTA_11, ModBlocks.USER_TERRACOTTA_12, ModBlocks.USER_TERRACOTTA_13, ModBlocks.USER_TERRACOTTA_14, ModBlocks.USER_TERRACOTTA_15,
            ModBlocks.USER_TERRACOTTA_16, ModBlocks.USER_TERRACOTTA_17, ModBlocks.USER_TERRACOTTA_18, ModBlocks.USER_TERRACOTTA_19, ModBlocks.USER_TERRACOTTA_20,
            // Concrete Powder (1-20)
            ModBlocks.USER_CONCRETE_POWDER_1, ModBlocks.USER_CONCRETE_POWDER_2, ModBlocks.USER_CONCRETE_POWDER_3, ModBlocks.USER_CONCRETE_POWDER_4, ModBlocks.USER_CONCRETE_POWDER_5,
            ModBlocks.USER_CONCRETE_POWDER_6, ModBlocks.USER_CONCRETE_POWDER_7, ModBlocks.USER_CONCRETE_POWDER_8, ModBlocks.USER_CONCRETE_POWDER_9, ModBlocks.USER_CONCRETE_POWDER_10,
            ModBlocks.USER_CONCRETE_POWDER_11, ModBlocks.USER_CONCRETE_POWDER_12, ModBlocks.USER_CONCRETE_POWDER_13, ModBlocks.USER_CONCRETE_POWDER_14, ModBlocks.USER_CONCRETE_POWDER_15,
            ModBlocks.USER_CONCRETE_POWDER_16, ModBlocks.USER_CONCRETE_POWDER_17, ModBlocks.USER_CONCRETE_POWDER_18, ModBlocks.USER_CONCRETE_POWDER_19, ModBlocks.USER_CONCRETE_POWDER_20,
            // Glass (1-20)
            ModBlocks.USER_GLASS_1, ModBlocks.USER_GLASS_2, ModBlocks.USER_GLASS_3, ModBlocks.USER_GLASS_4, ModBlocks.USER_GLASS_5,
            ModBlocks.USER_GLASS_6, ModBlocks.USER_GLASS_7, ModBlocks.USER_GLASS_8, ModBlocks.USER_GLASS_9, ModBlocks.USER_GLASS_10,
            ModBlocks.USER_GLASS_11, ModBlocks.USER_GLASS_12, ModBlocks.USER_GLASS_13, ModBlocks.USER_GLASS_14, ModBlocks.USER_GLASS_15,
            ModBlocks.USER_GLASS_16, ModBlocks.USER_GLASS_17, ModBlocks.USER_GLASS_18, ModBlocks.USER_GLASS_19, ModBlocks.USER_GLASS_20,
            // Diorite (1-20)
            ModBlocks.USER_DIORITE_1, ModBlocks.USER_DIORITE_2, ModBlocks.USER_DIORITE_3, ModBlocks.USER_DIORITE_4, ModBlocks.USER_DIORITE_5,
            ModBlocks.USER_DIORITE_6, ModBlocks.USER_DIORITE_7, ModBlocks.USER_DIORITE_8, ModBlocks.USER_DIORITE_9, ModBlocks.USER_DIORITE_10,
            ModBlocks.USER_DIORITE_11, ModBlocks.USER_DIORITE_12, ModBlocks.USER_DIORITE_13, ModBlocks.USER_DIORITE_14, ModBlocks.USER_DIORITE_15,
            ModBlocks.USER_DIORITE_16, ModBlocks.USER_DIORITE_17, ModBlocks.USER_DIORITE_18, ModBlocks.USER_DIORITE_19, ModBlocks.USER_DIORITE_20,
            // Calcite (1-20)
            ModBlocks.USER_CALCITE_1, ModBlocks.USER_CALCITE_2, ModBlocks.USER_CALCITE_3, ModBlocks.USER_CALCITE_4, ModBlocks.USER_CALCITE_5,
            ModBlocks.USER_CALCITE_6, ModBlocks.USER_CALCITE_7, ModBlocks.USER_CALCITE_8, ModBlocks.USER_CALCITE_9, ModBlocks.USER_CALCITE_10,
            ModBlocks.USER_CALCITE_11, ModBlocks.USER_CALCITE_12, ModBlocks.USER_CALCITE_13, ModBlocks.USER_CALCITE_14, ModBlocks.USER_CALCITE_15,
            ModBlocks.USER_CALCITE_16, ModBlocks.USER_CALCITE_17, ModBlocks.USER_CALCITE_18, ModBlocks.USER_CALCITE_19, ModBlocks.USER_CALCITE_20,
            // Mushroom Stem (1-20)
            ModBlocks.USER_MUSHROOM_STEM_1, ModBlocks.USER_MUSHROOM_STEM_2, ModBlocks.USER_MUSHROOM_STEM_3, ModBlocks.USER_MUSHROOM_STEM_4, ModBlocks.USER_MUSHROOM_STEM_5,
            ModBlocks.USER_MUSHROOM_STEM_6, ModBlocks.USER_MUSHROOM_STEM_7, ModBlocks.USER_MUSHROOM_STEM_8, ModBlocks.USER_MUSHROOM_STEM_9, ModBlocks.USER_MUSHROOM_STEM_10,
            ModBlocks.USER_MUSHROOM_STEM_11, ModBlocks.USER_MUSHROOM_STEM_12, ModBlocks.USER_MUSHROOM_STEM_13, ModBlocks.USER_MUSHROOM_STEM_14, ModBlocks.USER_MUSHROOM_STEM_15,
            ModBlocks.USER_MUSHROOM_STEM_16, ModBlocks.USER_MUSHROOM_STEM_17, ModBlocks.USER_MUSHROOM_STEM_18, ModBlocks.USER_MUSHROOM_STEM_19, ModBlocks.USER_MUSHROOM_STEM_20,
            // Dead Tube Coral (1-20)
            ModBlocks.USER_DEAD_TUBE_CORAL_1, ModBlocks.USER_DEAD_TUBE_CORAL_2, ModBlocks.USER_DEAD_TUBE_CORAL_3, ModBlocks.USER_DEAD_TUBE_CORAL_4, ModBlocks.USER_DEAD_TUBE_CORAL_5,
            ModBlocks.USER_DEAD_TUBE_CORAL_6, ModBlocks.USER_DEAD_TUBE_CORAL_7, ModBlocks.USER_DEAD_TUBE_CORAL_8, ModBlocks.USER_DEAD_TUBE_CORAL_9, ModBlocks.USER_DEAD_TUBE_CORAL_10,
            ModBlocks.USER_DEAD_TUBE_CORAL_11, ModBlocks.USER_DEAD_TUBE_CORAL_12, ModBlocks.USER_DEAD_TUBE_CORAL_13, ModBlocks.USER_DEAD_TUBE_CORAL_14, ModBlocks.USER_DEAD_TUBE_CORAL_15,
            ModBlocks.USER_DEAD_TUBE_CORAL_16, ModBlocks.USER_DEAD_TUBE_CORAL_17, ModBlocks.USER_DEAD_TUBE_CORAL_18, ModBlocks.USER_DEAD_TUBE_CORAL_19, ModBlocks.USER_DEAD_TUBE_CORAL_20,
            // Pearlescent Froglight (1-20)
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_1, ModBlocks.USER_PEARLESCENT_FROGLIGHT_2, ModBlocks.USER_PEARLESCENT_FROGLIGHT_3, ModBlocks.USER_PEARLESCENT_FROGLIGHT_4, ModBlocks.USER_PEARLESCENT_FROGLIGHT_5,
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_6, ModBlocks.USER_PEARLESCENT_FROGLIGHT_7, ModBlocks.USER_PEARLESCENT_FROGLIGHT_8, ModBlocks.USER_PEARLESCENT_FROGLIGHT_9, ModBlocks.USER_PEARLESCENT_FROGLIGHT_10,
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_11, ModBlocks.USER_PEARLESCENT_FROGLIGHT_12, ModBlocks.USER_PEARLESCENT_FROGLIGHT_13, ModBlocks.USER_PEARLESCENT_FROGLIGHT_14, ModBlocks.USER_PEARLESCENT_FROGLIGHT_15,
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_16, ModBlocks.USER_PEARLESCENT_FROGLIGHT_17, ModBlocks.USER_PEARLESCENT_FROGLIGHT_18, ModBlocks.USER_PEARLESCENT_FROGLIGHT_19, ModBlocks.USER_PEARLESCENT_FROGLIGHT_20,
            // Tinted Glass (1-20)
            ModBlocks.USER_TINTED_GLASS_1, ModBlocks.USER_TINTED_GLASS_2, ModBlocks.USER_TINTED_GLASS_3, ModBlocks.USER_TINTED_GLASS_4, ModBlocks.USER_TINTED_GLASS_5,
            ModBlocks.USER_TINTED_GLASS_6, ModBlocks.USER_TINTED_GLASS_7, ModBlocks.USER_TINTED_GLASS_8, ModBlocks.USER_TINTED_GLASS_9, ModBlocks.USER_TINTED_GLASS_10,
            ModBlocks.USER_TINTED_GLASS_11, ModBlocks.USER_TINTED_GLASS_12, ModBlocks.USER_TINTED_GLASS_13, ModBlocks.USER_TINTED_GLASS_14, ModBlocks.USER_TINTED_GLASS_15,
            ModBlocks.USER_TINTED_GLASS_16, ModBlocks.USER_TINTED_GLASS_17, ModBlocks.USER_TINTED_GLASS_18, ModBlocks.USER_TINTED_GLASS_19, ModBlocks.USER_TINTED_GLASS_20,
            // Stained Glass (1-20)
            ModBlocks.USER_STAINED_GLASS_1, ModBlocks.USER_STAINED_GLASS_2, ModBlocks.USER_STAINED_GLASS_3, ModBlocks.USER_STAINED_GLASS_4, ModBlocks.USER_STAINED_GLASS_5,
            ModBlocks.USER_STAINED_GLASS_6, ModBlocks.USER_STAINED_GLASS_7, ModBlocks.USER_STAINED_GLASS_8, ModBlocks.USER_STAINED_GLASS_9, ModBlocks.USER_STAINED_GLASS_10,
            ModBlocks.USER_STAINED_GLASS_11, ModBlocks.USER_STAINED_GLASS_12, ModBlocks.USER_STAINED_GLASS_13, ModBlocks.USER_STAINED_GLASS_14, ModBlocks.USER_STAINED_GLASS_15,
            ModBlocks.USER_STAINED_GLASS_16, ModBlocks.USER_STAINED_GLASS_17, ModBlocks.USER_STAINED_GLASS_18, ModBlocks.USER_STAINED_GLASS_19, ModBlocks.USER_STAINED_GLASS_20
        );
    }

    private void registerItemColors() {
        ColorProviderRegistry.ITEM.register(
            (stack, tintIndex) -> {
                NbtCompound tag = stack.getNbt();
                if (tintIndex == 0 && tag != null && tag.contains("Color")) {
                    String hexColor = tag.getString("Color");
                    try {
                        return Integer.parseInt(hexColor, 16);
                    } catch (NumberFormatException ignored) {}
                }
                return 0xFFFFFF;
            },
            // Dynamic block items
            ModBlocks.DYNAMIC_BLOCK.asItem(),
            ModBlocks.DYNAMIC_BLOCK_DIRT.asItem(),
            ModBlocks.DYNAMIC_BLOCK_SAND.asItem(),
            ModBlocks.DYNAMIC_BLOCK_WOOL.asItem(),
            ModBlocks.DYNAMIC_BLOCK_CONCRETE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_WOOD.asItem(),
            ModBlocks.DYNAMIC_BLOCK_STONE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.asItem(),
            ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.asItem(),
            ModBlocks.DYNAMIC_BLOCK_GLASS.asItem(),
            ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.asItem(),
            ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.asItem(),
            ModBlocks.DYNAMIC_BLOCK_DIORITE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_CALCITE.asItem(),
            ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.asItem(),
            ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.asItem(),
            ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.asItem(),
            // User block items - Wool (1-20)
            ModBlocks.USER_WOOL_1.asItem(), ModBlocks.USER_WOOL_2.asItem(), ModBlocks.USER_WOOL_3.asItem(), ModBlocks.USER_WOOL_4.asItem(), ModBlocks.USER_WOOL_5.asItem(),
            ModBlocks.USER_WOOL_6.asItem(), ModBlocks.USER_WOOL_7.asItem(), ModBlocks.USER_WOOL_8.asItem(), ModBlocks.USER_WOOL_9.asItem(), ModBlocks.USER_WOOL_10.asItem(),
            ModBlocks.USER_WOOL_11.asItem(), ModBlocks.USER_WOOL_12.asItem(), ModBlocks.USER_WOOL_13.asItem(), ModBlocks.USER_WOOL_14.asItem(), ModBlocks.USER_WOOL_15.asItem(),
            ModBlocks.USER_WOOL_16.asItem(), ModBlocks.USER_WOOL_17.asItem(), ModBlocks.USER_WOOL_18.asItem(), ModBlocks.USER_WOOL_19.asItem(), ModBlocks.USER_WOOL_20.asItem(),
            // Stone (1-20)
            ModBlocks.USER_STONE_1.asItem(), ModBlocks.USER_STONE_2.asItem(), ModBlocks.USER_STONE_3.asItem(), ModBlocks.USER_STONE_4.asItem(), ModBlocks.USER_STONE_5.asItem(),
            ModBlocks.USER_STONE_6.asItem(), ModBlocks.USER_STONE_7.asItem(), ModBlocks.USER_STONE_8.asItem(), ModBlocks.USER_STONE_9.asItem(), ModBlocks.USER_STONE_10.asItem(),
            ModBlocks.USER_STONE_11.asItem(), ModBlocks.USER_STONE_12.asItem(), ModBlocks.USER_STONE_13.asItem(), ModBlocks.USER_STONE_14.asItem(), ModBlocks.USER_STONE_15.asItem(),
            ModBlocks.USER_STONE_16.asItem(), ModBlocks.USER_STONE_17.asItem(), ModBlocks.USER_STONE_18.asItem(), ModBlocks.USER_STONE_19.asItem(), ModBlocks.USER_STONE_20.asItem(),
            // Concrete (1-20)
            ModBlocks.USER_CONCRETE_1.asItem(), ModBlocks.USER_CONCRETE_2.asItem(), ModBlocks.USER_CONCRETE_3.asItem(), ModBlocks.USER_CONCRETE_4.asItem(), ModBlocks.USER_CONCRETE_5.asItem(),
            ModBlocks.USER_CONCRETE_6.asItem(), ModBlocks.USER_CONCRETE_7.asItem(), ModBlocks.USER_CONCRETE_8.asItem(), ModBlocks.USER_CONCRETE_9.asItem(), ModBlocks.USER_CONCRETE_10.asItem(),
            ModBlocks.USER_CONCRETE_11.asItem(), ModBlocks.USER_CONCRETE_12.asItem(), ModBlocks.USER_CONCRETE_13.asItem(), ModBlocks.USER_CONCRETE_14.asItem(), ModBlocks.USER_CONCRETE_15.asItem(),
            ModBlocks.USER_CONCRETE_16.asItem(), ModBlocks.USER_CONCRETE_17.asItem(), ModBlocks.USER_CONCRETE_18.asItem(), ModBlocks.USER_CONCRETE_19.asItem(), ModBlocks.USER_CONCRETE_20.asItem(),
            // Wood (1-20)
            ModBlocks.USER_WOOD_1.asItem(), ModBlocks.USER_WOOD_2.asItem(), ModBlocks.USER_WOOD_3.asItem(), ModBlocks.USER_WOOD_4.asItem(), ModBlocks.USER_WOOD_5.asItem(),
            ModBlocks.USER_WOOD_6.asItem(), ModBlocks.USER_WOOD_7.asItem(), ModBlocks.USER_WOOD_8.asItem(), ModBlocks.USER_WOOD_9.asItem(), ModBlocks.USER_WOOD_10.asItem(),
            ModBlocks.USER_WOOD_11.asItem(), ModBlocks.USER_WOOD_12.asItem(), ModBlocks.USER_WOOD_13.asItem(), ModBlocks.USER_WOOD_14.asItem(), ModBlocks.USER_WOOD_15.asItem(),
            ModBlocks.USER_WOOD_16.asItem(), ModBlocks.USER_WOOD_17.asItem(), ModBlocks.USER_WOOD_18.asItem(), ModBlocks.USER_WOOD_19.asItem(), ModBlocks.USER_WOOD_20.asItem(),
            // Dirt (1-20)
            ModBlocks.USER_DIRT_1.asItem(), ModBlocks.USER_DIRT_2.asItem(), ModBlocks.USER_DIRT_3.asItem(), ModBlocks.USER_DIRT_4.asItem(), ModBlocks.USER_DIRT_5.asItem(),
            ModBlocks.USER_DIRT_6.asItem(), ModBlocks.USER_DIRT_7.asItem(), ModBlocks.USER_DIRT_8.asItem(), ModBlocks.USER_DIRT_9.asItem(), ModBlocks.USER_DIRT_10.asItem(),
            ModBlocks.USER_DIRT_11.asItem(), ModBlocks.USER_DIRT_12.asItem(), ModBlocks.USER_DIRT_13.asItem(), ModBlocks.USER_DIRT_14.asItem(), ModBlocks.USER_DIRT_15.asItem(),
            ModBlocks.USER_DIRT_16.asItem(), ModBlocks.USER_DIRT_17.asItem(), ModBlocks.USER_DIRT_18.asItem(), ModBlocks.USER_DIRT_19.asItem(), ModBlocks.USER_DIRT_20.asItem(),
            // Sand (1-20)
            ModBlocks.USER_SAND_1.asItem(), ModBlocks.USER_SAND_2.asItem(), ModBlocks.USER_SAND_3.asItem(), ModBlocks.USER_SAND_4.asItem(), ModBlocks.USER_SAND_5.asItem(),
            ModBlocks.USER_SAND_6.asItem(), ModBlocks.USER_SAND_7.asItem(), ModBlocks.USER_SAND_8.asItem(), ModBlocks.USER_SAND_9.asItem(), ModBlocks.USER_SAND_10.asItem(),
            ModBlocks.USER_SAND_11.asItem(), ModBlocks.USER_SAND_12.asItem(), ModBlocks.USER_SAND_13.asItem(), ModBlocks.USER_SAND_14.asItem(), ModBlocks.USER_SAND_15.asItem(),
            ModBlocks.USER_SAND_16.asItem(), ModBlocks.USER_SAND_17.asItem(), ModBlocks.USER_SAND_18.asItem(), ModBlocks.USER_SAND_19.asItem(), ModBlocks.USER_SAND_20.asItem(),
            // Deepslate (1-20)
            ModBlocks.USER_DEEPSLATE_1.asItem(), ModBlocks.USER_DEEPSLATE_2.asItem(), ModBlocks.USER_DEEPSLATE_3.asItem(), ModBlocks.USER_DEEPSLATE_4.asItem(), ModBlocks.USER_DEEPSLATE_5.asItem(),
            ModBlocks.USER_DEEPSLATE_6.asItem(), ModBlocks.USER_DEEPSLATE_7.asItem(), ModBlocks.USER_DEEPSLATE_8.asItem(), ModBlocks.USER_DEEPSLATE_9.asItem(), ModBlocks.USER_DEEPSLATE_10.asItem(),
            ModBlocks.USER_DEEPSLATE_11.asItem(), ModBlocks.USER_DEEPSLATE_12.asItem(), ModBlocks.USER_DEEPSLATE_13.asItem(), ModBlocks.USER_DEEPSLATE_14.asItem(), ModBlocks.USER_DEEPSLATE_15.asItem(),
            ModBlocks.USER_DEEPSLATE_16.asItem(), ModBlocks.USER_DEEPSLATE_17.asItem(), ModBlocks.USER_DEEPSLATE_18.asItem(), ModBlocks.USER_DEEPSLATE_19.asItem(), ModBlocks.USER_DEEPSLATE_20.asItem(),
            // Cobblestone (1-20)
            ModBlocks.USER_COBBLESTONE_1.asItem(), ModBlocks.USER_COBBLESTONE_2.asItem(), ModBlocks.USER_COBBLESTONE_3.asItem(), ModBlocks.USER_COBBLESTONE_4.asItem(), ModBlocks.USER_COBBLESTONE_5.asItem(),
            ModBlocks.USER_COBBLESTONE_6.asItem(), ModBlocks.USER_COBBLESTONE_7.asItem(), ModBlocks.USER_COBBLESTONE_8.asItem(), ModBlocks.USER_COBBLESTONE_9.asItem(), ModBlocks.USER_COBBLESTONE_10.asItem(),
            ModBlocks.USER_COBBLESTONE_11.asItem(), ModBlocks.USER_COBBLESTONE_12.asItem(), ModBlocks.USER_COBBLESTONE_13.asItem(), ModBlocks.USER_COBBLESTONE_14.asItem(), ModBlocks.USER_COBBLESTONE_15.asItem(),
            ModBlocks.USER_COBBLESTONE_16.asItem(), ModBlocks.USER_COBBLESTONE_17.asItem(), ModBlocks.USER_COBBLESTONE_18.asItem(), ModBlocks.USER_COBBLESTONE_19.asItem(), ModBlocks.USER_COBBLESTONE_20.asItem(),
            // Smooth Stone (1-20)
            ModBlocks.USER_SMOOTH_STONE_1.asItem(), ModBlocks.USER_SMOOTH_STONE_2.asItem(), ModBlocks.USER_SMOOTH_STONE_3.asItem(), ModBlocks.USER_SMOOTH_STONE_4.asItem(), ModBlocks.USER_SMOOTH_STONE_5.asItem(),
            ModBlocks.USER_SMOOTH_STONE_6.asItem(), ModBlocks.USER_SMOOTH_STONE_7.asItem(), ModBlocks.USER_SMOOTH_STONE_8.asItem(), ModBlocks.USER_SMOOTH_STONE_9.asItem(), ModBlocks.USER_SMOOTH_STONE_10.asItem(),
            ModBlocks.USER_SMOOTH_STONE_11.asItem(), ModBlocks.USER_SMOOTH_STONE_12.asItem(), ModBlocks.USER_SMOOTH_STONE_13.asItem(), ModBlocks.USER_SMOOTH_STONE_14.asItem(), ModBlocks.USER_SMOOTH_STONE_15.asItem(),
            ModBlocks.USER_SMOOTH_STONE_16.asItem(), ModBlocks.USER_SMOOTH_STONE_17.asItem(), ModBlocks.USER_SMOOTH_STONE_18.asItem(), ModBlocks.USER_SMOOTH_STONE_19.asItem(), ModBlocks.USER_SMOOTH_STONE_20.asItem(),
            // Terracotta (1-20)
            ModBlocks.USER_TERRACOTTA_1.asItem(), ModBlocks.USER_TERRACOTTA_2.asItem(), ModBlocks.USER_TERRACOTTA_3.asItem(), ModBlocks.USER_TERRACOTTA_4.asItem(), ModBlocks.USER_TERRACOTTA_5.asItem(),
            ModBlocks.USER_TERRACOTTA_6.asItem(), ModBlocks.USER_TERRACOTTA_7.asItem(), ModBlocks.USER_TERRACOTTA_8.asItem(), ModBlocks.USER_TERRACOTTA_9.asItem(), ModBlocks.USER_TERRACOTTA_10.asItem(),
            ModBlocks.USER_TERRACOTTA_11.asItem(), ModBlocks.USER_TERRACOTTA_12.asItem(), ModBlocks.USER_TERRACOTTA_13.asItem(), ModBlocks.USER_TERRACOTTA_14.asItem(), ModBlocks.USER_TERRACOTTA_15.asItem(),
            ModBlocks.USER_TERRACOTTA_16.asItem(), ModBlocks.USER_TERRACOTTA_17.asItem(), ModBlocks.USER_TERRACOTTA_18.asItem(), ModBlocks.USER_TERRACOTTA_19.asItem(), ModBlocks.USER_TERRACOTTA_20.asItem(),
            // Concrete Powder (1-20)
            ModBlocks.USER_CONCRETE_POWDER_1.asItem(), ModBlocks.USER_CONCRETE_POWDER_2.asItem(), ModBlocks.USER_CONCRETE_POWDER_3.asItem(), ModBlocks.USER_CONCRETE_POWDER_4.asItem(), ModBlocks.USER_CONCRETE_POWDER_5.asItem(),
            ModBlocks.USER_CONCRETE_POWDER_6.asItem(), ModBlocks.USER_CONCRETE_POWDER_7.asItem(), ModBlocks.USER_CONCRETE_POWDER_8.asItem(), ModBlocks.USER_CONCRETE_POWDER_9.asItem(), ModBlocks.USER_CONCRETE_POWDER_10.asItem(),
            ModBlocks.USER_CONCRETE_POWDER_11.asItem(), ModBlocks.USER_CONCRETE_POWDER_12.asItem(), ModBlocks.USER_CONCRETE_POWDER_13.asItem(), ModBlocks.USER_CONCRETE_POWDER_14.asItem(), ModBlocks.USER_CONCRETE_POWDER_15.asItem(),
            ModBlocks.USER_CONCRETE_POWDER_16.asItem(), ModBlocks.USER_CONCRETE_POWDER_17.asItem(), ModBlocks.USER_CONCRETE_POWDER_18.asItem(), ModBlocks.USER_CONCRETE_POWDER_19.asItem(), ModBlocks.USER_CONCRETE_POWDER_20.asItem(),
            // Glass (1-20)
            ModBlocks.USER_GLASS_1.asItem(), ModBlocks.USER_GLASS_2.asItem(), ModBlocks.USER_GLASS_3.asItem(), ModBlocks.USER_GLASS_4.asItem(), ModBlocks.USER_GLASS_5.asItem(),
            ModBlocks.USER_GLASS_6.asItem(), ModBlocks.USER_GLASS_7.asItem(), ModBlocks.USER_GLASS_8.asItem(), ModBlocks.USER_GLASS_9.asItem(), ModBlocks.USER_GLASS_10.asItem(),
            ModBlocks.USER_GLASS_11.asItem(), ModBlocks.USER_GLASS_12.asItem(), ModBlocks.USER_GLASS_13.asItem(), ModBlocks.USER_GLASS_14.asItem(), ModBlocks.USER_GLASS_15.asItem(),
            ModBlocks.USER_GLASS_16.asItem(), ModBlocks.USER_GLASS_17.asItem(), ModBlocks.USER_GLASS_18.asItem(), ModBlocks.USER_GLASS_19.asItem(), ModBlocks.USER_GLASS_20.asItem(),
            // Diorite (1-20)
            ModBlocks.USER_DIORITE_1.asItem(), ModBlocks.USER_DIORITE_2.asItem(), ModBlocks.USER_DIORITE_3.asItem(), ModBlocks.USER_DIORITE_4.asItem(), ModBlocks.USER_DIORITE_5.asItem(),
            ModBlocks.USER_DIORITE_6.asItem(), ModBlocks.USER_DIORITE_7.asItem(), ModBlocks.USER_DIORITE_8.asItem(), ModBlocks.USER_DIORITE_9.asItem(), ModBlocks.USER_DIORITE_10.asItem(),
            ModBlocks.USER_DIORITE_11.asItem(), ModBlocks.USER_DIORITE_12.asItem(), ModBlocks.USER_DIORITE_13.asItem(), ModBlocks.USER_DIORITE_14.asItem(), ModBlocks.USER_DIORITE_15.asItem(),
            ModBlocks.USER_DIORITE_16.asItem(), ModBlocks.USER_DIORITE_17.asItem(), ModBlocks.USER_DIORITE_18.asItem(), ModBlocks.USER_DIORITE_19.asItem(), ModBlocks.USER_DIORITE_20.asItem(),
            // Calcite (1-20)
            ModBlocks.USER_CALCITE_1.asItem(), ModBlocks.USER_CALCITE_2.asItem(), ModBlocks.USER_CALCITE_3.asItem(), ModBlocks.USER_CALCITE_4.asItem(), ModBlocks.USER_CALCITE_5.asItem(),
            ModBlocks.USER_CALCITE_6.asItem(), ModBlocks.USER_CALCITE_7.asItem(), ModBlocks.USER_CALCITE_8.asItem(), ModBlocks.USER_CALCITE_9.asItem(), ModBlocks.USER_CALCITE_10.asItem(),
            ModBlocks.USER_CALCITE_11.asItem(), ModBlocks.USER_CALCITE_12.asItem(), ModBlocks.USER_CALCITE_13.asItem(), ModBlocks.USER_CALCITE_14.asItem(), ModBlocks.USER_CALCITE_15.asItem(),
            ModBlocks.USER_CALCITE_16.asItem(), ModBlocks.USER_CALCITE_17.asItem(), ModBlocks.USER_CALCITE_18.asItem(), ModBlocks.USER_CALCITE_19.asItem(), ModBlocks.USER_CALCITE_20.asItem(),
            // Mushroom Stem (1-20)
            ModBlocks.USER_MUSHROOM_STEM_1.asItem(), ModBlocks.USER_MUSHROOM_STEM_2.asItem(), ModBlocks.USER_MUSHROOM_STEM_3.asItem(), ModBlocks.USER_MUSHROOM_STEM_4.asItem(), ModBlocks.USER_MUSHROOM_STEM_5.asItem(),
            ModBlocks.USER_MUSHROOM_STEM_6.asItem(), ModBlocks.USER_MUSHROOM_STEM_7.asItem(), ModBlocks.USER_MUSHROOM_STEM_8.asItem(), ModBlocks.USER_MUSHROOM_STEM_9.asItem(), ModBlocks.USER_MUSHROOM_STEM_10.asItem(),
            ModBlocks.USER_MUSHROOM_STEM_11.asItem(), ModBlocks.USER_MUSHROOM_STEM_12.asItem(), ModBlocks.USER_MUSHROOM_STEM_13.asItem(), ModBlocks.USER_MUSHROOM_STEM_14.asItem(), ModBlocks.USER_MUSHROOM_STEM_15.asItem(),
            ModBlocks.USER_MUSHROOM_STEM_16.asItem(), ModBlocks.USER_MUSHROOM_STEM_17.asItem(), ModBlocks.USER_MUSHROOM_STEM_18.asItem(), ModBlocks.USER_MUSHROOM_STEM_19.asItem(), ModBlocks.USER_MUSHROOM_STEM_20.asItem(),
            // Dead Tube Coral (1-20)
            ModBlocks.USER_DEAD_TUBE_CORAL_1.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_2.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_3.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_4.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_5.asItem(),
            ModBlocks.USER_DEAD_TUBE_CORAL_6.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_7.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_8.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_9.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_10.asItem(),
            ModBlocks.USER_DEAD_TUBE_CORAL_11.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_12.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_13.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_14.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_15.asItem(),
            ModBlocks.USER_DEAD_TUBE_CORAL_16.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_17.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_18.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_19.asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_20.asItem(),
            // Pearlescent Froglight (1-20)
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_1.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_2.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_3.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_4.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_5.asItem(),
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_6.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_7.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_8.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_9.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_10.asItem(),
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_11.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_12.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_13.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_14.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_15.asItem(),
            ModBlocks.USER_PEARLESCENT_FROGLIGHT_16.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_17.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_18.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_19.asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_20.asItem(),
            // Tinted Glass (1-20)
            ModBlocks.USER_TINTED_GLASS_1.asItem(), ModBlocks.USER_TINTED_GLASS_2.asItem(), ModBlocks.USER_TINTED_GLASS_3.asItem(), ModBlocks.USER_TINTED_GLASS_4.asItem(), ModBlocks.USER_TINTED_GLASS_5.asItem(),
            ModBlocks.USER_TINTED_GLASS_6.asItem(), ModBlocks.USER_TINTED_GLASS_7.asItem(), ModBlocks.USER_TINTED_GLASS_8.asItem(), ModBlocks.USER_TINTED_GLASS_9.asItem(), ModBlocks.USER_TINTED_GLASS_10.asItem(),
            ModBlocks.USER_TINTED_GLASS_11.asItem(), ModBlocks.USER_TINTED_GLASS_12.asItem(), ModBlocks.USER_TINTED_GLASS_13.asItem(), ModBlocks.USER_TINTED_GLASS_14.asItem(), ModBlocks.USER_TINTED_GLASS_15.asItem(),
            ModBlocks.USER_TINTED_GLASS_16.asItem(), ModBlocks.USER_TINTED_GLASS_17.asItem(), ModBlocks.USER_TINTED_GLASS_18.asItem(), ModBlocks.USER_TINTED_GLASS_19.asItem(), ModBlocks.USER_TINTED_GLASS_20.asItem(),
            // Stained Glass (1-20)
            ModBlocks.USER_STAINED_GLASS_1.asItem(), ModBlocks.USER_STAINED_GLASS_2.asItem(), ModBlocks.USER_STAINED_GLASS_3.asItem(), ModBlocks.USER_STAINED_GLASS_4.asItem(), ModBlocks.USER_STAINED_GLASS_5.asItem(),
            ModBlocks.USER_STAINED_GLASS_6.asItem(), ModBlocks.USER_STAINED_GLASS_7.asItem(), ModBlocks.USER_STAINED_GLASS_8.asItem(), ModBlocks.USER_STAINED_GLASS_9.asItem(), ModBlocks.USER_STAINED_GLASS_10.asItem(),
            ModBlocks.USER_STAINED_GLASS_11.asItem(), ModBlocks.USER_STAINED_GLASS_12.asItem(), ModBlocks.USER_STAINED_GLASS_13.asItem(), ModBlocks.USER_STAINED_GLASS_14.asItem(), ModBlocks.USER_STAINED_GLASS_15.asItem(),
            ModBlocks.USER_STAINED_GLASS_16.asItem(), ModBlocks.USER_STAINED_GLASS_17.asItem(), ModBlocks.USER_STAINED_GLASS_18.asItem(), ModBlocks.USER_STAINED_GLASS_19.asItem(), ModBlocks.USER_STAINED_GLASS_20.asItem()
        );
    }
}
