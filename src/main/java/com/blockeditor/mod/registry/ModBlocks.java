package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import com.blockeditor.mod.content.DynamicBlock;
import com.blockeditor.mod.content.UserBlock;
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

    // User-created block variants for WorldEdit autocomplete (numbered system)
    // Wool blocks (up to 10 custom wool blocks)
    public static final RegistryObject<Block> USER_WOOL_1 = BLOCKS.register("user_wool1",
        () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_2 = BLOCKS.register("user_wool2",
        () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_3 = BLOCKS.register("user_wool3",
        () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_4 = BLOCKS.register("user_wool4",
        () -> new UserBlock("wool"));
    public static final RegistryObject<Block> USER_WOOL_5 = BLOCKS.register("user_wool5",
        () -> new UserBlock("wool"));
    
    // Stone blocks (up to 10 custom stone blocks)
    public static final RegistryObject<Block> USER_STONE_1 = BLOCKS.register("user_stone1",
        () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_2 = BLOCKS.register("user_stone2",
        () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_3 = BLOCKS.register("user_stone3",
        () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_4 = BLOCKS.register("user_stone4",
        () -> new UserBlock("stone"));
    public static final RegistryObject<Block> USER_STONE_5 = BLOCKS.register("user_stone5",
        () -> new UserBlock("stone"));
    
    // Concrete blocks (up to 10 custom concrete blocks)
    public static final RegistryObject<Block> USER_CONCRETE_1 = BLOCKS.register("user_concrete1",
        () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_2 = BLOCKS.register("user_concrete2",
        () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_3 = BLOCKS.register("user_concrete3",
        () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_4 = BLOCKS.register("user_concrete4",
        () -> new UserBlock("concrete"));
    public static final RegistryObject<Block> USER_CONCRETE_5 = BLOCKS.register("user_concrete5",
        () -> new UserBlock("concrete"));
    
    // Wood blocks (up to 5 custom wood blocks)
    public static final RegistryObject<Block> USER_WOOD_1 = BLOCKS.register("user_wood1",
        () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_2 = BLOCKS.register("user_wood2",
        () -> new UserBlock("wood"));
    public static final RegistryObject<Block> USER_WOOD_3 = BLOCKS.register("user_wood3",
        () -> new UserBlock("wood"));
    
    // Dirt blocks (up to 5 custom dirt blocks)
    public static final RegistryObject<Block> USER_DIRT_1 = BLOCKS.register("user_dirt1",
        () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_2 = BLOCKS.register("user_dirt2",
        () -> new UserBlock("dirt"));
    public static final RegistryObject<Block> USER_DIRT_3 = BLOCKS.register("user_dirt3",
        () -> new UserBlock("dirt"));
    
    // Sand blocks (up to 5 custom sand blocks)
    public static final RegistryObject<Block> USER_SAND_1 = BLOCKS.register("user_sand1",
        () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_2 = BLOCKS.register("user_sand2",
        () -> new UserBlock("sand"));
    public static final RegistryObject<Block> USER_SAND_3 = BLOCKS.register("user_sand3",
        () -> new UserBlock("sand"));
    
    // Deepslate blocks (up to 3 custom deepslate blocks)
    public static final RegistryObject<Block> USER_DEEPSLATE_1 = BLOCKS.register("user_deepslate1",
        () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_2 = BLOCKS.register("user_deepslate2",
        () -> new UserBlock("deepslate"));
    public static final RegistryObject<Block> USER_DEEPSLATE_3 = BLOCKS.register("user_deepslate3",
        () -> new UserBlock("deepslate"));
}