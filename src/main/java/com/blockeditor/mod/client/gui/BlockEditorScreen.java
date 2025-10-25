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
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEditorScreen extends Screen {

    // Layout constants for main grid
    private static final int DEFAULT_BLOCKS_PER_ROW = 6; // default columns for most screens
    private static final int LARGE_SCREEN_BLOCKS = 7; // extra column for large displays
    private static final int LARGE_SCREEN_WIDTH_THRESHOLD = 1920; // px threshold to use extra column
    private static final int BLOCK_SIZE = 32;
    private static final int BLOCK_PADDING = 4;
    private static final int GRID_MAX_ROWS = 4; // number of visible rows in the grid
    private static final int HISTORY_GUTTER = 12; // horizontal gap between grid and history panel
    // How many pixels to shift the main content left to give more room to the history panel
    // private static final int MAIN_CONTENT_SHIFT = 30; // further reduced to keep the custom-panel on-screen

    private static final Logger LOGGER = LogUtils.getLogger();

    private String hexColor = "FFFFFF";
    private Block selectedBlock = Blocks.STONE;
    private int scrollOffset = 0;

    private EditBox hexBox;
    private EditBox searchBox;
    private EditBox nameBox;
    private Button createButton;
    private Button cancelButton;
    private Button clearRegistryButton;

    // Confirmation dialog state
    private boolean showingClearConfirmation = false;

    private final List<Block> allBlocks = new ArrayList<>();
    private List<Block> filteredBlocks = new ArrayList<>();

    // New: encapsulated history UI/logic
    private final HistoryPanel historyPanel = new HistoryPanel();

    // Helper to compute grid and history layout so the left half of the screen is the grid
    private int computeGridStartX(int blockGridWidth) {
        // Reserve the left half of the screen for the custom grid (minus a small margin)
        // Reduce left margin so the custom grid sits closer to the left edge
        int leftAreaMargin = 5;
        // Use a larger reserved area on large screens to make room for an extra column
        int reservedPercent = (this.width >= LARGE_SCREEN_WIDTH_THRESHOLD) ? 60 : 50; // percent of total width
        int leftAreaWidth = Math.max(0, (this.width * reservedPercent / 100) - (leftAreaMargin * 2));
         if (blockGridWidth >= leftAreaWidth) {
             // Not enough room to center: start at margin
             return leftAreaMargin;
         }
         // center the grid in the reserved left half
         return leftAreaMargin + (leftAreaWidth - blockGridWidth) / 2;
     }

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

        // Calculate block grid width based on a dynamic column count (large screens get +1 column)
        int blocksPerRow = computeBlocksPerRow();
        // Exact grid width: N * BLOCK_SIZE + (N-1) * BLOCK_PADDING (no trailing padding after last column)
        int blockGridWidth = blocksPerRow * BLOCK_SIZE + (blocksPerRow - 1) * BLOCK_PADDING;
        // Place the main grid centered inside the left half of the screen
        int gridStartX = computeGridStartX(blockGridWidth);

        // Search box - hidden from UI but functionality kept
        int searchWidth = 120; 
        searchBox = new EditBox(this.font, -1000, -1000, searchWidth, 20, Component.literal("Search")); // Move off-screen
        searchBox.setHint(Component.literal("Search blocks..."));
        // Don't add to renderableWidget - keep functionality but hide UI
        
        // Hex color input box - positioned to align with block grid
        int hexY = 30;

        // Custom hex input box
        hexBox = BlockEditorWidgets.createHexBox(this.font, gridStartX, hexY, hexColor);
        this.addRenderableWidget(hexBox);

        // Custom name input box - placed with consistent gap after hex box
        int gap = 10; // slightly thinner gap
        int nameX = gridStartX + hexBox.getWidth() + gap;

        // Compute where the block preview will be drawn (aligned with rightmost column)
        int rightmostCol = blocksPerRow - 1;
        int blockPreviewX = gridStartX + (rightmostCol * (BLOCK_SIZE + BLOCK_PADDING)) + 8; // same alignment as render()
        int previewPadding = 8; // space between name box and preview so they don't touch

        // Determine the available width for the name box so it stops before the preview
        int desiredNameRight = blockPreviewX - previewPadding;
        int computedNameWidth = Math.max(40, desiredNameRight - nameX); // ensure a small minimum
        // As a safety fallback, keep it at least 64px so hint text fits on very small screens
        if (computedNameWidth < 64) computedNameWidth = 64;

        nameBox = BlockEditorWidgets.createNameBox(this.font, nameX, hexY, computedNameWidth);
        this.addRenderableWidget(nameBox);

        // Buttons - positioned lower (moved down) so they don't overlap the block grid
        // Compute grid bottom and place buttons 30px below it instead of anchoring to window bottom
        int gridStartY = 55; // must match renderBlockGrid's startY
        // Exact grid height: rows * BLOCK_SIZE + (rows-1) * BLOCK_PADDING (no trailing padding after last row)
        int gridHeight = GRID_MAX_ROWS * BLOCK_SIZE + (GRID_MAX_ROWS - 1) * BLOCK_PADDING;
        int buttonY = gridStartY + gridHeight + 30; // 30px after bottom of the grid
        // Clamp buttonY so buttons stay on-screen (assume button height ~20px)
        int minBottomMargin = 20;
        int assumedButtonHeight = 20;
        if (buttonY + assumedButtonHeight + minBottomMargin > this.height) {
            buttonY = this.height - assumedButtonHeight - minBottomMargin;
        }
        int buttonSpacing = 10;
        // We'll place all buttons in one row, left-aligned to the grid
        int numButtons = 3; // Create, Cancel, Clear

        // Compute history panel left bound so it does not overlap the grid (use grid right + smaller gutter)
        int historyLeftBoundInit = gridStartX + blockGridWidth + HISTORY_GUTTER;
         // Available horizontal space from grid left to history panel (minus small margin)
        int availableForButtons = Math.max(0, historyLeftBoundInit - gridStartX - 20);

        // Compute the maximum possible width per button so the entire row fits inside availableForButtons
        int maxPossibleWidth = (availableForButtons - (buttonSpacing * (numButtons - 1))) / numButtons;
        // Choose a width that never exceeds the available space. If there's not enough room for the preferred min,
        // accept a smaller width (to avoid overlapping the history panel).
        // We intentionally allow widths smaller than minButtonWidth if space is limited.
        // Prefer reasonably small buttons on large screens so they align well under the grid.
        int preferredButtonWidth = 110;
        int buttonWidthActual = Math.min(preferredButtonWidth, Math.max(1, maxPossibleWidth));

        // Create the three equally sized buttons in one row.
        // Align the right edge of the button group to the right edge of the grid (user requested)
        int totalButtonsWidth = numButtons * buttonWidthActual + (numButtons - 1) * buttonSpacing;
        int gridRightX = gridStartX + blockGridWidth;
        int startButtonsX = Math.max(gridStartX, gridRightX - totalButtonsWidth); // clamp so buttons don't go left of grid

        createButton = BlockEditorWidgets.createButton("Create Block", startButtonsX, buttonY, buttonWidthActual, this::createColoredBlock);
        this.addRenderableWidget(createButton);

        cancelButton = BlockEditorWidgets.createButton("Cancel", startButtonsX + (buttonWidthActual + buttonSpacing), buttonY, buttonWidthActual, this::onClose);
        this.addRenderableWidget(cancelButton);

        clearRegistryButton = BlockEditorWidgets.createButton("Clear Registry", startButtonsX + (buttonWidthActual + buttonSpacing) * 2, buttonY, buttonWidthActual, this::handleClearRegistryClick);
        this.addRenderableWidget(clearRegistryButton);

        // Constrain history panel so it doesn't overlap the main grid or buttons; place it to the right of the block grid
        // On very wide screens, prefer to give the history panel a minimum width so it can show
        // up to 10 columns. Compute available width and, if necessary, shift the left bound left
        // so the panel expands.
        int desiredMinPanelWidth = 380; // enough to show ~10 columns with current ITEM_WIDTH/spacing
        int panelMargin = 5; // match HistoryPanel.PANEL_MARGIN
        int availableForPanel = Math.max(0, this.width - historyLeftBoundInit - panelMargin);
        if (this.width > 1920 && availableForPanel < desiredMinPanelWidth) {
            // Move left bound left so the available space becomes the desired minimum
            historyLeftBoundInit = Math.max(0, this.width - panelMargin - desiredMinPanelWidth);
        }
        historyPanel.setLeftBoundX(historyLeftBoundInit); // use grid-based bound so buttons stay left-aligned
        // Align the panel vertically so it starts at the color/name input row and ends at the button row.
        // This will make the history panel top align with the input boxes and bottom align with the buttons.
        int initialTop = hexY;
        if (hexBox != null) initialTop = hexBox.getY();
        if (nameBox != null) initialTop = Math.min(initialTop, nameBox.getY());
        // Use the maximum bottom of all buttons so the panel bottom lines up exactly.
        int initialBottom = buttonY + assumedButtonHeight - 1; // base guess
        if (createButton != null) initialBottom = Math.max(initialBottom, createButton.getY() + createButton.getHeight() - 1);
        if (this.cancelButton != null) initialBottom = Math.max(initialBottom, this.cancelButton.getY() + this.cancelButton.getHeight() - 1);
        if (clearRegistryButton != null) initialBottom = Math.max(initialBottom, clearRegistryButton.getY() + clearRegistryButton.getHeight() - 1);
        historyPanel.setVerticalBounds(initialTop, initialBottom);

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

        // Draw block grid
         renderBlockGrid(graphics, mouseX, mouseY);

        // Calculate positions to match init() - grid centered in left half
        int blocksPerRowRuntime = computeBlocksPerRow();
        int blockGridWidth = blocksPerRowRuntime * BLOCK_SIZE + (blocksPerRowRuntime - 1) * BLOCK_PADDING;
        int gridStartX = computeGridStartX(blockGridWidth);

        // # symbol is now integrated into the hex input box
        
        // NOTE: removed the red rectangle border in favor of coloring the text red when invalid
        // Name text color is now handled in tick() to ensure the EditBox shows red text when invalid

        // Draw block preview to the right of the name text box, same size as selection grid blocks
        if (hexBox != null && nameBox != null) {
            int color = BlockValidation.parseHexColor(hexBox.getValue());
            int previewSize = 16; // Same size as blocks in selection grid (16px item size, not scaled)

            // Position aligned with the rightmost column of the selection grid
            int rightmostCol = blocksPerRowRuntime - 1;
            int blockPreviewX = gridStartX + (rightmostCol * (BLOCK_SIZE + BLOCK_PADDING)) + 8; // align with rightmost column
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

        // Recompute left bound each frame based on the current grid so the panel follows the grid if the window is resized
        blocksPerRowRuntime = computeBlocksPerRow();
        int blockGridWidthRuntime = blocksPerRowRuntime * BLOCK_SIZE + (blocksPerRowRuntime - 1) * BLOCK_PADDING;
        int gridStartXRuntime = computeGridStartX(blockGridWidthRuntime);
        int runtimeHistoryLeftBound = gridStartXRuntime + blockGridWidthRuntime + HISTORY_GUTTER; // match gutter
        // If screen is wide but the available width for the panel is too small to show 10 columns,
        // shift the left bound left so the panel gets the desired minimum width.
        int desiredMinPanelWidthRt = 380;
        int panelMarginRt = 5;
        int availableRt = Math.max(0, this.width - runtimeHistoryLeftBound - panelMarginRt);
        if (this.width > 1920 && availableRt < desiredMinPanelWidthRt) {
            runtimeHistoryLeftBound = Math.max(0, this.width - panelMarginRt - desiredMinPanelWidthRt);
        }
        historyPanel.setLeftBoundX(runtimeHistoryLeftBound);
        // Also update vertical bounds each frame so the panel top starts at the input boxes
        // and the bottom aligns with the buttons. This keeps the red-box alignment you showed
        // even if the window is resized or widgets move.
        int panelTop = 30; // default top (matches init's hexY)
        // Prefer the actual hex/name widget positions when available so the panel aligns exactly
        if (hexBox != null) panelTop = hexBox.getY();
        if (nameBox != null) panelTop = Math.min(panelTop, nameBox.getY());
        // Use the maximum bottom of the visible button row so the panel bottom matches all buttons
        int panelBottom = this.height - 20; // default bottom fallback
        if (createButton != null) panelBottom = Math.max(panelBottom, createButton.getY() + createButton.getHeight() - 1);
        if (cancelButton != null) panelBottom = Math.max(panelBottom, cancelButton.getY() + cancelButton.getHeight() - 1);
        if (clearRegistryButton != null) panelBottom = Math.max(panelBottom, clearRegistryButton.getY() + clearRegistryButton.getHeight() - 1);
        historyPanel.setVerticalBounds(panelTop, panelBottom);
        historyPanel.render(this, graphics, this.font, mouseX, mouseY);

        // Render all widgets (buttons and text boxes) - THIS IS CRITICAL
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderBlockGrid(GuiGraphics graphics, int mouseX, int mouseY) {
        // Grid start X should be computed to occupy the left half
        int blocksPerRow = computeBlocksPerRow();
        int blockGridWidth = blocksPerRow * BLOCK_SIZE + (blocksPerRow - 1) * BLOCK_PADDING;
        int startX = computeGridStartX(blockGridWidth);
        int startY = 55;

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
        for (int row = 0; row < GRID_MAX_ROWS; row++) {
            for (int col = 0; col < blocksPerRow; col++) {
                int index = (row + scrollOffset) * blocksPerRow + col;
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
        // Use the same grid width math as renderBlockGrid for consistent alignment
        //int centerX3 = this.width / 2 - 40 - MAIN_CONTENT_SHIFT; // Shifted left
        //int blockGridWidth3 = BLOCKS_PER_ROW * BLOCK_SIZE + (BLOCKS_PER_ROW - 1) * BLOCK_PADDING;
        //int startX = centerX3 - (blockGridWidth3 / 2);
        int blocksPerRow = computeBlocksPerRow();
        int blockGridWidth3 = blocksPerRow * BLOCK_SIZE + (blocksPerRow - 1) * BLOCK_PADDING;
        int startX = computeGridStartX(blockGridWidth3);
         int startY = 55;  // Must match renderBlockGrid
         // use GRID_MAX_ROWS directly

        for (int row = 0; row < GRID_MAX_ROWS; row++) {
            for (int col = 0; col < blocksPerRow; col++) {
                int index = (row + scrollOffset) * blocksPerRow + col;
                if (index >= filteredBlocks.size()) break;

                int x = startX + col * (BLOCK_SIZE + BLOCK_PADDING);
                int y = startY + row * (BLOCK_SIZE + BLOCK_PADDING);

                if (mouseX >= x && mouseX < x + BLOCK_SIZE && mouseY >= y && mouseY < y + BLOCK_SIZE) {
                    selectedBlock = filteredBlocks.get(index);
                    return true;
                }
            }
        }

        // Check hex box click: select all on click
        if (hexBox != null) {
            int hx = hexBox.getX();
            int hy = hexBox.getY();
            int hw = hexBox.getWidth();
            int hh = hexBox.getHeight();
            if (mouseX >= hx && mouseX < hx + hw && mouseY >= hy && mouseY < hy + hh) {
                boolean result = super.mouseClicked(mouseX, mouseY, button);
                if (hexBox.isFocused()) {
                    hexBox.setHighlightPos(0);
                    hexBox.setCursorPosition(hexBox.getValue().length());
                }
                return result;
            }
        }
        
        // Check name box click - select all text when clicked only if it has placeholder or any text? Keep current behavior
        if (nameBox != null) {
            int nx = nameBox.getX();
            int ny = nameBox.getY();
            int nw = nameBox.getWidth();
            int nh = nameBox.getHeight();
            if (mouseX >= nx && mouseX < nx + nw && mouseY >= ny && mouseY < ny + nh) {
                boolean result = super.mouseClicked(mouseX, mouseY, button);
                if (nameBox.isFocused() && nameBox.getValue().trim().isEmpty()) {
                    nameBox.setHighlightPos(0);
                    nameBox.setCursorPosition(nameBox.getValue().length());
                }
                return result;
            }
        }
        
        // If clicked outside text boxes, unfocus them
        if (hexBox != null && hexBox.isFocused()) {
            hexBox.setFocused(false);
        }
        if (nameBox != null && nameBox.isFocused()) {
            nameBox.setFocused(false);
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
        int blocksPerRow2 = computeBlocksPerRow();
        int blockGridWidth2 = blocksPerRow2 * BLOCK_SIZE + (blocksPerRow2 - 1) * BLOCK_PADDING;
        int startX2 = computeGridStartX(blockGridWidth2);
         int startY2 = 55;
         // Exact grid height: rows * BLOCK_SIZE + (rows-1) * BLOCK_PADDING
         int gridHeight = GRID_MAX_ROWS * BLOCK_SIZE + (GRID_MAX_ROWS - 1) * BLOCK_PADDING;

         if (mouseX >= startX2 && mouseX <= startX2 + blockGridWidth2 &&
             mouseY >= startY2 && mouseY <= startY2 + gridHeight) {
            // Scroll in main block grid
            // Compute total rows using ceiling division so the scroll limit is correct for partial rows
            int totalRows = (filteredBlocks.size() + blocksPerRow2 - 1) / blocksPerRow2;
            int maxScroll = Math.max(0, totalRows - GRID_MAX_ROWS);
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
            // Update the edit box text color: red when invalid, white when valid
            try {
                int validColor = 16777215; // white
                int invalidColor = 16711680; // red
                nameBox.setTextColor(isNameBoxValid() ? validColor : invalidColor);
            } catch (Exception ignored) {
                // setTextColor may not exist or behave differently on some mappings; ignore failures
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
                    if (System.currentTimeMillis() % 200 < 50) // Throttle key repeat
                        scrollHistoryDown();
                }
            }
        }
    }

    private int computeBlocksPerRow() {
        // Detect large screens robustly across GUI scale/DPI settings by taking
        // the maximum of several width measurements we can reasonably obtain.
        int guiWidth = this.width; // scaled GUI units
        int windowPixelWidth = guiWidth;
        int guiScaledWidth = guiWidth;
        if (this.minecraft != null) {
            try {
                var window = this.minecraft.getWindow();
                // window is expected to be non-null here in the client environment; use directly
                windowPixelWidth = window.getScreenWidth();
                guiScaledWidth = window.getGuiScaledWidth();
            } catch (Exception ignored) {
                // fall back to guiWidth
            }
        }
        int maxObserved = Math.max(guiWidth, Math.max(windowPixelWidth, guiScaledWidth));
        int desired = (maxObserved >= LARGE_SCREEN_WIDTH_THRESHOLD) ? LARGE_SCREEN_BLOCKS : DEFAULT_BLOCKS_PER_ROW;

       // Now ensure the desired number of columns actually fits inside the reserved left half
        // Match the smaller left margin used for positioning so column calculation is consistent
        int leftAreaMargin = 5;
        int leftAreaWidth = Math.max(0, (this.width / 2) - (leftAreaMargin * 2));
        // Compute how many full block cells fit (accounting for N*BLOCK_SIZE + (N-1)*BLOCK_PADDING)
        int maxColumns = Math.max(1, (leftAreaWidth + BLOCK_PADDING) / (BLOCK_SIZE + BLOCK_PADDING));

        return Math.min(desired, maxColumns);
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
        // If any text input is focused, defer to the default text handling so characters
        // like 's' and 'w' can be typed into the box normally.
        if ((nameBox != null && nameBox.isFocused()) || (hexBox != null && hexBox.isFocused()) || (searchBox != null && searchBox.isFocused())) {
            return super.charTyped(codePoint, modifiers);
        }

        // Otherwise, use single-character input to provide quick history scrolling
        if (codePoint == 'w' || codePoint == 'W') {
            scrollHistoryUp();
            return true;
        }
        if (codePoint == 's' || codePoint == 'S') {
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
