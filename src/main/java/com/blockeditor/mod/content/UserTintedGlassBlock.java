package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * A UserBlock variant for tinted glass which blocks light but remains transparent.
 * Extends TintedDynamicBlock directly for proper tinted glass behavior and incorporates UserBlock functionality.
 */
public class UserTintedGlassBlock extends TintedDynamicBlock implements IUserBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String blockType;

    public UserTintedGlassBlock(String blockType) {
        super(); // Use TintedDynamicBlock's properties
        this.blockType = blockType;
    }

    public String getBlockType() {
        return blockType;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // Call the parent method first
        super.setPlacedBy(level, pos, state, placer, stack);

        // Then handle UserBlock-specific logic (copied from UserBlock)
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            CompoundTag tag = stack.getTag();

            if (tag != null && tag.contains("Color")) {
                // NBT color data was provided (from WorldEdit command)
                String hexColor = tag.getString("Color");
                try {
                    int color = Integer.parseInt(hexColor, 16);
                    blockEntity.setColor(color);
                    LOGGER.info("UserTintedGlassBlock: Applied NBT color {} for user block type {}", hexColor, blockType);

                    // Set mimic block from NBT if available, otherwise use default
                    if (tag.contains("OriginalBlock")) {
                        String mimicBlock = tag.getString("OriginalBlock");
                        blockEntity.setMimicBlock(mimicBlock);
                        LOGGER.info("UserTintedGlassBlock: Applied NBT mimic block {} for user block type {}", mimicBlock, blockType);
                    } else {
                        String mimicBlock = getMimicBlockForType(blockType);
                        blockEntity.setMimicBlock(mimicBlock);
                        LOGGER.info("UserTintedGlassBlock: Applied default mimic block {} for user block type {}", mimicBlock, blockType);
                    }
                    return; // Color was set from NBT, we're done
                } catch (NumberFormatException e) {
                    LOGGER.error("UserTintedGlassBlock: Failed to parse color: {}", hexColor);
                }
            }

            // No NBT color data, try to load from user block registry
            LOGGER.info("UserTintedGlassBlock: No NBT color data found, trying registry lookup");
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);

                // Extract the number from this block's registry name
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
                String blockName = blockId.getPath(); // e.g., "u_tinted_glass1"
                LOGGER.info("UserTintedGlassBlock: Block registry name: {}", blockName);

                // Parse the identifier (e.g., "tinted_glass1" from "u_tinted_glass1")
                if (blockName.startsWith("u_")) {
                    String identifier = blockName.substring(2); // Remove "u_" prefix
                    LOGGER.info("UserTintedGlassBlock: Looking up registry data for identifier: {}", identifier);
                    UserBlockRegistry.UserBlockData data = registry.getUserBlockData(identifier);

                    if (data != null) {
                        blockEntity.setColor(data.color());
                        blockEntity.setMimicBlock(data.mimicBlock());
                        LOGGER.info("UserTintedGlassBlock: Applied registry data for {}: color={}, mimic={}",
                            identifier, String.format("%06X", data.color()), data.mimicBlock());
                    } else {
                        LOGGER.warn("UserTintedGlassBlock: No registry data found for identifier: {}", identifier);
                        // Use defaults if not found in registry
                        applyDefaults(blockEntity);
                    }
                } else {
                    LOGGER.warn("UserTintedGlassBlock: Block name doesn't start with 'u_': {}", blockName);
                    applyDefaults(blockEntity);
                }
            } else {
                LOGGER.warn("UserTintedGlassBlock: Not a server level, applying defaults");
                applyDefaults(blockEntity);
            }

            // Force a user block data check in case WorldEdit placed this block
            if (placer != null) {
                blockEntity.forceUserBlockDataCheck();
            }
        }
        LOGGER.info("=== UserTintedGlassBlock.setPlacedBy END ===");
    }

    private String getMimicBlockForType(String type) {
        return switch (type.toLowerCase()) {
            case "tinted_glass" -> "minecraft:tinted_glass";
            case "glass" -> "minecraft:glass";
            default -> "minecraft:tinted_glass";
        };
    }

    private int getDefaultColorForType(String type) {
        return switch (type.toLowerCase()) {
            case "tinted_glass" -> 0x795E4A; // Tinted glass brown color
            case "glass" -> 0xFFFFFF; // White/Clear
            default -> 0x795E4A; // Tinted glass brown default
        };
    }

    private void applyDefaults(DynamicBlockEntity blockEntity) {
        String mimicBlock = getMimicBlockForType(blockType);
        blockEntity.setMimicBlock(mimicBlock);

        int defaultColor = getDefaultColorForType(blockType);
        blockEntity.setColor(defaultColor);

        LOGGER.info("UserTintedGlassBlock: Applied defaults for user block type {}: mimic={}, color={}",
            blockType, mimicBlock, String.format("%06X", defaultColor));
    }

    @Override
    public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            // Create the item stack with the correct block
            ItemStack stack = new ItemStack(this);

            // Create NBT data with the block entity's color and mimic block
            CompoundTag tag = new CompoundTag();
            tag.putString("Color", String.format("%06X", blockEntity.getColor()));
            tag.putString("OriginalBlock", blockEntity.getMimicBlock());
            stack.setTag(tag);

            return stack;
        }
        return new ItemStack(this);
    }
}

