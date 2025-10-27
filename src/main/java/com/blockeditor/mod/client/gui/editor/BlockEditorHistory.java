package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.BlockEditorMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockEditorHistory {
    // Folder to organize blocks
    public static class BlockFolder {
        public String id; // unique ID for persistence
        public String name;
        public List<CreatedBlockInfo> blocks;
        public boolean expanded;
        public int color; // RGB tint for UI (0xRRGGBB)

        public BlockFolder(String name) {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.blocks = new ArrayList<>();
            this.expanded = true; // default to expanded
            this.color = 0x66CCFF; // pleasant default tint
        }

        // For deserialization
        public BlockFolder(String id, String name, boolean expanded) {
            this.id = id;
            this.name = name;
            this.blocks = new ArrayList<>();
            this.expanded = expanded;
            this.color = 0x66CCFF;
        }
    }

    public static class CreatedBlockInfo {
        public Block originalBlock;
        public String hexColor;
        public int color;
        public String blockName;
        public long timestamp;

        public CreatedBlockInfo(Block block, String hex, int color) {
            this(block, hex, color, null);
        }

        public CreatedBlockInfo(Block block, String hex, int color, String customName) {
            this.originalBlock = block;
            this.hexColor = hex;
            this.color = color;
            this.timestamp = System.currentTimeMillis();
            if (customName != null && !customName.trim().isEmpty()) {
                this.blockName = customName.trim();
            } else {
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
                String path = blockId.getPath();
                if (path.startsWith("dynamic_block_")) {
                    this.blockName = path.substring(14) + " #" + hex;
                } else if (path.equals("dynamic_block")) {
                    this.blockName = "stone #" + hex;
                } else {
                    this.blockName = path.replace("_", " ") + " #" + hex;
                }
            }
        }
    }

    private static final List<CreatedBlockInfo> createdBlocksHistory = new ArrayList<>();
    private static final List<BlockFolder> folders = new ArrayList<>();

    public static List<CreatedBlockInfo> getHistory() {
        return createdBlocksHistory;
    }

    public static List<BlockFolder> getFolders() {
        return folders;
    }

    public static BlockFolder createFolder(String name) {
        BlockFolder folder = new BlockFolder(name);
        folders.add(folder);
        saveHistoryToFile();
        return folder;
    }

    // Overload allowing initial color
    public static BlockFolder createFolder(String name, int colorRgb) {
        BlockFolder folder = new BlockFolder(name);
        folder.color = colorRgb & 0xFFFFFF;
        folders.add(folder);
        saveHistoryToFile();
        return folder;
    }

    public static void deleteFolder(BlockFolder folder) {
        if (folder == null) return;
        // Remove folder and all its items permanently
        folders.remove(folder);
        // Do not move items back to history; they are deleted with the folder
        saveHistoryToFile();
    }

    public static void deleteBlock(CreatedBlockInfo block) {
        if (block == null) return;
        // Remove from main history
        createdBlocksHistory.remove(block);
        // Remove from any folder that may contain it
        for (BlockFolder f : folders) {
            f.blocks.remove(block);
        }
        saveHistoryToFile();
    }

    public static void moveBlockToFolder(CreatedBlockInfo block, BlockFolder targetFolder) {
        // Remove from all locations
        createdBlocksHistory.remove(block);
        for (BlockFolder f : folders) {
            f.blocks.remove(block);
        }
        // Add to target
        if (targetFolder != null) {
            targetFolder.blocks.add(block);
        } else {
            createdBlocksHistory.add(block);
        }
        saveHistoryToFile();
    }

    public static void saveHistoryToFile() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            File historyFile = new File(configDir, "blockeditor_history.dat");
            CompoundTag rootTag = new CompoundTag();
            
            // Save UI preferences
            rootTag.putBoolean("compactView", com.blockeditor.mod.client.gui.editor.HistoryPanel.isCompactView());
            
            // Save main history
            ListTag historyList = new ListTag();
            for (CreatedBlockInfo info : createdBlocksHistory) {
                CompoundTag blockTag = new CompoundTag();
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(info.originalBlock);
                blockTag.putString("block", blockId.toString());
                blockTag.putString("hexColor", info.hexColor);
                blockTag.putInt("color", info.color);
                blockTag.putLong("timestamp", info.timestamp);
                blockTag.putString("customName", info.blockName);
                historyList.add(blockTag);
            }
            rootTag.put("history", historyList);
            
            // Save folders
            ListTag foldersList = new ListTag();
            for (BlockFolder folder : folders) {
                CompoundTag folderTag = new CompoundTag();
                folderTag.putString("id", folder.id);
                folderTag.putString("name", folder.name);
                folderTag.putBoolean("expanded", folder.expanded);
                folderTag.putInt("color", folder.color);
                
                ListTag folderBlocks = new ListTag();
                for (CreatedBlockInfo info : folder.blocks) {
                    CompoundTag blockTag = new CompoundTag();
                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(info.originalBlock);
                    blockTag.putString("block", blockId.toString());
                    blockTag.putString("hexColor", info.hexColor);
                    blockTag.putInt("color", info.color);
                    blockTag.putLong("timestamp", info.timestamp);
                    blockTag.putString("customName", info.blockName);
                    folderBlocks.add(blockTag);
                }
                folderTag.put("blocks", folderBlocks);
                foldersList.add(folderTag);
            }
            rootTag.put("folders", foldersList);
            
            NbtIo.write(rootTag, historyFile);
        } catch (IOException e) {
            BlockEditorMod.LOGGER.warn("Failed to save block history", e);
        }
    }

    public static void loadHistoryFromFile() {
        try {
            File historyFile = new File("config/blockeditor_history.dat");
            if (!historyFile.exists()) {
                return;
            }
            CompoundTag rootTag = NbtIo.read(historyFile);
            if (rootTag == null) {
                return;
            }
            
            // Load UI preferences
            if (rootTag.contains("compactView")) {
                com.blockeditor.mod.client.gui.editor.HistoryPanel.setCompactView(rootTag.getBoolean("compactView"));
            }
            
            // Load main history
            createdBlocksHistory.clear();
            if (rootTag.contains("history")) {
                ListTag historyList = rootTag.getList("history", Tag.TAG_COMPOUND);
                for (int i = 0; i < historyList.size(); i++) {
                    CompoundTag blockTag = historyList.getCompound(i);
                    CreatedBlockInfo info = loadBlockInfo(blockTag);
                    if (info != null) {
                        createdBlocksHistory.add(info);
                    }
                }
            }
            
            // Load folders
            folders.clear();
            if (rootTag.contains("folders")) {
                ListTag foldersList = rootTag.getList("folders", Tag.TAG_COMPOUND);
                for (int i = 0; i < foldersList.size(); i++) {
                    CompoundTag folderTag = foldersList.getCompound(i);
                    String id = folderTag.getString("id");
                    String name = folderTag.getString("name");
                    boolean expanded = folderTag.getBoolean("expanded");
                    BlockFolder folder = new BlockFolder(id, name, expanded);
                    if (folderTag.contains("color")) {
                        folder.color = folderTag.getInt("color") & 0xFFFFFF;
                    }
                    
                    if (folderTag.contains("blocks")) {
                        ListTag folderBlocks = folderTag.getList("blocks", Tag.TAG_COMPOUND);
                        for (int j = 0; j < folderBlocks.size(); j++) {
                            CompoundTag blockTag = folderBlocks.getCompound(j);
                            CreatedBlockInfo info = loadBlockInfo(blockTag);
                            if (info != null) {
                                folder.blocks.add(info);
                            }
                        }
                    }
                    folders.add(folder);
                }
            }
        } catch (IOException e) {
            BlockEditorMod.LOGGER.warn("Failed to load block history", e);
        }
    }
    
    private static CreatedBlockInfo loadBlockInfo(CompoundTag blockTag) {
        String blockIdStr = blockTag.getString("block");
        ResourceLocation blockId = new ResourceLocation(blockIdStr);
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block != null && block != Blocks.AIR) {
            String hexColor = blockTag.getString("hexColor");
            int color = blockTag.getInt("color");
            String customName = blockTag.contains("customName") ? blockTag.getString("customName") : null;
            CreatedBlockInfo info = new CreatedBlockInfo(block, hexColor, color, customName);
            if (blockTag.contains("timestamp")) {
                info.timestamp = blockTag.getLong("timestamp");
            }
            return info;
        }
        return null;
    }
}

