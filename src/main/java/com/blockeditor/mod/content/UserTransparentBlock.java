package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.UserBlockRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * A UserBlock variant for transparent blocks like stained glass.
 * Extends TransparentDynamicBlock directly for proper glass behavior and incorporates UserBlock functionality.
 */
public class UserTransparentBlock extends TransparentDynamicBlock implements IUserBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String blockType;

    public UserTransparentBlock(String blockType) {
        super(); // Use TransparentDynamicBlock's properties
        this.blockType = blockType;
    }

    public String getBlockType() {
        return blockType;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // Call the parent method first
        super.onPlaced(world, pos, state, placer, stack);

        // Then handle UserBlock-specific logic (copied from UserBlock)
        if (!world.isClient && world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            NbtCompound tag = stack.getNbt();

            if (tag != null && tag.contains("Color")) {
                // NBT color data was provided (from WorldEdit command)
                String hexColor = tag.getString("Color");
                try {
                    int color = Integer.parseInt(hexColor, 16);
                    blockEntity.setColor(color);
                    LOGGER.info("UserTransparentBlock: Applied NBT color {} for user block type {}", hexColor, blockType);

                    // Set mimic block from NBT if available, otherwise use default
                    if (tag.contains("OriginalBlock")) {
                        String mimicBlock = tag.getString("OriginalBlock");
                        blockEntity.setMimicBlock(mimicBlock);
                        LOGGER.info("UserTransparentBlock: Applied NBT mimic block {} for user block type {}", mimicBlock, blockType);
                    } else {
                        String mimicBlock = getMimicBlockForType(blockType);
                        blockEntity.setMimicBlock(mimicBlock);
                        LOGGER.info("UserTransparentBlock: Applied default mimic block {} for user block type {}", mimicBlock, blockType);
                    }
                    return; // Color was set from NBT, we're done
                } catch (NumberFormatException e) {
                    LOGGER.error("UserTransparentBlock: Failed to parse color: {}", hexColor);
                }
            }

            // No NBT color data, try to load from user block registry
            LOGGER.info("UserTransparentBlock: No NBT color data found, trying registry lookup");
            if (world instanceof net.minecraft.server.world.ServerWorld serverLevel) {
                UserBlockRegistry registry = UserBlockRegistry.get(serverLevel);

                // Extract the number from this block's registry name
                Identifier blockId = Registries.BLOCK.getId(this);
                String blockName = blockId.getPath(); // e.g., "u_stained_glass1"
                LOGGER.info("UserTransparentBlock: Block registry name: {}", blockName);

                // Parse the identifier (e.g., "stained_glass1" from "u_stained_glass1")
                if (blockName.startsWith("u_")) {
                    String identifier = blockName.substring(2); // Remove "u_" prefix
                    LOGGER.info("UserTransparentBlock: Looking up registry data for identifier: {}", identifier);
                    UserBlockRegistry.UserBlockData data = registry.getUserBlockData(identifier);

                    if (data != null) {
                        blockEntity.setColor(data.color());
                        blockEntity.setMimicBlock(data.mimicBlock());
                        LOGGER.info("UserTransparentBlock: Applied registry data for {}: color={}, mimic={}",
                            identifier, String.format("%06X", data.color()), data.mimicBlock());
                    } else {
                        LOGGER.warn("UserTransparentBlock: No registry data found for identifier: {}", identifier);
                        // Use defaults if not found in registry
                        applyDefaults(blockEntity);
                    }
                } else {
                    LOGGER.warn("UserTransparentBlock: Block name doesn't start with 'u_': {}", blockName);
                    applyDefaults(blockEntity);
                }
            } else {
                LOGGER.warn("UserTransparentBlock: Not a server level, applying defaults");
                applyDefaults(blockEntity);
            }

            // Force a user block data check in case WorldEdit placed this block
            if (placer != null) {
                blockEntity.forceUserBlockDataCheck();
            }
        }
        LOGGER.info("=== UserTransparentBlock.setPlacedBy END ===");
    }

    private String getMimicBlockForType(String type) {
        return switch (type.toLowerCase()) {
            case "stained_glass" -> "minecraft:white_stained_glass";
            case "glass" -> "minecraft:glass";
            default -> "minecraft:white_stained_glass";
        };
    }

    private int getDefaultColorForType(String type) {
        return switch (type.toLowerCase()) {
            case "stained_glass" -> 0xFFFFFF; // White/Clear for stained glass
            case "glass" -> 0xFFFFFF; // White/Clear
            default -> 0xFFFFFF; // White default
        };
    }

    private void applyDefaults(DynamicBlockEntity blockEntity) {
        String mimicBlock = getMimicBlockForType(blockType);
        blockEntity.setMimicBlock(mimicBlock);

        int defaultColor = getDefaultColorForType(blockType);
        blockEntity.setColor(defaultColor);

        LOGGER.info("UserTransparentBlock: Applied defaults for user block type {}: mimic={}, color={}",
            blockType, mimicBlock, String.format("%06X", defaultColor));
    }

    @Override
    public ItemStack getPickStack(net.minecraft.world.BlockView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            // Create the item stack with the correct block
            ItemStack stack = new ItemStack(this);

            // Create NBT data with the block entity's color and mimic block
            NbtCompound tag = new NbtCompound();
            tag.putString("Color", String.format("%06X", blockEntity.getColor()));
            tag.putString("OriginalBlock", blockEntity.getMimicBlock());
            stack.setNbt(tag);

            return stack;
        }
        return new ItemStack(this);
    }
}

