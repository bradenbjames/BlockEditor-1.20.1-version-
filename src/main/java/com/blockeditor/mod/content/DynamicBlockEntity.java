package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DynamicBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private String mimicBlock = "minecraft:stone"; // Default mimic block
    private int color = 0xFFFFFF; // Default white color

    public DynamicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DYNAMIC_BLOCK_ENTITY.get(), pos, state);
        // Constructor: minimal logging to avoid spam during world loading
    }

    public String getMimicBlock() {
        checkAndApplyUserBlockData();
        return mimicBlock;
    }

    public BlockState getMimicState() {
        checkAndApplyUserBlockData();
        ResourceLocation location = ResourceLocation.tryParse(mimicBlock);
        if (location != null) {
            Block block = BuiltInRegistries.BLOCK.get(location);
            if (block != Blocks.AIR) {
                return block.defaultBlockState();
            }
        }
        return Blocks.STONE.defaultBlockState();
    }

    public void setMimicBlock(String mimicBlock) {
        this.mimicBlock = mimicBlock;
        setChanged();
        if (level != null && !level.isClientSide) {
            // Flag 2: notify clients only (no neighbor updates that could break block)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        }
    }

    public int getColor() {
        // For UserBlocks, check if we need to apply color data (only if still default white)
        if (color == 0xFFFFFF && getBlockState().getBlock() instanceof com.blockeditor.mod.content.UserBlock) {
            checkAndApplyUserBlockData();
        }
        
        // Add client-side debug logging
        if (getLevel() != null && getLevel().isClientSide) {
            LOGGER.info("CLIENT: getColor() called for block at {}, returning color: {}", getBlockPos(), String.format("%06X", color));
        }
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        setChanged();
        if (level != null && !level.isClientSide) {
            // Flag 2: notify clients only (no neighbor updates that could break block)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("MimicBlock", mimicBlock);
        tag.putInt("Color", color);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("MimicBlock")) {
            mimicBlock = tag.getString("MimicBlock");
        }
        if (tag.contains("Color")) {
            color = tag.getInt("Color");
        }
        
        // Debug logging for client/server
        if (getLevel() != null) {
            String side = getLevel().isClientSide ? "CLIENT" : "SERVER";
            LOGGER.info("{}: Loaded block entity at {} with color={}, mimic={}", 
                side, getBlockPos(), String.format("%06X", color), mimicBlock);
        }
        
        // Only check for UserBlock data if we don't already have a color set
        if (color == 0xFFFFFF) { // Default white color means it hasn't been set yet
            checkAndApplyUserBlockData();
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putString("MimicBlock", mimicBlock);
        tag.putInt("Color", color);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        // Force client-side refresh after receiving update
        if (getLevel() != null && getLevel().isClientSide) {
            // Request updated data from server if we don't have valid color data
            if (color == 0xFFFFFF && getBlockState().getBlock() instanceof com.blockeditor.mod.content.UserBlock) {
                // Schedule a delayed request for server data
                java.util.concurrent.CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(100); // Small delay
                        // Force a model data refresh and re-render
                        if (getLevel() != null) {
                            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 8);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }
    
    @Override
    public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        // Reset check flag when level changes
        hasCheckedUserBlockData = false;
        // Don't force check during world loading to avoid issues
    }
    
    // Public method to force re-checking (useful for WorldEdit placement)
    public void forceUserBlockDataCheck() {
        // Only allow forced check if level is fully loaded
        if (getLevel() != null && !getLevel().isClientSide && getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            // Check if the level is properly loaded
            if (serverLevel.isLoaded(getBlockPos())) {
                hasCheckedUserBlockData = false;
                checkAndApplyUserBlockData();
            }
        }
    }
    
    private boolean hasCheckedUserBlockData = false;
    
    public void checkAndApplyUserBlockData() {
        if (getLevel() == null || getLevel().isClientSide) {
            return;
        }
        
        // Allow re-checking if we haven't found user block data yet
        if (hasCheckedUserBlockData && color != 0xFFFFFF) {
            return; // Already applied successfully
        }
        
        hasCheckedUserBlockData = true;
        // Minimal logging to prevent spam during world loading
        
        if (getBlockState().getBlock() instanceof com.blockeditor.mod.content.UserBlock userBlock) {
            // Minimal logging for UserBlock detection
            autoApplyUserBlockData(userBlock);
        }
    }
    
    private void autoApplyUserBlockData(com.blockeditor.mod.content.UserBlock userBlock) {
        if (getLevel() == null || getLevel().isClientSide) {
            LOGGER.warn("DynamicBlockEntity: Cannot auto-apply, level is null or client side");
            return;
        }
        
        if (getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            com.blockeditor.mod.registry.UserBlockRegistry registry = 
                com.blockeditor.mod.registry.UserBlockRegistry.get(serverLevel);
            
            // Extract the identifier from the block name
            net.minecraft.resources.ResourceLocation blockId = 
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(userBlock);
            String blockName = blockId.getPath(); // e.g., "user_dirt1"
            
            LOGGER.info("DynamicBlockEntity: Auto-applying data for block: {} at {}", blockName, getBlockPos());
            
            if (blockName.startsWith("user_")) {
                String identifier = blockName.substring(5); // Remove "user_" prefix
                LOGGER.info("DynamicBlockEntity: Looking up identifier: {}", identifier);
                
                com.blockeditor.mod.registry.UserBlockRegistry.UserBlockData data = 
                    registry.getUserBlockData(identifier);
                
                if (data != null) {
                    // Set values directly
                    this.color = data.color();
                    this.mimicBlock = data.mimicBlock();
                    setChanged(); // Mark as changed
                    
                    // Simple client update
                    if (getLevel() != null && !getLevel().isClientSide) {
                        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                    }
                    
                    LOGGER.info("DynamicBlockEntity: Auto-applied registry data for {}: color={}, mimic={}", 
                        identifier, String.format("%06X", data.color()), data.mimicBlock());
                } else {
                    LOGGER.warn("DynamicBlockEntity: No registry data found for identifier: {} - You may need to create and register a custom block first", identifier);
                }
            } else {
                LOGGER.warn("DynamicBlockEntity: Block name doesn't start with 'user_': {}", blockName);
            }
        }
    }
}