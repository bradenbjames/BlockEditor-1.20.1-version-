package com.blockeditor.mod.content;

import com.blockeditor.mod.client.ClientColorManager;
import com.mojang.logging.LogUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DynamicBlockItem extends BlockItem {
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        LOGGER.info("DynamicBlockItem.useOnBlock called - isClient: {}", context.getWorld().isClient);
        
        // Register custom block data on client side when placing
        if (context.getWorld().isClient) {
            ClientColorManager.registerCustomBlockCreation(context.getStack());
        }
        
        ActionResult result = super.useOnBlock(context);
        LOGGER.info("DynamicBlockItem.useOn result: {} - isClient: {}", result, context.getWorld().isClient);
        return result;
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World level, @Nullable net.minecraft.entity.player.PlayerEntity player,
                                                  ItemStack stack, BlockState state) {
        boolean result = super.postPlacement(pos, level, player, stack, state);

        // Transfer NBT data from item to block entity, even in creative mode
        if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            NbtCompound tag = stack.getNbt();
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