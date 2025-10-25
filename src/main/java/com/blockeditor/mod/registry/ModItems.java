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

    public static final RegistryObject<Item> DYNAMIC_BLOCK_COBBLESTONE_ITEM = ITEMS.register("dynamic_block_cobblestone",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_SMOOTH_STONE_ITEM = ITEMS.register("dynamic_block_smooth_stone",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_TERRACOTTA_ITEM = ITEMS.register("dynamic_block_terracotta",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_CONCRETE_POWDER_ITEM = ITEMS.register("dynamic_block_concrete_powder",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_GLASS_ITEM = ITEMS.register("dynamic_block_glass",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_GLASS.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_TINTED_GLASS_ITEM = ITEMS.register("dynamic_block_tinted_glass",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_TINTED_GLASS.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_STAINED_GLASS_ITEM = ITEMS.register("dynamic_block_stained_glass",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_STAINED_GLASS.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_DIORITE_ITEM = ITEMS.register("dynamic_block_diorite",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_DIORITE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_CALCITE_ITEM = ITEMS.register("dynamic_block_calcite",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_CALCITE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_MUSHROOM_STEM_ITEM = ITEMS.register("dynamic_block_mushroom_stem",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_DEAD_TUBE_CORAL_ITEM = ITEMS.register("dynamic_block_dead_tube_coral",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get(), new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT_ITEM = ITEMS.register("dynamic_block_pearlescent_froglight",
        () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get(), new Item.Properties()));

    // User block items for WorldEdit autocomplete (numbered system)
    // Wool blocks (1-20)
    public static final RegistryObject<Item> USER_WOOL_1_ITEM = ITEMS.register("u_wool1", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_2_ITEM = ITEMS.register("u_wool2", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_3_ITEM = ITEMS.register("u_wool3", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_4_ITEM = ITEMS.register("u_wool4", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_5_ITEM = ITEMS.register("u_wool5", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_6_ITEM = ITEMS.register("u_wool6", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_7_ITEM = ITEMS.register("u_wool7", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_8_ITEM = ITEMS.register("u_wool8", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_9_ITEM = ITEMS.register("u_wool9", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_10_ITEM = ITEMS.register("u_wool10", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_11_ITEM = ITEMS.register("u_wool11", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_12_ITEM = ITEMS.register("u_wool12", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_13_ITEM = ITEMS.register("u_wool13", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_14_ITEM = ITEMS.register("u_wool14", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_15_ITEM = ITEMS.register("u_wool15", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_16_ITEM = ITEMS.register("u_wool16", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_17_ITEM = ITEMS.register("u_wool17", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_18_ITEM = ITEMS.register("u_wool18", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_19_ITEM = ITEMS.register("u_wool19", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOL_20_ITEM = ITEMS.register("u_wool20", () -> new DynamicBlockItem(ModBlocks.USER_WOOL_20.get(), new Item.Properties()));

    // Stone blocks (1-20)
    public static final RegistryObject<Item> USER_STONE_1_ITEM = ITEMS.register("u_stone1", () -> new DynamicBlockItem(ModBlocks.USER_STONE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_2_ITEM = ITEMS.register("u_stone2", () -> new DynamicBlockItem(ModBlocks.USER_STONE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_3_ITEM = ITEMS.register("u_stone3", () -> new DynamicBlockItem(ModBlocks.USER_STONE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_4_ITEM = ITEMS.register("u_stone4", () -> new DynamicBlockItem(ModBlocks.USER_STONE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_5_ITEM = ITEMS.register("u_stone5", () -> new DynamicBlockItem(ModBlocks.USER_STONE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_6_ITEM = ITEMS.register("u_stone6", () -> new DynamicBlockItem(ModBlocks.USER_STONE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_7_ITEM = ITEMS.register("u_stone7", () -> new DynamicBlockItem(ModBlocks.USER_STONE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_8_ITEM = ITEMS.register("u_stone8", () -> new DynamicBlockItem(ModBlocks.USER_STONE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_9_ITEM = ITEMS.register("u_stone9", () -> new DynamicBlockItem(ModBlocks.USER_STONE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_10_ITEM = ITEMS.register("u_stone10", () -> new DynamicBlockItem(ModBlocks.USER_STONE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_11_ITEM = ITEMS.register("u_stone11", () -> new DynamicBlockItem(ModBlocks.USER_STONE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_12_ITEM = ITEMS.register("u_stone12", () -> new DynamicBlockItem(ModBlocks.USER_STONE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_13_ITEM = ITEMS.register("u_stone13", () -> new DynamicBlockItem(ModBlocks.USER_STONE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_14_ITEM = ITEMS.register("u_stone14", () -> new DynamicBlockItem(ModBlocks.USER_STONE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_15_ITEM = ITEMS.register("u_stone15", () -> new DynamicBlockItem(ModBlocks.USER_STONE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_16_ITEM = ITEMS.register("u_stone16", () -> new DynamicBlockItem(ModBlocks.USER_STONE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_17_ITEM = ITEMS.register("u_stone17", () -> new DynamicBlockItem(ModBlocks.USER_STONE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_18_ITEM = ITEMS.register("u_stone18", () -> new DynamicBlockItem(ModBlocks.USER_STONE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_19_ITEM = ITEMS.register("u_stone19", () -> new DynamicBlockItem(ModBlocks.USER_STONE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STONE_20_ITEM = ITEMS.register("u_stone20", () -> new DynamicBlockItem(ModBlocks.USER_STONE_20.get(), new Item.Properties()));

    // Concrete blocks (1-20)
    public static final RegistryObject<Item> USER_CONCRETE_1_ITEM = ITEMS.register("u_concrete1", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_2_ITEM = ITEMS.register("u_concrete2", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_3_ITEM = ITEMS.register("u_concrete3", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_4_ITEM = ITEMS.register("u_concrete4", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_5_ITEM = ITEMS.register("u_concrete5", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_6_ITEM = ITEMS.register("u_concrete6", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_7_ITEM = ITEMS.register("u_concrete7", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_8_ITEM = ITEMS.register("u_concrete8", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_9_ITEM = ITEMS.register("u_concrete9", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_10_ITEM = ITEMS.register("u_concrete10", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_11_ITEM = ITEMS.register("u_concrete11", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_12_ITEM = ITEMS.register("u_concrete12", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_13_ITEM = ITEMS.register("u_concrete13", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_14_ITEM = ITEMS.register("u_concrete14", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_15_ITEM = ITEMS.register("u_concrete15", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_16_ITEM = ITEMS.register("u_concrete16", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_17_ITEM = ITEMS.register("u_concrete17", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_18_ITEM = ITEMS.register("u_concrete18", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_19_ITEM = ITEMS.register("u_concrete19", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_20_ITEM = ITEMS.register("u_concrete20", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_20.get(), new Item.Properties()));

    // Wood blocks (1-20)
    public static final RegistryObject<Item> USER_WOOD_1_ITEM = ITEMS.register("u_wood1", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_2_ITEM = ITEMS.register("u_wood2", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_3_ITEM = ITEMS.register("u_wood3", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_4_ITEM = ITEMS.register("u_wood4", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_5_ITEM = ITEMS.register("u_wood5", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_6_ITEM = ITEMS.register("u_wood6", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_7_ITEM = ITEMS.register("u_wood7", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_8_ITEM = ITEMS.register("u_wood8", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_9_ITEM = ITEMS.register("u_wood9", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_10_ITEM = ITEMS.register("u_wood10", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_11_ITEM = ITEMS.register("u_wood11", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_12_ITEM = ITEMS.register("u_wood12", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_13_ITEM = ITEMS.register("u_wood13", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_14_ITEM = ITEMS.register("u_wood14", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_15_ITEM = ITEMS.register("u_wood15", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_16_ITEM = ITEMS.register("u_wood16", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_17_ITEM = ITEMS.register("u_wood17", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_18_ITEM = ITEMS.register("u_wood18", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_19_ITEM = ITEMS.register("u_wood19", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_WOOD_20_ITEM = ITEMS.register("u_wood20", () -> new DynamicBlockItem(ModBlocks.USER_WOOD_20.get(), new Item.Properties()));

    // Dirt blocks (1-20)
    public static final RegistryObject<Item> USER_DIRT_1_ITEM = ITEMS.register("u_dirt1", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_2_ITEM = ITEMS.register("u_dirt2", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_3_ITEM = ITEMS.register("u_dirt3", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_4_ITEM = ITEMS.register("u_dirt4", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_5_ITEM = ITEMS.register("u_dirt5", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_6_ITEM = ITEMS.register("u_dirt6", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_7_ITEM = ITEMS.register("u_dirt7", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_8_ITEM = ITEMS.register("u_dirt8", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_9_ITEM = ITEMS.register("u_dirt9", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_10_ITEM = ITEMS.register("u_dirt10", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_11_ITEM = ITEMS.register("u_dirt11", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_12_ITEM = ITEMS.register("u_dirt12", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_13_ITEM = ITEMS.register("u_dirt13", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_14_ITEM = ITEMS.register("u_dirt14", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_15_ITEM = ITEMS.register("u_dirt15", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_16_ITEM = ITEMS.register("u_dirt16", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_17_ITEM = ITEMS.register("u_dirt17", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_18_ITEM = ITEMS.register("u_dirt18", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_19_ITEM = ITEMS.register("u_dirt19", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIRT_20_ITEM = ITEMS.register("u_dirt20", () -> new DynamicBlockItem(ModBlocks.USER_DIRT_20.get(), new Item.Properties()));

    // Sand blocks (1-20)
    public static final RegistryObject<Item> USER_SAND_1_ITEM = ITEMS.register("u_sand1", () -> new DynamicBlockItem(ModBlocks.USER_SAND_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_2_ITEM = ITEMS.register("u_sand2", () -> new DynamicBlockItem(ModBlocks.USER_SAND_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_3_ITEM = ITEMS.register("u_sand3", () -> new DynamicBlockItem(ModBlocks.USER_SAND_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_4_ITEM = ITEMS.register("u_sand4", () -> new DynamicBlockItem(ModBlocks.USER_SAND_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_5_ITEM = ITEMS.register("u_sand5", () -> new DynamicBlockItem(ModBlocks.USER_SAND_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_6_ITEM = ITEMS.register("u_sand6", () -> new DynamicBlockItem(ModBlocks.USER_SAND_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_7_ITEM = ITEMS.register("u_sand7", () -> new DynamicBlockItem(ModBlocks.USER_SAND_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_8_ITEM = ITEMS.register("u_sand8", () -> new DynamicBlockItem(ModBlocks.USER_SAND_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_9_ITEM = ITEMS.register("u_sand9", () -> new DynamicBlockItem(ModBlocks.USER_SAND_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_10_ITEM = ITEMS.register("u_sand10", () -> new DynamicBlockItem(ModBlocks.USER_SAND_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_11_ITEM = ITEMS.register("u_sand11", () -> new DynamicBlockItem(ModBlocks.USER_SAND_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_12_ITEM = ITEMS.register("u_sand12", () -> new DynamicBlockItem(ModBlocks.USER_SAND_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_13_ITEM = ITEMS.register("u_sand13", () -> new DynamicBlockItem(ModBlocks.USER_SAND_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_14_ITEM = ITEMS.register("u_sand14", () -> new DynamicBlockItem(ModBlocks.USER_SAND_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_15_ITEM = ITEMS.register("u_sand15", () -> new DynamicBlockItem(ModBlocks.USER_SAND_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_16_ITEM = ITEMS.register("u_sand16", () -> new DynamicBlockItem(ModBlocks.USER_SAND_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_17_ITEM = ITEMS.register("u_sand17", () -> new DynamicBlockItem(ModBlocks.USER_SAND_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_18_ITEM = ITEMS.register("u_sand18", () -> new DynamicBlockItem(ModBlocks.USER_SAND_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_19_ITEM = ITEMS.register("u_sand19", () -> new DynamicBlockItem(ModBlocks.USER_SAND_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SAND_20_ITEM = ITEMS.register("u_sand20", () -> new DynamicBlockItem(ModBlocks.USER_SAND_20.get(), new Item.Properties()));

    // Deepslate blocks (1-20)
    public static final RegistryObject<Item> USER_DEEPSLATE_1_ITEM = ITEMS.register("u_deepslate1", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_2_ITEM = ITEMS.register("u_deepslate2", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_3_ITEM = ITEMS.register("u_deepslate3", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_4_ITEM = ITEMS.register("u_deepslate4", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_5_ITEM = ITEMS.register("u_deepslate5", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_6_ITEM = ITEMS.register("u_deepslate6", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_7_ITEM = ITEMS.register("u_deepslate7", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_8_ITEM = ITEMS.register("u_deepslate8", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_9_ITEM = ITEMS.register("u_deepslate9", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_10_ITEM = ITEMS.register("u_deepslate10", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_11_ITEM = ITEMS.register("u_deepslate11", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_12_ITEM = ITEMS.register("u_deepslate12", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_13_ITEM = ITEMS.register("u_deepslate13", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_14_ITEM = ITEMS.register("u_deepslate14", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_15_ITEM = ITEMS.register("u_deepslate15", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_16_ITEM = ITEMS.register("u_deepslate16", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_17_ITEM = ITEMS.register("u_deepslate17", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_18_ITEM = ITEMS.register("u_deepslate18", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_19_ITEM = ITEMS.register("u_deepslate19", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEEPSLATE_20_ITEM = ITEMS.register("u_deepslate20", () -> new DynamicBlockItem(ModBlocks.USER_DEEPSLATE_20.get(), new Item.Properties()));

    // Cobblestone blocks (1-20)
    public static final RegistryObject<Item> USER_COBBLESTONE_1_ITEM = ITEMS.register("u_cobblestone1", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_2_ITEM = ITEMS.register("u_cobblestone2", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_3_ITEM = ITEMS.register("u_cobblestone3", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_4_ITEM = ITEMS.register("u_cobblestone4", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_5_ITEM = ITEMS.register("u_cobblestone5", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_6_ITEM = ITEMS.register("u_cobblestone6", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_7_ITEM = ITEMS.register("u_cobblestone7", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_8_ITEM = ITEMS.register("u_cobblestone8", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_9_ITEM = ITEMS.register("u_cobblestone9", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_10_ITEM = ITEMS.register("u_cobblestone10", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_11_ITEM = ITEMS.register("u_cobblestone11", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_12_ITEM = ITEMS.register("u_cobblestone12", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_13_ITEM = ITEMS.register("u_cobblestone13", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_14_ITEM = ITEMS.register("u_cobblestone14", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_15_ITEM = ITEMS.register("u_cobblestone15", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_16_ITEM = ITEMS.register("u_cobblestone16", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_17_ITEM = ITEMS.register("u_cobblestone17", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_18_ITEM = ITEMS.register("u_cobblestone18", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_19_ITEM = ITEMS.register("u_cobblestone19", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_COBBLESTONE_20_ITEM = ITEMS.register("u_cobblestone20", () -> new DynamicBlockItem(ModBlocks.USER_COBBLESTONE_20.get(), new Item.Properties()));

    // Smooth Stone blocks (1-20)
    public static final RegistryObject<Item> USER_SMOOTH_STONE_1_ITEM = ITEMS.register("u_smooth_stone1", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_2_ITEM = ITEMS.register("u_smooth_stone2", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_3_ITEM = ITEMS.register("u_smooth_stone3", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_4_ITEM = ITEMS.register("u_smooth_stone4", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_5_ITEM = ITEMS.register("u_smooth_stone5", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_6_ITEM = ITEMS.register("u_smooth_stone6", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_7_ITEM = ITEMS.register("u_smooth_stone7", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_8_ITEM = ITEMS.register("u_smooth_stone8", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_9_ITEM = ITEMS.register("u_smooth_stone9", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_10_ITEM = ITEMS.register("u_smooth_stone10", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_11_ITEM = ITEMS.register("u_smooth_stone11", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_12_ITEM = ITEMS.register("u_smooth_stone12", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_13_ITEM = ITEMS.register("u_smooth_stone13", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_14_ITEM = ITEMS.register("u_smooth_stone14", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_15_ITEM = ITEMS.register("u_smooth_stone15", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_16_ITEM = ITEMS.register("u_smooth_stone16", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_17_ITEM = ITEMS.register("u_smooth_stone17", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_18_ITEM = ITEMS.register("u_smooth_stone18", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_19_ITEM = ITEMS.register("u_smooth_stone19", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_SMOOTH_STONE_20_ITEM = ITEMS.register("u_smooth_stone20", () -> new DynamicBlockItem(ModBlocks.USER_SMOOTH_STONE_20.get(), new Item.Properties()));

    // Terracotta items (1-20)
    public static final RegistryObject<Item> USER_TERRACOTTA_1_ITEM = ITEMS.register("u_terracotta1", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_2_ITEM = ITEMS.register("u_terracotta2", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_3_ITEM = ITEMS.register("u_terracotta3", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_4_ITEM = ITEMS.register("u_terracotta4", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_5_ITEM = ITEMS.register("u_terracotta5", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_6_ITEM = ITEMS.register("u_terracotta6", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_7_ITEM = ITEMS.register("u_terracotta7", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_8_ITEM = ITEMS.register("u_terracotta8", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_9_ITEM = ITEMS.register("u_terracotta9", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_10_ITEM = ITEMS.register("u_terracotta10", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_11_ITEM = ITEMS.register("u_terracotta11", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_12_ITEM = ITEMS.register("u_terracotta12", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_13_ITEM = ITEMS.register("u_terracotta13", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_14_ITEM = ITEMS.register("u_terracotta14", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_15_ITEM = ITEMS.register("u_terracotta15", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_16_ITEM = ITEMS.register("u_terracotta16", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_17_ITEM = ITEMS.register("u_terracotta17", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_18_ITEM = ITEMS.register("u_terracotta18", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_19_ITEM = ITEMS.register("u_terracotta19", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TERRACOTTA_20_ITEM = ITEMS.register("u_terracotta20", () -> new DynamicBlockItem(ModBlocks.USER_TERRACOTTA_20.get(), new Item.Properties()));

    // Concrete Powder items (1-20)
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_1_ITEM = ITEMS.register("u_concrete_powder1", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_2_ITEM = ITEMS.register("u_concrete_powder2", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_3_ITEM = ITEMS.register("u_concrete_powder3", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_4_ITEM = ITEMS.register("u_concrete_powder4", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_5_ITEM = ITEMS.register("u_concrete_powder5", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_6_ITEM = ITEMS.register("u_concrete_powder6", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_7_ITEM = ITEMS.register("u_concrete_powder7", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_8_ITEM = ITEMS.register("u_concrete_powder8", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_9_ITEM = ITEMS.register("u_concrete_powder9", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_10_ITEM = ITEMS.register("u_concrete_powder10", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_11_ITEM = ITEMS.register("u_concrete_powder11", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_12_ITEM = ITEMS.register("u_concrete_powder12", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_13_ITEM = ITEMS.register("u_concrete_powder13", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_14_ITEM = ITEMS.register("u_concrete_powder14", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_15_ITEM = ITEMS.register("u_concrete_powder15", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_16_ITEM = ITEMS.register("u_concrete_powder16", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_17_ITEM = ITEMS.register("u_concrete_powder17", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_18_ITEM = ITEMS.register("u_concrete_powder18", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_19_ITEM = ITEMS.register("u_concrete_powder19", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CONCRETE_POWDER_20_ITEM = ITEMS.register("u_concrete_powder20", () -> new DynamicBlockItem(ModBlocks.USER_CONCRETE_POWDER_20.get(), new Item.Properties()));

    // Glass items (1-20)
    public static final RegistryObject<Item> USER_GLASS_1_ITEM = ITEMS.register("u_glass1", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_2_ITEM = ITEMS.register("u_glass2", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_3_ITEM = ITEMS.register("u_glass3", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_4_ITEM = ITEMS.register("u_glass4", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_5_ITEM = ITEMS.register("u_glass5", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_6_ITEM = ITEMS.register("u_glass6", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_7_ITEM = ITEMS.register("u_glass7", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_8_ITEM = ITEMS.register("u_glass8", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_9_ITEM = ITEMS.register("u_glass9", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_10_ITEM = ITEMS.register("u_glass10", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_11_ITEM = ITEMS.register("u_glass11", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_12_ITEM = ITEMS.register("u_glass12", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_13_ITEM = ITEMS.register("u_glass13", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_14_ITEM = ITEMS.register("u_glass14", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_15_ITEM = ITEMS.register("u_glass15", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_16_ITEM = ITEMS.register("u_glass16", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_17_ITEM = ITEMS.register("u_glass17", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_18_ITEM = ITEMS.register("u_glass18", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_19_ITEM = ITEMS.register("u_glass19", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_GLASS_20_ITEM = ITEMS.register("u_glass20", () -> new DynamicBlockItem(ModBlocks.USER_GLASS_20.get(), new Item.Properties()));

    // Tinted glass items (1-20)
    public static final RegistryObject<Item> USER_TINTED_GLASS_1_ITEM = ITEMS.register("u_tinted_glass1", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_2_ITEM = ITEMS.register("u_tinted_glass2", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_3_ITEM = ITEMS.register("u_tinted_glass3", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_4_ITEM = ITEMS.register("u_tinted_glass4", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_5_ITEM = ITEMS.register("u_tinted_glass5", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_6_ITEM = ITEMS.register("u_tinted_glass6", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_7_ITEM = ITEMS.register("u_tinted_glass7", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_8_ITEM = ITEMS.register("u_tinted_glass8", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_9_ITEM = ITEMS.register("u_tinted_glass9", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_10_ITEM = ITEMS.register("u_tinted_glass10", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_11_ITEM = ITEMS.register("u_tinted_glass11", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_12_ITEM = ITEMS.register("u_tinted_glass12", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_13_ITEM = ITEMS.register("u_tinted_glass13", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_14_ITEM = ITEMS.register("u_tinted_glass14", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_15_ITEM = ITEMS.register("u_tinted_glass15", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_16_ITEM = ITEMS.register("u_tinted_glass16", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_17_ITEM = ITEMS.register("u_tinted_glass17", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_18_ITEM = ITEMS.register("u_tinted_glass18", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_19_ITEM = ITEMS.register("u_tinted_glass19", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_TINTED_GLASS_20_ITEM = ITEMS.register("u_tinted_glass20", () -> new DynamicBlockItem(ModBlocks.USER_TINTED_GLASS_20.get(), new Item.Properties()));

    // Stained Glass items (1-20)
    public static final RegistryObject<Item> USER_STAINED_GLASS_1_ITEM = ITEMS.register("u_stained_glass1", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_2_ITEM = ITEMS.register("u_stained_glass2", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_3_ITEM = ITEMS.register("u_stained_glass3", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_4_ITEM = ITEMS.register("u_stained_glass4", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_5_ITEM = ITEMS.register("u_stained_glass5", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_6_ITEM = ITEMS.register("u_stained_glass6", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_7_ITEM = ITEMS.register("u_stained_glass7", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_8_ITEM = ITEMS.register("u_stained_glass8", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_9_ITEM = ITEMS.register("u_stained_glass9", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_10_ITEM = ITEMS.register("u_stained_glass10", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_11_ITEM = ITEMS.register("u_stained_glass11", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_12_ITEM = ITEMS.register("u_stained_glass12", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_13_ITEM = ITEMS.register("u_stained_glass13", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_14_ITEM = ITEMS.register("u_stained_glass14", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_15_ITEM = ITEMS.register("u_stained_glass15", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_16_ITEM = ITEMS.register("u_stained_glass16", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_17_ITEM = ITEMS.register("u_stained_glass17", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_18_ITEM = ITEMS.register("u_stained_glass18", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_19_ITEM = ITEMS.register("u_stained_glass19", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_STAINED_GLASS_20_ITEM = ITEMS.register("u_stained_glass20", () -> new DynamicBlockItem(ModBlocks.USER_STAINED_GLASS_20.get(), new Item.Properties()));

    // Diorite items (1-20)
    public static final RegistryObject<Item> USER_DIORITE_1_ITEM = ITEMS.register("u_diorite1", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_2_ITEM = ITEMS.register("u_diorite2", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_3_ITEM = ITEMS.register("u_diorite3", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_4_ITEM = ITEMS.register("u_diorite4", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_5_ITEM = ITEMS.register("u_diorite5", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_6_ITEM = ITEMS.register("u_diorite6", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_7_ITEM = ITEMS.register("u_diorite7", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_8_ITEM = ITEMS.register("u_diorite8", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_9_ITEM = ITEMS.register("u_diorite9", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_10_ITEM = ITEMS.register("u_diorite10", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_11_ITEM = ITEMS.register("u_diorite11", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_12_ITEM = ITEMS.register("u_diorite12", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_13_ITEM = ITEMS.register("u_diorite13", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_14_ITEM = ITEMS.register("u_diorite14", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_15_ITEM = ITEMS.register("u_diorite15", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_16_ITEM = ITEMS.register("u_diorite16", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_17_ITEM = ITEMS.register("u_diorite17", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_18_ITEM = ITEMS.register("u_diorite18", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_19_ITEM = ITEMS.register("u_diorite19", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DIORITE_20_ITEM = ITEMS.register("u_diorite20", () -> new DynamicBlockItem(ModBlocks.USER_DIORITE_20.get(), new Item.Properties()));

    // Calcite items (1-20)
    public static final RegistryObject<Item> USER_CALCITE_1_ITEM = ITEMS.register("u_calcite1", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_2_ITEM = ITEMS.register("u_calcite2", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_3_ITEM = ITEMS.register("u_calcite3", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_4_ITEM = ITEMS.register("u_calcite4", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_5_ITEM = ITEMS.register("u_calcite5", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_6_ITEM = ITEMS.register("u_calcite6", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_7_ITEM = ITEMS.register("u_calcite7", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_8_ITEM = ITEMS.register("u_calcite8", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_9_ITEM = ITEMS.register("u_calcite9", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_10_ITEM = ITEMS.register("u_calcite10", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_11_ITEM = ITEMS.register("u_calcite11", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_12_ITEM = ITEMS.register("u_calcite12", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_13_ITEM = ITEMS.register("u_calcite13", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_14_ITEM = ITEMS.register("u_calcite14", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_15_ITEM = ITEMS.register("u_calcite15", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_16_ITEM = ITEMS.register("u_calcite16", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_17_ITEM = ITEMS.register("u_calcite17", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_18_ITEM = ITEMS.register("u_calcite18", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_19_ITEM = ITEMS.register("u_calcite19", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_CALCITE_20_ITEM = ITEMS.register("u_calcite20", () -> new DynamicBlockItem(ModBlocks.USER_CALCITE_20.get(), new Item.Properties()));

    // Mushroom Stem items (1-20)
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_1_ITEM = ITEMS.register("u_mushroom_stem1", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_2_ITEM = ITEMS.register("u_mushroom_stem2", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_3_ITEM = ITEMS.register("u_mushroom_stem3", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_4_ITEM = ITEMS.register("u_mushroom_stem4", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_5_ITEM = ITEMS.register("u_mushroom_stem5", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_6_ITEM = ITEMS.register("u_mushroom_stem6", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_7_ITEM = ITEMS.register("u_mushroom_stem7", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_8_ITEM = ITEMS.register("u_mushroom_stem8", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_9_ITEM = ITEMS.register("u_mushroom_stem9", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_10_ITEM = ITEMS.register("u_mushroom_stem10", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_11_ITEM = ITEMS.register("u_mushroom_stem11", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_12_ITEM = ITEMS.register("u_mushroom_stem12", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_13_ITEM = ITEMS.register("u_mushroom_stem13", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_14_ITEM = ITEMS.register("u_mushroom_stem14", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_15_ITEM = ITEMS.register("u_mushroom_stem15", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_16_ITEM = ITEMS.register("u_mushroom_stem16", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_17_ITEM = ITEMS.register("u_mushroom_stem17", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_18_ITEM = ITEMS.register("u_mushroom_stem18", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_19_ITEM = ITEMS.register("u_mushroom_stem19", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_MUSHROOM_STEM_20_ITEM = ITEMS.register("u_mushroom_stem20", () -> new DynamicBlockItem(ModBlocks.USER_MUSHROOM_STEM_20.get(), new Item.Properties()));

    // Dead Tube Coral items (1-20)
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_1_ITEM = ITEMS.register("u_dead_tube_coral1", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_2_ITEM = ITEMS.register("u_dead_tube_coral2", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_3_ITEM = ITEMS.register("u_dead_tube_coral3", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_4_ITEM = ITEMS.register("u_dead_tube_coral4", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_5_ITEM = ITEMS.register("u_dead_tube_coral5", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_6_ITEM = ITEMS.register("u_dead_tube_coral6", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_7_ITEM = ITEMS.register("u_dead_tube_coral7", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_8_ITEM = ITEMS.register("u_dead_tube_coral8", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_9_ITEM = ITEMS.register("u_dead_tube_coral9", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_10_ITEM = ITEMS.register("u_dead_tube_coral10", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_11_ITEM = ITEMS.register("u_dead_tube_coral11", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_12_ITEM = ITEMS.register("u_dead_tube_coral12", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_13_ITEM = ITEMS.register("u_dead_tube_coral13", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_14_ITEM = ITEMS.register("u_dead_tube_coral14", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_15_ITEM = ITEMS.register("u_dead_tube_coral15", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_16_ITEM = ITEMS.register("u_dead_tube_coral16", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_17_ITEM = ITEMS.register("u_dead_tube_coral17", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_18_ITEM = ITEMS.register("u_dead_tube_coral18", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_19_ITEM = ITEMS.register("u_dead_tube_coral19", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_DEAD_TUBE_CORAL_20_ITEM = ITEMS.register("u_dead_tube_coral20", () -> new DynamicBlockItem(ModBlocks.USER_DEAD_TUBE_CORAL_20.get(), new Item.Properties()));

    // Pearlescent Froglight items (1-20)
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_1_ITEM = ITEMS.register("u_pearlescent_froglight1", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_1.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_2_ITEM = ITEMS.register("u_pearlescent_froglight2", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_2.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_3_ITEM = ITEMS.register("u_pearlescent_froglight3", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_3.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_4_ITEM = ITEMS.register("u_pearlescent_froglight4", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_4.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_5_ITEM = ITEMS.register("u_pearlescent_froglight5", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_5.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_6_ITEM = ITEMS.register("u_pearlescent_froglight6", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_6.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_7_ITEM = ITEMS.register("u_pearlescent_froglight7", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_7.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_8_ITEM = ITEMS.register("u_pearlescent_froglight8", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_8.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_9_ITEM = ITEMS.register("u_pearlescent_froglight9", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_9.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_10_ITEM = ITEMS.register("u_pearlescent_froglight10", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_10.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_11_ITEM = ITEMS.register("u_pearlescent_froglight11", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_11.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_12_ITEM = ITEMS.register("u_pearlescent_froglight12", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_12.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_13_ITEM = ITEMS.register("u_pearlescent_froglight13", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_13.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_14_ITEM = ITEMS.register("u_pearlescent_froglight14", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_14.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_15_ITEM = ITEMS.register("u_pearlescent_froglight15", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_15.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_16_ITEM = ITEMS.register("u_pearlescent_froglight16", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_16.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_17_ITEM = ITEMS.register("u_pearlescent_froglight17", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_17.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_18_ITEM = ITEMS.register("u_pearlescent_froglight18", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_18.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_19_ITEM = ITEMS.register("u_pearlescent_froglight19", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_19.get(), new Item.Properties()));
    public static final RegistryObject<Item> USER_PEARLESCENT_FROGLIGHT_20_ITEM = ITEMS.register("u_pearlescent_froglight20", () -> new DynamicBlockItem(ModBlocks.USER_PEARLESCENT_FROGLIGHT_20.get(), new Item.Properties()));
}