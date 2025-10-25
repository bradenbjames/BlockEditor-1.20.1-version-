package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.content.DynamicBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BlockEditorMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<DynamicBlockEntity>> DYNAMIC_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("dynamic_block_entity", () ->
            BlockEntityType.Builder.of(DynamicBlockEntity::new,
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

                // User glass blocks (1-20)
                ModBlocks.USER_GLASS_1.get(), ModBlocks.USER_GLASS_2.get(), ModBlocks.USER_GLASS_3.get(), ModBlocks.USER_GLASS_4.get(), ModBlocks.USER_GLASS_5.get(),
                ModBlocks.USER_GLASS_6.get(), ModBlocks.USER_GLASS_7.get(), ModBlocks.USER_GLASS_8.get(), ModBlocks.USER_GLASS_9.get(), ModBlocks.USER_GLASS_10.get(),
                ModBlocks.USER_GLASS_11.get(), ModBlocks.USER_GLASS_12.get(), ModBlocks.USER_GLASS_13.get(), ModBlocks.USER_GLASS_14.get(), ModBlocks.USER_GLASS_15.get(),
                ModBlocks.USER_GLASS_16.get(), ModBlocks.USER_GLASS_17.get(), ModBlocks.USER_GLASS_18.get(), ModBlocks.USER_GLASS_19.get(), ModBlocks.USER_GLASS_20.get(),

                // User tinted glass blocks (1-20)
                ModBlocks.USER_TINTED_GLASS_1.get(), ModBlocks.USER_TINTED_GLASS_2.get(), ModBlocks.USER_TINTED_GLASS_3.get(), ModBlocks.USER_TINTED_GLASS_4.get(), ModBlocks.USER_TINTED_GLASS_5.get(),
                ModBlocks.USER_TINTED_GLASS_6.get(), ModBlocks.USER_TINTED_GLASS_7.get(), ModBlocks.USER_TINTED_GLASS_8.get(), ModBlocks.USER_TINTED_GLASS_9.get(), ModBlocks.USER_TINTED_GLASS_10.get(),
                ModBlocks.USER_TINTED_GLASS_11.get(), ModBlocks.USER_TINTED_GLASS_12.get(), ModBlocks.USER_TINTED_GLASS_13.get(), ModBlocks.USER_TINTED_GLASS_14.get(), ModBlocks.USER_TINTED_GLASS_15.get(),
                ModBlocks.USER_TINTED_GLASS_16.get(), ModBlocks.USER_TINTED_GLASS_17.get(), ModBlocks.USER_TINTED_GLASS_18.get(), ModBlocks.USER_TINTED_GLASS_19.get(), ModBlocks.USER_TINTED_GLASS_20.get(),

                // User stained glass blocks (1-20)
                ModBlocks.USER_STAINED_GLASS_1.get(), ModBlocks.USER_STAINED_GLASS_2.get(), ModBlocks.USER_STAINED_GLASS_3.get(), ModBlocks.USER_STAINED_GLASS_4.get(), ModBlocks.USER_STAINED_GLASS_5.get(),
                ModBlocks.USER_STAINED_GLASS_6.get(), ModBlocks.USER_STAINED_GLASS_7.get(), ModBlocks.USER_STAINED_GLASS_8.get(), ModBlocks.USER_STAINED_GLASS_9.get(), ModBlocks.USER_STAINED_GLASS_10.get(),
                ModBlocks.USER_STAINED_GLASS_11.get(), ModBlocks.USER_STAINED_GLASS_12.get(), ModBlocks.USER_STAINED_GLASS_13.get(), ModBlocks.USER_STAINED_GLASS_14.get(), ModBlocks.USER_STAINED_GLASS_15.get(),
                ModBlocks.USER_STAINED_GLASS_16.get(), ModBlocks.USER_STAINED_GLASS_17.get(), ModBlocks.USER_STAINED_GLASS_18.get(), ModBlocks.USER_STAINED_GLASS_19.get(), ModBlocks.USER_STAINED_GLASS_20.get()
            ).build(null)
        );
}