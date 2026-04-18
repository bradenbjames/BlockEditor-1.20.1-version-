package com.blockeditor.mod.content;

import com.blockeditor.mod.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class DynamicBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Thread-safe cache of block colors for map color lookups from non-main threads (e.g. Xaero's map thread)
    private static final ConcurrentHashMap<BlockPos, Integer> COLOR_CACHE = new ConcurrentHashMap<>();

    /** Get a cached color for a position, or -1 if not cached. */
    public static int getCachedColor(BlockPos pos) {
        Integer c = COLOR_CACHE.get(pos);
        return c != null ? c : -1;
    }

    private void updateColorCache() {
        COLOR_CACHE.put(getPos().toImmutable(), color);
    }

    private String mimicBlock = "minecraft:stone"; // Default mimic block
    private int color = 0xFFFFFF; // Default white color

    public DynamicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DYNAMIC_BLOCK_ENTITY, pos, state);
        // Constructor: minimal logging to avoid spam during world loading
    }

    public String getMimicBlock() {
        checkAndApplyUserBlockData();
        return mimicBlock;
    }

    public BlockState getMimicState() {
        checkAndApplyUserBlockData();
        Identifier location = Identifier.tryParse(mimicBlock);
        if (location != null) {
            Block block = Registries.BLOCK.get(location);
            if (block != Blocks.AIR) {
                return block.getDefaultState();
            }
        }
        return Blocks.STONE.getDefaultState();
    }

    public void setMimicBlock(String mimicBlock) {
        this.mimicBlock = mimicBlock;
        markDirty();
        if (world != null && !world.isClient) {
            // Flag 2: notify clients only (no neighbor updates that could break block)
            world.updateListeners(getPos(), getCachedState(), getCachedState(), 2);
        }
    }

    /**
     * Returns the raw color value without triggering any side effects.
     * Used by map color lookups where we only need the stored value.
     */
    public int getRawColor() {
        return color;
    }

    public int getColor() {
        // For UserBlocks (including specialized glass variants), check if we need to apply color data (only if still default white)
        if (color == 0xFFFFFF && getCachedState().getBlock() instanceof IUserBlock) {
            checkAndApplyUserBlockData();
        }
        
        // Debug logging for glass blocks
        if (getCachedState().getBlock().toString().contains("glass")) {
            LOGGER.info("DynamicBlockEntity.getColor(): Block={}, Color={}, Pos={}",
                getCachedState().getBlock(), String.format("#%06X", color), getPos());
        }

        return color;
    }

    public void setColor(int color) {
        // Debug logging for glass blocks
        if (getCachedState().getBlock().toString().contains("glass")) {
            LOGGER.info("DynamicBlockEntity.setColor(): Block={}, Old Color={}, New Color={}, Pos={}",
                getCachedState().getBlock(), String.format("#%06X", this.color),
                String.format("#%06X", color), getPos());
        }

        this.color = color;
        updateColorCache();
        markDirty();
        if (world != null && !world.isClient) {
            // Flag 2: notify clients only (no neighbor updates that could break block)
            world.updateListeners(getPos(), getCachedState(), getCachedState(), 2);
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putString("MimicBlock", mimicBlock);
        tag.putInt("Color", color);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("MimicBlock")) {
            mimicBlock = tag.getString("MimicBlock");
        }
        if (tag.contains("Color")) {
            color = tag.getInt("Color");
        }
        updateColorCache();
        
        // Only check for UserBlock data if we don't already have a color set
        if (color == 0xFFFFFF) { // Default white color means it hasn't been set yet
            checkAndApplyUserBlockData();
        }
    }

    @Override
    public void markRemoved() {
        COLOR_CACHE.remove(getPos());
        super.markRemoved();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = super.toInitialChunkDataNbt();
        tag.putString("MimicBlock", mimicBlock);
        tag.putInt("Color", color);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public void setWorld(net.minecraft.world.World world) {
        super.setWorld(world);
        // Reset check flag when level changes
        hasCheckedUserBlockData = false;
        // Don't force check during world loading to avoid issues
    }
    
    // Public method to force re-checking (useful for WorldEdit placement)
    public void forceUserBlockDataCheck() {
        // Only allow forced check if level is fully loaded
        if (getWorld() != null && !getWorld().isClient && getWorld() instanceof net.minecraft.server.world.ServerWorld serverLevel) {
            // Check if the level is properly loaded
            if (serverLevel.getChunkManager().isChunkLoaded(getPos().getX() >> 4, getPos().getZ() >> 4)) {
                hasCheckedUserBlockData = false;
                checkAndApplyUserBlockData();
            }
        }
    }
    
    private boolean hasCheckedUserBlockData = false;
    
    public void checkAndApplyUserBlockData() {
        if (getWorld() == null || getWorld().isClient) {
            return;
        }
        
        // Allow re-checking if we haven't found user block data yet
        if (hasCheckedUserBlockData && color != 0xFFFFFF) {
            return; // Already applied successfully
        }
        
        hasCheckedUserBlockData = true;
        // Minimal logging to prevent spam during world loading
        
        if (getCachedState().getBlock() instanceof com.blockeditor.mod.content.UserBlock userBlock) {
            // Minimal logging for UserBlock detection
            autoApplyUserBlockData(userBlock);
        }
    }
    
    private void autoApplyUserBlockData(com.blockeditor.mod.content.UserBlock userBlock) {
        if (getWorld() == null || getWorld().isClient) {
            LOGGER.warn("DynamicBlockEntity: Cannot auto-apply, level is null or client side");
            return;
        }
        
        if (getWorld() instanceof net.minecraft.server.world.ServerWorld serverLevel) {
            com.blockeditor.mod.registry.UserBlockRegistry registry = 
                com.blockeditor.mod.registry.UserBlockRegistry.get(serverLevel);
            
            // Extract the identifier from the block name
            net.minecraft.util.Identifier blockId = 
                net.minecraft.registry.Registries.BLOCK.getId(userBlock);
            String blockName = blockId.getPath(); // e.g., "u_dirt1"
            
            LOGGER.info("DynamicBlockEntity: Auto-applying data for block: {} at {}", blockName, getPos());
            
            if (blockName.startsWith("u_")) {
                String identifier = blockName.substring(2); // Remove "u_" prefix
                LOGGER.info("DynamicBlockEntity: Looking up identifier: {}", identifier);
                
                com.blockeditor.mod.registry.UserBlockRegistry.UserBlockData data = 
                    registry.getUserBlockData(identifier);
                
                if (data != null) {
                    // Set values directly
                    this.color = data.color();
                    this.mimicBlock = data.mimicBlock();
                    updateColorCache();
                    markDirty(); // Mark as changed
                    
                    // Simple client update
                    if (getWorld() != null && !getWorld().isClient) {
                        getWorld().updateListeners(getPos(), getCachedState(), getCachedState(), 3);
                    }
                    
                    LOGGER.info("DynamicBlockEntity: Auto-applied registry data for {}: color={}, mimic={}", 
                        identifier, String.format("%06X", data.color()), data.mimicBlock());
                } else {
                    LOGGER.warn("DynamicBlockEntity: No registry data found for identifier: {} - You may need to create and register a custom block first", identifier);
                }
            } else {
                LOGGER.warn("DynamicBlockEntity: Block name doesn't start with 'u_': {}", blockName);
            }
        }
    }
}