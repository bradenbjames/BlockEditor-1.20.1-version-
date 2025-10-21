package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.blockeditor.mod.content.DynamicBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BlockEditorMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<DynamicBlockEntity>> DYNAMIC_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("dynamic_block_entity", () -> {
            return BlockEntityType.Builder.of(DynamicBlockEntity::new,
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
                ModBlocks.DYNAMIC_BLOCK_DIORITE.get(),
                ModBlocks.DYNAMIC_BLOCK_CALCITE.get(),
                ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get(),
                ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get(),
                ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get()
            ).build(null);
        });
}