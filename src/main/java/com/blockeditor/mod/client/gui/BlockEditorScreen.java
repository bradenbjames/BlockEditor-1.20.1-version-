package com.blockeditor.mod.client.gui;

import com.blockeditor.mod.network.CreateBlockPacket;
import com.blockeditor.mod.network.ClearRegistryPacket;
import com.blockeditor.mod.network.ModNetworking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import com.blockeditor.mod.registry.ModItems;
import com.blockeditor.mod.network.CreateBlockPacket;
import com.blockeditor.mod.network.ModNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
    private EditBox nameBox;
    private Button createButton;
    private Button clearRegistryButton;

    // Confirmation dialog state
    private boolean showingClearConfirmation = false;

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
            this(block, hex, color, null);
        }

        CreatedBlockInfo(Block block, String hex, int color, String customName) {
            this.originalBlock = block;
            this.hexColor = hex;
            this.color = color;
            this.timestamp = System.currentTimeMillis();
            
            if (customName != null && !customName.trim().isEmpty()) {
                // Use custom name if provided
                this.blockName = customName.trim();
            } else {
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
    }

    public BlockEditorScreen() {
        super(Component.literal("Block Editor"));
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
                blockTag.putString("customName", info.blockName); // Save the custom name
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
                    String customName = blockTag.contains("customName") ? blockTag.getString("customName") : null;
                    
                    CreatedBlockInfo info = new CreatedBlockInfo(block, hexColor, color, customName);
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

        int centerX = this.width / 2 - 40; // Shift main content left by 40px

        // Calculate block grid width: 8 blocks × (32 + 4 padding) = 288 pixels
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridStartX = centerX - (blockGridWidth / 2);

        // Search box - hidden from UI but functionality kept
        int searchWidth = 120; 
        searchBox = new EditBox(this.font, -1000, -1000, searchWidth, 20, Component.literal("Search")); // Move off-screen
        searchBox.setHint(Component.literal("Search blocks..."));
        // Don't add to renderableWidget - keep functionality but hide UI
        
        // Hex color input box - positioned to align with block grid
        int hexY = 30;
        int hexX = gridStartX; // Align with block grid left edge

        // Custom hex input box with integrated # symbol and two-tone background
        hexBox = new EditBox(this.font, hexX + 16, hexY, 74, 20, Component.literal("Hex Color")) {
            @Override
            public void renderWidget(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                if (this.isVisible()) {
                    // Draw custom two-tone background aligned with EditBox position
                    int x = this.getX() - 16; // Use actual EditBox position minus prefix width
                    int y = this.getY();
                    int totalWidth = 90;
                    int prefixWidth = 16;
                    
                    // Dark gray background for # section
                    int darkGray = 0xFF666666;
                    graphics.fill(x, y, x + prefixWidth, y + 20, darkGray);
                    
                    // Light gray background for text input section
                    int lightBackground = 0xFF3F3F3F;
                    graphics.fill(x + prefixWidth, y, x + totalWidth, y + 20, lightBackground);
                    
                    // Border around entire box
                    int borderColor = this.isFocused() ? 0xFFFFFFFF : 0xFF999999;
                    graphics.fill(x - 1, y - 1, x + totalWidth + 1, y, borderColor); // Top
                    graphics.fill(x - 1, y + 20, x + totalWidth + 1, y + 21, borderColor); // Bottom
                    graphics.fill(x - 1, y - 1, x, y + 21, borderColor); // Left
                    graphics.fill(x + totalWidth, y - 1, x + totalWidth + 1, y + 21, borderColor); // Right
                    
                    // Draw # symbol in the dark section
                    graphics.drawString(BlockEditorScreen.this.font, "#", x + 4, y + 6, 0xFFFFFFFF);
                    
                    // Render text content manually in the light section
                    String text = this.getValue();
                    if (!text.isEmpty() || this.isFocused()) {
                        graphics.drawString(BlockEditorScreen.this.font, text, x + prefixWidth + 4, y + 6, 0xFFFFFFFF);
                        
                        // Draw cursor if focused
                        if (this.isFocused() && this.getCursorPosition() >= 0) {
                            int cursorX = x + prefixWidth + 4 + BlockEditorScreen.this.font.width(text.substring(0, Math.min(this.getCursorPosition(), text.length())));
                            if ((System.currentTimeMillis() / 500) % 2 == 0) { // Blinking cursor
                                graphics.fill(cursorX, y + 2, cursorX + 1, y + 18, 0xFFFFFFFF);
                            }
                        }
                    } else if (!this.isFocused()) {
                        // Draw hint text
                        graphics.drawString(BlockEditorScreen.this.font, "FFFFFF", x + prefixWidth + 4, y + 6, 0xFF888888);
                    }
                }
            }
        };
        
        hexBox.setValue(hexColor);
        hexBox.setMaxLength(6);
        hexBox.setHint(Component.literal("FFFFFF"));
        this.addRenderableWidget(hexBox);

        // Custom name input box - positioned after hex box with clear placeholder
        int nameX = hexX + 90 + 15; // After hex box + gap (updated for 90px hex box)
        nameBox = new EditBox(this.font, nameX, hexY, 140, 20, Component.literal("Block Name"));
        nameBox.setMaxLength(32);
        nameBox.setHint(Component.literal("Block name (required)"));
        this.addRenderableWidget(nameBox);

        // Buttons - positioned just above player inventory
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));
        // Position buttons just above player inventory (which is typically ~76 pixels from bottom)
        int buttonY = this.height - 85; // Position buttons just above player inventory
        int buttonSpacing = 10;
        int buttonWidth = 90;
        int totalButtonWidth = (buttonWidth * 2) + buttonSpacing;
        int buttonStartX = centerX - (totalButtonWidth / 2);

        // Create Block button
        createButton = Button.builder(
            Component.literal("Create Block"),
            button -> createColoredBlock()
        ).bounds(buttonStartX, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(createButton);

        // Cancel button
        this.addRenderableWidget(Button.builder(
            Component.literal("Cancel"),
            button -> this.onClose()
        ).bounds(buttonStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, 20).build());

        // Clear Registry button - positioned below other buttons
        clearRegistryButton = Button.builder(
            Component.literal("Clear Registry"),
            button -> handleClearRegistryClick()
        ).bounds(buttonStartX, buttonY + 25, buttonWidth, 20).build();
        this.addRenderableWidget(clearRegistryButton);


    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        
        // Render background
        graphics.fill(0, 0, this.width, this.height, 0xC0101010);

        // Draw title
        graphics.drawCenteredString(this.font, this.title, this.width / 2 - 40, 10, 0xFFFFFF);

        // Draw block grid
        renderBlockGrid(graphics, mouseX, mouseY);

        // Calculate positions to match init()
        int centerX = this.width / 2 - 40; // Shift main content left
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridStartX = centerX - (blockGridWidth / 2);
        int gridEndY = 55 + (4 * (BLOCK_SIZE + BLOCK_PADDING));

        // # symbol is now integrated into the hex input box
        
        // Draw red border around name box if invalid (use actual nameBox position)
        if (nameBox != null && !isNameBoxValid()) {
            int borderColor = 0xFFFF4444; // Bright red
            int boxX = nameBox.getX();
            int boxY = nameBox.getY(); 
            int boxWidth = nameBox.getWidth();
            int boxHeight = nameBox.getHeight();
            
            // Draw red border (2 pixels thick)
            graphics.fill(boxX - 2, boxY - 2, boxX + boxWidth + 2, boxY - 1, borderColor); // Top
            graphics.fill(boxX - 2, boxY + boxHeight + 1, boxX + boxWidth + 2, boxY + boxHeight + 2, borderColor); // Bottom  
            graphics.fill(boxX - 2, boxY - 1, boxX - 1, boxY + boxHeight + 1, borderColor); // Left
            graphics.fill(boxX + boxWidth + 1, boxY - 1, boxX + boxWidth + 2, boxY + boxHeight + 1, borderColor); // Right
        }

        // Draw block preview to the right of the name text box, same size as selection grid blocks
        int color = parseHexColor(hexBox.getValue());
        int previewSize = 16; // Same size as blocks in selection grid (16px item size, not scaled)
        
        // Position aligned with the rightmost column of the selection grid (column 7)
        int blockPreviewX = gridStartX + (7 * (BLOCK_SIZE + BLOCK_PADDING)) + 8; // Column 7 + 8px offset like grid blocks
        int blockPreviewY = nameBox.getY() + (nameBox.getHeight() - previewSize) / 2; // Vertically centered with name box
        
        // Save the current pose
        var pose = graphics.pose();
        pose.pushPose();
        
        // No scaling - render at natural 16px item size like the grid blocks
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
        renderHistoryPanel(graphics, mouseX, mouseY);

        // Render all widgets (buttons and text boxes) - THIS IS CRITICAL
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderHistoryPanel(GuiGraphics graphics, int mouseX, int mouseY) {
        
        // Calculate main block grid dimensions to check for overlap
        int centerX = this.width / 2;
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridEndX = centerX + (blockGridWidth / 2);
        
        // Calculate compact panel dimensions for optimal layout
        // Each item needs: 16px (block) + 2px (padding) + ~24px (hex text) = ~42px per item
        int itemWidth = 42; // Compact width per item
        int panelMargin = 10;
        
        // Calculate how many columns can actually fit based on available space
        int availableWidth = this.width - gridEndX - panelMargin - 20; // 20px buffer
        
        // Dynamic column calculation based on screen width for optimal 2-row layout
        int maxColumns;
        if (this.width >= 1800) { // For 1920x1080 and larger screens
            maxColumns = Math.min(6, availableWidth / itemWidth); // Allow up to 6 columns for 2 nice rows
        } else if (this.width >= 1400) { // Medium screens
            maxColumns = Math.min(4, availableWidth / itemWidth); // 4 columns max
        } else { // Smaller screens
            maxColumns = Math.min(3, availableWidth / itemWidth); // 3 columns max like before
        }
        
        int actualColumns = Math.max(1, maxColumns);
        
        // Only hide text if we're really cramped (less than 25px per item)
        boolean hideHexText = (availableWidth / actualColumns) < 25;
        
        int spacing = 2; // Small gap between boxes
        int individualItemWidth = hideHexText ? 20 : itemWidth;
        int finalPanelWidth = actualColumns * (individualItemWidth + spacing) - spacing + 8; // Include spacing and padding
        int panelX = this.width - finalPanelWidth - panelMargin;
        int panelY = 60;
        int panelHeight = this.height - panelY - 20; // Leave only 20px spacing at the bottom
        
        if (createdBlocksHistory.isEmpty()) {
            // Draw empty panel to show it exists
            graphics.fill(panelX - 5, panelY - 25, panelX + finalPanelWidth + 5, panelY + 75, 0xE0000000);
            // Draw title bar
            graphics.fill(panelX - 5, panelY - 25, panelX + finalPanelWidth + 5, panelY - 5, 0xFF333333);
            
            // Draw scaled title with better text handling
            var titlePose = graphics.pose();
            titlePose.pushPose();
            titlePose.translate(panelX + finalPanelWidth / 2, panelY - 18, 0);
            titlePose.scale(0.6f, 0.6f, 1.0f); // Smaller text
            
            // Split text if panel is narrow
            String titleText = "Recent Blocks";
            int textWidth = this.font.width(titleText);
            if (textWidth > finalPanelWidth * 1.4f) { // If text is too wide for panel
                graphics.drawCenteredString(this.font, "§eRecent", 0, -5, 0xFFFFFF);
                graphics.drawCenteredString(this.font, "§eBlocks", 0, 5, 0xFFFFFF);
            } else {
                graphics.drawCenteredString(this.font, "§eRecent Blocks", 0, 0, 0xFFFFFF);
            }
            titlePose.popPose();
            
            graphics.drawCenteredString(this.font, "§7No blocks yet", panelX + finalPanelWidth / 2, panelY + 20, 0xAAAAAA);
            return;
        }

        // Calculate layout based on dynamic column count
        int itemHeight = 20; // Height for each row
        int blocksPerRow = actualColumns;
        int totalRows = (int) Math.ceil((double) createdBlocksHistory.size() / blocksPerRow);
        int maxVisibleRows = panelHeight / itemHeight;
        int maxVisibleItems = maxVisibleRows * blocksPerRow;

        // Draw panel background
        graphics.fill(panelX - 5, panelY - 25, panelX + finalPanelWidth + 5, panelY + panelHeight + 5, 0xE0000000);

        // Draw title bar
        graphics.fill(panelX - 5, panelY - 25, panelX + finalPanelWidth + 5, panelY - 5, 0xFF333333);
        
        // Draw scaled title with better text handling
        var titlePose = graphics.pose();
        titlePose.pushPose();
        titlePose.translate(panelX + finalPanelWidth / 2, panelY - 18, 0);
        titlePose.scale(0.6f, 0.6f, 1.0f); // Smaller text
        
        // Split text if panel is narrow
        String titleText = "Recent Blocks";
        int textWidth = this.font.width(titleText);
        if (textWidth > finalPanelWidth * 1.4f) { // If text is too wide for panel
            graphics.drawCenteredString(this.font, "§eRecent", 0, -5, 0xFFFFFF);
            graphics.drawCenteredString(this.font, "§eBlocks", 0, 5, 0xFFFFFF);
        } else {
            graphics.drawCenteredString(this.font, "§eRecent Blocks", 0, 0, 0xFFFFFF);
        }
        titlePose.popPose();

        // Calculate visible range
        int startIndex = Math.max(0, Math.min(historyScrollOffset, createdBlocksHistory.size() - maxVisibleItems));
        int endIndex = Math.min(createdBlocksHistory.size(), startIndex + maxVisibleItems);

        // Enable scissoring for scrolling
        graphics.enableScissor(panelX - 5, panelY, panelX + finalPanelWidth + 5, panelY + panelHeight);

        // Draw blocks in grid (1 or 2 columns based on space)
        for (int i = startIndex; i < endIndex; i++) {
            CreatedBlockInfo info = createdBlocksHistory.get(i);
            
            int relativeIndex = i - startIndex;
            int row = relativeIndex / blocksPerRow;
            int col = relativeIndex % blocksPerRow;
            
            int itemY = panelY + (row * (itemHeight + spacing));
            
            // Calculate compact item positioning with spacing
            int itemX = panelX + 4 + (col * (individualItemWidth + spacing));
            
            // Check if mouse is hovering over this item
            boolean isHovered = mouseX >= itemX && mouseX < itemX + (individualItemWidth - spacing) && 
                               mouseY >= itemY && mouseY < itemY + (itemHeight - spacing);
            
            // Draw item background with rounded corners (alternating rows + hover effect)
            int bgColor;
            if (isHovered) {
                bgColor = 0x80CCCCCC; // Light gray hover effect
            } else {
                bgColor = (row % 2 == 0) ? 0x40FFFFFF : 0x20FFFFFF; // Normal alternating colors
            }
            drawRoundedRect(graphics, itemX, itemY, individualItemWidth - spacing, itemHeight - spacing, 3, bgColor);

            // Draw block icon with color tint
            var pose = graphics.pose();
            pose.pushPose();

            RenderSystem.setShaderColor(
                ((info.color >> 16) & 0xFF) / 255.0f,
                ((info.color >> 8) & 0xFF) / 255.0f,
                (info.color & 0xFF) / 255.0f,
                1.0f
            );

            // Position block icon
            int blockX = itemX + 1;
            int blockY = itemY + 2;
            graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), blockX, blockY);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            pose.popPose();

            // Draw block name and hex color text centered in the available text area
            if (!hideHexText) {
                String blockName = info.blockName;
                String hexText = "(#" + info.hexColor + ")";
                
                // Calculate available text area (total width - icon width - spacing)
                int textAreaWidth = individualItemWidth - 18; // 18px used by icon + spacing
                int textAreaCenterX = itemX + 18 + (textAreaWidth / 2); // Center of text area
                
                int nameY = itemY + (itemHeight / 2) - 6; // Position name higher
                int hexY = itemY + (itemHeight / 2) + 2; // Position hex lower
                
                // Draw block name (primary text) - centered
                pose = graphics.pose();
                pose.pushPose();
                pose.translate(textAreaCenterX, nameY, 0);
                pose.scale(0.5f, 0.5f, 1.0f);
                // Calculate text width at original scale, then offset by half to center
                int nameWidth = this.font.width(blockName);
                graphics.drawString(this.font, blockName, -nameWidth / 2, 0, 0xFFFFFF); // White text for name
                pose.popPose();
                
                // Draw hex color in parentheses (secondary text, slightly smaller) - centered
                pose.pushPose();
                pose.translate(textAreaCenterX, hexY, 0);
                pose.scale(0.4f, 0.4f, 1.0f);
                // Calculate text width at original scale, then offset by half to center
                int hexWidth = this.font.width(hexText);
                graphics.drawString(this.font, hexText, -hexWidth / 2, 0, 0xAAAAAA); // Gray text for hex
                pose.popPose();
            }
        }

        graphics.disableScissor();

        // Draw scroll bar
        if (totalRows > maxVisibleRows) {
            // Draw scrollbar background
            int scrollBarX = panelX + finalPanelWidth + 2; // Position outside the panel
            graphics.fill(scrollBarX, panelY, scrollBarX + 3, panelY + panelHeight, 0xFF666666);
            
            // Draw scrollbar thumb
            int thumbHeight = Math.max(10, (maxVisibleRows * panelHeight) / totalRows);
            int scrollRowOffset = historyScrollOffset / blocksPerRow;
            int maxScrollRows = Math.max(1, totalRows - maxVisibleRows);
            int thumbY = panelY + (scrollRowOffset * (panelHeight - thumbHeight)) / maxScrollRows;
            graphics.fill(scrollBarX, thumbY, scrollBarX + 3, thumbY + thumbHeight, 0xFFCCCCCC);
        }
    }

    private void renderBlockGrid(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = (this.width / 2 - 40) - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2; // Shifted left
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
        // Reset confirmation state if user clicks elsewhere (not on the clear button)
        if (showingClearConfirmation && clearRegistryButton != null) {
            if (!clearRegistryButton.isMouseOver(mouseX, mouseY)) {
                showingClearConfirmation = false;
                updateClearButtonText();
            }
        }
        
        // Check if clicking in history panel first - use same compact logic as rendering
        int centerX = this.width / 2;
        int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
        int gridEndX = centerX + (blockGridWidth / 2);
        
        // Calculate compact panel dimensions (same as render method)
        int itemWidth = 42;
        int maxColumns = 3;
        int panelMargin = 10;
        
        int availableWidth = this.width - gridEndX - panelMargin - 20;
        int actualColumns = Math.min(maxColumns, Math.max(1, availableWidth / itemWidth));
        boolean hideHexText = (availableWidth / actualColumns) < 25;
        
        int spacing = 2; // Small gap between boxes
        int individualItemWidth = hideHexText ? 20 : itemWidth;
        int finalPanelWidth = actualColumns * (individualItemWidth + spacing) - spacing + 8; // Include spacing and padding
        int panelX = this.width - finalPanelWidth - panelMargin;
        int panelY = 60;
        int panelHeight = this.height - panelY - 20;
        
        // History panel item clicks
        if (!createdBlocksHistory.isEmpty() && mouseX >= panelX - 5 && mouseX <= panelX + finalPanelWidth + 5 && 
            mouseY >= panelY && mouseY <= panelY + panelHeight) {
            
            int itemHeight = 20;
            int blocksPerRow = actualColumns;
            int maxVisibleRows = panelHeight / itemHeight;
            int maxVisibleItems = maxVisibleRows * blocksPerRow;
            
            int startIndex = Math.max(0, Math.min(historyScrollOffset, createdBlocksHistory.size() - maxVisibleItems));
            
            int relativeY = (int) (mouseY - panelY);
            int row = relativeY / (itemHeight + spacing);
            
            // Calculate column based on compact layout with spacing
            int col = (int) ((mouseX - (panelX + 4)) / (individualItemWidth + spacing));
            
            int clickedIndex = startIndex + (row * blocksPerRow) + col;
            
            if (clickedIndex >= 0 && clickedIndex < createdBlocksHistory.size() && col >= 0 && col < blocksPerRow) {
                CreatedBlockInfo info = createdBlocksHistory.get(clickedIndex);
                
                if (button == 2) { // Middle click - find and equip the block
                    findAndEquipBlock(info);
                    return true;
                } else { // Left/right click - set as template
                    selectedBlock = info.originalBlock;
                    hexColor = info.hexColor;
                    hexBox.setValue(hexColor);
                    
                    // Clear the name box so user can type a fresh name
                    if (nameBox != null) {
                        nameBox.setValue("");
                    }
                }
                
                return true;
            }
        }

        // Handle block selection clicks - MUST match renderBlockGrid positions
        int startX = (this.width / 2 - 40) - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2; // Shifted left
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

        // Check if clicking on text boxes for special handling
        boolean clickedOnTextBox = false;
        
        // Check hex box click
        if (hexBox != null) {
            int hexX = centerX - (blockGridWidth / 2); // Match the position (already shifted)
            int hexY = 30;
            int hexWidth = 90; // Updated width
            int hexHeight = 20;
            
            if (mouseX >= hexX && mouseX < hexX + hexWidth && mouseY >= hexY && mouseY < hexY + hexHeight) {
                clickedOnTextBox = true;
            }
        }
        
        // Check name box click - select all text when clicked (if it has placeholder text)
        if (nameBox != null) {
            int nameX = centerX - (blockGridWidth / 2) + 90 + 15; // Calculate nameBox X position (already shifted)
            int nameY = 30;
            int nameWidth = 140;
            int nameHeight = 20;
            
            if (mouseX >= nameX && mouseX < nameX + nameWidth && mouseY >= nameY && mouseY < nameY + nameHeight) {
                clickedOnTextBox = true;
                // If nameBox is empty or contains the placeholder-style text, select all when clicked
                if (nameBox.getValue().trim().isEmpty()) {
                    // Let the EditBox handle the click first
                    boolean result = super.mouseClicked(mouseX, mouseY, button);
                    // Then select all text if it gets focus
                    if (nameBox.isFocused()) {
                        nameBox.setHighlightPos(0);
                        nameBox.setCursorPosition(nameBox.getValue().length());
                    }
                    return result;
                }
            }
        }
        
        // If clicked outside text boxes, unfocus them
        if (!clickedOnTextBox) {
            if (hexBox != null && hexBox.isFocused()) {
                hexBox.setFocused(false);
            }
            if (nameBox != null && nameBox.isFocused()) {
                nameBox.setFocused(false);
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
            // Calculate scroll parameters with same responsive logic as render method
            int centerX = this.width / 2;
            int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
            int gridEndX = centerX + (blockGridWidth / 2);
            int panelWidth = 120;
            int panelMargin = 10;
            boolean useSingleColumn = (this.width - panelWidth - panelMargin) < (gridEndX + 20);
            
            int panelY = 60;
            int panelHeight = this.height - panelY - 20;
            int itemHeight = 20;
            int blocksPerRow = useSingleColumn ? 1 : 2;
            int maxVisibleRows = panelHeight / itemHeight;
            int maxVisibleItems = maxVisibleRows * blocksPerRow;
            int maxScroll = Math.max(0, createdBlocksHistory.size() - maxVisibleItems);
            
            if (delta > 0 && historyScrollOffset > 0) {
                historyScrollOffset = Math.max(0, historyScrollOffset - blocksPerRow); // Scroll by row
                return true;
            } else if (delta < 0 && historyScrollOffset < maxScroll) {
                historyScrollOffset = Math.min(maxScroll, historyScrollOffset + blocksPerRow); // Scroll by row
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
        // Reset confirmation state if user presses escape or any other key (except Enter)
        if (showingClearConfirmation && keyCode != 257) { // 257 is ENTER key
            showingClearConfirmation = false;
            updateClearButtonText();
        }
        
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
        
        if (nameBox != null) {
            nameBox.tick();
        }

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
        
        // Real-time validation of name input (filter illegal characters as user types)
        if (nameBox != null) {
            String nameValue = nameBox.getValue();
            // Remove illegal characters in real-time
            String cleanedName = nameValue.replaceAll("[^a-zA-Z0-9 \\-_'.()]", "");
            if (cleanedName.length() > 32) {
                cleanedName = cleanedName.substring(0, 32);
            }
            if (!nameValue.equals(cleanedName)) {
                nameBox.setValue(cleanedName);
            }
        }
        
        // Update Create Block button state based on validation
        if (createButton != null) {
            boolean isValid = hexColor.length() == 6 && isNameBoxValid();
            createButton.active = isValid;
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

    private boolean isValidBlockName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        // Check for illegal characters (anything that might break Minecraft systems)
        // Allow letters, numbers, spaces, hyphens, underscores, and basic punctuation
        return name.matches("^[a-zA-Z0-9 \\-_'.()]+$") && name.trim().length() >= 1 && name.trim().length() <= 32;
    }
    
    private boolean isNameBoxValid() {
        if (nameBox == null) {
            return false;
        }
        
        String nameValue = nameBox.getValue();
        boolean validName = isValidBlockName(nameValue);
        boolean notDuplicate = !isDuplicateName(nameValue);
        
        return validName && notDuplicate;
    }

    private boolean isDuplicateBlock(Block textureBlock, String hexColor) {
        // Get the actual block type that would be created for the given texture
        Block blockType = getBlockTypeForTexture(textureBlock);
        
        for (CreatedBlockInfo info : createdBlocksHistory) {
            // Get the block type that was used for the history item
            Block historyBlockType = getBlockTypeForTexture(info.originalBlock);
            
            // Check if both the final block type AND hex color match
            if (historyBlockType == blockType && info.hexColor.equalsIgnoreCase(hexColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicateName(String customName) {
        if (customName == null || customName.trim().isEmpty()) {
            return false; // Empty names are handled by other validation
        }
        
        String trimmedName = customName.trim();
        
        for (int i = 0; i < createdBlocksHistory.size(); i++) {
            CreatedBlockInfo info = createdBlocksHistory.get(i);
            if (info.blockName != null) {
                if (info.blockName.equalsIgnoreCase(trimmedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void giveExistingBlockToPlayer(Block textureBlock, String hexColor) {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Check if inventory is full
            if (isInventoryFull()) {
                String blockName = BuiltInRegistries.BLOCK.getKey(textureBlock).getPath().replace("_", " ");
                this.minecraft.player.displayClientMessage(
                    Component.literal("§eInventory full! §7Block §f" + blockName + " §7(#" + hexColor.toUpperCase() + ") will be dropped near you."),
                    false
                );
            }

            // Determine which block type to use based on the selected block
            Block blockToUse = getBlockTypeForTexture(textureBlock);
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockToUse);
            ResourceLocation mimicBlockId = BuiltInRegistries.BLOCK.getKey(textureBlock);

            // Find the existing block info to get the custom name
            String existingCustomName = "";
            Block blockType = getBlockTypeForTexture(textureBlock);
            for (CreatedBlockInfo info : createdBlocksHistory) {
                Block historyBlockType = getBlockTypeForTexture(info.originalBlock);
                if (historyBlockType == blockType && info.hexColor.equalsIgnoreCase(hexColor)) {
                    existingCustomName = info.blockName;
                    break;
                }
            }

            // Send packet to server to give the existing block
            ModNetworking.sendToServer(new CreateBlockPacket(
                hexColor.toUpperCase(),
                mimicBlockId.toString(),
                blockId.toString(),
                existingCustomName
            ));

            // Show message that we're giving the existing block (only if inventory wasn't full)
            if (!isInventoryFull()) {
                String blockName = mimicBlockId.getPath().replace("_", " ");
                this.minecraft.player.displayClientMessage(
                    Component.literal("§aGiving existing block: §f" + blockName + " §7(#" + hexColor.toUpperCase() + ")"),
                    false
                );
            }
        }
    }

    private boolean isInventoryFull() {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Check if player has any empty slots in their main inventory (slots 0-35)
            for (int i = 0; i < this.minecraft.player.getInventory().getContainerSize(); i++) {
                if (this.minecraft.player.getInventory().getItem(i).isEmpty()) {
                    return false; // Found an empty slot
                }
            }
            return true; // No empty slots found
        }
        return false;
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
            
            // Strict validation: Block name MUST be provided and valid
            if (!isNameBoxValid()) {
                if (this.minecraft.player != null) {
                    String errorMsg = nameBox.getValue().trim().isEmpty() ? 
                        "§cError: Block name cannot be empty!" : 
                        "§cError: Block name contains illegal characters! Use letters, numbers, spaces, and basic punctuation only.";
                    this.minecraft.player.displayClientMessage(
                        Component.literal(errorMsg),
                        false
                    );
                }
                return;
            }

        // Check for duplicate custom names - CAPTURE VALUE IMMEDIATELY
        String nameBoxRawValue = nameBox.getValue();
        String customNameCheck = nameBoxRawValue.trim();
        
        if (isDuplicateName(customNameCheck)) {
            if (this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cError: Block name '" + customNameCheck + "' already exists! Please choose a different name."),
                    false
                );
            }
            return;
        }

            // Check if inventory is full before creating
            boolean inventoryWasFull = isInventoryFull();
            if (inventoryWasFull && this.minecraft.player != null) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§eInventory full! §7Block will be dropped near you."),
                    false
                );
            }

            // Determine which block type to use based on the selected block
            Block blockToUse = getBlockTypeForTexture(selectedBlock);

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockToUse);
            ResourceLocation mimicBlockId = BuiltInRegistries.BLOCK.getKey(selectedBlock);

            // Send packet to server to create the item - CAPTURE VALUE AGAIN
            String packetNameBoxRaw = nameBox.getValue();
            String customName = packetNameBoxRaw.trim();
            
            try {
                ModNetworking.sendToServer(new CreateBlockPacket(
                    hexColor.toUpperCase(),
                    mimicBlockId.toString(),
                    blockId.toString(),
                    customName
                ));
            } catch (Exception e) {
                System.out.println("CLIENT ERROR: Failed to send packet: " + e.getMessage());
                e.printStackTrace();
            }

            // Add to history (client-side only for display)
            int color = parseHexColor(hexColor);
            createdBlocksHistory.add(0, new CreatedBlockInfo(selectedBlock, hexColor.toUpperCase(), color, customName));
            if (createdBlocksHistory.size() > 500) {
                createdBlocksHistory.remove(500);
            }
            
            // Save history to file
            saveHistoryToFile();

            // Show success message (only if inventory wasn't full)
            String blockName = mimicBlockId.getPath().replace("_", " ");
            if (this.minecraft.player != null && !inventoryWasFull) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§aCreated colored block: §f" + blockName + " §7(#" + hexColor.toUpperCase() + ")"),
                    false
                );
            }
        }
        this.onClose();
    }

    private void handleClearRegistryClick() {
        if (!showingClearConfirmation) {
            // First click - show confirmation
            showingClearConfirmation = true;
            updateClearButtonText();
        } else {
            // Second click - actually clear
            clearServerRegistry();
            showingClearConfirmation = false;
            updateClearButtonText();
        }
    }

    private void updateClearButtonText() {
        if (clearRegistryButton != null) {
            if (showingClearConfirmation) {
                clearRegistryButton.setMessage(Component.literal("§c§lARE YOU SURE?"));
            } else {
                clearRegistryButton.setMessage(Component.literal("Clear Registry"));
            }
        }
    }

    private void clearServerRegistry() {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Clear client-side history first
            createdBlocksHistory.clear();
            saveHistoryToFile();
            
            // Also delete the history file completely
            try {
                File historyFile = new File("config", "blockeditor_history.dat");
                if (historyFile.exists()) {
                    historyFile.delete();
                    this.minecraft.player.displayClientMessage(
                        Component.literal("§aDeleted history file: " + historyFile.getAbsolutePath()),
                        false
                    );
                }
            } catch (Exception e) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cError deleting history file: " + e.getMessage()),
                    false
                );
            }
            
            // Send packet to clear server-side registry
            ModNetworking.sendToServer(new ClearRegistryPacket(true));
            
            // Show confirmation message
            this.minecraft.player.displayClientMessage(
                Component.literal("§aClearing server registry and client history..."),
                false
            );
        }
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
            "deepslate",           // Deepslate
            "white_terracotta",    // White terracotta for base texture
            "white_concrete_powder", // White concrete powder for base texture
            "glass",               // Clear glass for base texture
            "diorite",             // Diorite
            "calcite",             // Calcite
            "mushroom_stem",       // Mushroom stem
            "dead_tube_coral_block", // Dead tube coral block
            "pearlescent_froglight"  // Pearlescent froglight
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

        // Check for texture category keywords
        if (blockName.contains("dirt") || blockName.contains("coarse")) {
            return ModBlocks.DYNAMIC_BLOCK_DIRT.get();
        } else if (blockName.contains("planks") || blockName.contains("wood")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOD.get();
        } else if (blockName.contains("wool")) {
            return ModBlocks.DYNAMIC_BLOCK_WOOL.get();
        } else if (blockName.contains("concrete") && !blockName.contains("powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE.get();
        } else if (blockName.contains("concrete_powder")) {
            return ModBlocks.DYNAMIC_BLOCK_CONCRETE_POWDER.get();
        } else if (blockName.contains("terracotta")) {
            return ModBlocks.DYNAMIC_BLOCK_TERRACOTTA.get();
        } else if (blockName.contains("glass") && !blockName.contains("pane")) {
            return ModBlocks.DYNAMIC_BLOCK_GLASS.get();
        } else if (blockName.contains("diorite")) {
            return ModBlocks.DYNAMIC_BLOCK_DIORITE.get();
        } else if (blockName.contains("calcite")) {
            return ModBlocks.DYNAMIC_BLOCK_CALCITE.get();
        } else if (blockName.contains("mushroom_stem")) {
            return ModBlocks.DYNAMIC_BLOCK_MUSHROOM_STEM.get();
        } else if (blockName.contains("dead_tube_coral")) {
            return ModBlocks.DYNAMIC_BLOCK_DEAD_TUBE_CORAL.get();
        } else if (blockName.contains("pearlescent_froglight")) {
            return ModBlocks.DYNAMIC_BLOCK_PEARLESCENT_FROGLIGHT.get();
        } else if (blockName.contains("cobblestone")) {
            return ModBlocks.DYNAMIC_BLOCK_COBBLESTONE.get();
        } else if (blockName.contains("deepslate")) {
            return ModBlocks.DYNAMIC_BLOCK_DEEPSLATE.get();
        } else if (blockName.contains("sand")) {
            return ModBlocks.DYNAMIC_BLOCK_SAND.get();
        } else if (blockName.contains("smooth_stone")) {
            return ModBlocks.DYNAMIC_BLOCK_SMOOTH_STONE.get();
        } else if (blockName.contains("stone")) {
            return ModBlocks.DYNAMIC_BLOCK.get();
        } else {
            // Default to stone texture for any other blocks
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
            historyScrollOffset = (historyScrollOffset + 1) % Math.max(1, createdBlocksHistory.size());
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override 
    public void setFocused(net.minecraft.client.gui.components.events.GuiEventListener focused) {
        super.setFocused(focused);
    }

    private void findAndEquipBlock(CreatedBlockInfo blockInfo) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        
        String targetColor = blockInfo.hexColor.toUpperCase();
        String targetCustomName = blockInfo.blockName;
        
        // Search through player's inventory for a matching block
        var inventory = this.minecraft.player.getInventory();
        
        // Debug: Log what we're searching for
        System.out.println("[BlockEditor] Searching for block: '" + targetCustomName + "' with color: '" + targetColor + "'");
        
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            
            if (stack.isEmpty()) continue;
            
            // Check if this item is one of our custom blocks (both DynamicBlockItem and UserBlock items)
            if (stack.getItem() instanceof com.blockeditor.mod.content.DynamicBlockItem) {
                var tag = stack.getTag();
                if (tag != null && tag.contains("Color") && tag.contains("CustomName")) {
                    String stackColor = tag.getString("Color").toUpperCase();
                    String stackCustomName = tag.getString("CustomName");
                    
                    // Debug: Log each block we find
                    System.out.println("[BlockEditor] Found custom block in slot " + slot + ": '" + stackCustomName + "' with color: '" + stackColor + "'");
                    
                    // Check if this matches the block we're looking for (by color and name)
                    if (stackColor.equals(targetColor) && stackCustomName.equals(targetCustomName)) {
                        
                        System.out.println("[BlockEditor] Match found! Moving to hotbar slot 0");
                        
                        // Found the matching block! 
                        // Strategy: Find the best hotbar slot to place it in
                        int targetSlot = findBestHotbarSlot(inventory);
                        
                        // Move the block to the target hotbar slot
                        ItemStack currentItem = inventory.getItem(targetSlot);
                        inventory.setItem(targetSlot, stack.copy());
                        inventory.setItem(slot, currentItem);
                        
                        // Set the player's selected slot to the target slot
                        inventory.selected = targetSlot;
                        
                        // Show success message
                        this.minecraft.player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("§a✓ Found and equipped: §f" + targetCustomName + " §7(#" + targetColor + ")"),
                            true // Action bar
                        );
                        
                        // Close the screen
                        this.onClose();
                        return;
                    }
                }
            }
        }
        
        System.out.println("[BlockEditor] Block not found in inventory, creating new one");
        
        // Block not found in inventory - create a new one exactly like it
        try {
            // Get the block type for the target texture
            Block targetBlockType = getBlockTypeForTexture(blockInfo.originalBlock);
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(targetBlockType);
            ResourceLocation mimicBlockId = BuiltInRegistries.BLOCK.getKey(blockInfo.originalBlock);
            
            // Generate a more unique name by combining timestamp + random element
            // This approach is more likely to avoid server-side conflicts
            long timestamp = System.currentTimeMillis();
            int randomComponent = (int) (timestamp % 9999); // Use last 4 digits for uniqueness
            
            // Try different strategies for unique naming
            String finalCustomName;
            
            // Strategy 1: Try original name first
            finalCustomName = targetCustomName;
            
            // Strategy 2: If we know this might conflict, start with a more unique approach
            boolean likelyToConflict = false;
            for (CreatedBlockInfo historyBlock : createdBlocksHistory) {
                if (historyBlock.blockName.startsWith(targetCustomName)) {
                    likelyToConflict = true;
                    break;
                }
            }
            
            if (likelyToConflict) {
                // Use timestamp-based naming for higher success rate
                finalCustomName = targetCustomName + "_" + randomComponent;
            }
            
            // Send the packet to create the block with the unique name
            CreateBlockPacket packet = new CreateBlockPacket(
                targetColor,
                mimicBlockId.toString(),
                blockId.toString(),
                finalCustomName
            );
            
            // Send the packet to the server to register the block
            ModNetworking.sendToServer(packet);
            
            // Show success message that the block will be created
            String displayName = finalCustomName.equals(targetCustomName) ? 
                finalCustomName : finalCustomName + " (auto-named)";
            
            this.minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§a✓ Creating: §f" + displayName + " §7(#" + targetColor + ")"),
                true // Action bar
            );
            
            // Close the screen
            this.onClose();
            return;
            
        } catch (Exception e) {
            // If creation fails, show error
            this.minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cFailed to create block: §f" + targetCustomName),
                true // Action bar
            );
        }
        
        // If we get here, something went wrong
        this.minecraft.player.displayClientMessage(
            net.minecraft.network.chat.Component.literal("§cUnable to find or create block: §f" + targetCustomName + " §7(#" + targetColor + ")"),
            true // Action bar
        );
    }

    /**
     * Find the best hotbar slot to place a block in.
     * Priority: Currently selected slot if empty > First empty slot > Currently selected slot (replace)
     */
    private int findBestHotbarSlot(net.minecraft.world.entity.player.Inventory inventory) {
        int currentSlot = inventory.selected;
        
        // First choice: Use currently selected slot if it's empty
        if (inventory.getItem(currentSlot).isEmpty()) {
            return currentSlot;
        }
        
        // Second choice: Find any empty hotbar slot (0-8)
        for (int slot = 0; slot < 9; slot++) {
            if (inventory.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        
        // Last resort: Use currently selected slot (will replace whatever is there)
        return currentSlot;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Helper method to draw rounded rectangles (optimized version with enhanced corners)
    private void drawRoundedRect(GuiGraphics graphics, int x, int y, int width, int height, int radius, int color) {
        // Clamp radius to prevent issues with small rectangles
        radius = Math.min(radius, Math.min(width / 2, height / 2));
        
        if (radius <= 0) {
            // Just draw a regular rectangle if radius is 0
            graphics.fill(x, y, x + width, y + height, color);
            return;
        }
        
        // Draw the main rectangular areas (more efficient than nested loops)
        graphics.fill(x + radius, y, x + width - radius, y + height, color); // Center rectangle
        graphics.fill(x, y + radius, x + radius, y + height - radius, color); // Left rectangle
        graphics.fill(x + width - radius, y + radius, x + width, y + height - radius, color); // Right rectangle
        
        // Draw enhanced rounded corners (slightly more pixels for better rounding)
        // Top-left corner
        graphics.fill(x + 1, y + 1, x + radius, y + 2, color);
        graphics.fill(x + 1, y + 2, x + 3, y + radius, color);
        if (radius >= 3) {
            graphics.fill(x + 2, y + 1, x + 3, y + 2, color);
        }
        
        // Top-right corner
        graphics.fill(x + width - radius, y + 1, x + width - 1, y + 2, color);
        graphics.fill(x + width - 3, y + 2, x + width - 1, y + radius, color);
        if (radius >= 3) {
            graphics.fill(x + width - 3, y + 1, x + width - 2, y + 2, color);
        }
        
        // Bottom-left corner
        graphics.fill(x + 1, y + height - 2, x + radius, y + height - 1, color);
        graphics.fill(x + 1, y + height - radius, x + 3, y + height - 2, color);
        if (radius >= 3) {
            graphics.fill(x + 2, y + height - 2, x + 3, y + height - 1, color);
        }
        
        // Bottom-right corner
        graphics.fill(x + width - radius, y + height - 2, x + width - 1, y + height - 1, color);
        graphics.fill(x + width - 3, y + height - radius, x + width - 1, y + height - 2, color);
        if (radius >= 3) {
            graphics.fill(x + width - 3, y + height - 2, x + width - 2, y + height - 1, color);
        }
    }
}