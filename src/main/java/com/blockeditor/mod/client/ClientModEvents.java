package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.client.gui.BlockEditorScreen;
import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.ModBlocks;
import com.blockeditor.mod.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlockEditorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.OPEN_BLOCK_EDITOR);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        System.out.println("=== STARTING BLOCK COLOR REGISTRATION ===");
        // Log that we're registering colors
        System.out.println("=== REGISTERING BLOCK COLORS FOR USER BLOCKS ===");
        
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return 0xFFFFFF; // Only tint layer 0
            if (level != null && pos != null && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                int color = blockEntity.getColor();
                return color;
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
        ModBlocks.USER_SMOOTH_STONE_16.get(), ModBlocks.USER_SMOOTH_STONE_17.get(), ModBlocks.USER_SMOOTH_STONE_18.get(), ModBlocks.USER_SMOOTH_STONE_19.get(), ModBlocks.USER_SMOOTH_STONE_20.get());
        
        System.out.println("=== FINISHED REGISTERING BLOCK COLORS - INCLUDING ALL USER BLOCKS ===");
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Color")) {
                String hexColor = tag.getString("Color");
                try {
                    return Integer.parseInt(hexColor, 16);
                } catch (NumberFormatException ignored) {}
            }
            return 0xFFFFFF; // Default white
        },
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
        
        // User block items - ALL of them need color registration! (1-20 for each type)
        // Wool items (1-20)
        ModItems.USER_WOOL_1_ITEM.get(), ModItems.USER_WOOL_2_ITEM.get(), ModItems.USER_WOOL_3_ITEM.get(), ModItems.USER_WOOL_4_ITEM.get(), ModItems.USER_WOOL_5_ITEM.get(),
        ModItems.USER_WOOL_6_ITEM.get(), ModItems.USER_WOOL_7_ITEM.get(), ModItems.USER_WOOL_8_ITEM.get(), ModItems.USER_WOOL_9_ITEM.get(), ModItems.USER_WOOL_10_ITEM.get(),
        ModItems.USER_WOOL_11_ITEM.get(), ModItems.USER_WOOL_12_ITEM.get(), ModItems.USER_WOOL_13_ITEM.get(), ModItems.USER_WOOL_14_ITEM.get(), ModItems.USER_WOOL_15_ITEM.get(),
        ModItems.USER_WOOL_16_ITEM.get(), ModItems.USER_WOOL_17_ITEM.get(), ModItems.USER_WOOL_18_ITEM.get(), ModItems.USER_WOOL_19_ITEM.get(), ModItems.USER_WOOL_20_ITEM.get(),
        // Stone items (1-20)
        ModItems.USER_STONE_1_ITEM.get(), ModItems.USER_STONE_2_ITEM.get(), ModItems.USER_STONE_3_ITEM.get(), ModItems.USER_STONE_4_ITEM.get(), ModItems.USER_STONE_5_ITEM.get(),
        ModItems.USER_STONE_6_ITEM.get(), ModItems.USER_STONE_7_ITEM.get(), ModItems.USER_STONE_8_ITEM.get(), ModItems.USER_STONE_9_ITEM.get(), ModItems.USER_STONE_10_ITEM.get(),
        ModItems.USER_STONE_11_ITEM.get(), ModItems.USER_STONE_12_ITEM.get(), ModItems.USER_STONE_13_ITEM.get(), ModItems.USER_STONE_14_ITEM.get(), ModItems.USER_STONE_15_ITEM.get(),
        ModItems.USER_STONE_16_ITEM.get(), ModItems.USER_STONE_17_ITEM.get(), ModItems.USER_STONE_18_ITEM.get(), ModItems.USER_STONE_19_ITEM.get(), ModItems.USER_STONE_20_ITEM.get(),
        // Concrete items (1-20)
        ModItems.USER_CONCRETE_1_ITEM.get(), ModItems.USER_CONCRETE_2_ITEM.get(), ModItems.USER_CONCRETE_3_ITEM.get(), ModItems.USER_CONCRETE_4_ITEM.get(), ModItems.USER_CONCRETE_5_ITEM.get(),
        ModItems.USER_CONCRETE_6_ITEM.get(), ModItems.USER_CONCRETE_7_ITEM.get(), ModItems.USER_CONCRETE_8_ITEM.get(), ModItems.USER_CONCRETE_9_ITEM.get(), ModItems.USER_CONCRETE_10_ITEM.get(),
        ModItems.USER_CONCRETE_11_ITEM.get(), ModItems.USER_CONCRETE_12_ITEM.get(), ModItems.USER_CONCRETE_13_ITEM.get(), ModItems.USER_CONCRETE_14_ITEM.get(), ModItems.USER_CONCRETE_15_ITEM.get(),
        ModItems.USER_CONCRETE_16_ITEM.get(), ModItems.USER_CONCRETE_17_ITEM.get(), ModItems.USER_CONCRETE_18_ITEM.get(), ModItems.USER_CONCRETE_19_ITEM.get(), ModItems.USER_CONCRETE_20_ITEM.get(),
        // Wood items (1-20)
        ModItems.USER_WOOD_1_ITEM.get(), ModItems.USER_WOOD_2_ITEM.get(), ModItems.USER_WOOD_3_ITEM.get(), ModItems.USER_WOOD_4_ITEM.get(), ModItems.USER_WOOD_5_ITEM.get(),
        ModItems.USER_WOOD_6_ITEM.get(), ModItems.USER_WOOD_7_ITEM.get(), ModItems.USER_WOOD_8_ITEM.get(), ModItems.USER_WOOD_9_ITEM.get(), ModItems.USER_WOOD_10_ITEM.get(),
        ModItems.USER_WOOD_11_ITEM.get(), ModItems.USER_WOOD_12_ITEM.get(), ModItems.USER_WOOD_13_ITEM.get(), ModItems.USER_WOOD_14_ITEM.get(), ModItems.USER_WOOD_15_ITEM.get(),
        ModItems.USER_WOOD_16_ITEM.get(), ModItems.USER_WOOD_17_ITEM.get(), ModItems.USER_WOOD_18_ITEM.get(), ModItems.USER_WOOD_19_ITEM.get(), ModItems.USER_WOOD_20_ITEM.get(),
        // Dirt items (1-20)
        ModItems.USER_DIRT_1_ITEM.get(), ModItems.USER_DIRT_2_ITEM.get(), ModItems.USER_DIRT_3_ITEM.get(), ModItems.USER_DIRT_4_ITEM.get(), ModItems.USER_DIRT_5_ITEM.get(),
        ModItems.USER_DIRT_6_ITEM.get(), ModItems.USER_DIRT_7_ITEM.get(), ModItems.USER_DIRT_8_ITEM.get(), ModItems.USER_DIRT_9_ITEM.get(), ModItems.USER_DIRT_10_ITEM.get(),
        ModItems.USER_DIRT_11_ITEM.get(), ModItems.USER_DIRT_12_ITEM.get(), ModItems.USER_DIRT_13_ITEM.get(), ModItems.USER_DIRT_14_ITEM.get(), ModItems.USER_DIRT_15_ITEM.get(),
        ModItems.USER_DIRT_16_ITEM.get(), ModItems.USER_DIRT_17_ITEM.get(), ModItems.USER_DIRT_18_ITEM.get(), ModItems.USER_DIRT_19_ITEM.get(), ModItems.USER_DIRT_20_ITEM.get(),
        // Sand items (1-20)
        ModItems.USER_SAND_1_ITEM.get(), ModItems.USER_SAND_2_ITEM.get(), ModItems.USER_SAND_3_ITEM.get(), ModItems.USER_SAND_4_ITEM.get(), ModItems.USER_SAND_5_ITEM.get(),
        ModItems.USER_SAND_6_ITEM.get(), ModItems.USER_SAND_7_ITEM.get(), ModItems.USER_SAND_8_ITEM.get(), ModItems.USER_SAND_9_ITEM.get(), ModItems.USER_SAND_10_ITEM.get(),
        ModItems.USER_SAND_11_ITEM.get(), ModItems.USER_SAND_12_ITEM.get(), ModItems.USER_SAND_13_ITEM.get(), ModItems.USER_SAND_14_ITEM.get(), ModItems.USER_SAND_15_ITEM.get(),
        ModItems.USER_SAND_16_ITEM.get(), ModItems.USER_SAND_17_ITEM.get(), ModItems.USER_SAND_18_ITEM.get(), ModItems.USER_SAND_19_ITEM.get(), ModItems.USER_SAND_20_ITEM.get(),
        // Deepslate items (1-20)
        ModItems.USER_DEEPSLATE_1_ITEM.get(), ModItems.USER_DEEPSLATE_2_ITEM.get(), ModItems.USER_DEEPSLATE_3_ITEM.get(), ModItems.USER_DEEPSLATE_4_ITEM.get(), ModItems.USER_DEEPSLATE_5_ITEM.get(),
        ModItems.USER_DEEPSLATE_6_ITEM.get(), ModItems.USER_DEEPSLATE_7_ITEM.get(), ModItems.USER_DEEPSLATE_8_ITEM.get(), ModItems.USER_DEEPSLATE_9_ITEM.get(), ModItems.USER_DEEPSLATE_10_ITEM.get(),
        ModItems.USER_DEEPSLATE_11_ITEM.get(), ModItems.USER_DEEPSLATE_12_ITEM.get(), ModItems.USER_DEEPSLATE_13_ITEM.get(), ModItems.USER_DEEPSLATE_14_ITEM.get(), ModItems.USER_DEEPSLATE_15_ITEM.get(),
        ModItems.USER_DEEPSLATE_16_ITEM.get(), ModItems.USER_DEEPSLATE_17_ITEM.get(), ModItems.USER_DEEPSLATE_18_ITEM.get(), ModItems.USER_DEEPSLATE_19_ITEM.get(), ModItems.USER_DEEPSLATE_20_ITEM.get(),
        // Cobblestone items (1-20)
        ModItems.USER_COBBLESTONE_1_ITEM.get(), ModItems.USER_COBBLESTONE_2_ITEM.get(), ModItems.USER_COBBLESTONE_3_ITEM.get(), ModItems.USER_COBBLESTONE_4_ITEM.get(), ModItems.USER_COBBLESTONE_5_ITEM.get(),
        ModItems.USER_COBBLESTONE_6_ITEM.get(), ModItems.USER_COBBLESTONE_7_ITEM.get(), ModItems.USER_COBBLESTONE_8_ITEM.get(), ModItems.USER_COBBLESTONE_9_ITEM.get(), ModItems.USER_COBBLESTONE_10_ITEM.get(),
        ModItems.USER_COBBLESTONE_11_ITEM.get(), ModItems.USER_COBBLESTONE_12_ITEM.get(), ModItems.USER_COBBLESTONE_13_ITEM.get(), ModItems.USER_COBBLESTONE_14_ITEM.get(), ModItems.USER_COBBLESTONE_15_ITEM.get(),
        ModItems.USER_COBBLESTONE_16_ITEM.get(), ModItems.USER_COBBLESTONE_17_ITEM.get(), ModItems.USER_COBBLESTONE_18_ITEM.get(), ModItems.USER_COBBLESTONE_19_ITEM.get(), ModItems.USER_COBBLESTONE_20_ITEM.get(),
        // Smooth Stone items (1-20)
        ModItems.USER_SMOOTH_STONE_1_ITEM.get(), ModItems.USER_SMOOTH_STONE_2_ITEM.get(), ModItems.USER_SMOOTH_STONE_3_ITEM.get(), ModItems.USER_SMOOTH_STONE_4_ITEM.get(), ModItems.USER_SMOOTH_STONE_5_ITEM.get(),
        ModItems.USER_SMOOTH_STONE_6_ITEM.get(), ModItems.USER_SMOOTH_STONE_7_ITEM.get(), ModItems.USER_SMOOTH_STONE_8_ITEM.get(), ModItems.USER_SMOOTH_STONE_9_ITEM.get(), ModItems.USER_SMOOTH_STONE_10_ITEM.get(),
        ModItems.USER_SMOOTH_STONE_11_ITEM.get(), ModItems.USER_SMOOTH_STONE_12_ITEM.get(), ModItems.USER_SMOOTH_STONE_13_ITEM.get(), ModItems.USER_SMOOTH_STONE_14_ITEM.get(), ModItems.USER_SMOOTH_STONE_15_ITEM.get(),
        ModItems.USER_SMOOTH_STONE_16_ITEM.get(), ModItems.USER_SMOOTH_STONE_17_ITEM.get(), ModItems.USER_SMOOTH_STONE_18_ITEM.get(), ModItems.USER_SMOOTH_STONE_19_ITEM.get(), ModItems.USER_SMOOTH_STONE_20_ITEM.get());
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