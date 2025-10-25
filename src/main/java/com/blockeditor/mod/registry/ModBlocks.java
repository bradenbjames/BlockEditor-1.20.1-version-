package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.content.DynamicBlock;
import com.blockeditor.mod.content.UserBlock;
import com.blockeditor.mod.content.TransparentDynamicBlock;
import com.blockeditor.mod.content.TintedDynamicBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, BlockEditorMod.MOD_ID);

    // Original block (stone texture)
    public static final RegistryObject<Block> DYNAMIC_BLOCK = BLOCKS.register("dynamic_block",
        () -> new DynamicBlock());

    // Additional blocks for different textures
    public static final RegistryObject<Block> DYNAMIC_BLOCK_DIRT = BLOCKS.register("dynamic_block_dirt",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_SAND = BLOCKS.register("dynamic_block_sand",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_WOOL = BLOCKS.register("dynamic_block_wool",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_CONCRETE = BLOCKS.register("dynamic_block_concrete",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_DEEPSLATE = BLOCKS.register("dynamic_block_deepslate",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_WOOD = BLOCKS.register("dynamic_block_wood",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_STONE = BLOCKS.register("dynamic_block_stone",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_COBBLESTONE = BLOCKS.register("dynamic_block_cobblestone",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_SMOOTH_STONE = BLOCKS.register("dynamic_block_smooth_stone",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_TERRACOTTA = BLOCKS.register("dynamic_block_terracotta",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_CONCRETE_POWDER = BLOCKS.register("dynamic_block_concrete_powder",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_GLASS = BLOCKS.register("dynamic_block_glass",
        TransparentDynamicBlock::new);

    public static final RegistryObject<Block> DYNAMIC_BLOCK_TINTED_GLASS = BLOCKS.register("dynamic_block_tinted_glass",
        TintedDynamicBlock::new);

    public static final RegistryObject<Block> DYNAMIC_BLOCK_STAINED_GLASS = BLOCKS.register("dynamic_block_stained_glass",
        TranslucentDynamicBlock::new);

    public static final RegistryObject<Block> DYNAMIC_BLOCK_DIORITE = BLOCKS.register("dynamic_block_diorite",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_CALCITE = BLOCKS.register("dynamic_block_calcite",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_MUSHROOM_STEM = BLOCKS.register("dynamic_block_mushroom_stem",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_DEAD_TUBE_CORAL = BLOCKS.register("dynamic_block_dead_tube_coral",
        () -> new DynamicBlock());

    public static final RegistryObject<Block> DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT = BLOCKS.register("dynamic_block_pearlescent_froglight",
        () -> new DynamicBlock());

    // User-created block variants for WorldEdit autocomplete (numbered system)
    // Wool blocks (up to 20 custom wool blocks)
    public static final RegistryObject<Block> USER_WOOL_1 = BLOCKS.register("u_wool1", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_2 = BLOCKS.register("u_wool2", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_3 = BLOCKS.register("u_wool3", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_4 = BLOCKS.register("u_wool4", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_5 = BLOCKS.register("u_wool5", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_6 = BLOCKS.register("u_wool6", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_7 = BLOCKS.register("u_wool7", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_8 = BLOCKS.register("u_wool8", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_9 = BLOCKS.register("u_wool9", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_10 = BLOCKS.register("u_wool10", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_11 = BLOCKS.register("u_wool11", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_12 = BLOCKS.register("u_wool12", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_13 = BLOCKS.register("u_wool13", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_14 = BLOCKS.register("u_wool14", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_15 = BLOCKS.register("u_wool15", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_16 = BLOCKS.register("u_wool16", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_17 = BLOCKS.register("u_wool17", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_18 = BLOCKS.register("u_wool18", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_19 = BLOCKS.register("u_wool19", () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_20 = BLOCKS.register("u_wool20", () -> new UserBlock("wool"));
    
    // Stone blocks (up to 20 custom stone blocks)
    public static final RegistryObject<Block> USER_STONE_1 = BLOCKS.register("u_stone1", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_2 = BLOCKS.register("u_stone2", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_3 = BLOCKS.register("u_stone3", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_4 = BLOCKS.register("u_stone4", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_5 = BLOCKS.register("u_stone5", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_6 = BLOCKS.register("u_stone6", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_7 = BLOCKS.register("u_stone7", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_8 = BLOCKS.register("u_stone8", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_9 = BLOCKS.register("u_stone9", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_10 = BLOCKS.register("u_stone10", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_11 = BLOCKS.register("u_stone11", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_12 = BLOCKS.register("u_stone12", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_13 = BLOCKS.register("u_stone13", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_14 = BLOCKS.register("u_stone14", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_15 = BLOCKS.register("u_stone15", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_16 = BLOCKS.register("u_stone16", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_17 = BLOCKS.register("u_stone17", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_18 = BLOCKS.register("u_stone18", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_19 = BLOCKS.register("u_stone19", () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_20 = BLOCKS.register("u_stone20", () -> new UserBlock("stone"));
    
    // Concrete blocks (up to 20 custom concrete blocks)
    public static final RegistryObject<Block> USER_CONCRETE_1 = BLOCKS.register("u_concrete1", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_2 = BLOCKS.register("u_concrete2", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_3 = BLOCKS.register("u_concrete3", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_4 = BLOCKS.register("u_concrete4", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_5 = BLOCKS.register("u_concrete5", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_6 = BLOCKS.register("u_concrete6", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_7 = BLOCKS.register("u_concrete7", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_8 = BLOCKS.register("u_concrete8", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_9 = BLOCKS.register("u_concrete9", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_10 = BLOCKS.register("u_concrete10", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_11 = BLOCKS.register("u_concrete11", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_12 = BLOCKS.register("u_concrete12", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_13 = BLOCKS.register("u_concrete13", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_14 = BLOCKS.register("u_concrete14", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_15 = BLOCKS.register("u_concrete15", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_16 = BLOCKS.register("u_concrete16", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_17 = BLOCKS.register("u_concrete17", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_18 = BLOCKS.register("u_concrete18", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_19 = BLOCKS.register("u_concrete19", () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_20 = BLOCKS.register("u_concrete20", () -> new UserBlock("concrete"));
    
    // Wood blocks (up to 5 custom wood blocks)
    public static final RegistryObject<Block> USER_WOOD_1 = BLOCKS.register("u_wood1", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_2 = BLOCKS.register("u_wood2", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_3 = BLOCKS.register("u_wood3", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_4 = BLOCKS.register("u_wood4", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_5 = BLOCKS.register("u_wood5", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_6 = BLOCKS.register("u_wood6", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_7 = BLOCKS.register("u_wood7", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_8 = BLOCKS.register("u_wood8", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_9 = BLOCKS.register("u_wood9", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_10 = BLOCKS.register("u_wood10", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_11 = BLOCKS.register("u_wood11", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_12 = BLOCKS.register("u_wood12", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_13 = BLOCKS.register("u_wood13", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_14 = BLOCKS.register("u_wood14", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_15 = BLOCKS.register("u_wood15", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_16 = BLOCKS.register("u_wood16", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_17 = BLOCKS.register("u_wood17", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_18 = BLOCKS.register("u_wood18", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_19 = BLOCKS.register("u_wood19", () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_20 = BLOCKS.register("u_wood20", () -> new UserBlock("wood"));
    
    // Dirt blocks (up to 20 custom dirt blocks)
    public static final RegistryObject<Block> USER_DIRT_1 = BLOCKS.register("u_dirt1", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_2 = BLOCKS.register("u_dirt2", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_3 = BLOCKS.register("u_dirt3", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_4 = BLOCKS.register("u_dirt4", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_5 = BLOCKS.register("u_dirt5", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_6 = BLOCKS.register("u_dirt6", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_7 = BLOCKS.register("u_dirt7", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_8 = BLOCKS.register("u_dirt8", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_9 = BLOCKS.register("u_dirt9", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_10 = BLOCKS.register("u_dirt10", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_11 = BLOCKS.register("u_dirt11", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_12 = BLOCKS.register("u_dirt12", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_13 = BLOCKS.register("u_dirt13", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_14 = BLOCKS.register("u_dirt14", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_15 = BLOCKS.register("u_dirt15", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_16 = BLOCKS.register("u_dirt16", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_17 = BLOCKS.register("u_dirt17", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_18 = BLOCKS.register("u_dirt18", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_19 = BLOCKS.register("u_dirt19", () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_20 = BLOCKS.register("u_dirt20", () -> new UserBlock("dirt"));
    
    // Sand blocks (up to 20 custom sand blocks)
    public static final RegistryObject<Block> USER_SAND_1 = BLOCKS.register("u_sand1", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_2 = BLOCKS.register("u_sand2", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_3 = BLOCKS.register("u_sand3", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_4 = BLOCKS.register("u_sand4", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_5 = BLOCKS.register("u_sand5", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_6 = BLOCKS.register("u_sand6", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_7 = BLOCKS.register("u_sand7", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_8 = BLOCKS.register("u_sand8", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_9 = BLOCKS.register("u_sand9", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_10 = BLOCKS.register("u_sand10", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_11 = BLOCKS.register("u_sand11", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_12 = BLOCKS.register("u_sand12", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_13 = BLOCKS.register("u_sand13", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_14 = BLOCKS.register("u_sand14", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_15 = BLOCKS.register("u_sand15", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_16 = BLOCKS.register("u_sand16", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_17 = BLOCKS.register("u_sand17", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_18 = BLOCKS.register("u_sand18", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_19 = BLOCKS.register("u_sand19", () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_20 = BLOCKS.register("u_sand20", () -> new UserBlock("sand"));
    
    // Deepslate blocks (up to 20 custom deepslate blocks)
    public static final RegistryObject<Block> USER_DEEPSLATE_1 = BLOCKS.register("u_deepslate1", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_2 = BLOCKS.register("u_deepslate2", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_3 = BLOCKS.register("u_deepslate3", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_4 = BLOCKS.register("u_deepslate4", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_5 = BLOCKS.register("u_deepslate5", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_6 = BLOCKS.register("u_deepslate6", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_7 = BLOCKS.register("u_deepslate7", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_8 = BLOCKS.register("u_deepslate8", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_9 = BLOCKS.register("u_deepslate9", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_10 = BLOCKS.register("u_deepslate10", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_11 = BLOCKS.register("u_deepslate11", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_12 = BLOCKS.register("u_deepslate12", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_13 = BLOCKS.register("u_deepslate13", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_14 = BLOCKS.register("u_deepslate14", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_15 = BLOCKS.register("u_deepslate15", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_16 = BLOCKS.register("u_deepslate16", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_17 = BLOCKS.register("u_deepslate17", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_18 = BLOCKS.register("u_deepslate18", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_19 = BLOCKS.register("u_deepslate19", () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_20 = BLOCKS.register("u_deepslate20", () -> new UserBlock("deepslate"));
    
    // Cobblestone blocks (up to 20 custom cobblestone blocks)
    public static final RegistryObject<Block> USER_COBBLESTONE_1 = BLOCKS.register("u_cobblestone1", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_2 = BLOCKS.register("u_cobblestone2", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_3 = BLOCKS.register("u_cobblestone3", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_4 = BLOCKS.register("u_cobblestone4", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_5 = BLOCKS.register("u_cobblestone5", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_6 = BLOCKS.register("u_cobblestone6", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_7 = BLOCKS.register("u_cobblestone7", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_8 = BLOCKS.register("u_cobblestone8", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_9 = BLOCKS.register("u_cobblestone9", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_10 = BLOCKS.register("u_cobblestone10", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_11 = BLOCKS.register("u_cobblestone11", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_12 = BLOCKS.register("u_cobblestone12", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_13 = BLOCKS.register("u_cobblestone13", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_14 = BLOCKS.register("u_cobblestone14", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_15 = BLOCKS.register("u_cobblestone15", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_16 = BLOCKS.register("u_cobblestone16", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_17 = BLOCKS.register("u_cobblestone17", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_18 = BLOCKS.register("u_cobblestone18", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_19 = BLOCKS.register("u_cobblestone19", () -> new UserBlock("cobblestone"));
    public static final RegistryObject<Block> USER_COBBLESTONE_20 = BLOCKS.register("u_cobblestone20", () -> new UserBlock("cobblestone"));
    
    // Smooth Stone blocks (up to 20 custom smooth stone blocks)
    public static final RegistryObject<Block> USER_SMOOTH_STONE_1 = BLOCKS.register("u_smooth_stone1", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_2 = BLOCKS.register("u_smooth_stone2", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_3 = BLOCKS.register("u_smooth_stone3", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_4 = BLOCKS.register("u_smooth_stone4", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_5 = BLOCKS.register("u_smooth_stone5", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_6 = BLOCKS.register("u_smooth_stone6", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_7 = BLOCKS.register("u_smooth_stone7", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_8 = BLOCKS.register("u_smooth_stone8", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_9 = BLOCKS.register("u_smooth_stone9", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_10 = BLOCKS.register("u_smooth_stone10", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_11 = BLOCKS.register("u_smooth_stone11", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_12 = BLOCKS.register("u_smooth_stone12", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_13 = BLOCKS.register("u_smooth_stone13", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_14 = BLOCKS.register("u_smooth_stone14", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_15 = BLOCKS.register("u_smooth_stone15", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_16 = BLOCKS.register("u_smooth_stone16", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_17 = BLOCKS.register("u_smooth_stone17", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_18 = BLOCKS.register("u_smooth_stone18", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_19 = BLOCKS.register("u_smooth_stone19", () -> new UserBlock("smooth_stone"));
    public static final RegistryObject<Block> USER_SMOOTH_STONE_20 = BLOCKS.register("u_smooth_stone20", () -> new UserBlock("smooth_stone"));
    
    // Terracotta blocks (up to 20 custom terracotta blocks)
    public static final RegistryObject<Block> USER_TERRACOTTA_1 = BLOCKS.register("u_terracotta1", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_2 = BLOCKS.register("u_terracotta2", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_3 = BLOCKS.register("u_terracotta3", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_4 = BLOCKS.register("u_terracotta4", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_5 = BLOCKS.register("u_terracotta5", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_6 = BLOCKS.register("u_terracotta6", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_7 = BLOCKS.register("u_terracotta7", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_8 = BLOCKS.register("u_terracotta8", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_9 = BLOCKS.register("u_terracotta9", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_10 = BLOCKS.register("u_terracotta10", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_11 = BLOCKS.register("u_terracotta11", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_12 = BLOCKS.register("u_terracotta12", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_13 = BLOCKS.register("u_terracotta13", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_14 = BLOCKS.register("u_terracotta14", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_15 = BLOCKS.register("u_terracotta15", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_16 = BLOCKS.register("u_terracotta16", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_17 = BLOCKS.register("u_terracotta17", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_18 = BLOCKS.register("u_terracotta18", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_19 = BLOCKS.register("u_terracotta19", () -> new UserBlock("terracotta"));
    public static final RegistryObject<Block> USER_TERRACOTTA_20 = BLOCKS.register("u_terracotta20", () -> new UserBlock("terracotta"));
    
    // Concrete Powder blocks (up to 20 custom concrete powder blocks)
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_1 = BLOCKS.register("u_concrete_powder1", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_2 = BLOCKS.register("u_concrete_powder2", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_3 = BLOCKS.register("u_concrete_powder3", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_4 = BLOCKS.register("u_concrete_powder4", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_5 = BLOCKS.register("u_concrete_powder5", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_6 = BLOCKS.register("u_concrete_powder6", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_7 = BLOCKS.register("u_concrete_powder7", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_8 = BLOCKS.register("u_concrete_powder8", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_9 = BLOCKS.register("u_concrete_powder9", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_10 = BLOCKS.register("u_concrete_powder10", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_11 = BLOCKS.register("u_concrete_powder11", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_12 = BLOCKS.register("u_concrete_powder12", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_13 = BLOCKS.register("u_concrete_powder13", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_14 = BLOCKS.register("u_concrete_powder14", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_15 = BLOCKS.register("u_concrete_powder15", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_16 = BLOCKS.register("u_concrete_powder16", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_17 = BLOCKS.register("u_concrete_powder17", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_18 = BLOCKS.register("u_concrete_powder18", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_19 = BLOCKS.register("u_concrete_powder19", () -> new UserBlock("concrete_powder"));
    public static final RegistryObject<Block> USER_CONCRETE_POWDER_20 = BLOCKS.register("u_concrete_powder20", () -> new UserBlock("concrete_powder"));
    
    // Glass blocks (up to 20 custom glass blocks)
    public static final RegistryObject<Block> USER_GLASS_1 = BLOCKS.register("u_glass1", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_2 = BLOCKS.register("u_glass2", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_3 = BLOCKS.register("u_glass3", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_4 = BLOCKS.register("u_glass4", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_5 = BLOCKS.register("u_glass5", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_6 = BLOCKS.register("u_glass6", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_7 = BLOCKS.register("u_glass7", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_8 = BLOCKS.register("u_glass8", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_9 = BLOCKS.register("u_glass9", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_10 = BLOCKS.register("u_glass10", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_11 = BLOCKS.register("u_glass11", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_12 = BLOCKS.register("u_glass12", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_13 = BLOCKS.register("u_glass13", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_14 = BLOCKS.register("u_glass14", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_15 = BLOCKS.register("u_glass15", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_16 = BLOCKS.register("u_glass16", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_17 = BLOCKS.register("u_glass17", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_18 = BLOCKS.register("u_glass18", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_19 = BLOCKS.register("u_glass19", () -> new UserBlock("glass"));
    public static final RegistryObject<Block> USER_GLASS_20 = BLOCKS.register("u_glass20", () -> new UserBlock("glass"));

    // Tinted glass blocks (1-20)
    public static final RegistryObject<Block> USER_TINTED_GLASS_1 = BLOCKS.register("u_tinted_glass1", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_2 = BLOCKS.register("u_tinted_glass2", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_3 = BLOCKS.register("u_tinted_glass3", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_4 = BLOCKS.register("u_tinted_glass4", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_5 = BLOCKS.register("u_tinted_glass5", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_6 = BLOCKS.register("u_tinted_glass6", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_7 = BLOCKS.register("u_tinted_glass7", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_8 = BLOCKS.register("u_tinted_glass8", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_9 = BLOCKS.register("u_tinted_glass9", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_10 = BLOCKS.register("u_tinted_glass10", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_11 = BLOCKS.register("u_tinted_glass11", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_12 = BLOCKS.register("u_tinted_glass12", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_13 = BLOCKS.register("u_tinted_glass13", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_14 = BLOCKS.register("u_tinted_glass14", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_15 = BLOCKS.register("u_tinted_glass15", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_16 = BLOCKS.register("u_tinted_glass16", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_17 = BLOCKS.register("u_tinted_glass17", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_18 = BLOCKS.register("u_tinted_glass18", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_19 = BLOCKS.register("u_tinted_glass19", () -> new UserBlock("tinted_glass"));
    public static final RegistryObject<Block> USER_TINTED_GLASS_20 = BLOCKS.register("u_tinted_glass20", () -> new UserBlock("tinted_glass"));
    
    // Stained Glass blocks (1-20)
    public static final RegistryObject<Block> USER_STAINED_GLASS_1 = BLOCKS.register("u_stained_glass1", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_2 = BLOCKS.register("u_stained_glass2", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_3 = BLOCKS.register("u_stained_glass3", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_4 = BLOCKS.register("u_stained_glass4", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_5 = BLOCKS.register("u_stained_glass5", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_6 = BLOCKS.register("u_stained_glass6", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_7 = BLOCKS.register("u_stained_glass7", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_8 = BLOCKS.register("u_stained_glass8", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_9 = BLOCKS.register("u_stained_glass9", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_10 = BLOCKS.register("u_stained_glass10", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_11 = BLOCKS.register("u_stained_glass11", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_12 = BLOCKS.register("u_stained_glass12", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_13 = BLOCKS.register("u_stained_glass13", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_14 = BLOCKS.register("u_stained_glass14", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_15 = BLOCKS.register("u_stained_glass15", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_16 = BLOCKS.register("u_stained_glass16", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_17 = BLOCKS.register("u_stained_glass17", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_18 = BLOCKS.register("u_stained_glass18", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_19 = BLOCKS.register("u_stained_glass19", () -> new UserBlock("stained_glass"));
    public static final RegistryObject<Block> USER_STAINED_GLASS_20 = BLOCKS.register("u_stained_glass20", () -> new UserBlock("stained_glass"));
    
    // Diorite blocks (up to 20 custom diorite blocks)
    public static final RegistryObject<Block> USER_DIORITE_1 = BLOCKS.register("u_diorite1", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_2 = BLOCKS.register("u_diorite2", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_3 = BLOCKS.register("u_diorite3", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_4 = BLOCKS.register("u_diorite4", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_5 = BLOCKS.register("u_diorite5", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_6 = BLOCKS.register("u_diorite6", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_7 = BLOCKS.register("u_diorite7", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_8 = BLOCKS.register("u_diorite8", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_9 = BLOCKS.register("u_diorite9", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_10 = BLOCKS.register("u_diorite10", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_11 = BLOCKS.register("u_diorite11", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_12 = BLOCKS.register("u_diorite12", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_13 = BLOCKS.register("u_diorite13", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_14 = BLOCKS.register("u_diorite14", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_15 = BLOCKS.register("u_diorite15", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_16 = BLOCKS.register("u_diorite16", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_17 = BLOCKS.register("u_diorite17", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_18 = BLOCKS.register("u_diorite18", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_19 = BLOCKS.register("u_diorite19", () -> new UserBlock("diorite"));
    public static final RegistryObject<Block> USER_DIORITE_20 = BLOCKS.register("u_diorite20", () -> new UserBlock("diorite"));
    
    // Calcite blocks (up to 20 custom calcite blocks)
    public static final RegistryObject<Block> USER_CALCITE_1 = BLOCKS.register("u_calcite1", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_2 = BLOCKS.register("u_calcite2", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_3 = BLOCKS.register("u_calcite3", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_4 = BLOCKS.register("u_calcite4", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_5 = BLOCKS.register("u_calcite5", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_6 = BLOCKS.register("u_calcite6", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_7 = BLOCKS.register("u_calcite7", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_8 = BLOCKS.register("u_calcite8", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_9 = BLOCKS.register("u_calcite9", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_10 = BLOCKS.register("u_calcite10", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_11 = BLOCKS.register("u_calcite11", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_12 = BLOCKS.register("u_calcite12", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_13 = BLOCKS.register("u_calcite13", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_14 = BLOCKS.register("u_calcite14", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_15 = BLOCKS.register("u_calcite15", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_16 = BLOCKS.register("u_calcite16", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_17 = BLOCKS.register("u_calcite17", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_18 = BLOCKS.register("u_calcite18", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_19 = BLOCKS.register("u_calcite19", () -> new UserBlock("calcite"));
    public static final RegistryObject<Block> USER_CALCITE_20 = BLOCKS.register("u_calcite20", () -> new UserBlock("calcite"));
    
    // Mushroom Stem blocks (up to 20 custom mushroom stem blocks)
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_1 = BLOCKS.register("u_mushroom_stem1", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_2 = BLOCKS.register("u_mushroom_stem2", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_3 = BLOCKS.register("u_mushroom_stem3", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_4 = BLOCKS.register("u_mushroom_stem4", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_5 = BLOCKS.register("u_mushroom_stem5", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_6 = BLOCKS.register("u_mushroom_stem6", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_7 = BLOCKS.register("u_mushroom_stem7", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_8 = BLOCKS.register("u_mushroom_stem8", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_9 = BLOCKS.register("u_mushroom_stem9", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_10 = BLOCKS.register("u_mushroom_stem10", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_11 = BLOCKS.register("u_mushroom_stem11", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_12 = BLOCKS.register("u_mushroom_stem12", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_13 = BLOCKS.register("u_mushroom_stem13", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_14 = BLOCKS.register("u_mushroom_stem14", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_15 = BLOCKS.register("u_mushroom_stem15", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_16 = BLOCKS.register("u_mushroom_stem16", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_17 = BLOCKS.register("u_mushroom_stem17", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_18 = BLOCKS.register("u_mushroom_stem18", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_19 = BLOCKS.register("u_mushroom_stem19", () -> new UserBlock("mushroom_stem"));
    public static final RegistryObject<Block> USER_MUSHROOM_STEM_20 = BLOCKS.register("u_mushroom_stem20", () -> new UserBlock("mushroom_stem"));
    
    // Dead Tube Coral blocks (up to 20 custom dead tube coral blocks)
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_1 = BLOCKS.register("u_dead_tube_coral1", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_2 = BLOCKS.register("u_dead_tube_coral2", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_3 = BLOCKS.register("u_dead_tube_coral3", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_4 = BLOCKS.register("u_dead_tube_coral4", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_5 = BLOCKS.register("u_dead_tube_coral5", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_6 = BLOCKS.register("u_dead_tube_coral6", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_7 = BLOCKS.register("u_dead_tube_coral7", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_8 = BLOCKS.register("u_dead_tube_coral8", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_9 = BLOCKS.register("u_dead_tube_coral9", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_10 = BLOCKS.register("u_dead_tube_coral10", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_11 = BLOCKS.register("u_dead_tube_coral11", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_12 = BLOCKS.register("u_dead_tube_coral12", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_13 = BLOCKS.register("u_dead_tube_coral13", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_14 = BLOCKS.register("u_dead_tube_coral14", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_15 = BLOCKS.register("u_dead_tube_coral15", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_16 = BLOCKS.register("u_dead_tube_coral16", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_17 = BLOCKS.register("u_dead_tube_coral17", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_18 = BLOCKS.register("u_dead_tube_coral18", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_19 = BLOCKS.register("u_dead_tube_coral19", () -> new UserBlock("dead_tube_coral"));
    public static final RegistryObject<Block> USER_DEAD_TUBE_CORAL_20 = BLOCKS.register("u_dead_tube_coral20", () -> new UserBlock("dead_tube_coral"));
    
    // Pearlescent Froglight blocks (up to 20 custom pearlescent froglight blocks)
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_1 = BLOCKS.register("u_pearlescent_froglight1", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_2 = BLOCKS.register("u_pearlescent_froglight2", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_3 = BLOCKS.register("u_pearlescent_froglight3", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_4 = BLOCKS.register("u_pearlescent_froglight4", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_5 = BLOCKS.register("u_pearlescent_froglight5", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_6 = BLOCKS.register("u_pearlescent_froglight6", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_7 = BLOCKS.register("u_pearlescent_froglight7", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_8 = BLOCKS.register("u_pearlescent_froglight8", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_9 = BLOCKS.register("u_pearlescent_froglight9", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_10 = BLOCKS.register("u_pearlescent_froglight10", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_11 = BLOCKS.register("u_pearlescent_froglight11", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_12 = BLOCKS.register("u_pearlescent_froglight12", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_13 = BLOCKS.register("u_pearlescent_froglight13", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_14 = BLOCKS.register("u_pearlescent_froglight14", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_15 = BLOCKS.register("u_pearlescent_froglight15", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_16 = BLOCKS.register("u_pearlescent_froglight16", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_17 = BLOCKS.register("u_pearlescent_froglight17", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_18 = BLOCKS.register("u_pearlescent_froglight18", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_19 = BLOCKS.register("u_pearlescent_froglight19", () -> new UserBlock("pearlescent_froglight"));
    public static final RegistryObject<Block> USER_PEARLESCENT_FROGLIGHT_20 = BLOCKS.register("u_pearlescent_froglight20", () -> new UserBlock("pearlescent_froglight"));
}