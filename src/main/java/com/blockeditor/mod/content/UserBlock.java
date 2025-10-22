package com.blockeditor.mod.content;

import com.blockeditor.mod.client.ClientColorManager;
import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * UserBlock is a specialized DynamicBlock that represents user-created custom blocks.
 * These blocks appear in WorldEdit autocomplete with "USER_" prefix followed by their type.
 */
public class UserBlock extends DynamicBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String blockType;

    public UserBlock(String blockType) {
        super();
        this.blockType = blockType;
    }

    public String getBlockType() {
        return blockType;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // Call the parent method first
        super.setPlacedBy(level, pos, state, placer, stack);
        
        // Then handle UserBlock-specific logic
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            CompoundTag tag = stack.getTag();
            
            if (tag != null && tag.contains("Color")) {
                // NBT color data was provided (from WorldEdit command)
                String hexColor = tag.getString("Color");
                try {
                    int color = Integer.parseInt(hexColor, 16);
                    blockEntity.setColor(color);
                    LOGGER.info("UserBlock: Applied NBT color {} for user block type {}", hexColor, blockType);
                    
                    // Also set mimic block if not already set
                    if (!tag.contains("OriginalBlock")) {
                        String mimicBlock = getMimicBlockForType(blockType);
                        blockEntity.setMimicBlock(mimicBlock);
                        LOGGER.info("UserBlock: Applied default mimic block {} for user block type {}", mimicBlock, blockType);
                    }
                    return; // Color was set from NBT, we're done
                } catch (NumberFormatException e) {
                    LOGGER.error("UserBlock: Failed to parse color: {}", hexColor);
                }
            }
            
            // No NBT color data, try to load from user block registry
            LOGGER.info("UserBlock: No NBT color data found, trying registry lookup");
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);
                
                // Extract the number from this block's registry name
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
                String blockName = blockId.getPath(); // e.g., "u_wool1"
                LOGGER.info("UserBlock: Block registry name: {}", blockName);
                
                // Parse the identifier (e.g., "wool1" from "u_wool1")
                if (blockName.startsWith("u_")) {
                    String identifier = blockName.substring(2); // Remove "u_" prefix
                    LOGGER.info("UserBlock: Looking up registry data for identifier: {}", identifier);
                    UserBlockRegistry.UserBlockData data = registry.getUserBlockData(identifier);
                    
                    if (data != null) {
                        blockEntity.setColor(data.color());
                        blockEntity.setMimicBlock(data.mimicBlock());
                        LOGGER.info("UserBlock: Applied registry data for {}: color={}, mimic={}", 
                            identifier, String.format("%06X", data.color()), data.mimicBlock());
                    } else {
                        LOGGER.warn("UserBlock: No registry data found for identifier: {}", identifier);
                        // Use defaults if not found in registry
                        applyDefaults(blockEntity);
                    }
                } else {
                    LOGGER.warn("UserBlock: Block name doesn't start with 'u_': {}", blockName);
                    applyDefaults(blockEntity);
                }
            } else {
                LOGGER.warn("UserBlock: Not a server level, applying defaults");
                applyDefaults(blockEntity);
            }
            
            // Force a user block data check in case WorldEdit placed this block
            // But only if we're not during world loading (placer indicates manual placement)
            if (placer != null) {
                blockEntity.forceUserBlockDataCheck();
            }
        }
        LOGGER.info("=== UserBlock.setPlacedBy END ===");
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        // Color application is now handled by WorldEditIntegration event handler
        // This ensures consistency whether blocks are placed manually or via WorldEdit
    }

    private int getDefaultColorForType(String type) {
        return switch (type.toLowerCase()) {
            case "wool" -> 0xFFFFFF; // White
            case "stone" -> 0x808080; // Gray
            case "concrete" -> 0xE0E0E0; // Light gray
            case "concrete_powder" -> 0xE0E0E0; // Light gray
            case "terracotta" -> 0xD2B1A1; // Light terracotta
            case "glass" -> 0xFFFFFF; // White/Clear
            case "diorite" -> 0xC4C4C4; // Light gray
            case "calcite" -> 0xE3DDD4; // Off-white
            case "mushroom_stem" -> 0xC9B8A1; // Tan
            case "dead_tube_coral" -> 0x827E7E; // Gray
            case "pearlescent_froglight" -> 0xE2C1D6; // Light purple
            case "wood" -> 0xD2B48C; // Tan
            case "dirt" -> 0x8B4513; // Saddle brown
            case "sand" -> 0xF4A460; // Sandy brown
            case "deepslate" -> 0x2F2F2F; // Dark gray
            case "cobblestone" -> 0x7F7F7F; // Medium gray
            case "smooth_stone" -> 0x9E9E9E; // Light gray
            default -> 0xFFFFFF; // White default
        };
    }

    private String getMimicBlockForType(String type) {
        return switch (type.toLowerCase()) {
            case "wool" -> "minecraft:white_wool";
            case "stone" -> "minecraft:stone";
            case "concrete" -> "minecraft:white_concrete";
            case "concrete_powder" -> "minecraft:white_concrete_powder";
            case "terracotta" -> "minecraft:white_terracotta";
            case "glass" -> "minecraft:glass";
            case "diorite" -> "minecraft:diorite";
            case "calcite" -> "minecraft:calcite";
            case "mushroom_stem" -> "minecraft:mushroom_stem";
            case "dead_tube_coral" -> "minecraft:dead_tube_coral_block";
            case "pearlescent_froglight" -> "minecraft:pearlescent_froglight";
            case "wood" -> "minecraft:oak_planks";
            case "dirt" -> "minecraft:dirt";
            case "sand" -> "minecraft:sand";
            case "deepslate" -> "minecraft:deepslate";
            case "cobblestone" -> "minecraft:cobblestone";
            case "smooth_stone" -> "minecraft:smooth_stone";
            default -> "minecraft:stone";
        };
    }

    private void applyDefaults(DynamicBlockEntity blockEntity) {
        String mimicBlock = getMimicBlockForType(blockType);
        blockEntity.setMimicBlock(mimicBlock);
        
        int defaultColor = getDefaultColorForType(blockType);
        blockEntity.setColor(defaultColor);
        
        LOGGER.info("UserBlock: Applied defaults for user block type {}: mimic={}, color={}", 
            blockType, mimicBlock, String.format("%06X", defaultColor));
    }

    @Override
    public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            // Create the item stack with the correct block
            ItemStack stack = new ItemStack(this);
            
            // Create NBT data with the block entity's color and mimic block
            net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
            tag.putString("Color", String.format("%06X", blockEntity.getColor()));
            tag.putString("OriginalBlock", blockEntity.getMimicBlock());
            
            // Parse color to RGB for backwards compatibility
            int color = blockEntity.getColor();
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;
            tag.putInt("Red", red);
            tag.putInt("Green", green);
            tag.putInt("Blue", blue);
            
            // Set the NBT tag
            stack.setTag(tag);
            
            // Set display name to show it's a user block with color and type
            String hexColor = String.format("%06X", blockEntity.getColor());
            stack.setHoverName(net.minecraft.network.chat.Component.literal(
                "§6USER_" + blockType.toUpperCase() + " §7(#" + hexColor + ")"
            ));
            
            LOGGER.info("getCloneItemStack: Created user {} item with color #{}", blockType, hexColor);
            return stack;
        }
        
        // Fallback to default behavior
        return super.getCloneItemStack(level, pos, state);
    }
    
    // Make glass blocks transparent
    @Override
    public boolean propagatesSkylightDown(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return blockType.equals("glass");
    }
    
    @Override
    public float getShadeBrightness(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return blockType.equals("glass") ? 1.0F : 0.2F;
    }
}