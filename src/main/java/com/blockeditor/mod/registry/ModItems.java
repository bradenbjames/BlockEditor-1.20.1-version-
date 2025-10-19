package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.content.DynamicBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, BlockEditorMod.MOD_ID);

    public static final RegistryObject<Item> DYNAMIC_BLOCK_ITEM = ITEMS.register("dynamic_block",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_DIRT_ITEM = ITEMS.register("dynamic_block_dirt",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_DIRT.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_SAND_ITEM = ITEMS.register("dynamic_block_sand",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_SAND.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_WOOL_ITEM = ITEMS.register("dynamic_block_wool",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_WOOL.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_CONCRETE_ITEM = ITEMS.register("dynamic_block_concrete",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_CONCRETE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_DEEPSLATE_ITEM = ITEMS.register("dynamic_block_deepslate",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_WOOD_ITEM = ITEMS.register("dynamic_block_wood",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_WOOD.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_STONE_ITEM = ITEMS.register("dynamic_block_stone",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_STONE.get(), new Item.Properties()));

    // User block items for WorldEdit autocomplete (numbered system)
    // Wool blocks
    public static final RegistryObject<Item> USER_WOOL_1_ITEM = ITEMS.register("user_wool1",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOL_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_2_ITEM = ITEMS.register("user_wool2",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOL_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_3_ITEM = ITEMS.register("user_wool3",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOL_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_4_ITEM = ITEMS.register("user_wool4",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOL_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_5_ITEM = ITEMS.register("user_wool5",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOL_5.get(), new Item.Properties()));

    // Stone blocks
    public static final RegistryObject<Item> USER_STONE_1_ITEM = ITEMS.register("user_stone1",
        () -> new DynamicBlockItem(ModBlocks.USER_STONE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_2_ITEM = ITEMS.register("user_stone2",
        () -> new DynamicBlockItem(ModBlocks.USER_STONE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_3_ITEM = ITEMS.register("user_stone3",
        () -> new DynamicBlockItem(ModBlocks.USER_STONE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_4_ITEM = ITEMS.register("user_stone4",
        () -> new DynamicBlockItem(ModBlocks.USER_STONE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_5_ITEM = ITEMS.register("user_stone5",
        () -> new DynamicBlockItem(ModBlocks.USER_STONE_5.get(), new Item.Properties()));

    // Concrete blocks
    public static final RegistryObject<Item> USER_CONCRETE_1_ITEM = ITEMS.register("user_concrete1",
        () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_2_ITEM = ITEMS.register("user_concrete2",
        () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_3_ITEM = ITEMS.register("user_concrete3",
        () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_4_ITEM = ITEMS.register("user_concrete4",
        () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_5_ITEM = ITEMS.register("user_concrete5",
        () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_5.get(), new Item.Properties()));

    // Wood blocks
    public static final RegistryObject<Item> USER_WOOD_1_ITEM = ITEMS.register("user_wood1",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOD_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_2_ITEM = ITEMS.register("user_wood2",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOD_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_3_ITEM = ITEMS.register("user_wood3",
        () -> new DynamicBlockItem(ModBlocks.USER_WOOD_3.get(), new Item.Properties()));

    // Dirt blocks
    public static final RegistryObject<Item> USER_DIRT_1_ITEM = ITEMS.register("user_dirt1",
        () -> new DynamicBlockItem(ModBlocks.USER_DIRT_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_2_ITEM = ITEMS.register("user_dirt2",
        () -> new DynamicBlockItem(ModBlocks.USER_DIRT_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_3_ITEM = ITEMS.register("user_dirt3",
        () -> new DynamicBlockItem(ModBlocks.USER_DIRT_3.get(), new Item.Properties()));

    // Sand blocks
    public static final RegistryObject<Item> USER_SAND_1_ITEM = ITEMS.register("user_sand1",
        () -> new DynamicBlockItem(ModBlocks.USER_SAND_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_2_ITEM = ITEMS.register("user_sand2",
        () -> new DynamicBlockItem(ModBlocks.USER_SAND_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_3_ITEM = ITEMS.register("user_sand3",
        () -> new DynamicBlockItem(ModBlocks.USER_SAND_3.get(), new Item.Properties()));

    // Deepslate blocks
    public static final RegistryObject<Item> USER_DEEPSLATE_1_ITEM = ITEMS.register("user_deepslate1",
        () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_2_ITEM = ITEMS.register("user_deepslate2",
        () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_3_ITEM = ITEMS.register("user_deepslate3",
        () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_3.get(), new Item.Properties()));
}