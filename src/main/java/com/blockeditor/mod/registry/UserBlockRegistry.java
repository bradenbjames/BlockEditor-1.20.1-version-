package com.blockeditor.mod.registry;

import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the assignment of numbered user blocks to custom colors
 */
public class UserBlockRegistry extends SavedData {
    private static final String DATA_NAME = BlockEditorMod.MOD_ID + "_user_blocks";
    
    // Maps block type to next available number (e.g., "wool" -> 3 means next available is user_wool3)
    private final Map<String, Integer> nextAvailableNumbers = new HashMap<>();
    
    // Maps user block identifier to color and mimic block (e.g., "wool1" -> {color, mimicBlock})
    private final Map<String, UserBlockData> assignedBlocks = new HashMap<>();
    
    // Maps custom names to internal block identifiers (e.g., "my_block" -> "wool1")
    private final Map<String, String> customNameMappings = new HashMap<>();
    
    // Maps internal block identifiers back to custom names (e.g., "wool1" -> "my_block")
    private final Map<String, String> reverseCustomNameMappings = new HashMap<>();
    
    public UserBlockRegistry() {
        // Initialize with 1 as the first available number for each type
        nextAvailableNumbers.put("wool", 1);
        nextAvailableNumbers.put("stone", 1);
        nextAvailableNumbers.put("concrete", 1);
        nextAvailableNumbers.put("wood", 1);
        nextAvailableNumbers.put("dirt", 1);
        nextAvailableNumbers.put("sand", 1);
        nextAvailableNumbers.put("deepslate", 1);
    }
    
    /**
     * Assigns a new user block for the given type and returns the assigned number
     */
    public int assignUserBlock(String blockType, int color, String mimicBlock) {
        int assignedNumber = nextAvailableNumbers.getOrDefault(blockType, 1);
        
        // Check maximum limits
        int maxBlocks = getMaxBlocksForType(blockType);
        if (assignedNumber > maxBlocks) {
            return -1; // No more slots available
        }
        
        String identifier = blockType + assignedNumber;
        assignedBlocks.put(identifier, new UserBlockData(color, mimicBlock));
        nextAvailableNumbers.put(blockType, assignedNumber + 1);
        
        setDirty(); // Mark for saving
        return assignedNumber;
    }
    
    /**
     * Assigns a new user block with a custom name
     */
    public String assignUserBlockWithCustomName(String blockType, int color, String mimicBlock, String customName) {
        // Check if custom name is already taken
        if (customNameMappings.containsKey(customName)) {
            return null; // Custom name already exists
        }
        
        int assignedNumber = assignUserBlock(blockType, color, mimicBlock);
        if (assignedNumber == -1) {
            return null; // No more slots available
        }
        
        String internalIdentifier = blockType + assignedNumber;
        customNameMappings.put(customName, internalIdentifier);
        reverseCustomNameMappings.put(internalIdentifier, customName);
        
        setDirty(); // Mark for saving
        return internalIdentifier;
    }
    
    /**
     * Gets the internal identifier for a custom name
     */
    public String getInternalIdentifier(String customName) {
        return customNameMappings.get(customName);
    }
    
    /**
     * Gets the custom name for an internal identifier
     */
    public String getCustomName(String internalIdentifier) {
        return reverseCustomNameMappings.get(internalIdentifier);
    }
    
    /**
     * Gets the user block data for a given identifier (e.g., "wool1")
     */
    public UserBlockData getUserBlockData(String identifier) {
        return assignedBlocks.get(identifier);
    }
    
    /**
     * Gets the user block data for a given type and number
     */
    public UserBlockData getUserBlockData(String blockType, int number) {
        return getUserBlockData(blockType + number);
    }
    
    private int getMaxBlocksForType(String blockType) {
        return switch (blockType) {
            case "wool", "stone", "concrete", "wood", "dirt", "sand", "deepslate", "cobblestone", "smooth_stone" -> 20;
            default -> 20; // Default to 20 for any new block types
        };
    }
    
    @Override
    public CompoundTag save(CompoundTag tag) {
        // Save next available numbers
        CompoundTag nextNumbers = new CompoundTag();
        for (Map.Entry<String, Integer> entry : nextAvailableNumbers.entrySet()) {
            nextNumbers.putInt(entry.getKey(), entry.getValue());
        }
        tag.put("nextNumbers", nextNumbers);
        
        // Save assigned blocks
        ListTag assignedList = new ListTag();
        for (Map.Entry<String, UserBlockData> entry : assignedBlocks.entrySet()) {
            CompoundTag blockTag = new CompoundTag();
            blockTag.putString("identifier", entry.getKey());
            blockTag.putInt("color", entry.getValue().color());
            blockTag.putString("mimicBlock", entry.getValue().mimicBlock());
            assignedList.add(blockTag);
        }
        tag.put("assignedBlocks", assignedList);
        
        // Save custom name mappings
        CompoundTag customNames = new CompoundTag();
        for (Map.Entry<String, String> entry : customNameMappings.entrySet()) {
            customNames.putString(entry.getKey(), entry.getValue());
        }
        tag.put("customNames", customNames);
        
        return tag;
    }
    
    public static UserBlockRegistry load(CompoundTag tag) {
        UserBlockRegistry registry = new UserBlockRegistry();
        
        // Load next available numbers
        if (tag.contains("nextNumbers")) {
            CompoundTag nextNumbers = tag.getCompound("nextNumbers");
            for (String key : nextNumbers.getAllKeys()) {
                registry.nextAvailableNumbers.put(key, nextNumbers.getInt(key));
            }
        }
        
        // Load assigned blocks
        if (tag.contains("assignedBlocks")) {
            ListTag assignedList = tag.getList("assignedBlocks", 10); // 10 = CompoundTag
            for (int i = 0; i < assignedList.size(); i++) {
                CompoundTag blockTag = assignedList.getCompound(i);
                String identifier = blockTag.getString("identifier");
                int color = blockTag.getInt("color");
                String mimicBlock = blockTag.getString("mimicBlock");
                registry.assignedBlocks.put(identifier, new UserBlockData(color, mimicBlock));
            }
        }
        
        // Load custom name mappings
        if (tag.contains("customNames")) {
            CompoundTag customNames = tag.getCompound("customNames");
            for (String customName : customNames.getAllKeys()) {
                String internalId = customNames.getString(customName);
                registry.customNameMappings.put(customName, internalId);
                registry.reverseCustomNameMappings.put(internalId, customName);
            }
        }
        
        return registry;
    }
    
    /**
     * Gets the user block registry for the given server level
     */
    public static UserBlockRegistry get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(UserBlockRegistry::load, UserBlockRegistry::new, DATA_NAME);
    }
    
    /**
     * Clears all user block assignments and resets counters
     * @return the number of blocks that were cleared
     */
    public int clearAllUserBlocks() {
        int clearedCount = assignedBlocks.size();
        assignedBlocks.clear();
        customNameMappings.clear();
        reverseCustomNameMappings.clear();
        
        // Reset next available numbers to 1
        nextAvailableNumbers.clear();
        nextAvailableNumbers.put("wool", 1);
        nextAvailableNumbers.put("stone", 1);
        nextAvailableNumbers.put("concrete", 1);
        nextAvailableNumbers.put("wood", 1);
        nextAvailableNumbers.put("dirt", 1);
        nextAvailableNumbers.put("sand", 1);
        nextAvailableNumbers.put("deepslate", 1);
        
        setDirty(); // Mark as dirty to save changes
        return clearedCount;
    }
    
    /**
     * Removes a specific custom name mapping
     */
    public boolean removeCustomName(String customName) {
        String internalId = customNameMappings.remove(customName);
        if (internalId != null) {
            reverseCustomNameMappings.remove(internalId);
            assignedBlocks.remove(internalId);
            setDirty();
            return true;
        }
        return false;
    }
    
    /**
     * Gets all custom names currently registered
     */
    public java.util.Set<String> getAllCustomNames() {
        return customNameMappings.keySet();
    }
    
    /**
     * Data class to hold user block information
     */
    public record UserBlockData(int color, String mimicBlock) {}
}