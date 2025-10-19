package com.blockeditor.mod.content;

import com.blockeditor.mod.client.ClientColorManager;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DynamicBlockItem extends BlockItem {
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        LOGGER.info("DynamicBlockItem.useOn called - isClient: {}", context.getLevel().isClientSide);
        
        // Register custom block data on client side when placing
        if (context.getLevel().isClientSide) {
            ClientColorManager.registerCustomBlockCreation(context.getItemInHand());
        }
        
        InteractionResult result = super.useOn(context);
        LOGGER.info("DynamicBlockItem.useOn result: {} - isClient: {}", result, context.getLevel().isClientSide);
        return result;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable net.minecraft.world.entity.player.Player player,
                                                  ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        // Transfer NBT data from item to block entity, even in creative mode
        if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                // Read color from NBT
                if (tag.contains("Color")) {
                    String hexColor = tag.getString("Color");
                    try {
                        int color = Integer.parseInt(hexColor, 16);
                        blockEntity.setColor(color);
                        System.out.println("DynamicBlockItem: Set block color to #" + hexColor);
                    } catch (NumberFormatException e) {
                        System.out.println("DynamicBlockItem: Failed to parse color: " + hexColor);
                    }
                }

                // Read original block from NBT
                if (tag.contains("OriginalBlock")) {
                    String originalBlock = tag.getString("OriginalBlock");
                    blockEntity.setMimicBlock(originalBlock);
                    System.out.println("DynamicBlockItem: Set mimic block to " + originalBlock);
                }
            } else {
                System.out.println("DynamicBlockItem: No NBT tag found on item");
            }
        } else {
            System.out.println("DynamicBlockItem: BlockEntity not found at " + pos);
        }

        return result;
    }
}