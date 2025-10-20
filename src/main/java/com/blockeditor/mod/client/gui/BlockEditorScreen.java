package com.blockeditor.mod.client.gui;

import com.blockeditor.mod.network.CreateBlockPacket;
import com.blockeditor.mod.network.ModNetworking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import com.blockeditor.mod.registry.ModBlocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockEditorScreen extends Screen {

    private String hexColor = "FFFFFF";
    private Block selectedBlock = Blocks.STONE;
    private int scrollOffset = 0;
    private int historyScrollOffset = 0;

    private EditBox hexBox;
    private EditBox searchBox;

    private List<Block> allBlocks = new ArrayList<>();
    private List<Block> filteredBlocks = new ArrayList<>();

    // History of created blocks
    private static List<CreatedBlockInfo> createdBlocksHistory = new ArrayList<>();

    private static final int BLOCKS_PER_ROW = 8;
    private static final int BLOCK_SIZE = 32;
    private static final int BLOCK_PADDING = 4;

    // Inner class to store created block info
    private static class CreatedBlockInfo {
        Block originalBlock;
        String hexColor;
        int color;
        String blockName;
        long timestamp;

        CreatedBlockInfo(Block block, String hex, int color) {
            this.originalBlock = block;
            this.hexColor = hex;
            this.color = color;
            this.timestamp = System.currentTimeMillis();
            
            // Generate a readable block name
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            String path = blockId.getPath();
            if (path.startsWith("dynamic_block_")) {
                this.blockName = path.substring(14) + " #" + hex; // Remove "dynamic_block_" prefix
            } else if (path.equals("dynamic_block")) {
                this.blockName = "stone #" + hex; // Default case
            } else {
                this.blockName = path.replace("_", " ") + " #" + hex;
            }
        }
    }

    public BlockEditorScreen() {
        super(Component.literal("Block Editor - Fixed Scroll"));
        loadHistoryFromFile(); // Load history when screen is created
    }
    
    private static void saveHistoryToFile() {
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
                historyList.add(blockTag);
            }
            
            rootTag.put("history", historyList);
            NbtIo.write(rootTag, historyFile);
        } catch (IOException e) {
            System.err.println("Failed to save block history: " + e.getMessage());
        }
    }
    
    private static void loadHistoryFromFile() {
        try {
            File historyFile = new File("config/blockeditor_history.dat");
            if (!historyFile.exists()) {
                return; // No history file exists yet
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
                    
                    CreatedBlockInfo info = new CreatedBlockInfo(block, hexColor, color);
                    if (blockTag.contains("timestamp")) {
                        info.timestamp = blockTag.getLong("timestamp");
                    }
                    createdBlocksHistory.add(info);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load block history: " + e.getMessage());
        }
    }

    @Override
    protected void init() {
        super.init();
        


        // Get all registered blocks (filter to only allowed blocks)
        allBlocks.clear();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR) {
                // Only add blocks that pass our validation
                if (isFullSolidBlock(block)) {
                    allBlocks.add(block);
                }
            }
        }
        filteredBlocks = new ArrayList<>(allBlocks);

        int centerX = this.width / 2;

        // Calculate block grid width: 8 blocks × (32 + 4 padding) = 288 pixels
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridStartX = centerX - (blockGridWidth / 2);

        // Search box - align with the left edge of the block grid
        int searchWidth = 150; // Slightly smaller to fit better
        searchBox = new EditBox(this.font, gridStartX, 30, searchWidth, 20, Component.literal("Search"));
        searchBox.setHint(Component.literal("Search blocks..."));
        this.addRenderableWidget(searchBox);

        // Hex color input box - positioned after search box
        int hexY = 30;
        int hexX = gridStartX + searchWidth + 20; // After search box + gap

        hexBox = new EditBox(this.font, hexX, hexY, 80, 20, Component.literal("Hex Color"));
        hexBox.setValue(hexColor);
        hexBox.setMaxLength(6);
        hexBox.setHint(Component.literal("FFFFFF"));
        this.addRenderableWidget(hexBox);

        // Buttons - positioned just above player inventory
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));
        // Position buttons just above player inventory (which is typically ~76 pixels from bottom)
        int buttonY = this.height - 85; // Position buttons just above player inventory
        int buttonSpacing = 10;
        int buttonWidth = 90;
        int totalButtonWidth = (buttonWidth * 2) + buttonSpacing;
        int buttonStartX = centerX - (totalButtonWidth / 2);

        // Create Block button
        this.addRenderableWidget(Button.builder(
            Component.literal("Create Block"),
            button -> createColoredBlock()
        ).bounds(buttonStartX, buttonY, buttonWidth, 20).build());

        // Cancel button
        this.addRenderableWidget(Button.builder(
            Component.literal("Cancel"),
            button -> this.onClose()
        ).bounds(buttonStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, 20).build());


    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        
        // Render background
        graphics.fill(0, 0, this.width, this.height, 0xC0101010);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);

        // Draw block grid
        renderBlockGrid(graphics, mouseX, mouseY);

        // Calculate positions to match init()
        int centerX = this.width / 2;
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridStartX = centerX - (blockGridWidth / 2);
        int searchWidth = 150;
        int hexX = gridStartX + searchWidth + 20;
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));

        // Draw "#" label before hex input box
        graphics.drawString(this.font, "#", hexX - 10, 34, 0xFFFFFF);

        // Draw block preview aligned with hex input box height
        int color = parseHexColor(hexBox.getValue());
        int previewSize = 20; // Match hex input box height
        int blockPreviewX = hexX + 90;
        int blockPreviewY = 30; // Align with hex input box Y position (same as hexBox Y)
        
        // Save the current pose
        var pose = graphics.pose();
        pose.pushPose();
        
        // Scale and translate for proper sizing - no background, just the block
        pose.translate(blockPreviewX, blockPreviewY, 0);

        // Apply color tint using RenderSystem
        RenderSystem.setShaderColor(
            ((color >> 16) & 0xFF) / 255.0f,
            ((color >> 8) & 0xFF) / 255.0f,
            (color & 0xFF) / 255.0f,
            1.0f
        );

        // Render the selected block item with tint
        graphics.renderItem(selectedBlock.asItem().getDefaultInstance(), 0, 0);

        // Reset color and transform
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        pose.popPose();

        // Draw history panel on the right side
        renderHistoryPanel(graphics);

        // Render all widgets (buttons and text boxes) - THIS IS CRITICAL
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderHistoryPanel(GuiGraphics graphics) {
        
        // Determine if we should use condensed mode based on available width
        boolean isCondensed = this.width < 1000; // Use condensed mode for smaller screens
        
        // Calculate panel dimensions based on mode
        int panelWidth = isCondensed ? 60 : 170; // Narrow for condensed, wide for normal
        int panelX = this.width - panelWidth - 10; // 10px margin from right edge
        int panelY = 60;
        int panelHeight = this.height - panelY - 20; // Leave only 20px spacing at the bottom
        
        if (createdBlocksHistory.isEmpty()) {
            // Draw empty panel to show it exists
            graphics.fill(panelX - 5, panelY - 25, panelX + panelWidth + 5, panelY + 75, 0xE0000000);
            // Draw title bar
            graphics.fill(panelX - 5, panelY - 25, panelX + panelWidth + 5, panelY - 5, 0xFF333333);
            
            if (!isCondensed) {
                graphics.drawCenteredString(this.font, "§eRecent Blocks", panelX + panelWidth / 2, panelY - 18, 0xFFFFFF);
                graphics.drawCenteredString(this.font, "§7No blocks created yet", panelX + panelWidth / 2, panelY + 20, 0xAAAAAA);
            } else {
                graphics.drawCenteredString(this.font, "§eRecent", panelX + panelWidth / 2, panelY - 18, 0xFFFFFF);
                graphics.drawCenteredString(this.font, "§7None", panelX + panelWidth / 2, panelY + 20, 0xAAAAAA);
            }
            return;
        }

        // Calculate layout based on mode
        int itemHeight = isCondensed ? 20 : 24; // Smaller items in condensed mode
        int maxVisibleItems = panelHeight / itemHeight;
        int totalItems = createdBlocksHistory.size();

        // Draw panel background
        graphics.fill(panelX - 5, panelY - 25, panelX + panelWidth + 5, panelY + panelHeight + 5, 0xE0000000);

        // Draw title bar
        graphics.fill(panelX - 5, panelY - 25, panelX + panelWidth + 5, panelY - 5, 0xFF333333);
        if (isCondensed) {
            graphics.drawCenteredString(this.font, "§eRecent", panelX + panelWidth / 2, panelY - 18, 0xFFFFFF);
        } else {
            graphics.drawCenteredString(this.font, "§eRecent Blocks", panelX + panelWidth / 2, panelY - 18, 0xFFFFFF);
        }

        // Calculate visible range
        int startIndex = Math.max(0, Math.min(historyScrollOffset, totalItems - maxVisibleItems));
        int endIndex = Math.min(totalItems, startIndex + maxVisibleItems);

        // Enable scissoring for scrolling
        graphics.enableScissor(panelX - 5, panelY, panelX + panelWidth + 5, panelY + panelHeight);

        // Draw each visible created block in history
        for (int i = startIndex; i < endIndex; i++) {
            CreatedBlockInfo info = createdBlocksHistory.get(i);
            int itemY = panelY + ((i - startIndex) * itemHeight);

            // Draw item background (alternating colors)
            int bgColor = (i % 2 == 0) ? 0x40FFFFFF : 0x20FFFFFF;
            graphics.fill(panelX, itemY, panelX + panelWidth, itemY + itemHeight - 1, bgColor);

            // Draw block icon with color tint
            var pose = graphics.pose();
            pose.pushPose();

            RenderSystem.setShaderColor(
                ((info.color >> 16) & 0xFF) / 255.0f,
                ((info.color >> 8) & 0xFF) / 255.0f,
                (info.color & 0xFF) / 255.0f,
                1.0f
            );

            if (isCondensed) {
                // Condensed mode: center the block icon in the narrow panel
                graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), panelX + (panelWidth - 16) / 2, itemY + 2);
            } else {
                // Normal mode: block on left, text on right
                graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), panelX + 2, itemY + 4);
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            pose.popPose();

            // Draw block name only in normal mode (not condensed)
            if (!isCondensed) {
                String displayName = info.blockName;
                if (font.width(displayName) > panelWidth - 25) {
                    displayName = font.plainSubstrByWidth(displayName, panelWidth - 25) + "...";
                }
                graphics.drawString(this.font, displayName, panelX + 20, itemY + 6, 0xFFFFFF);
            }
        }

        graphics.disableScissor();

        // Draw scroll indicators 
        if (totalItems > maxVisibleItems) {
            // Draw scrollbar background
            graphics.fill(panelX + panelWidth - 3, panelY, panelX + panelWidth, panelY + panelHeight, 0xFF666666);
            
            // Draw scrollbar thumb
            int thumbHeight = Math.max(10, (maxVisibleItems * panelHeight) / totalItems);
            int thumbY = panelY + (startIndex * (panelHeight - thumbHeight)) / Math.max(1, totalItems - maxVisibleItems);
            graphics.fill(panelX + panelWidth - 3, thumbY, panelX + panelWidth, thumbY + thumbHeight, 0xFFCCCCCC);

            // Draw simple scroll indicators
            if (startIndex > 0) {
                graphics.drawString(this.font, "▲", panelX + panelWidth - 12, panelY - 10, 0xFFFFFF);
            }
            if (endIndex < totalItems) {
                graphics.drawString(this.font, "▼", panelX + panelWidth - 12, panelY + panelHeight - 5, 0xFFFFFF);
            }
        }
    }

    private void renderBlockGrid(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = this.width / 2 - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
        int startY = 55;
        int maxRows = 4;
        int gridHeight = maxRows * (BLOCK_SIZE + BLOCK_PADDING);

        // Filter blocks based on search
        String search = searchBox.getValue().toLowerCase();
        if (!search.isEmpty()) {
            filteredBlocks = new ArrayList<>();
            for (Block block : allBlocks) {
                String blockName = BuiltInRegistries.BLOCK.getKey(block).toString();
                if (blockName.contains(search)) {
                    filteredBlocks.add(block);
                }
            }
        } else {
            filteredBlocks = new ArrayList<>(allBlocks);
        }

        // Render visible blocks
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < BLOCKS_PER_ROW; col++) {
                int index = (row + scrollOffset) * BLOCKS_PER_ROW + col;
                if (index >= filteredBlocks.size()) break;

                Block block = filteredBlocks.get(index);
                int x = startX + col * (BLOCK_SIZE + BLOCK_PADDING);
                int y = startY + row * (BLOCK_SIZE + BLOCK_PADDING);

                // Draw block background
                int bgColor = block == selectedBlock ? 0xFF44FF44 : 0xFF888888;
                graphics.fill(x, y, x + BLOCK_SIZE, y + BLOCK_SIZE, bgColor);

                // Draw block item
                graphics.renderItem(block.asItem().getDefaultInstance(), x + 8, y + 8);

                // Highlight on hover
                if (mouseX >= x && mouseX < x + BLOCK_SIZE && mouseY >= y && mouseY < y + BLOCK_SIZE) {
                    graphics.fill(x, y, x + BLOCK_SIZE, y + BLOCK_SIZE, 0x80FFFFFF);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if clicking in history panel first
        boolean isCondensed = this.width < 1000;
        int panelWidth = isCondensed ? 60 : 170;
        int panelX = this.width - panelWidth - 10;
        int panelY = 60;
        int panelHeight = 300;
        int itemHeight = isCondensed ? 20 : 24;
        
        // History panel item clicks
        if (mouseX >= panelX && mouseX <= panelX + panelWidth && 
            mouseY >= panelY && mouseY <= panelY + panelHeight) {
            
            int clickedIndex = (int) ((mouseY - panelY) / itemHeight) + historyScrollOffset;
            if (clickedIndex >= 0 && clickedIndex < createdBlocksHistory.size()) {
                CreatedBlockInfo info = createdBlocksHistory.get(clickedIndex);
                selectedBlock = info.originalBlock;
                hexColor = info.hexColor;
                hexBox.setValue(hexColor);
                return true;
            }
        }

        // Handle block selection clicks - MUST match renderBlockGrid positions
        int startX = this.width / 2 - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
        int startY = 55;  // Changed from 60 to match renderBlockGrid
        int maxRows = 4;  // Changed from 6 to match renderBlockGrid

        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < BLOCKS_PER_ROW; col++) {
                int index = (row + scrollOffset) * BLOCKS_PER_ROW + col;
                if (index >= filteredBlocks.size()) break;

                int x = startX + col * (BLOCK_SIZE + BLOCK_PADDING);
                int y = startY + row * (BLOCK_SIZE + BLOCK_PADDING);

                if (mouseX >= x && mouseX < x + BLOCK_SIZE && mouseY >= y && mouseY < y + BLOCK_SIZE) {
                    selectedBlock = filteredBlocks.get(index);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        // Track mouse position for debugging
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Simple scroll for history panel - right side of screen
        if (mouseX > this.width * 0.75 && !createdBlocksHistory.isEmpty()) {
            // Calculate maxScroll using the same logic as render method
            boolean isCondensed = this.width < 1000;
            int panelY = 60;
            int panelHeight = this.height - panelY - 20; // Same as render method
            int itemHeight = isCondensed ? 20 : 24;
            int maxVisibleItems = panelHeight / itemHeight;
            int maxScroll = Math.max(0, createdBlocksHistory.size() - maxVisibleItems);
            
            if (delta > 0 && historyScrollOffset > 0) {
                historyScrollOffset--;
                return true;
            } else if (delta < 0 && historyScrollOffset < maxScroll) {
                historyScrollOffset++;
                return true;
            }
        }
        
        // Check if mouse is over main block grid area
        int startX = this.width / 2 - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
        int startY = 55;
        int gridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridHeight = 4 * (BLOCK_SIZE + BLOCK_PADDING);
        
        if (mouseX >= startX && mouseX <= startX + gridWidth && 
            mouseY >= startY && mouseY <= startY + gridHeight) {
            // Scroll in main block grid
            int maxScroll = Math.max(0, (filteredBlocks.size() / BLOCKS_PER_ROW) - 4);
            int oldOffset = scrollOffset;
            
            if (delta > 0) {
                scrollOffset = Math.max(0, scrollOffset - 1);
            } else {
                scrollOffset = Math.min(maxScroll, scrollOffset + 1);
            }
            
            return scrollOffset != oldOffset;
        }
        
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Test direct scroll with number keys (not affected by text focus)
        if (keyCode == 49) { // 1 key
            scrollHistoryUp();
            return true;
        } else if (keyCode == 50) { // 2 key  
            scrollHistoryDown();
            return true;
        } else if (keyCode == 32) { // SPACE
            if (!createdBlocksHistory.isEmpty()) {
                int maxScroll = Math.max(0, createdBlocksHistory.size() - 5);
                historyScrollOffset = (historyScrollOffset + 1) % (maxScroll + 1);
            }
            return true;
        }
        
        // Add keyboard controls for history scrolling (Arrow keys and Page Up/Down)
        if (keyCode == 265) { // Up Arrow
            scrollHistoryUp();
            return true;
        } else if (keyCode == 264) { // Down Arrow
            scrollHistoryDown();
            return true;
        } else if (keyCode == 266) { // Page Up
            scrollHistoryUp();
            return true;
        } else if (keyCode == 267) { // Page Down
            scrollHistoryDown();
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void scrollHistoryUp() {
        if (historyScrollOffset > 0) {
            historyScrollOffset--;
        }
    }

    private void scrollHistoryDown() {
        int maxOffset = Math.max(0, createdBlocksHistory.size() - 10);
        if (historyScrollOffset < maxOffset) {
            historyScrollOffset++;
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Check for scroll input using Minecraft's input system
        checkScrollInput();

        // Validate and clean hex input (only allow hex characters)
        String value = hexBox.getValue().toUpperCase();
        value = value.replaceAll("[^0-9A-F]", "");
        if (value.length() > 6) {
            value = value.substring(0, 6);
        }
        hexColor = value;
        if (!hexBox.getValue().equals(value)) {
            hexBox.setValue(value);
        }
    }

    private void checkScrollInput() {
        // Use Minecraft's direct input system to detect scroll
        if (minecraft != null && minecraft.mouseHandler != null) {
            // Get mouse position
            double mouseX = minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
            double mouseY = minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
            
            // Check if mouse is over history panel
            boolean isCondensed = this.width < 1000;
            int panelWidth = isCondensed ? 60 : 170;
            int panelX = this.width - panelWidth - 10;
            int panelY = 60;
            int panelHeight = 300;
            
            if (mouseX >= panelX && mouseX <= panelX + panelWidth && 
                mouseY >= panelY && mouseY <= panelY + panelHeight) {
                
                // Check for key-based scrolling as alternative
                if (org.lwjgl.glfw.GLFW.glfwGetKey(minecraft.getWindow().getWindow(), org.lwjgl.glfw.GLFW.GLFW_KEY_UP) == org.lwjgl.glfw.GLFW.GLFW_PRESS) {
                    if (System.currentTimeMillis() % 200 < 50) { // Throttle key repeat
                        scrollHistoryUp();
                    }
                }
                if (org.lwjgl.glfw.GLFW.glfwGetKey(minecraft.getWindow().getWindow(), org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN) == org.lwjgl.glfw.GLFW.GLFW_PRESS) {
                    if (System.currentTimeMillis() % 200 < 50) { // Throttle key repeat
                        scrollHistoryDown();
                    }
                }
            }
        }
    }

    private int parseHexColor(String hex) {
        try {
            // Remove any # prefix if present
            hex = hex.replace("#", "").trim();

            // Pad with zeros if too short
            while (hex.length() < 6) {
                hex = "0" + hex;
            }

            // Parse hex string to integer
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return 0xFFFFFF; // Default to white on error
        }
    }

    private void createColoredBlock() {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Validate hex color is exactly 6 characters
            if (hexColor.length() != 6) {
                // Show error message to player
                if (this.minecraft.player != null) {
                    this.minecraft.player.displayClientMessage(
                        Component.literal("§cError: Hex color must be exactly 6 characters (e.g., FF0000 for red)"),
                        false
                    );
                }
                return;
            }

            // Determine which block type to use based on the selected block
            Block blockToUse = getBlockTypeForTexture(selectedBlock);

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockToUse);
            ResourceLocation mimicBlockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);

            System.out.println("BlockEditorScreen: Sending CreateBlockPacket to server");
            System.out.println("  Block type: " + blockId);
            System.out.println("  Mimic: " + mimicBlockId);
            System.out.println("  Color: " + hexColor.toUpperCase());

            // Send packet to server to create the item
            ModNetworking.sendToServer(new CreateBlockPacket(
                hexColor.toUpperCase(),
                mimicBlockId.toString(),
                blockId.toString()
            ));

            System.out.println("BlockEditorScreen: Packet sent!");

            // Add to history (client-side only for display)
            int color = parseHexColor(hexColor);
            createdBlocksHistory.add(0, new CreatedBlockInfo(selectedBlock, hexColor.toUpperCase(), color));
            if (createdBlocksHistory.size() > 500) {
                createdBlocksHistory.remove(500);
            }
            
            // Save history to file
            saveHistoryToFile();

            // Show success message
            String blockName = mimicBlockId.getPath().replace("_", " ");
            if (this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§aCreated colored block: §f" + blockName + " §7(#" + hexColor.toUpperCase() + ")"),
                    false
                );
            }
        }
        this.onClose();
    }

    // Helper method to check if a block is allowed (only specific base blocks)
    private boolean isFullSolidBlock(Block block) {
        String blockId = BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase();

        // Only allow these specific blocks (base versions only)
        String[] allowedBlocks = {
            "white_wool",           // White wool only
            "sand",                 // Regular sand
            "stone",                // Regular stone
            "smooth_stone",         // Smooth stone
            "white_concrete",       // White concrete only
            "oak_planks",          // Oak planks
            "bamboo_planks",       // Bamboo planks
            "cobblestone",         // Cobblestone
            "deepslate"            // Deepslate
        };

        // Check for exact matches only
        for (String allowedBlock : allowedBlocks) {
            if (blockId.equals(allowedBlock)) {
                return true;
            }
        }

        return false;
    }

    // Helper method to determine which block type to use based on the selected block
    private Block getBlockTypeForTexture(Block selectedBlock) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);
        String blockName = blockId.getPath().toLowerCase();

        System.out.println("getBlockTypeForTexture: blockId=" + blockId + ", blockName=" + blockName);

        // Check for texture category keywords
        if (blockName.contains("dirt") || blockName.contains("coarse")) {
            System.out.println("Matched DIRT texture");
            return ModBlocks.DYNAMIC_BLOCK_DIRT.get();
        } else if (blockName.contains("planks") || blockName.contains("wood")) {
            System.out.println("Matched WOOD texture");
            return ModBlocks.DYNAMIC_BLOCK_WOOD.get();
        } else if (blockName.contains("wool")) {
            System.out.println("Matched WOOL texture");
            return ModBlocks.DYNAMIC_BLOCK_WOOL.get();
        } else if (blockName.contains("concrete") && !blockName.contains("powder")) {
            System.out.println("Matched CONCRETE texture");
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE.get();
        } else if (blockName.contains("cobblestone")) {
            System.out.println("Matched COBBLESTONE texture");
            return ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get();
        } else if (blockName.contains("deepslate")) {
            System.out.println("Matched DEEPSLATE texture");
            return ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get();
        } else if (blockName.contains("sand")) {
            System.out.println("Matched SAND texture");
            return ModBlocks.DYNAMIC_BLOCK_SAND.get();
        } else if (blockName.contains("smooth_stone")) {
            System.out.println("Matched SMOOTH_STONE texture");
            return ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get();
        } else if (blockName.contains("stone")) {
            System.out.println("Matched STONE texture");
            return ModBlocks.DYNAMIC_BLOCK.get();
        } else {
            // Default to stone texture for any other blocks
            System.out.println("Defaulting to STONE texture");
            return ModBlocks.DYNAMIC_BLOCK.get();
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        // Try catching scroll events through char input
        if (codePoint == 'w' || codePoint == 'W') {
            System.out.println("MANUAL SCROLL: UP with W key");
            scrollHistoryUp();
            return true;
        }
        if (codePoint == 's' || codePoint == 'S') {
            System.out.println("MANUAL SCROLL: DOWN with S key");
            scrollHistoryDown();
            return true;
        }
        if (codePoint == ' ') {
            System.out.println("SPACE TEST: Directly incrementing historyScrollOffset from " + historyScrollOffset);
            historyScrollOffset = (historyScrollOffset + 1) % Math.max(1, createdBlocksHistory.size());
            System.out.println("SPACE TEST: historyScrollOffset is now " + historyScrollOffset);
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override 
    public void setFocused(net.minecraft.client.gui.components.events.GuiEventListener focused) {
        System.out.println("FOCUS DEBUG: setFocused called with " + (focused != null ? focused.getClass().getSimpleName() : "null"));
        super.setFocused(focused);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}