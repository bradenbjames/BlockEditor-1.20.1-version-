package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DynamicBlock extends Block implements BlockEntityProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicBlock() {
        this(AbstractBlock.Settings.create()
            .mapColor(MapColor.STONE_GRAY)
            .strength(1.5f, 6.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    }

    /**
     * Advanced constructor so subclasses (or special cases like glass) can customize properties
     * such as noOcclusion without duplicating logic.
     */
    protected DynamicBlock(AbstractBlock.Settings properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.DYNAMIC_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // Use vanilla model rendering; tint is applied via BlockColors based on BlockEntity data
        return BlockRenderType.MODEL;
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
        
        // Transfer NBT data from item to block entity
        if (world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            NbtCompound tag = stack.getNbt();
            if (tag != null) {
                // Read color from NBT
                if (tag.contains("Color")) {
                    String hexColor = tag.getString("Color");
                    try {
                        int color = Integer.parseInt(hexColor, 16);
                        blockEntity.setColor(color);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Failed to parse color: {}", hexColor);
                    }
                }

                // Read original block from NBT
                if (tag.contains("OriginalBlock")) {
                    String originalBlock = tag.getString("OriginalBlock");
                    blockEntity.setMimicBlock(originalBlock);
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        // Only show info when player is sneaking (shift + right-click)
        if (player.isSneaking()) {
            if (!world.isClient && world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                // Format color with zero-padding to always show 6 hex digits
                String colorHex = String.format("%06X", blockEntity.getColor());
                
                // Get the block ID
                Identifier blockId = Registries.BLOCK.getId(this);
                
                // Create WorldEdit command for regular dynamic block
                String worldEditCommand = String.format("//set %s{Color:\"%s\",OriginalBlock:\"%s\"}", 
                    blockId.toString(), colorHex, blockEntity.getMimicBlock());
                
                // Show block info
                player.sendMessage(net.minecraft.text.Text.literal(
                    "§aMimic: §f" + blockEntity.getMimicBlock() + "§a, Color: §f#" + colorHex), false);
                
                // Show WorldEdit command
                player.sendMessage(net.minecraft.text.Text.literal(
                    "§bWorldEdit: §7" + worldEditCommand), false);
                
                // Show user block alternatives for WorldEdit autocomplete
                String mimicBlock = blockEntity.getMimicBlock();
                String userBlockType = getUserBlockTypeFromMimic(mimicBlock);
                if (userBlockType != null) {
                    // Try to assign this block if not already assigned
                    if (world instanceof net.minecraft.server.world.ServerWorld serverLevel) {
                        com.blockeditor.mod.registry.UserBlockRegistry registry = 
                            com.blockeditor.mod.registry.UserBlockRegistry.get(serverLevel);
                        
                        int assignedNumber = registry.assignUserBlock(userBlockType, blockEntity.getColor(), mimicBlock);
                        if (assignedNumber > 0) {
                            String userBlockCommand = String.format("//set be:u_%s%d{Color:\"%s\"}", 
                                userBlockType, assignedNumber, colorHex);
                            player.sendMessage(net.minecraft.text.Text.literal(
                                "§6User Block Assigned: §7" + userBlockCommand), false);
                            player.sendMessage(net.minecraft.text.Text.literal(
                                "§7(This block is now available as USER_" + userBlockType.toUpperCase() + assignedNumber + " with color #" + colorHex + ")"), false);
                        } else {
                            player.sendMessage(net.minecraft.text.Text.literal(
                                "§cNo more user " + userBlockType + " slots available!"), false);
                        }
                    }
                }
                
                // Copy to clipboard message
                player.sendMessage(net.minecraft.text.Text.literal(
                    "§7(Copy any command above for WorldEdit)"), false);
            }
            return ActionResult.SUCCESS;
        }
        // Pass through to allow normal block placement
        return ActionResult.PASS;
    }

    /**
     * Determines the user block type based on the mimic block
     */
    private String getUserBlockTypeFromMimic(String mimicBlock) {
        if (mimicBlock.contains("wool")) return "wool";
        if (mimicBlock.contains("stone") && !mimicBlock.contains("deepslate")) return "stone";
        if (mimicBlock.contains("concrete") && !mimicBlock.contains("powder")) return "concrete";
        if (mimicBlock.contains("concrete_powder")) return "concrete_powder";
        if (mimicBlock.contains("terracotta")) return "terracotta";
        if (mimicBlock.contains("tinted_glass")) return "tinted_glass";
        if (mimicBlock.contains("white_stained_glass") || mimicBlock.contains("stained_glass")) return "stained_glass";
        if (mimicBlock.contains("glass") && !mimicBlock.contains("pane")) return "glass";
        if (mimicBlock.contains("diorite")) return "diorite";
        if (mimicBlock.contains("calcite")) return "calcite";
        if (mimicBlock.contains("mushroom_stem")) return "mushroom_stem";
        if (mimicBlock.contains("dead_tube_coral")) return "dead_tube_coral";
        if (mimicBlock.contains("pearlescent_froglight")) return "pearlescent_froglight";
        if (mimicBlock.contains("planks") || mimicBlock.contains("wood") || mimicBlock.contains("log")) return "wood";
        if (mimicBlock.contains("dirt")) return "dirt";
        if (mimicBlock.contains("sand")) return "sand";
        if (mimicBlock.contains("deepslate")) return "deepslate";
        return null; // No matching user block type
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        // For UserBlocks, check for color data when first placed
        if (!world.isClient && this instanceof com.blockeditor.mod.content.UserBlock) {
            if (world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                // Only apply if no color has been set yet (default white)
                if (blockEntity.getColor() == 0xFFFFFF) {
                    blockEntity.checkAndApplyUserBlockData();
                }
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, net.minecraft.server.world.ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        // Tick method disabled to prevent loading lag
        // Use manual /be refresh command instead for color updates
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public ItemStack getPickStack(net.minecraft.world.BlockView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            // Create the item stack with the correct block
            ItemStack stack = new ItemStack(this);
            
            // Create NBT data with the block entity's color and mimic block
            net.minecraft.nbt.NbtCompound tag = new net.minecraft.nbt.NbtCompound();
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
            stack.setNbt(tag);
            
            // Set display name to show the color and block type
            String mimicBlockName = blockEntity.getMimicBlock().replace("minecraft:", "").replace("_", " ");
            String hexColor = String.format("%06X", blockEntity.getColor());
            stack.setCustomName(net.minecraft.text.Text.literal(
                "§r" + mimicBlockName + " §7(#" + hexColor + ")"
            ));
            
            return stack;
        }
        
        // Fallback to default behavior
        return super.getPickStack(world, pos, state);
    }
}