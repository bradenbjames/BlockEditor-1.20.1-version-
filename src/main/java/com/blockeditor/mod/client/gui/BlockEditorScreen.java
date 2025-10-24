package com.blockeditor.mod.client.gui;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory;
import com.blockeditor.mod.client.gui.editor.BlockEditorWidgets;
import com.blockeditor.mod.client.gui.editor.HistoryPanel;
import com.blockeditor.mod.client.gui.editor.BlockAllowList;
import com.blockeditor.mod.client.gui.editor.BlockValidation;
import com.blockeditor.mod.client.gui.editor.TextureBlockResolver;
import com.blockeditor.mod.client.gui.editor.InventoryHelper;
import com.blockeditor.mod.network.ClearRegistryPacket;
import com.blockeditor.mod.network.CreateBlockPacket;
import com.blockeditor.mod.network.ModNetworking;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockEditorScreen extends Screen {

    // Layout constants for main grid
    private static final int BLOCKS_PER_ROW = 8;
    private static final int BLOCK_SIZE = 32;
    private static final int BLOCK_PADDING = 4;

    private static final Logger LOGGER = LogUtils.getLogger();

    private String hexColor = "FFFFFF";
    private Block selectedBlock = Blocks.STONE;
    private int scrollOffset = 0;

    private EditBox hexBox;
    private EditBox searchBox;
    private EditBox nameBox;
    private Button createButton;
    private Button clearRegistryButton;

    // Confirmation dialog state
    private boolean showingClearConfirmation = false;

    private final List<Block> allBlocks = new ArrayList<>();
    private List<Block> filteredBlocks = new ArrayList<>();

    // New: encapsulated history UI/logic
    private final HistoryPanel historyPanel = new HistoryPanel();

    public BlockEditorScreen() {
        super(Component.literal("Block Editor"));
        BlockEditorHistory.loadHistoryFromFile(); // Load history when screen is created
    }
    
    private static void saveHistoryToFile() {
        BlockEditorHistory.saveHistoryToFile();
    }

    @Override
    protected void init() {
        super.init();
        
        // Get all registered blocks (filter to only allowed blocks)
        allBlocks.clear();
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR) {
                if (BlockAllowList.isAllowed(block)) {
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

        // Custom hex input box with integrated # symbol and two-tone background
        hexBox = BlockEditorWidgets.createHexBox(this.font, gridStartX + 16, hexY, hexColor);
        this.addRenderableWidget(hexBox);

        // Custom name input box - positioned after hex box with clear placeholder
        int nameX = gridStartX + 90 + 15; // After hex box + gap (updated for 90px hex box)
        nameBox = BlockEditorWidgets.createNameBox(this.font, nameX, hexY);
        this.addRenderableWidget(nameBox);

        // Buttons - positioned just above player inventory
        int buttonY = this.height - 85; // Position buttons just above player inventory
        int buttonSpacing = 10;
        int buttonWidth = 90;
        int totalButtonWidth = (buttonWidth * 2) + buttonSpacing;
        int buttonStartX = centerX - (totalButtonWidth / 2);

        // Create Block button
        createButton = BlockEditorWidgets.createButton("Create Block", buttonStartX, buttonY, buttonWidth, this::createColoredBlock);
        this.addRenderableWidget(createButton);

        // Cancel button
        this.addRenderableWidget(BlockEditorWidgets.createButton("Cancel", buttonStartX + buttonWidth + buttonSpacing, buttonY, buttonWidth, this::onClose));

        // Clear Registry button - positioned below other buttons
        clearRegistryButton = BlockEditorWidgets.createButton("Clear Registry", buttonStartX, buttonY + 25, buttonWidth, this::handleClearRegistryClick);
        this.addRenderableWidget(clearRegistryButton);

        // Wire history panel item click behavior
        historyPanel.setOnItemClick((info, mouseButton) -> {
            if (mouseButton == 2) { // middle-click: find and equip
                findAndEquipBlock(info);
            } else { // left/right: set as template
                selectedBlock = info.originalBlock;
                hexColor = info.hexColor;
                if (hexBox != null) hexBox.setValue(hexColor);
                if (nameBox != null) nameBox.setValue("");
            }
        });

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
            graphics.fill(boxX + boxWidth + 1, boxY - 1, boxX + boxWidth + 2, boxY + boxHeight + 1, borderColor); // Right (fixed x2/y2)
        }

        // Draw block preview to the right of the name text box, same size as selection grid blocks
        if (hexBox != null && nameBox != null) {
            int color = BlockValidation.parseHexColor(hexBox.getValue());
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
        }

        // Draw history panel on the right side (delegated)
        historyPanel.render(this, graphics, this.font, mouseX, mouseY);

        // Render all widgets (buttons and text boxes) - THIS IS CRITICAL
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderBlockGrid(GuiGraphics graphics, int mouseX, int mouseY) {
        int startX = (this.width / 2 - 40) - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2; // Shifted left
        int startY = 55;
        int maxRows = 4;

        // Filter blocks based on search
        String search = (searchBox != null ? searchBox.getValue() : "").toLowerCase();
        if (!search.isEmpty()) {
            filteredBlocks = new ArrayList<>();
            for (Block block : allBlocks) {
                ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
                if (key != null) {
                    String blockName = key.toString();
                    if (blockName.contains(search)) {
                        filteredBlocks.add(block);
                    }
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
        
        // Delegate to history panel for item clicks
        if (historyPanel.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
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
            int centerX = this.width / 2 - 40;
            int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
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
            int centerX = this.width / 2 - 40;
            int blockGridWidth = BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING);
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
        // Delegate scrolling to history panel first
        if (historyPanel.mouseScrolled(this, mouseX, mouseY, delta)) {
            return true;
        }
        
        // Check if mouse is over main block grid area
        int startX = (this.width / 2 - 40) - (BLOCKS_PER_ROW * (BLOCK_SIZE + BLOCK_PADDING)) / 2;
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
            historyPanel.scrollUp();
            return true;
        } else if (keyCode == 50) { // 2 key  
            historyPanel.scrollDown();
            return true;
        } else if (keyCode == 32) { // SPACE
            if (!BlockEditorHistory.getHistory().isEmpty()) {
                historyPanel.scrollDown();
            }
            return true;
        }
        
        // Add keyboard controls for history scrolling (Arrow keys and Page Up/Down)
        if (keyCode == 265) { // Up Arrow
            historyPanel.scrollUp();
            return true;
        } else if (keyCode == 264) { // Down Arrow
            historyPanel.scrollDown();
            return true;
        } else if (keyCode == 266) { // Page Up
            historyPanel.scrollUp();
            return true;
        } else if (keyCode == 267) { // Page Down
            historyPanel.scrollDown();
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void scrollHistoryUp() {
        historyPanel.scrollUp();
    }

    private void scrollHistoryDown() {
        historyPanel.scrollDown();
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
        if (hexBox != null) {
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
        
        // Real-time validation of name input (filter illegal characters as user types)
        if (nameBox != null) {
            String nameValue = nameBox.getValue();
            // Remove illegal characters in real-time
            String cleanedName = nameValue.replaceAll("[^a-zA-Z0-9 _\\-'.()]", "");
            if (cleanedName.length() > 32) {
                cleanedName = cleanedName.substring(0, 32);
            }
            if (!nameValue.equals(cleanedName)) {
                nameBox.setValue(cleanedName);
            }
        }
        
        // Update Create Block button state based on validation
        if (createButton != null) {
            createButton.active = hexColor.length() == 6 && isNameBoxValid();
        }
    }

    private void checkScrollInput() {
        if (minecraft != null) {
            // Get mouse position
            double mouseX = minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
            double mouseY = minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
            
            // Check if mouse is over history panel
            if (historyPanel.isMouseOver(this, mouseX, mouseY)) {
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

    private boolean isNameBoxValid() {
        if (nameBox == null) {
            return false;
        }
        
        String nameValue = nameBox.getValue();
        boolean validName = BlockValidation.isValidBlockName(nameValue);
        boolean notDuplicate = !isDuplicateName(nameValue);
        
        return validName && notDuplicate;
    }

    private boolean isDuplicateName(String customName) {
        if (customName == null || customName.trim().isEmpty()) {
            return false; // Empty names are handled by other validation
        }
        
        String trimmedName = customName.trim();
        
        for (int i = 0; i < BlockEditorHistory.getHistory().size(); i++) {
            BlockEditorHistory.CreatedBlockInfo info = BlockEditorHistory.getHistory().get(i);
            if (info.blockName != null) {
                if (info.blockName.equalsIgnoreCase(trimmedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createColoredBlock() {
        if (this.minecraft != null && this.minecraft.player != null) {
            // Validate hex color is exactly 6 characters
            if (hexColor.length() != 6) {
                // Show error message to player
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cError: Hex color must be exactly 6 characters (e.g., FF0000 for red)"),
                    false
                );
                return;
            }
            
            // Strict validation: Block name MUST be provided and valid
            if (!isNameBoxValid()) {
                String errorMsg = nameBox.getValue().trim().isEmpty() ?
                    "§cError: Block name cannot be empty!" :
                    "§cError: Block name contains illegal characters! Use letters, numbers, spaces, and basic punctuation only.";
                this.minecraft.player.displayClientMessage(
                    Component.literal(errorMsg),
                    false
                );
                return;
            }

            // Check for duplicate custom names - CAPTURE VALUE IMMEDIATELY
            String nameBoxRawValue = nameBox.getValue();
            String customNameCheck = nameBoxRawValue.trim();

            if (isDuplicateName(customNameCheck)) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cError: Block name '" + customNameCheck + "' already exists! Please choose a different name."),
                    false
                );
                return;
            }

            // Check if inventory is full before creating
            boolean inventoryWasFull = isInventoryFull();
            if (inventoryWasFull) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§eInventory full! §7Block will be dropped near you."),
                    false
                );
            }

            // Determine which block type to use based on the selected block
            Block blockToUse = TextureBlockResolver.resolve(selectedBlock);

            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(blockToUse);
            ResourceLocation mimicBlockId = ForgeRegistries.BLOCKS.getKey(selectedBlock);
            if (blockId == null || mimicBlockId == null) {
                LOGGER.error("Missing registry key for block(s): blockId={}, mimicBlockId={}", blockId, mimicBlockId);
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cInternal error: Could not resolve block registry keys."),
                    false
                );
                return;
            }

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
                LOGGER.error("CLIENT ERROR: Failed to send CreateBlockPacket", e);
            }

            // Add to history (client-side only for display)
            int color = BlockValidation.parseHexColor(hexColor);
            BlockEditorHistory.getHistory().add(0, new BlockEditorHistory.CreatedBlockInfo(selectedBlock, hexColor.toUpperCase(), color, customName));
            if (BlockEditorHistory.getHistory().size() > 500) {
                BlockEditorHistory.getHistory().remove(500);
            }
            
            // Save history to file
            saveHistoryToFile();

            // Show success message (only if inventory wasn't full)
            if (!inventoryWasFull) {
                String blockName = mimicBlockId.getPath().replace("_", " ");
                this.minecraft.player.displayClientMessage(
                    Component.literal("§aCreated colored block: §f" + blockName + " §7(#" + hexColor.toUpperCase() + ")"),
                    false
                );
            }
        }
        this.onClose();
    }

    private boolean isInventoryFull() {
        if (this.minecraft != null && this.minecraft.player != null) {
            for (int i = 0; i < this.minecraft.player.getInventory().getContainerSize(); i++) {
                if (this.minecraft.player.getInventory().getItem(i).isEmpty()) {
                    return false; // Found an empty slot
                }
            }
            return true; // No empty slots found
        }
        return false;
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
            BlockEditorHistory.getHistory().clear();
            saveHistoryToFile();
            
            // Also delete the history file completely
            try {
                File historyFile = new File("config", "blockeditor_history.dat");
                if (historyFile.exists()) {
                    boolean deleted = historyFile.delete();
                    if (deleted) {
                        this.minecraft.player.displayClientMessage(
                            Component.literal("§aDeleted history file: " + historyFile.getAbsolutePath()),
                            false
                        );
                    } else {
                        this.minecraft.player.displayClientMessage(
                            Component.literal("§cCould not delete history file: " + historyFile.getAbsolutePath()),
                            false
                        );
                        LOGGER.warn("Failed to delete history file at {}", historyFile.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                this.minecraft.player.displayClientMessage(
                    Component.literal("§cError deleting history file: " + e.getMessage()),
                    false
                );
                LOGGER.error("Error deleting history file", e);
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

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        // Try catching scroll events through char input
        if (codePoint == 'w' || codePoint == 'W') {
            LOGGER.debug("MANUAL SCROLL: UP with W key");
            scrollHistoryUp();
            return true;
        }
        if (codePoint == 's' || codePoint == 'S') {
            LOGGER.debug("MANUAL SCROLL: DOWN with S key");
            scrollHistoryDown();
            return true;
        }
        if (codePoint == ' ') {
            if (!BlockEditorHistory.getHistory().isEmpty()) {
                historyPanel.scrollDown();
            }
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override 
    public void setFocused(net.minecraft.client.gui.components.events.GuiEventListener focused) {
        super.setFocused(focused);
    }

    public static void findAndEquipBlock(BlockEditorHistory.CreatedBlockInfo blockInfo) {
        InventoryHelper.findAndEquipBlock(blockInfo);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Removed local drawRoundedRect; HistoryPanel now handles its own rounded rect rendering via GuiRenderUtil.
}

