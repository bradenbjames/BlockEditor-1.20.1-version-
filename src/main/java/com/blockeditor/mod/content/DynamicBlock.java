package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DynamicBlock extends Block implements EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(1.5f, 6.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return false; // Block is opaque
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.DYNAMIC_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // Use vanilla model rendering; tint is applied via BlockColors based on BlockEntity data
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        LOGGER.info("=== DynamicBlock.setPlacedBy ===");
        LOGGER.info("Position: {} | Is Client: {}", pos, level.isClientSide);
        BlockState worldState = level.getBlockState(pos);
        LOGGER.info("World has block: {} (same as placed: {})", worldState.getBlock(), worldState.getBlock() == state.getBlock());
        if (!level.isClientSide) {
            LOGGER.info("[SERVER] Block placed at {}. Block: {}", pos, state.getBlock());
        } else {
            LOGGER.info("[CLIENT] Block placed at {}. Block: {}", pos, state.getBlock());
        }
        // Transfer NBT data from item to block entity
        if (level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
            LOGGER.info("Found DynamicBlockEntity at {}", pos);
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                // Read color from NBT
                if (tag.contains("Color")) {
                    String hexColor = tag.getString("Color");
                    try {
                        int color = Integer.parseInt(hexColor, 16);
                        blockEntity.setColor(color);
                        LOGGER.info("Set block color to: #{} (int: {})", hexColor, color);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Failed to parse color: {}", hexColor);
                    }
                }

                // Read original block from NBT
                if (tag.contains("OriginalBlock")) {
                    String originalBlock = tag.getString("OriginalBlock");
                    blockEntity.setMimicBlock(originalBlock);
                    LOGGER.info("Set mimic block to: {}", originalBlock);
                }
            } else {
                LOGGER.warn("No NBT tag found on placed item");
            }
        } else {
            LOGGER.error("ERROR: BlockEntity not found at {}!", pos);
            LOGGER.error("BlockEntity type: {}", level.getBlockEntity(pos) != null ? level.getBlockEntity(pos).getClass().getName() : "null");
        }
        LOGGER.info("=== End setPlacedBy ===");
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Only show info when player is sneaking (shift + right-click)
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                // Format color with zero-padding to always show 6 hex digits
                String colorHex = String.format("%06X", blockEntity.getColor());
                
                // Get the block ID
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
                
                // Create WorldEdit command for regular dynamic block
                String worldEditCommand = String.format("//set %s{Color:\"%s\",OriginalBlock:\"%s\"}", 
                    blockId.toString(), colorHex, blockEntity.getMimicBlock());
                
                // Show block info
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    "§aMimic: §f" + blockEntity.getMimicBlock() + "§a, Color: §f#" + colorHex), false);
                
                // Show WorldEdit command
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    "§bWorldEdit: §7" + worldEditCommand), false);
                
                // Show user block alternatives for WorldEdit autocomplete
                String mimicBlock = blockEntity.getMimicBlock();
                String userBlockType = getUserBlockTypeFromMimic(mimicBlock);
                if (userBlockType != null) {
                    // Try to assign this block if not already assigned
                    if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        com.blockeditor.mod.registry.UserBlockRegistry registry = 
                            com.blockeditor.mod.registry.UserBlockRegistry.get(serverLevel);
                        
                        int assignedNumber = registry.assignUserBlock(userBlockType, blockEntity.getColor(), mimicBlock);
                        if (assignedNumber > 0) {
                            String userBlockCommand = String.format("//set blockeditor:user_%s%d{Color:\"%s\"}", 
                                userBlockType, assignedNumber, colorHex);
                            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                                "§6User Block Assigned: §7" + userBlockCommand), false);
                            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                                "§7(This block is now available as USER_" + userBlockType.toUpperCase() + assignedNumber + " with color #" + colorHex + ")"), false);
                        } else {
                            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                                "§cNo more user " + userBlockType + " slots available!"), false);
                        }
                    }
                }
                
                // Copy to clipboard message
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    "§7(Copy any command above for WorldEdit)"), false);
            }
            return InteractionResult.SUCCESS;
        }
        // Pass through to allow normal block placement
        return InteractionResult.PASS;
    }

    /**
     * Determines the user block type based on the mimic block
     */
    private String getUserBlockTypeFromMimic(String mimicBlock) {
        if (mimicBlock.contains("wool")) return "wool";
        if (mimicBlock.contains("stone") && !mimicBlock.contains("deepslate")) return "stone";
        if (mimicBlock.contains("concrete")) return "concrete";
        if (mimicBlock.contains("planks") || mimicBlock.contains("wood") || mimicBlock.contains("log")) return "wood";
        if (mimicBlock.contains("dirt")) return "dirt";
        if (mimicBlock.contains("sand")) return "sand";
        if (mimicBlock.contains("deepslate")) return "deepslate";
        return null; // No matching user block type
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        LOGGER.info("DynamicBlock.onPlace at {} oldState={} isMoving={} isClient={}", pos, oldState, isMoving, level.isClientSide);

        // Schedule a delayed check to see if block still exists
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 5);
        }
    }

    @Override
    public void tick(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        // Delayed check to verify block still exists
        BlockState currentState = level.getBlockState(pos);
        LOGGER.info("DynamicBlock.tick delayed check at {}: currentBlock={}", pos, currentState.getBlock());
        if (currentState.getBlock() != this) {
            LOGGER.error("BLOCK WAS REPLACED! Now has: {}", currentState);
        } else {
            LOGGER.info("Block still exists correctly at {}", pos);
            
            // Check for UserBlock auto-application during tick
            if (this instanceof com.blockeditor.mod.content.UserBlock && level.getBlockEntity(pos) instanceof DynamicBlockEntity blockEntity) {
                // Force the auto-application check during tick
                blockEntity.getColor(); // This will trigger checkAndApplyUserBlockData()
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.warn("DynamicBlock.onRemove at {} newState={} isMoving={}", pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
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
            
            // Set display name to show the color and block type
            String mimicBlockName = blockEntity.getMimicBlock().replace("minecraft:", "").replace("_", " ");
            String hexColor = String.format("%06X", blockEntity.getColor());
            stack.setHoverName(net.minecraft.network.chat.Component.literal(
                "§r" + mimicBlockName + " §7(#" + hexColor + ")"
            ));
            
            LOGGER.info("getCloneItemStack: Created item with color #{} and mimic {}", hexColor, blockEntity.getMimicBlock());
            return stack;
        }
        
        // Fallback to default behavior
        return super.getCloneItemStack(level, pos, state);
    }
}