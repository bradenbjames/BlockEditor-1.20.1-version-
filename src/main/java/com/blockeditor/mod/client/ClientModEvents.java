package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.client.gui.BlockEditorScreen;
import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Set glass blocks to render as translucent
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DYNAMIC_BLOCK_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get(), RenderType.translucent());

            // Set all 20 user glass blocks to translucent
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_1.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_2.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_3.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_4.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_5.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_6.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_7.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_8.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_9.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_10.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_11.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_12.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_13.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_14.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_15.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_16.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_17.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_18.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_19.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_GLASS_20.get(), RenderType.translucent());

            // Set all 20 user tinted glass blocks to translucent
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_1.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_2.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_3.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_4.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_5.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_6.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_7.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_8.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_9.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_10.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_11.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_12.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_13.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_14.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_15.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_16.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_17.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_18.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_19.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_TINTED_GLASS_20.get(), RenderType.translucent());

            // Set all 20 user stained glass blocks to translucent
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_1.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_2.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_3.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_4.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_5.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_6.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_7.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_8.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_9.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_10.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_11.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_12.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_13.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_14.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_15.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_16.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_17.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_18.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_19.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.USER_STAINED_GLASS_20.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.OPEN_BLOCK_EDITOR);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return 0xFFFFFF; // Only tint layer 0
            if (level != null && pos != null && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                return blockEntity.getColor();
            }
            return 0xFFFFFF; // Default white
        },
        // Regular dynamic blocks
        ModBlocks.DYNAMIC_BLOCK.get(),
        ModBlocks.DYNAMIC_BLOCK_DIRT.get(),
        ModBlocks.DYNAMIC_BLOCK_SAND.get(),
        ModBlocks.DYNAMIC_BLOCK_WOOL.get(),
        ModBlocks.DYNAMIC_BLOCK_CONCRETE.get(),
        ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get(),
        ModBlocks.DYNAMIC_BLOCK_WOOD.get(),
        ModBlocks.DYNAMIC_BLOCK_STONE.get(),
        ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get(),
        ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get(),
        ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.get(),
        ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.get(),
        ModBlocks.DYNAMIC_BLOCK_GLASS.get(),
        ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get(),
        ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get(),
        ModBlocks.DYNAMIC_BLOCK_DIORITE.get(),
        ModBlocks.DYNAMIC_BLOCK_CALCITE.get(),
        ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get(),
        ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get(),
        ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get(),
        // User blocks - ALL of them need color registration! (1-20 for each type)
        // Wool blocks (1-20)
        ModBlocks.USER_WOOL_1.get(), ModBlocks.USER_WOOL_2.get(), ModBlocks.USER_WOOL_3.get(), ModBlocks.USER_WOOL_4.get(), ModBlocks.USER_WOOL_5.get(),
        ModBlocks.USER_WOOL_6.get(), ModBlocks.USER_WOOL_7.get(), ModBlocks.USER_WOOL_8.get(), ModBlocks.USER_WOOL_9.get(), ModBlocks.USER_WOOL_10.get(),
        ModBlocks.USER_WOOL_11.get(), ModBlocks.USER_WOOL_12.get(), ModBlocks.USER_WOOL_13.get(), ModBlocks.USER_WOOL_14.get(), ModBlocks.USER_WOOL_15.get(),
        ModBlocks.USER_WOOL_16.get(), ModBlocks.USER_WOOL_17.get(), ModBlocks.USER_WOOL_18.get(), ModBlocks.USER_WOOL_19.get(), ModBlocks.USER_WOOL_20.get(),
        // Stone blocks (1-20)
        ModBlocks.USER_STONE_1.get(), ModBlocks.USER_STONE_2.get(), ModBlocks.USER_STONE_3.get(), ModBlocks.USER_STONE_4.get(), ModBlocks.USER_STONE_5.get(),
        ModBlocks.USER_STONE_6.get(), ModBlocks.USER_STONE_7.get(), ModBlocks.USER_STONE_8.get(), ModBlocks.USER_STONE_9.get(), ModBlocks.USER_STONE_10.get(),
        ModBlocks.USER_STONE_11.get(), ModBlocks.USER_STONE_12.get(), ModBlocks.USER_STONE_13.get(), ModBlocks.USER_STONE_14.get(), ModBlocks.USER_STONE_15.get(),
        ModBlocks.USER_STONE_16.get(), ModBlocks.USER_STONE_17.get(), ModBlocks.USER_STONE_18.get(), ModBlocks.USER_STONE_19.get(), ModBlocks.USER_STONE_20.get(),
        // Concrete blocks (1-20)
        ModBlocks.USER_CONCRETE_1.get(), ModBlocks.USER_CONCRETE_2.get(), ModBlocks.USER_CONCRETE_3.get(), ModBlocks.USER_CONCRETE_4.get(), ModBlocks.USER_CONCRETE_5.get(),
        ModBlocks.USER_CONCRETE_6.get(), ModBlocks.USER_CONCRETE_7.get(), ModBlocks.USER_CONCRETE_8.get(), ModBlocks.USER_CONCRETE_9.get(), ModBlocks.USER_CONCRETE_10.get(),
        ModBlocks.USER_CONCRETE_11.get(), ModBlocks.USER_CONCRETE_12.get(), ModBlocks.USER_CONCRETE_13.get(), ModBlocks.USER_CONCRETE_14.get(), ModBlocks.USER_CONCRETE_15.get(),
        ModBlocks.USER_CONCRETE_16.get(), ModBlocks.USER_CONCRETE_17.get(), ModBlocks.USER_CONCRETE_18.get(), ModBlocks.USER_CONCRETE_19.get(), ModBlocks.USER_CONCRETE_20.get(),
        // Wood blocks (1-20)
        ModBlocks.USER_WOOD_1.get(), ModBlocks.USER_WOOD_2.get(), ModBlocks.USER_WOOD_3.get(), ModBlocks.USER_WOOD_4.get(), ModBlocks.USER_WOOD_5.get(),
        ModBlocks.USER_WOOD_6.get(), ModBlocks.USER_WOOD_7.get(), ModBlocks.USER_WOOD_8.get(), ModBlocks.USER_WOOD_9.get(), ModBlocks.USER_WOOD_10.get(),
        ModBlocks.USER_WOOD_11.get(), ModBlocks.USER_WOOD_12.get(), ModBlocks.USER_WOOD_13.get(), ModBlocks.USER_WOOD_14.get(), ModBlocks.USER_WOOD_15.get(),
        ModBlocks.USER_WOOD_16.get(), ModBlocks.USER_WOOD_17.get(), ModBlocks.USER_WOOD_18.get(), ModBlocks.USER_WOOD_19.get(), ModBlocks.USER_WOOD_20.get(),
        // Dirt blocks (1-20)
        ModBlocks.USER_DIRT_1.get(), ModBlocks.USER_DIRT_2.get(), ModBlocks.USER_DIRT_3.get(), ModBlocks.USER_DIRT_4.get(), ModBlocks.USER_DIRT_5.get(),
        ModBlocks.USER_DIRT_6.get(), ModBlocks.USER_DIRT_7.get(), ModBlocks.USER_DIRT_8.get(), ModBlocks.USER_DIRT_9.get(), ModBlocks.USER_DIRT_10.get(),
        ModBlocks.USER_DIRT_11.get(), ModBlocks.USER_DIRT_12.get(), ModBlocks.USER_DIRT_13.get(), ModBlocks.USER_DIRT_14.get(), ModBlocks.USER_DIRT_15.get(),
        ModBlocks.USER_DIRT_16.get(), ModBlocks.USER_DIRT_17.get(), ModBlocks.USER_DIRT_18.get(), ModBlocks.USER_DIRT_19.get(), ModBlocks.USER_DIRT_20.get(),
        // Sand blocks (1-20)
        ModBlocks.USER_SAND_1.get(), ModBlocks.USER_SAND_2.get(), ModBlocks.USER_SAND_3.get(), ModBlocks.USER_SAND_4.get(), ModBlocks.USER_SAND_5.get(),
        ModBlocks.USER_SAND_6.get(), ModBlocks.USER_SAND_7.get(), ModBlocks.USER_SAND_8.get(), ModBlocks.USER_SAND_9.get(), ModBlocks.USER_SAND_10.get(),
        ModBlocks.USER_SAND_11.get(), ModBlocks.USER_SAND_12.get(), ModBlocks.USER_SAND_13.get(), ModBlocks.USER_SAND_14.get(), ModBlocks.USER_SAND_15.get(),
        ModBlocks.USER_SAND_16.get(), ModBlocks.USER_SAND_17.get(), ModBlocks.USER_SAND_18.get(), ModBlocks.USER_SAND_19.get(), ModBlocks.USER_SAND_20.get(),
        // Deepslate blocks (1-20)
        ModBlocks.USER_DEEPSLATE_1.get(), ModBlocks.USER_DEEPSLATE_2.get(), ModBlocks.USER_DEEPSLATE_3.get(), ModBlocks.USER_DEEPSLATE_4.get(), ModBlocks.USER_DEEPSLATE_5.get(),
        ModBlocks.USER_DEEPSLATE_6.get(), ModBlocks.USER_DEEPSLATE_7.get(), ModBlocks.USER_DEEPSLATE_8.get(), ModBlocks.USER_DEEPSLATE_9.get(), ModBlocks.USER_DEEPSLATE_10.get(),
        ModBlocks.USER_DEEPSLATE_11.get(), ModBlocks.USER_DEEPSLATE_12.get(), ModBlocks.USER_DEEPSLATE_13.get(), ModBlocks.USER_DEEPSLATE_14.get(), ModBlocks.USER_DEEPSLATE_15.get(),
        ModBlocks.USER_DEEPSLATE_16.get(), ModBlocks.USER_DEEPSLATE_17.get(), ModBlocks.USER_DEEPSLATE_18.get(), ModBlocks.USER_DEEPSLATE_19.get(), ModBlocks.USER_DEEPSLATE_20.get(),
        // Cobblestone blocks (1-20)
        ModBlocks.USER_COBBLESTONE_1.get(), ModBlocks.USER_COBBLESTONE_2.get(), ModBlocks.USER_COBBLESTONE_3.get(), ModBlocks.USER_COBBLESTONE_4.get(), ModBlocks.USER_COBBLESTONE_5.get(),
        ModBlocks.USER_COBBLESTONE_6.get(), ModBlocks.USER_COBBLESTONE_7.get(), ModBlocks.USER_COBBLESTONE_8.get(), ModBlocks.USER_COBBLESTONE_9.get(), ModBlocks.USER_COBBLESTONE_10.get(),
        ModBlocks.USER_COBBLESTONE_11.get(), ModBlocks.USER_COBBLESTONE_12.get(), ModBlocks.USER_COBBLESTONE_13.get(), ModBlocks.USER_COBBLESTONE_14.get(), ModBlocks.USER_COBBLESTONE_15.get(),
        ModBlocks.USER_COBBLESTONE_16.get(), ModBlocks.USER_COBBLESTONE_17.get(), ModBlocks.USER_COBBLESTONE_18.get(), ModBlocks.USER_COBBLESTONE_19.get(), ModBlocks.USER_COBBLESTONE_20.get(),
        // Smooth Stone blocks (1-20)
        ModBlocks.USER_SMOOTH_STONE_1.get(), ModBlocks.USER_SMOOTH_STONE_2.get(), ModBlocks.USER_SMOOTH_STONE_3.get(), ModBlocks.USER_SMOOTH_STONE_4.get(), ModBlocks.USER_SMOOTH_STONE_5.get(),
        ModBlocks.USER_SMOOTH_STONE_6.get(), ModBlocks.USER_SMOOTH_STONE_7.get(), ModBlocks.USER_SMOOTH_STONE_8.get(), ModBlocks.USER_SMOOTH_STONE_9.get(), ModBlocks.USER_SMOOTH_STONE_10.get(),
        ModBlocks.USER_SMOOTH_STONE_11.get(), ModBlocks.USER_SMOOTH_STONE_12.get(), ModBlocks.USER_SMOOTH_STONE_13.get(), ModBlocks.USER_SMOOTH_STONE_14.get(), ModBlocks.USER_SMOOTH_STONE_15.get(),
        ModBlocks.USER_SMOOTH_STONE_16.get(), ModBlocks.USER_SMOOTH_STONE_17.get(), ModBlocks.USER_SMOOTH_STONE_18.get(), ModBlocks.USER_SMOOTH_STONE_19.get(), ModBlocks.USER_SMOOTH_STONE_20.get(),
        // Terracotta blocks (1-20)
        ModBlocks.USER_TERRACOTTA_1.get(), ModBlocks.USER_TERRACOTTA_2.get(), ModBlocks.USER_TERRACOTTA_3.get(), ModBlocks.USER_TERRACOTTA_4.get(), ModBlocks.USER_TERRACOTTA_5.get(),
        ModBlocks.USER_TERRACOTTA_6.get(), ModBlocks.USER_TERRACOTTA_7.get(), ModBlocks.USER_TERRACOTTA_8.get(), ModBlocks.USER_TERRACOTTA_9.get(), ModBlocks.USER_TERRACOTTA_10.get(),
        ModBlocks.USER_TERRACOTTA_11.get(), ModBlocks.USER_TERRACOTTA_12.get(), ModBlocks.USER_TERRACOTTA_13.get(), ModBlocks.USER_TERRACOTTA_14.get(), ModBlocks.USER_TERRACOTTA_15.get(),
        ModBlocks.USER_TERRACOTTA_16.get(), ModBlocks.USER_TERRACOTTA_17.get(), ModBlocks.USER_TERRACOTTA_18.get(), ModBlocks.USER_TERRACOTTA_19.get(), ModBlocks.USER_TERRACOTTA_20.get(),
        // Concrete Powder blocks (1-20)
        ModBlocks.USER_CONCRETE_POWDER_1.get(), ModBlocks.USER_CONCRETE_POWDER_2.get(), ModBlocks.USER_CONCRETE_POWDER_3.get(), ModBlocks.USER_CONCRETE_POWDER_4.get(), ModBlocks.USER_CONCRETE_POWDER_5.get(),
        ModBlocks.USER_CONCRETE_POWDER_6.get(), ModBlocks.USER_CONCRETE_POWDER_7.get(), ModBlocks.USER_CONCRETE_POWDER_8.get(), ModBlocks.USER_CONCRETE_POWDER_9.get(), ModBlocks.USER_CONCRETE_POWDER_10.get(),
        ModBlocks.USER_CONCRETE_POWDER_11.get(), ModBlocks.USER_CONCRETE_POWDER_12.get(), ModBlocks.USER_CONCRETE_POWDER_13.get(), ModBlocks.USER_CONCRETE_POWDER_14.get(), ModBlocks.USER_CONCRETE_POWDER_15.get(),
        ModBlocks.USER_CONCRETE_POWDER_16.get(), ModBlocks.USER_CONCRETE_POWDER_17.get(), ModBlocks.USER_CONCRETE_POWDER_18.get(), ModBlocks.USER_CONCRETE_POWDER_19.get(), ModBlocks.USER_CONCRETE_POWDER_20.get(),
        // Glass blocks (1-20)
        ModBlocks.USER_GLASS_1.get(), ModBlocks.USER_GLASS_2.get(), ModBlocks.USER_GLASS_3.get(), ModBlocks.USER_GLASS_4.get(), ModBlocks.USER_GLASS_5.get(),
        ModBlocks.USER_GLASS_6.get(), ModBlocks.USER_GLASS_7.get(), ModBlocks.USER_GLASS_8.get(), ModBlocks.USER_GLASS_9.get(), ModBlocks.USER_GLASS_10.get(),
        ModBlocks.USER_GLASS_11.get(), ModBlocks.USER_GLASS_12.get(), ModBlocks.USER_GLASS_13.get(), ModBlocks.USER_GLASS_14.get(), ModBlocks.USER_GLASS_15.get(),
        ModBlocks.USER_GLASS_16.get(), ModBlocks.USER_GLASS_17.get(), ModBlocks.USER_GLASS_18.get(), ModBlocks.USER_GLASS_19.get(), ModBlocks.USER_GLASS_20.get(),
        // Diorite blocks (1-20)
        ModBlocks.USER_DIORITE_1.get(), ModBlocks.USER_DIORITE_2.get(), ModBlocks.USER_DIORITE_3.get(), ModBlocks.USER_DIORITE_4.get(), ModBlocks.USER_DIORITE_5.get(),
        ModBlocks.USER_DIORITE_6.get(), ModBlocks.USER_DIORITE_7.get(), ModBlocks.USER_DIORITE_8.get(), ModBlocks.USER_DIORITE_9.get(), ModBlocks.USER_DIORITE_10.get(),
        ModBlocks.USER_DIORITE_11.get(), ModBlocks.USER_DIORITE_12.get(), ModBlocks.USER_DIORITE_13.get(), ModBlocks.USER_DIORITE_14.get(), ModBlocks.USER_DIORITE_15.get(),
        ModBlocks.USER_DIORITE_16.get(), ModBlocks.USER_DIORITE_17.get(), ModBlocks.USER_DIORITE_18.get(), ModBlocks.USER_DIORITE_19.get(), ModBlocks.USER_DIORITE_20.get(),
        // Calcite blocks (1-20)
        ModBlocks.USER_CALCITE_1.get(), ModBlocks.USER_CALCITE_2.get(), ModBlocks.USER_CALCITE_3.get(), ModBlocks.USER_CALCITE_4.get(), ModBlocks.USER_CALCITE_5.get(),
        ModBlocks.USER_CALCITE_6.get(), ModBlocks.USER_CALCITE_7.get(), ModBlocks.USER_CALCITE_8.get(), ModBlocks.USER_CALCITE_9.get(), ModBlocks.USER_CALCITE_10.get(),
        ModBlocks.USER_CALCITE_11.get(), ModBlocks.USER_CALCITE_12.get(), ModBlocks.USER_CALCITE_13.get(), ModBlocks.USER_CALCITE_14.get(), ModBlocks.USER_CALCITE_15.get(),
        ModBlocks.USER_CALCITE_16.get(), ModBlocks.USER_CALCITE_17.get(), ModBlocks.USER_CALCITE_18.get(), ModBlocks.USER_CALCITE_19.get(), ModBlocks.USER_CALCITE_20.get(),
        // Mushroom Stem blocks (1-20)
        ModBlocks.USER_MUSHROOM_STEM_1.get(), ModBlocks.USER_MUSHROOM_STEM_2.get(), ModBlocks.USER_MUSHROOM_STEM_3.get(), ModBlocks.USER_MUSHROOM_STEM_4.get(), ModBlocks.USER_MUSHROOM_STEM_5.get(),
        ModBlocks.USER_MUSHROOM_STEM_6.get(), ModBlocks.USER_MUSHROOM_STEM_7.get(), ModBlocks.USER_MUSHROOM_STEM_8.get(), ModBlocks.USER_MUSHROOM_STEM_9.get(), ModBlocks.USER_MUSHROOM_STEM_10.get(),
        ModBlocks.USER_MUSHROOM_STEM_11.get(), ModBlocks.USER_MUSHROOM_STEM_12.get(), ModBlocks.USER_MUSHROOM_STEM_13.get(), ModBlocks.USER_MUSHROOM_STEM_14.get(), ModBlocks.USER_MUSHROOM_STEM_15.get(),
        ModBlocks.USER_MUSHROOM_STEM_16.get(), ModBlocks.USER_MUSHROOM_STEM_17.get(), ModBlocks.USER_MUSHROOM_STEM_18.get(), ModBlocks.USER_MUSHROOM_STEM_19.get(), ModBlocks.USER_MUSHROOM_STEM_20.get(),
        // Dead Tube Coral blocks (1-20)
        ModBlocks.USER_DEAD_TUBE_CORAL_1.get(), ModBlocks.USER_DEAD_TUBE_CORAL_2.get(), ModBlocks.USER_DEAD_TUBE_CORAL_3.get(), ModBlocks.USER_DEAD_TUBE_CORAL_4.get(), ModBlocks.USER_DEAD_TUBE_CORAL_5.get(),
        ModBlocks.USER_DEAD_TUBE_CORAL_6.get(), ModBlocks.USER_DEAD_TUBE_CORAL_7.get(), ModBlocks.USER_DEAD_TUBE_CORAL_8.get(), ModBlocks.USER_DEAD_TUBE_CORAL_9.get(), ModBlocks.USER_DEAD_TUBE_CORAL_10.get(),
        ModBlocks.USER_DEAD_TUBE_CORAL_11.get(), ModBlocks.USER_DEAD_TUBE_CORAL_12.get(), ModBlocks.USER_DEAD_TUBE_CORAL_13.get(), ModBlocks.USER_DEAD_TUBE_CORAL_14.get(), ModBlocks.USER_DEAD_TUBE_CORAL_15.get(),
        ModBlocks.USER_DEAD_TUBE_CORAL_16.get(), ModBlocks.USER_DEAD_TUBE_CORAL_17.get(), ModBlocks.USER_DEAD_TUBE_CORAL_18.get(), ModBlocks.USER_DEAD_TUBE_CORAL_19.get(), ModBlocks.USER_DEAD_TUBE_CORAL_20.get(),
        // Pearlescent Froglight blocks (1-20)
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_1.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_2.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_3.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_4.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_5.get(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_6.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_7.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_8.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_9.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_10.get(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_11.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_12.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_13.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_14.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_15.get(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_16.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_17.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_18.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_19.get(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_20.get(),
        // Tinted Glass blocks (dynamic and user variants)
        ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get(),
        ModBlocks.USER_TINTED_GLASS_1.get(), ModBlocks.USER_TINTED_GLASS_2.get(), ModBlocks.USER_TINTED_GLASS_3.get(), ModBlocks.USER_TINTED_GLASS_4.get(), ModBlocks.USER_TINTED_GLASS_5.get(),
        ModBlocks.USER_TINTED_GLASS_6.get(), ModBlocks.USER_TINTED_GLASS_7.get(), ModBlocks.USER_TINTED_GLASS_8.get(), ModBlocks.USER_TINTED_GLASS_9.get(), ModBlocks.USER_TINTED_GLASS_10.get(),
        ModBlocks.USER_TINTED_GLASS_11.get(), ModBlocks.USER_TINTED_GLASS_12.get(), ModBlocks.USER_TINTED_GLASS_13.get(), ModBlocks.USER_TINTED_GLASS_14.get(), ModBlocks.USER_TINTED_GLASS_15.get(),
        ModBlocks.USER_TINTED_GLASS_16.get(), ModBlocks.USER_TINTED_GLASS_17.get(), ModBlocks.USER_TINTED_GLASS_18.get(), ModBlocks.USER_TINTED_GLASS_19.get(), ModBlocks.USER_TINTED_GLASS_20.get(),
        // Stained Glass blocks (dynamic and user variants)
        ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get(),
        ModBlocks.USER_STAINED_GLASS_1.get(), ModBlocks.USER_STAINED_GLASS_2.get(), ModBlocks.USER_STAINED_GLASS_3.get(), ModBlocks.USER_STAINED_GLASS_4.get(), ModBlocks.USER_STAINED_GLASS_5.get(),
        ModBlocks.USER_STAINED_GLASS_6.get(), ModBlocks.USER_STAINED_GLASS_7.get(), ModBlocks.USER_STAINED_GLASS_8.get(), ModBlocks.USER_STAINED_GLASS_9.get(), ModBlocks.USER_STAINED_GLASS_10.get(),
        ModBlocks.USER_STAINED_GLASS_11.get(), ModBlocks.USER_STAINED_GLASS_12.get(), ModBlocks.USER_STAINED_GLASS_13.get(), ModBlocks.USER_STAINED_GLASS_14.get(), ModBlocks.USER_STAINED_GLASS_15.get(),
        ModBlocks.USER_STAINED_GLASS_16.get(), ModBlocks.USER_STAINED_GLASS_17.get(), ModBlocks.USER_STAINED_GLASS_18.get(), ModBlocks.USER_STAINED_GLASS_19.get(), ModBlocks.USER_STAINED_GLASS_20.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if (tintIndex == 0 && tag != null && tag.contains("Color")) {
                String hexColor = tag.getString("Color");
                try {
                    return Integer.parseInt(hexColor, 16);
                } catch (NumberFormatException ignored) {}
            }
            return 0xFFFFFF; // Default white
        },
        // Dynamic block items
        ModBlocks.DYNAMIC_BLOCK.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_DIRT.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_SAND.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_WOOL.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_CONCRETE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_WOOD.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_STONE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_GLASS.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_DIORITE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_CALCITE.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get().asItem(),
        ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get().asItem(),
        // User block items - all user variants (wool, stone, concrete, wood, dirt, sand, deepslate, cobblestone, smooth stone,
        // terracotta, concrete powder, glass, diorite, calcite, mushroom stem, dead tube coral, pearlescent froglight,
        // tinted glass, stained glass)
        // Wool (1-20)
        ModBlocks.USER_WOOL_1.get().asItem(), ModBlocks.USER_WOOL_2.get().asItem(), ModBlocks.USER_WOOL_3.get().asItem(), ModBlocks.USER_WOOL_4.get().asItem(), ModBlocks.USER_WOOL_5.get().asItem(),
        ModBlocks.USER_WOOL_6.get().asItem(), ModBlocks.USER_WOOL_7.get().asItem(), ModBlocks.USER_WOOL_8.get().asItem(), ModBlocks.USER_WOOL_9.get().asItem(), ModBlocks.USER_WOOL_10.get().asItem(),
        ModBlocks.USER_WOOL_11.get().asItem(), ModBlocks.USER_WOOL_12.get().asItem(), ModBlocks.USER_WOOL_13.get().asItem(), ModBlocks.USER_WOOL_14.get().asItem(), ModBlocks.USER_WOOL_15.get().asItem(),
        ModBlocks.USER_WOOL_16.get().asItem(), ModBlocks.USER_WOOL_17.get().asItem(), ModBlocks.USER_WOOL_18.get().asItem(), ModBlocks.USER_WOOL_19.get().asItem(), ModBlocks.USER_WOOL_20.get().asItem(),
        // Stone (1-20)
        ModBlocks.USER_STONE_1.get().asItem(), ModBlocks.USER_STONE_2.get().asItem(), ModBlocks.USER_STONE_3.get().asItem(), ModBlocks.USER_STONE_4.get().asItem(), ModBlocks.USER_STONE_5.get().asItem(),
        ModBlocks.USER_STONE_6.get().asItem(), ModBlocks.USER_STONE_7.get().asItem(), ModBlocks.USER_STONE_8.get().asItem(), ModBlocks.USER_STONE_9.get().asItem(), ModBlocks.USER_STONE_10.get().asItem(),
        ModBlocks.USER_STONE_11.get().asItem(), ModBlocks.USER_STONE_12.get().asItem(), ModBlocks.USER_STONE_13.get().asItem(), ModBlocks.USER_STONE_14.get().asItem(), ModBlocks.USER_STONE_15.get().asItem(),
        ModBlocks.USER_STONE_16.get().asItem(), ModBlocks.USER_STONE_17.get().asItem(), ModBlocks.USER_STONE_18.get().asItem(), ModBlocks.USER_STONE_19.get().asItem(), ModBlocks.USER_STONE_20.get().asItem(),
        // Concrete (1-20)
        ModBlocks.USER_CONCRETE_1.get().asItem(), ModBlocks.USER_CONCRETE_2.get().asItem(), ModBlocks.USER_CONCRETE_3.get().asItem(), ModBlocks.USER_CONCRETE_4.get().asItem(), ModBlocks.USER_CONCRETE_5.get().asItem(),
        ModBlocks.USER_CONCRETE_6.get().asItem(), ModBlocks.USER_CONCRETE_7.get().asItem(), ModBlocks.USER_CONCRETE_8.get().asItem(), ModBlocks.USER_CONCRETE_9.get().asItem(), ModBlocks.USER_CONCRETE_10.get().asItem(),
        ModBlocks.USER_CONCRETE_11.get().asItem(), ModBlocks.USER_CONCRETE_12.get().asItem(), ModBlocks.USER_CONCRETE_13.get().asItem(), ModBlocks.USER_CONCRETE_14.get().asItem(), ModBlocks.USER_CONCRETE_15.get().asItem(),
        ModBlocks.USER_CONCRETE_16.get().asItem(), ModBlocks.USER_CONCRETE_17.get().asItem(), ModBlocks.USER_CONCRETE_18.get().asItem(), ModBlocks.USER_CONCRETE_19.get().asItem(), ModBlocks.USER_CONCRETE_20.get().asItem(),
        // Wood (1-20)
        ModBlocks.USER_WOOD_1.get().asItem(), ModBlocks.USER_WOOD_2.get().asItem(), ModBlocks.USER_WOOD_3.get().asItem(), ModBlocks.USER_WOOD_4.get().asItem(), ModBlocks.USER_WOOD_5.get().asItem(),
        ModBlocks.USER_WOOD_6.get().asItem(), ModBlocks.USER_WOOD_7.get().asItem(), ModBlocks.USER_WOOD_8.get().asItem(), ModBlocks.USER_WOOD_9.get().asItem(), ModBlocks.USER_WOOD_10.get().asItem(),
        ModBlocks.USER_WOOD_11.get().asItem(), ModBlocks.USER_WOOD_12.get().asItem(), ModBlocks.USER_WOOD_13.get().asItem(), ModBlocks.USER_WOOD_14.get().asItem(), ModBlocks.USER_WOOD_15.get().asItem(),
        ModBlocks.USER_WOOD_16.get().asItem(), ModBlocks.USER_WOOD_17.get().asItem(), ModBlocks.USER_WOOD_18.get().asItem(), ModBlocks.USER_WOOD_19.get().asItem(), ModBlocks.USER_WOOD_20.get().asItem(),
        // Dirt (1-20)
        ModBlocks.USER_DIRT_1.get().asItem(), ModBlocks.USER_DIRT_2.get().asItem(), ModBlocks.USER_DIRT_3.get().asItem(), ModBlocks.USER_DIRT_4.get().asItem(), ModBlocks.USER_DIRT_5.get().asItem(),
        ModBlocks.USER_DIRT_6.get().asItem(), ModBlocks.USER_DIRT_7.get().asItem(), ModBlocks.USER_DIRT_8.get().asItem(), ModBlocks.USER_DIRT_9.get().asItem(), ModBlocks.USER_DIRT_10.get().asItem(),
        ModBlocks.USER_DIRT_11.get().asItem(), ModBlocks.USER_DIRT_12.get().asItem(), ModBlocks.USER_DIRT_13.get().asItem(), ModBlocks.USER_DIRT_14.get().asItem(), ModBlocks.USER_DIRT_15.get().asItem(),
        ModBlocks.USER_DIRT_16.get().asItem(), ModBlocks.USER_DIRT_17.get().asItem(), ModBlocks.USER_DIRT_18.get().asItem(), ModBlocks.USER_DIRT_19.get().asItem(), ModBlocks.USER_DIRT_20.get().asItem(),
        // Sand (1-20)
        ModBlocks.USER_SAND_1.get().asItem(), ModBlocks.USER_SAND_2.get().asItem(), ModBlocks.USER_SAND_3.get().asItem(), ModBlocks.USER_SAND_4.get().asItem(), ModBlocks.USER_SAND_5.get().asItem(),
        ModBlocks.USER_SAND_6.get().asItem(), ModBlocks.USER_SAND_7.get().asItem(), ModBlocks.USER_SAND_8.get().asItem(), ModBlocks.USER_SAND_9.get().asItem(), ModBlocks.USER_SAND_10.get().asItem(),
        ModBlocks.USER_SAND_11.get().asItem(), ModBlocks.USER_SAND_12.get().asItem(), ModBlocks.USER_SAND_13.get().asItem(), ModBlocks.USER_SAND_14.get().asItem(), ModBlocks.USER_SAND_15.get().asItem(),
        ModBlocks.USER_SAND_16.get().asItem(), ModBlocks.USER_SAND_17.get().asItem(), ModBlocks.USER_SAND_18.get().asItem(), ModBlocks.USER_SAND_19.get().asItem(), ModBlocks.USER_SAND_20.get().asItem(),
        // Deepslate (1-20)
        ModBlocks.USER_DEEPSLATE_1.get().asItem(), ModBlocks.USER_DEEPSLATE_2.get().asItem(), ModBlocks.USER_DEEPSLATE_3.get().asItem(), ModBlocks.USER_DEEPSLATE_4.get().asItem(), ModBlocks.USER_DEEPSLATE_5.get().asItem(),
        ModBlocks.USER_DEEPSLATE_6.get().asItem(), ModBlocks.USER_DEEPSLATE_7.get().asItem(), ModBlocks.USER_DEEPSLATE_8.get().asItem(), ModBlocks.USER_DEEPSLATE_9.get().asItem(), ModBlocks.USER_DEEPSLATE_10.get().asItem(),
        ModBlocks.USER_DEEPSLATE_11.get().asItem(), ModBlocks.USER_DEEPSLATE_12.get().asItem(), ModBlocks.USER_DEEPSLATE_13.get().asItem(), ModBlocks.USER_DEEPSLATE_14.get().asItem(), ModBlocks.USER_DEEPSLATE_15.get().asItem(),
        ModBlocks.USER_DEEPSLATE_16.get().asItem(), ModBlocks.USER_DEEPSLATE_17.get().asItem(), ModBlocks.USER_DEEPSLATE_18.get().asItem(), ModBlocks.USER_DEEPSLATE_19.get().asItem(), ModBlocks.USER_DEEPSLATE_20.get().asItem(),
        // Cobblestone (1-20)
        ModBlocks.USER_COBBLESTONE_1.get().asItem(), ModBlocks.USER_COBBLESTONE_2.get().asItem(), ModBlocks.USER_COBBLESTONE_3.get().asItem(), ModBlocks.USER_COBBLESTONE_4.get().asItem(), ModBlocks.USER_COBBLESTONE_5.get().asItem(),
        ModBlocks.USER_COBBLESTONE_6.get().asItem(), ModBlocks.USER_COBBLESTONE_7.get().asItem(), ModBlocks.USER_COBBLESTONE_8.get().asItem(), ModBlocks.USER_COBBLESTONE_9.get().asItem(), ModBlocks.USER_COBBLESTONE_10.get().asItem(),
        ModBlocks.USER_COBBLESTONE_11.get().asItem(), ModBlocks.USER_COBBLESTONE_12.get().asItem(), ModBlocks.USER_COBBLESTONE_13.get().asItem(), ModBlocks.USER_COBBLESTONE_14.get().asItem(), ModBlocks.USER_COBBLESTONE_15.get().asItem(),
        ModBlocks.USER_COBBLESTONE_16.get().asItem(), ModBlocks.USER_COBBLESTONE_17.get().asItem(), ModBlocks.USER_COBBLESTONE_18.get().asItem(), ModBlocks.USER_COBBLESTONE_19.get().asItem(), ModBlocks.USER_COBBLESTONE_20.get().asItem(),
        // Smooth Stone (1-20)
        ModBlocks.USER_SMOOTH_STONE_1.get().asItem(), ModBlocks.USER_SMOOTH_STONE_2.get().asItem(), ModBlocks.USER_SMOOTH_STONE_3.get().asItem(), ModBlocks.USER_SMOOTH_STONE_4.get().asItem(), ModBlocks.USER_SMOOTH_STONE_5.get().asItem(),
        ModBlocks.USER_SMOOTH_STONE_6.get().asItem(), ModBlocks.USER_SMOOTH_STONE_7.get().asItem(), ModBlocks.USER_SMOOTH_STONE_8.get().asItem(), ModBlocks.USER_SMOOTH_STONE_9.get().asItem(), ModBlocks.USER_SMOOTH_STONE_10.get().asItem(),
        ModBlocks.USER_SMOOTH_STONE_11.get().asItem(), ModBlocks.USER_SMOOTH_STONE_12.get().asItem(), ModBlocks.USER_SMOOTH_STONE_13.get().asItem(), ModBlocks.USER_SMOOTH_STONE_14.get().asItem(), ModBlocks.USER_SMOOTH_STONE_15.get().asItem(),
        ModBlocks.USER_SMOOTH_STONE_16.get().asItem(), ModBlocks.USER_SMOOTH_STONE_17.get().asItem(), ModBlocks.USER_SMOOTH_STONE_18.get().asItem(), ModBlocks.USER_SMOOTH_STONE_19.get().asItem(), ModBlocks.USER_SMOOTH_STONE_20.get().asItem(),
        // Terracotta (1-20)
        ModBlocks.USER_TERRACOTTA_1.get().asItem(), ModBlocks.USER_TERRACOTTA_2.get().asItem(), ModBlocks.USER_TERRACOTTA_3.get().asItem(), ModBlocks.USER_TERRACOTTA_4.get().asItem(), ModBlocks.USER_TERRACOTTA_5.get().asItem(),
        ModBlocks.USER_TERRACOTTA_6.get().asItem(), ModBlocks.USER_TERRACOTTA_7.get().asItem(), ModBlocks.USER_TERRACOTTA_8.get().asItem(), ModBlocks.USER_TERRACOTTA_9.get().asItem(), ModBlocks.USER_TERRACOTTA_10.get().asItem(),
        ModBlocks.USER_TERRACOTTA_11.get().asItem(), ModBlocks.USER_TERRACOTTA_12.get().asItem(), ModBlocks.USER_TERRACOTTA_13.get().asItem(), ModBlocks.USER_TERRACOTTA_14.get().asItem(), ModBlocks.USER_TERRACOTTA_15.get().asItem(),
        ModBlocks.USER_TERRACOTTA_16.get().asItem(), ModBlocks.USER_TERRACOTTA_17.get().asItem(), ModBlocks.USER_TERRACOTTA_18.get().asItem(), ModBlocks.USER_TERRACOTTA_19.get().asItem(), ModBlocks.USER_TERRACOTTA_20.get().asItem(),
        // Concrete Powder (1-20)
        ModBlocks.USER_CONCRETE_POWDER_1.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_2.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_3.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_4.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_5.get().asItem(),
        ModBlocks.USER_CONCRETE_POWDER_6.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_7.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_8.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_9.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_10.get().asItem(),
        ModBlocks.USER_CONCRETE_POWDER_11.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_12.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_13.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_14.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_15.get().asItem(),
        ModBlocks.USER_CONCRETE_POWDER_16.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_17.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_18.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_19.get().asItem(), ModBlocks.USER_CONCRETE_POWDER_20.get().asItem(),
        // Glass (1-20)
        ModBlocks.USER_GLASS_1.get().asItem(), ModBlocks.USER_GLASS_2.get().asItem(), ModBlocks.USER_GLASS_3.get().asItem(), ModBlocks.USER_GLASS_4.get().asItem(), ModBlocks.USER_GLASS_5.get().asItem(),
        ModBlocks.USER_GLASS_6.get().asItem(), ModBlocks.USER_GLASS_7.get().asItem(), ModBlocks.USER_GLASS_8.get().asItem(), ModBlocks.USER_GLASS_9.get().asItem(), ModBlocks.USER_GLASS_10.get().asItem(),
        ModBlocks.USER_GLASS_11.get().asItem(), ModBlocks.USER_GLASS_12.get().asItem(), ModBlocks.USER_GLASS_13.get().asItem(), ModBlocks.USER_GLASS_14.get().asItem(), ModBlocks.USER_GLASS_15.get().asItem(),
        ModBlocks.USER_GLASS_16.get().asItem(), ModBlocks.USER_GLASS_17.get().asItem(), ModBlocks.USER_GLASS_18.get().asItem(), ModBlocks.USER_GLASS_19.get().asItem(), ModBlocks.USER_GLASS_20.get().asItem(),
        // Diorite (1-20)
        ModBlocks.USER_DIORITE_1.get().asItem(), ModBlocks.USER_DIORITE_2.get().asItem(), ModBlocks.USER_DIORITE_3.get().asItem(), ModBlocks.USER_DIORITE_4.get().asItem(), ModBlocks.USER_DIORITE_5.get().asItem(),
        ModBlocks.USER_DIORITE_6.get().asItem(), ModBlocks.USER_DIORITE_7.get().asItem(), ModBlocks.USER_DIORITE_8.get().asItem(), ModBlocks.USER_DIORITE_9.get().asItem(), ModBlocks.USER_DIORITE_10.get().asItem(),
        ModBlocks.USER_DIORITE_11.get().asItem(), ModBlocks.USER_DIORITE_12.get().asItem(), ModBlocks.USER_DIORITE_13.get().asItem(), ModBlocks.USER_DIORITE_14.get().asItem(), ModBlocks.USER_DIORITE_15.get().asItem(),
        ModBlocks.USER_DIORITE_16.get().asItem(), ModBlocks.USER_DIORITE_17.get().asItem(), ModBlocks.USER_DIORITE_18.get().asItem(), ModBlocks.USER_DIORITE_19.get().asItem(), ModBlocks.USER_DIORITE_20.get().asItem(),
        // Calcite (1-20)
        ModBlocks.USER_CALCITE_1.get().asItem(), ModBlocks.USER_CALCITE_2.get().asItem(), ModBlocks.USER_CALCITE_3.get().asItem(), ModBlocks.USER_CALCITE_4.get().asItem(), ModBlocks.USER_CALCITE_5.get().asItem(),
        ModBlocks.USER_CALCITE_6.get().asItem(), ModBlocks.USER_CALCITE_7.get().asItem(), ModBlocks.USER_CALCITE_8.get().asItem(), ModBlocks.USER_CALCITE_9.get().asItem(), ModBlocks.USER_CALCITE_10.get().asItem(),
        ModBlocks.USER_CALCITE_11.get().asItem(), ModBlocks.USER_CALCITE_12.get().asItem(), ModBlocks.USER_CALCITE_13.get().asItem(), ModBlocks.USER_CALCITE_14.get().asItem(), ModBlocks.USER_CALCITE_15.get().asItem(),
        ModBlocks.USER_CALCITE_16.get().asItem(), ModBlocks.USER_CALCITE_17.get().asItem(), ModBlocks.USER_CALCITE_18.get().asItem(), ModBlocks.USER_CALCITE_19.get().asItem(), ModBlocks.USER_CALCITE_20.get().asItem(),
        // Mushroom Stem (1-20)
        ModBlocks.USER_MUSHROOM_STEM_1.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_2.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_3.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_4.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_5.get().asItem(),
        ModBlocks.USER_MUSHROOM_STEM_6.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_7.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_8.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_9.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_10.get().asItem(),
        ModBlocks.USER_MUSHROOM_STEM_11.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_12.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_13.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_14.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_15.get().asItem(),
        ModBlocks.USER_MUSHROOM_STEM_16.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_17.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_18.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_19.get().asItem(), ModBlocks.USER_MUSHROOM_STEM_20.get().asItem(),
        // Dead Tube Coral (1-20)
        ModBlocks.USER_DEAD_TUBE_CORAL_1.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_2.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_3.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_4.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_5.get().asItem(),
        ModBlocks.USER_DEAD_TUBE_CORAL_6.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_7.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_8.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_9.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_10.get().asItem(),
        ModBlocks.USER_DEAD_TUBE_CORAL_11.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_12.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_13.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_14.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_15.get().asItem(),
        ModBlocks.USER_DEAD_TUBE_CORAL_16.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_17.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_18.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_19.get().asItem(), ModBlocks.USER_DEAD_TUBE_CORAL_20.get().asItem(),
        // Pearlescent Froglight (1-20)
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_1.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_2.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_3.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_4.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_5.get().asItem(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_6.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_7.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_8.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_9.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_10.get().asItem(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_11.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_12.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_13.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_14.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_15.get().asItem(),
        ModBlocks.USER_PEARLESCENT_FROGLIGHT_16.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_17.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_18.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_19.get().asItem(), ModBlocks.USER_PEARLESCENT_FROGLIGHT_20.get().asItem(),
        // Tinted Glass (dynamic + user 1-20)
        ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get().asItem(),
        ModBlocks.USER_TINTED_GLASS_1.get().asItem(), ModBlocks.USER_TINTED_GLASS_2.get().asItem(), ModBlocks.USER_TINTED_GLASS_3.get().asItem(), ModBlocks.USER_TINTED_GLASS_4.get().asItem(), ModBlocks.USER_TINTED_GLASS_5.get().asItem(),
        ModBlocks.USER_TINTED_GLASS_6.get().asItem(), ModBlocks.USER_TINTED_GLASS_7.get().asItem(), ModBlocks.USER_TINTED_GLASS_8.get().asItem(), ModBlocks.USER_TINTED_GLASS_9.get().asItem(), ModBlocks.USER_TINTED_GLASS_10.get().asItem(),
        ModBlocks.USER_TINTED_GLASS_11.get().asItem(), ModBlocks.USER_TINTED_GLASS_12.get().asItem(), ModBlocks.USER_TINTED_GLASS_13.get().asItem(), ModBlocks.USER_TINTED_GLASS_14.get().asItem(), ModBlocks.USER_TINTED_GLASS_15.get().asItem(),
        ModBlocks.USER_TINTED_GLASS_16.get().asItem(), ModBlocks.USER_TINTED_GLASS_17.get().asItem(), ModBlocks.USER_TINTED_GLASS_18.get().asItem(), ModBlocks.USER_TINTED_GLASS_19.get().asItem(), ModBlocks.USER_TINTED_GLASS_20.get().asItem(),
        // Stained Glass (dynamic + user 1-20)
        ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get().asItem(),
        ModBlocks.USER_STAINED_GLASS_1.get().asItem(), ModBlocks.USER_STAINED_GLASS_2.get().asItem(), ModBlocks.USER_STAINED_GLASS_3.get().asItem(), ModBlocks.USER_STAINED_GLASS_4.get().asItem(), ModBlocks.USER_STAINED_GLASS_5.get().asItem(),
        ModBlocks.USER_STAINED_GLASS_6.get().asItem(), ModBlocks.USER_STAINED_GLASS_7.get().asItem(), ModBlocks.USER_STAINED_GLASS_8.get().asItem(), ModBlocks.USER_STAINED_GLASS_9.get().asItem(), ModBlocks.USER_STAINED_GLASS_10.get().asItem(),
        ModBlocks.USER_STAINED_GLASS_11.get().asItem(), ModBlocks.USER_STAINED_GLASS_12.get().asItem(), ModBlocks.USER_STAINED_GLASS_13.get().asItem(), ModBlocks.USER_STAINED_GLASS_14.get().asItem(), ModBlocks.USER_STAINED_GLASS_15.get().asItem(),
        ModBlocks.USER_STAINED_GLASS_16.get().asItem(), ModBlocks.USER_STAINED_GLASS_17.get().asItem(), ModBlocks.USER_STAINED_GLASS_18.get().asItem(), ModBlocks.USER_STAINED_GLASS_19.get().asItem(), ModBlocks.USER_STAINED_GLASS_20.get().asItem()
        );
    }

}

@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID, value = Dist.CLIENT)
class ClientForgeEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (ModKeyMappings.OPEN_BLOCK_EDITOR.consumeClick() && mc.screen == null) {
                mc.setScreen(new BlockEditorScreen());
            }
        }
    }
}
