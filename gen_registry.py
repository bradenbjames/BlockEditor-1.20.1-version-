import os

base = r'c:\Users\brade\Desktop\BlockEditorMod\src\main\java\com\blockeditor\mod\registry'

DYNAMIC = [
    ('DYNAMIC_BLOCK_ITEM','dynamic_block','DYNAMIC_BLOCK'),
    ('DYNAMIC_BLOCK_DIRT_ITEM','dynamic_block_dirt','DYNAMIC_BLOCK_DIRT'),
    ('DYNAMIC_BLOCK_SAND_ITEM','dynamic_block_sand','DYNAMIC_BLOCK_SAND'),
    ('DYNAMIC_BLOCK_WOOL_ITEM','dynamic_block_wool','DYNAMIC_BLOCK_WOOL'),
    ('DYNAMIC_BLOCK_CONCRETE_ITEM','dynamic_block_concrete','DYNAMIC_BLOCK_CONCRETE'),
    ('DYNAMIC_BLOCK_DEEPSLATE_ITEM','dynamic_block_deepslate','DYNAMIC_BLOCK_DEEPSLATE'),
    ('DYNAMIC_BLOCK_WOOD_ITEM','dynamic_block_wood','DYNAMIC_BLOCK_WOOD'),
    ('DYNAMIC_BLOCK_STONE_ITEM','dynamic_block_stone','DYNAMIC_BLOCK_STONE'),
    ('DYNAMIC_BLOCK_COBBLESTONE_ITEM','dynamic_block_cobblestone','DYNAMIC_BLOCK_COBBLESTONE'),
    ('DYNAMIC_BLOCK_SMOOTH_STONE_ITEM','dynamic_block_smooth_stone','DYNAMIC_BLOCK_SMOOTH_STONE'),
    ('DYNAMIC_BLOCK_TERRACOTTA_ITEM','dynamic_block_terracotta','DYNAMIC_BLOCK_TERRACOTTA'),
    ('DYNAMIC_BLOCK_CONCRETE_POWDER_ITEM','dynamic_block_concrete_powder','DYNAMIC_BLOCK_CONCRETE_POWDER'),
    ('DYNAMIC_BLOCK_GLASS_ITEM','dynamic_block_glass','DYNAMIC_BLOCK_GLASS'),
    ('DYNAMIC_BLOCK_TINTED_GLASS_ITEM','dynamic_block_tinted_glass','DYNAMIC_BLOCK_TINTED_GLASS'),
    ('DYNAMIC_BLOCK_STAINED_GLASS_ITEM','dynamic_block_stained_glass','DYNAMIC_BLOCK_STAINED_GLASS'),
    ('DYNAMIC_BLOCK_DIORITE_ITEM','dynamic_block_diorite','DYNAMIC_BLOCK_DIORITE'),
    ('DYNAMIC_BLOCK_CALCITE_ITEM','dynamic_block_calcite','DYNAMIC_BLOCK_CALCITE'),
    ('DYNAMIC_BLOCK_MUSHROOM_STEM_ITEM','dynamic_block_mushroom_stem','DYNAMIC_BLOCK_MUSHROOM_STEM'),
    ('DYNAMIC_BLOCK_DEAD_TUBE_CORAL_ITEM','dynamic_block_dead_tube_coral','DYNAMIC_BLOCK_DEAD_TUBE_CORAL'),
    ('DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT_ITEM','dynamic_block_pearlescent_froglight','DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT'),
]

CATS = [
    ('USER_WOOL','u_wool','USER_WOOL'),
    ('USER_STONE','u_stone','USER_STONE'),
    ('USER_CONCRETE','u_concrete','USER_CONCRETE'),
    ('USER_WOOD','u_wood','USER_WOOD'),
    ('USER_DIRT','u_dirt','USER_DIRT'),
    ('USER_SAND','u_sand','USER_SAND'),
    ('USER_DEEPSLATE','u_deepslate','USER_DEEPSLATE'),
    ('USER_COBBLESTONE','u_cobblestone','USER_COBBLESTONE'),
    ('USER_SMOOTH_STONE','u_smooth_stone','USER_SMOOTH_STONE'),
    ('USER_TERRACOTTA','u_terracotta','USER_TERRACOTTA'),
    ('USER_CONCRETE_POWDER','u_concrete_powder','USER_CONCRETE_POWDER'),
    ('USER_GLASS','u_glass','USER_GLASS'),
    ('USER_TINTED_GLASS','u_tinted_glass','USER_TINTED_GLASS'),
    ('USER_STAINED_GLASS','u_stained_glass','USER_STAINED_GLASS'),
    ('USER_DIORITE','u_diorite','USER_DIORITE'),
    ('USER_CALCITE','u_calcite','USER_CALCITE'),
    ('USER_MUSHROOM_STEM','u_mushroom_stem','USER_MUSHROOM_STEM'),
    ('USER_DEAD_TUBE_CORAL','u_dead_tube_coral','USER_DEAD_TUBE_CORAL'),
    ('USER_PEARLESCENT_FROGLIGHT','u_pearlescent_froglight','USER_PEARLESCENT_FROGLIGHT'),
]

# All 19 dynamic blocks (no "_ITEM" suffix for block side, matches ModBlocks)
DYNAMIC_BLOCKS = [
    ('DYNAMIC_BLOCK','dynamic_block','DynamicBlock'),
    ('DYNAMIC_BLOCK_DIRT','dynamic_block_dirt','DynamicBlock'),
    ('DYNAMIC_BLOCK_SAND','dynamic_block_sand','DynamicBlock'),
    ('DYNAMIC_BLOCK_WOOL','dynamic_block_wool','DynamicBlock'),
    ('DYNAMIC_BLOCK_CONCRETE','dynamic_block_concrete','DynamicBlock'),
    ('DYNAMIC_BLOCK_DEEPSLATE','dynamic_block_deepslate','DynamicBlock'),
    ('DYNAMIC_BLOCK_WOOD','dynamic_block_wood','DynamicBlock'),
    ('DYNAMIC_BLOCK_STONE','dynamic_block_stone','DynamicBlock'),
    ('DYNAMIC_BLOCK_COBBLESTONE','dynamic_block_cobblestone','DynamicBlock'),
    ('DYNAMIC_BLOCK_SMOOTH_STONE','dynamic_block_smooth_stone','DynamicBlock'),
    ('DYNAMIC_BLOCK_TERRACOTTA','dynamic_block_terracotta','DynamicBlock'),
    ('DYNAMIC_BLOCK_CONCRETE_POWDER','dynamic_block_concrete_powder','DynamicBlock'),
    ('DYNAMIC_BLOCK_GLASS','dynamic_block_glass','TransparentDynamicBlock'),
    ('DYNAMIC_BLOCK_TINTED_GLASS','dynamic_block_tinted_glass','TintedDynamicBlock'),
    ('DYNAMIC_BLOCK_STAINED_GLASS','dynamic_block_stained_glass','TransparentDynamicBlock'),
    ('DYNAMIC_BLOCK_DIORITE','dynamic_block_diorite','DynamicBlock'),
    ('DYNAMIC_BLOCK_CALCITE','dynamic_block_calcite','DynamicBlock'),
    ('DYNAMIC_BLOCK_MUSHROOM_STEM','dynamic_block_mushroom_stem','DynamicBlock'),
    ('DYNAMIC_BLOCK_DEAD_TUBE_CORAL','dynamic_block_dead_tube_coral','DynamicBlock'),
    ('DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT','dynamic_block_pearlescent_froglight','DynamicBlock'),
]

# ===== Generate ModItems.java =====
lines = [
    'package com.blockeditor.mod.registry;',
    '',
    'import com.blockeditor.mod.BlockEditorMod;',
    'import com.blockeditor.mod.content.DynamicBlockItem;',
    'import net.minecraft.item.Item;',
    'import net.minecraft.registry.Registries;',
    'import net.minecraft.registry.Registry;',
    'import net.minecraft.util.Identifier;',
    '',
    'public class ModItems {',
    '',
]
for f, id_, block in DYNAMIC:
    lines.append(f'    public static final Item {f} = register("{id_}", new DynamicBlockItem(ModBlocks.{block}, new Item.Settings()));')
lines.append('')
for prefix, id_prefix, block_prefix in CATS:
    for i in range(1, 21):
        lines.append(f'    public static final Item {prefix}_{i}_ITEM = register("{id_prefix}{i}", new DynamicBlockItem(ModBlocks.{block_prefix}_{i}, new Item.Settings()));')
    lines.append('')
lines += [
    '    private static Item register(String name, Item item) {',
    '        return Registry.register(Registries.ITEM, new Identifier(BlockEditorMod.MOD_ID, name), item);',
    '    }',
    '',
    '    public static void register() {',
    '        // Triggers class loading and static field initialization',
    '    }',
    '}',
]
with open(os.path.join(base, 'ModItems.java'), 'w', encoding='utf-8') as f:
    f.write('\n'.join(lines) + '\n')
print(f'ModItems.java: {len(lines)} lines')

# ===== Generate ModBlockEntities.java =====
# Build the list of all blocks for the BlockEntityType builder
all_blocks = [f'ModBlocks.{b[0]}' for b in DYNAMIC_BLOCKS]
for prefix, _, block_prefix in CATS:
    for i in range(1, 21):
        all_blocks.append(f'ModBlocks.{block_prefix}_{i}')

lines = [
    'package com.blockeditor.mod.registry;',
    '',
    'import com.blockeditor.mod.BlockEditorMod;',
    'import com.blockeditor.mod.content.DynamicBlockEntity;',
    'import net.minecraft.block.entity.BlockEntityType;',
    'import net.minecraft.registry.Registries;',
    'import net.minecraft.registry.Registry;',
    'import net.minecraft.util.Identifier;',
    '',
    'public class ModBlockEntities {',
    '',
    '    public static final BlockEntityType<DynamicBlockEntity> DYNAMIC_BLOCK_ENTITY = register(',
    '        "dynamic_block_entity",',
    '        BlockEntityType.Builder.create(DynamicBlockEntity::new,',
]
# Add all blocks as arguments to the builder, in groups of 5
for i, block in enumerate(all_blocks):
    comma = '' if i == len(all_blocks) - 1 else ','
    lines.append(f'            {block}{comma}')
lines.append('        ).build(null)')
lines.append('    );')
lines.append('')
lines += [
    '    private static <T extends net.minecraft.block.entity.BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {',
    '        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(BlockEditorMod.MOD_ID, name), type);',
    '    }',
    '',
    '    public static void register() {',
    '        // Triggers class loading and static field initialization',
    '    }',
    '}',
]
with open(os.path.join(base, 'ModBlockEntities.java'), 'w', encoding='utf-8') as f:
    f.write('\n'.join(lines) + '\n')
print(f'ModBlockEntities.java: {len(lines)} lines')

# ===== Generate ModCreativeModeTabs.java =====
# The file was originally named ModCreativeModeTabs.java, class was ModItemGroups
# Rewrite using Fabric FabricItemGroup API
lines = [
    'package com.blockeditor.mod.registry;',
    '',
    'import com.blockeditor.mod.BlockEditorMod;',
    'import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;',
    'import net.minecraft.item.ItemGroup;',
    'import net.minecraft.item.ItemStack;',
    'import net.minecraft.registry.Registries;',
    'import net.minecraft.registry.Registry;',
    'import net.minecraft.text.Text;',
    'import net.minecraft.util.Identifier;',
    '',
    'public class ModCreativeModeTabs {',
    '',
    '    public static final ItemGroup BLOCK_EDITOR_TAB = Registry.register(',
    '        Registries.ITEM_GROUP,',
    '        new Identifier(BlockEditorMod.MOD_ID, "block_editor_tab"),',
    '        FabricItemGroup.builder()',
    '            .icon(() -> new ItemStack(ModItems.DYNAMIC_BLOCK_ITEM))',
    '            .displayName(Text.translatable("itemGroup.blockeditor"))',
    '            .entries((ctx, entries) -> {',
]
# Dynamic block items
for f, id_, block in DYNAMIC:
    lines.append(f'                entries.add(ModItems.{f});')
lines.append('')
# User block items
for prefix, id_prefix, block_prefix in CATS:
    lines.append(f'                // {prefix} blocks (1-20)')
    for i in range(1, 21):
        lines.append(f'                entries.add(ModItems.{prefix}_{i}_ITEM);')
    lines.append('')
lines += [
    '            })',
    '            .build()',
    '    );',
    '',
    '    public static void register() {',
    '        // Triggers class loading and static field initialization',
    '    }',
    '}',
]
with open(os.path.join(base, 'ModCreativeModeTabs.java'), 'w', encoding='utf-8') as f:
    f.write('\n'.join(lines) + '\n')
print(f'ModCreativeModeTabs.java: {len(lines)} lines')
print('Done!')
