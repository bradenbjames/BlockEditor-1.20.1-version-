# Adding New Block Types to BlockEditor Mod

## Checklist for Adding a New Block Type (e.g., "newblock")

### 1. Block Registration (ModBlocks.java)
- [ ] Add new `RegistryObject<Block>` in ModBlocks.java:
```java
public static final RegistryObject<Block> DYNAMIC_BLOCK_NEWBLOCK = BLOCKS.register("dynamic_block_newblock",
    () -> new DynamicBlock());
```

### 2. Item Registration (ModItems.java)
- [ ] Add corresponding item in ModItems.java:
```java
public static final RegistryObject<Item> DYNAMIC_BLOCK_NEWBLOCK_ITEM = ITEMS.register("dynamic_block_newblock",
    () -> new DynamicBlockItem(ModBlocks.DYNAMIC_BLOCK_NEWBLOCK.get(), new Item.Properties()));
```

### 3. Block Entity Registration (ModBlockEntities.java)
- [ ] Add the new block to the BlockEntityType builder:
```java
// Add to the list in DYNAMIC_BLOCK_ENTITY registration:
ModBlocks.DYNAMIC_BLOCK_NEWBLOCK.get()
```

### 4. Model Files
- [ ] Create blockstate file: `assets/blockeditor/blockstates/dynamic_block_newblock.json`
```json
{
  "variants": {
    "": {
      "model": "blockeditor:block/dynamic_block_newblock"
    }
  }
}
```

- [ ] Create block model: `assets/blockeditor/models/block/dynamic_block_newblock.json`
```json
{
  "parent": "blockeditor:block/tinted_cube_all",
  "textures": {
    "all": "minecraft:block/your_texture_name"
  }
}
```

- [ ] Create item model: `assets/blockeditor/models/item/dynamic_block_newblock.json`
```json
{
  "parent": "blockeditor:block/dynamic_block_newblock"
}
```

### 5. Color Handler Registration (ClientModEvents.java)
- [ ] Add to block color handler in `registerBlockColors()`:
```java
ModBlocks.DYNAMIC_BLOCK_NEWBLOCK.get(),
```

- [ ] Add to item color handler in `registerItemColors()`:
```java
ModBlocks.DYNAMIC_BLOCK_NEWBLOCK.get(),
```

### 6. Block Selection Logic (BlockEditorScreen.java)
- [ ] Add keyword to `allowedKeywords` array in `isFullSolidBlock()`:
```java
String[] allowedKeywords = {
    "planks", "wool", "dirt", "stone", "concrete", "cobblestone", "newblock"
};
```

- [ ] Add texture mapping in `getBlockTypeForTexture()`:
```java
} else if (blockName.contains("newblock")) {
    System.out.println("Matched NEWBLOCK texture");
    return ModBlocks.DYNAMIC_BLOCK_NEWBLOCK.get();
```

### 7. Build and Test
- [ ] Run `gradlew build`
- [ ] Copy JAR to test environment
- [ ] Test block selection in GUI
- [ ] Test block creation with custom color
- [ ] Verify color appears in inventory
- [ ] Verify color appears when placed in world

## Notes
- **CRITICAL**: Always use `blockeditor:block/tinted_cube_all` as parent for block models to enable color tinting
- The texture path should point to existing Minecraft textures (e.g., `minecraft:block/cobblestone`)
- Block names should follow the pattern `dynamic_block_[type]` for consistency
- Both block and item color handlers are required for complete functionality