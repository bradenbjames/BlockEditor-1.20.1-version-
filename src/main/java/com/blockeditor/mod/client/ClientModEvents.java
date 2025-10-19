package com.blockeditor.mod.client;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.client.gui.BlockEditorScreen;
import com.blockeditor.mod.content.DynamicBlockEntity;
import com.blockeditor.mod.registry.ModBlocks;
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
        // Log that we're registering colors
        System.out.println("=== REGISTERING BLOCK COLORS FOR USER BLOCKS ===");
        
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) return 0xFFFFFF; // Only tint layer 0
            if (level != null && pos != null && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                int color = blockEntity.getColor();
                // Use both System.out and LOGGER to ensure we see the output
                String msg = "BlockColors: tintIndex=" + tintIndex + ", pos=" + pos + ", color=" + String.format("#%06X", color);
                System.out.println(msg);
                com.mojang.logging.LogUtils.getLogger().info(msg);
                return color;
            }
            if (pos != null) {
                String msg = "BlockColors: no BE at pos=" + pos + ", tintIndex=" + tintIndex + ", default white";
                System.out.println(msg);
                com.mojang.logging.LogUtils.getLogger().info(msg);
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
        
        // User blocks - ALL of them need color registration!
        ModBlocks.USER_WOOL_1.get(),
        ModBlocks.USER_WOOL_2.get(),
        ModBlocks.USER_WOOL_3.get(),
        ModBlocks.USER_WOOL_4.get(),
        ModBlocks.USER_WOOL_5.get(),
        ModBlocks.USER_STONE_1.get(),
        ModBlocks.USER_STONE_2.get(),
        ModBlocks.USER_STONE_3.get(),
        ModBlocks.USER_STONE_4.get(),
        ModBlocks.USER_STONE_5.get(),
        ModBlocks.USER_CONCRETE_1.get(),
        ModBlocks.USER_CONCRETE_2.get(),
        ModBlocks.USER_CONCRETE_3.get(),
        ModBlocks.USER_CONCRETE_4.get(),
        ModBlocks.USER_CONCRETE_5.get(),
        ModBlocks.USER_WOOD_1.get(),
        ModBlocks.USER_WOOD_2.get(),
        ModBlocks.USER_WOOD_3.get(),
        ModBlocks.USER_DIRT_1.get(),
        ModBlocks.USER_DIRT_2.get(),
        ModBlocks.USER_DIRT_3.get(),
        ModBlocks.USER_SAND_1.get(),
        ModBlocks.USER_SAND_2.get(),
        ModBlocks.USER_SAND_3.get(),
        ModBlocks.USER_DEEPSLATE_1.get(),
        ModBlocks.USER_DEEPSLATE_2.get(),
        ModBlocks.USER_DEEPSLATE_3.get());
        
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
        ModBlocks.DYNAMIC_BLOCK_STONE.get());
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