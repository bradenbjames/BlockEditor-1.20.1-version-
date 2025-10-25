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

public class BlockEditorHistory {
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

    public static List<CreatedBlockInfo> getHistory() {
        return createdBlocksHistory;
    }

    public static void saveHistoryToFile() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            File historyFile = new File(configDir, "blockeditor_history.dat");
            CompoundTag rootTag = new CompoundTag();
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
            if (rootTag == null || !rootTag.contains("history")) {
                return;
            }
            ListTag historyList = rootTag.getList("history", Tag.TAG_COMPOUND);
            createdBlocksHistory.clear();
            for (int i = 0; i < historyList.size(); i++) {
                CompoundTag blockTag = historyList.getCompound(i);
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
                    createdBlocksHistory.add(info);
                }
            }
        } catch (IOException e) {
            BlockEditorMod.LOGGER.warn("Failed to load block history", e);
        }
    }
}

