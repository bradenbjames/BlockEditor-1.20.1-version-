package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory.CreatedBlockInfo;
import com.blockeditor.mod.client.gui.editor.BlockEditorHistory.BlockFolder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.BiConsumer;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class HistoryPanel {
    // Layout constants
    private static final int PANEL_MARGIN = 5;
    private static final int INNER_PADDING = 8; // equal space on left/right inside the panel
    private static final int ITEM_HEIGHT = 18; // compact height
    // Reduce width so items end near the hex code and occupy less horizontal space
    private static final int ITEM_WIDTH = 34;  // narrower as requested (base compact width)
    private static final int ITEM_SPACING = 2; // tighter spacing
    private static final int TITLE_BAR_HEIGHT = 16;
    // Increase top padding so there is a visible gap under the "Recent Blocks" header
    // Use the same padding as the horizontal inner padding so spacing is consistent on all sides
    private static final int CONTENT_TOP_PADDING = INNER_PADDING;
    // Larger bottom inset to avoid the panel overlapping bottom-aligned buttons/widgets
    private static final int BOTTOM_INSET = 6; // small inset so panel doesn't overlap bottom-aligned widgets
    private static final int SCROLLBAR_WIDTH = 6; // width reserved for the external scrollbar

    // Text scales for compact entries
    // Name is primary; hex should be just slightly smaller than the name for readability
    private static final float NAME_TEXT_SCALE = 0.65f;
    private static final float HEX_TEXT_SCALE = 0.30f; // slightly smaller than name

    private int scrollOffset = 0; // in items
    private BiConsumer<CreatedBlockInfo, Integer> onItemClick;
    // Temporary storage for hover-tooltips collected during scissored rendering
    // We'll render these after disabling scissor so they aren't clipped.

    // New: left bound X that the panel should not cross (to avoid overlapping main content)
    // If < 0, the panel will use full available screen width up to margin.
    private int leftBoundX = -1;
    // Optional vertical constraints so the screen can align the panel with other UI elements
    // If topBoundY or bottomBoundY < 0 they are ignored and defaults are used
    private int topBoundY = -1;
    private int bottomBoundY = -1;

    // Toggle state: true = compact (current), false = enlarged (double width)
    // Persist the compact/enlarged toggle for the running game instance (static so it survives screen re-opens)
    private static boolean compactView = true;

    // Full stack toggle: true = create full stacks (64) in survival mode, false = create single items
    private static boolean fullStackMode = false;

    private static final int TOGGLE_SIZE = 12;
    
    // iOS-style toggle button for full stack mode (only visible in survival)
    private PixelatedToggleButton fullStackToggleButton = null;

    // Drag-and-drop state
    private CreatedBlockInfo draggedBlock = null;
    private BlockFolder draggedFromFolder = null; // null if dragged from main list
    private double dragStartX = 0;
    private double dragStartY = 0;
    // Drag initiation threshold and candidate tracking so simple clicks don't trigger drag
    private static final double DRAG_THRESHOLD_PX_SQ = 36.0; // 6px squared
    private CreatedBlockInfo dragCandidateBlock = null;
    private BlockFolder dragCandidateFromFolder = null;
    private double mouseDownX = 0;
    private double mouseDownY = 0;
    private boolean isDragging = false;

    // Public accessors so other screens / code can read/modify the view state for the running instance
    @SuppressWarnings("unused")
    public static boolean isCompactView() {
        return compactView;
    }
    @SuppressWarnings("unused")
    public static void setCompactView(boolean value) {
        compactView = value;
    }

    // Public accessors for full stack mode
    public static boolean isFullStackMode() {
        return fullStackMode;
    }

    public static void setFullStackMode(boolean value) {
        fullStackMode = value;
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    // Represents an item in the display list (either a folder or a block)
    private static class DisplayItem {
        final BlockFolder folder;        // when this represents a folder
        final CreatedBlockInfo block;    // when this represents a block
        final BlockFolder parentFolder;  // non-null when this block is inside a folder
        final boolean isFolder;

        DisplayItem(BlockFolder folder) {
            this.folder = folder;
            this.block = null;
            this.parentFolder = null;
            this.isFolder = true;
        }

        DisplayItem(CreatedBlockInfo block, BlockFolder parentFolder) {
            this.folder = null;
            this.block = block;
            this.parentFolder = parentFolder;
            this.isFolder = false;
        }
    }

    // Build the flat display list: folders, then their contents (if expanded), then main history
    private List<DisplayItem> buildDisplayList() {
        List<DisplayItem> display = new java.util.ArrayList<>();
        List<BlockFolder> folders = BlockEditorHistory.getFolders();
        
        // Add folders and their contents
        for (BlockFolder folder : folders) {
            display.add(new DisplayItem(folder));
            if (folder.expanded) {
                for (CreatedBlockInfo block : folder.blocks) {
                    display.add(new DisplayItem(block, folder));
                }
            }
        }
        
        // Add main history
        for (CreatedBlockInfo block : BlockEditorHistory.getHistory()) {
            display.add(new DisplayItem(block, null));
        }
        
        return display;
    }

    // Allow the screen to constrain the panel to the right side of a given X
    public void setLeftBoundX(int x) {
        this.leftBoundX = x;
    }

    // Allow the screen to specify exact vertical bounds for the panel (top and bottom Y coordinates)
    public void setVerticalBounds(int topY, int bottomY) {
        this.topBoundY = topY;
        this.bottomBoundY = bottomY;
    }

    public void render(Screen screen, GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        List<DisplayItem> displayList = buildDisplayList();

        int panelWidth = computePanelWidth(screen);
        // Compute effective content width accounting for symmetric inner padding and the
        // internal scrollbar width so items never draw under the rail.
        int contentWidth = Math.max(0, panelWidth - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);

        // Use layout-aware columns: compact uses the previous heuristic; enlarged uses fixed columns
        int cols = computeColumnsForLayout(screen, contentWidth);
        Bounds bounds = computeBounds(screen, panelWidth);
        int panelX = bounds.x;
        int panelY = bounds.y;
        int panelHeight = bounds.height;

        // Background and title bar (use exact panel bounds so we don't overlap the external scrollbar)
        graphics.fill(panelX, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth, panelY + panelHeight, 0xE0000000);
        graphics.fill(panelX, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth, panelY, 0xFF333333);

        // Title and debug overlay will be redrawn after content is rendered (after scissor is disabled)
        // to ensure the title bar is always on top and covers any artifact fragments from the content

        if (displayList.isEmpty()) {
            var none = net.minecraft.network.chat.Component.literal("No blocks yet").withStyle(s -> s.withColor(0xAAAAAA));
            graphics.drawCenteredString(font, none, panelX + panelWidth / 2, panelY + 20, 0);
            return;
        }

        // Compute visible rows and scrolling using the padded content height so layout, scrollbar
        // and interaction all agree about where items are placed.
        // Effective panel height for items excludes the title bar and top content padding so
        // rows and scrollbars align correctly with the visible item area.
        int effectivePanelHeight = Math.max(0, panelHeight - CONTENT_TOP_PADDING);
        int rowsVisible = Math.max(1, effectivePanelHeight / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;
        int maxScroll = Math.max(0, displayList.size() - maxVisible);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        // Use exact panel bounds for scissor; start just below the content top plus the configured top padding
        // so items are clipped to the item area and cannot render into the title/header region.
        graphics.enableScissor(panelX, panelY + CONTENT_TOP_PADDING, panelX + panelWidth, panelY + panelHeight);

        // Account for top padding inside the panel so there is space between the title and the first row
        // (variables computed above are reused here)

        String hoveredName = null;
        double hoverX = 0, hoverY = 0; // mouse position for tooltip

        // Compute item width so columns divide the available content width exactly
        int itemWidth = computeItemWidthForLayout(contentWidth, cols);

        for (int visibleIndex = 0; visibleIndex < maxVisible; visibleIndex++) {
            int dataIndex = scrollOffset + visibleIndex;
            if (dataIndex >= displayList.size()) break;
            DisplayItem item = displayList.get(dataIndex);

            int row = visibleIndex / cols;
            int col = visibleIndex % cols;

            // No extra per-item shift here: panelX is already adjusted in computeBounds when constrained.
            int itemX = panelX + INNER_PADDING + col * (itemWidth + ITEM_SPACING);
            // Place the first row at the content-top plus the configured top padding
            int itemY = panelY + CONTENT_TOP_PADDING + row * (ITEM_HEIGHT + ITEM_SPACING);

            boolean hovered = mouseX >= itemX && mouseX < itemX + itemWidth && mouseY >= itemY && mouseY < itemY + ITEM_HEIGHT;
            if (item.isFolder) {
                // Tinted folder background
                int alpha = hovered ? 0x99 : 0x66; // more opaque on hover
                int tintBg = ((alpha & 0xFF) << 24) | (item.folder.color & 0xFFFFFF);
                GuiRenderUtil.drawRoundedRect(graphics, itemX, itemY, itemWidth, ITEM_HEIGHT, 3, tintBg);
            } else {
                boolean insideFolder = item.parentFolder != null;
                if (insideFolder) {
                    // Tint items with the folder color when shown under an expanded folder
                    int alpha = hovered ? 0x99 : 0x66; // more opaque on hover
                    int tintBg = ((alpha & 0xFF) << 24) | (item.parentFolder.color & 0xFFFFFF);
                    GuiRenderUtil.drawRoundedRect(graphics, itemX, itemY, itemWidth, ITEM_HEIGHT, 3, tintBg);
                } else {
                    int bg = hovered ? 0x80CCCCCC : ((row % 2 == 0) ? 0x40FFFFFF : 0x20FFFFFF);
                    GuiRenderUtil.drawRoundedRect(graphics, itemX, itemY, itemWidth, ITEM_HEIGHT, 3, bg);
                }
            }

            if (item.isFolder) {
                // Render folder card (looks like a block card but with text and arrow)
                BlockFolder folder = item.folder;
                
                // Folder name text (same scale and position as block name)
                String folderName = folder.name != null ? folder.name : "Unnamed Folder";
                int nameColor = 0xFFFFFF;
                int textLeft = itemX + 4; // left padding
                int textAvail = itemWidth - 16; // reserve space for arrow on right
                String trimmedName = trimToWidthScaled(font, folderName, textAvail, NAME_TEXT_SCALE);
                drawScaledString(graphics, font, trimmedName, textLeft, itemY + 6, NAME_TEXT_SCALE, nameColor);
                
                // Arrow icon on the right (▼ expanded, ► collapsed)
                int arrowX = itemX + itemWidth - 10;
                int arrowY = itemY + (ITEM_HEIGHT - 7) / 2;
                GuiRenderUtil.drawSmallArrow(graphics, arrowX, arrowY, folder.expanded, 0xFFAAAAAA);
                
                // Tooltip with folder info
                if (hovered) {
                    hoveredName = folderName + " (" + folder.blocks.size() + " blocks)";
                    hoverX = mouseX;
                    hoverY = mouseY;
                }
            } else {
                // Render block card (existing code)
                CreatedBlockInfo info = item.block;
                
                // Render tinted item icon (16x16) with tight padding
                var pose = graphics.pose();
                pose.pushPose();
                RenderSystem.setShaderColor(
                    ((info.color >> 16) & 0xFF) / 255.0f,
                    ((info.color >> 8) & 0xFF) / 255.0f,
                    (info.color & 0xFF) / 255.0f,
                    1.0f
                );
                graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), itemX + 1, itemY + 1);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                pose.popPose();

                // Text area (scaled)
                String name = info.blockName != null ? info.blockName : "";
                // If name is empty show a cyan/turquoise placeholder; otherwise show the real name in white
                String displayName = name.isEmpty() ? "Unnamed" : name;
                int nameColor = name.isEmpty() ? 0x00FFFF : 0xFFFFFF; // cyan/turquoise for placeholder
                // Always format hex as '#RRGGBB' (no parentheses). In compact view we show it for all items;
                // in enlarged view we fall back to only showing it when the name fits (previous behavior).
                String hexText = (info.hexColor != null && !info.hexColor.isEmpty()) ? "#" + info.hexColor : null;
                int textLeft = itemX + 18; // thinner left padding
                int textAvail = itemWidth - 20; // balance margins left/right

                // Decide whether the raw name fits entirely (in unscaled px space) in the available width.
                // Only consider real names for the hex-layout decision; placeholder should not force hex rendering.
                boolean nameFitsRaw = !name.isEmpty() && font.width(name) <= (int) Math.floor(textAvail / Math.max(0.001f, NAME_TEXT_SCALE));

                // Name (smaller)
                String trimmedName = trimToWidthScaled(font, displayName, textAvail, NAME_TEXT_SCALE);
                drawScaledString(graphics, font, trimmedName, textLeft, itemY + 2, NAME_TEXT_SCALE, nameColor);

                // Hex (smaller) - show formatted '#FFFFFF' in compact view for all items; otherwise only if the name fits
                if (hexText != null && (compactView || nameFitsRaw)) {
                    String trimmedHex = trimToWidthScaled(font, hexText, textAvail, HEX_TEXT_SCALE);
                    drawScaledString(graphics, font, trimmedHex, textLeft, itemY + 10, HEX_TEXT_SCALE, 0xAAAAAA);
                }

                // Collect hover info so we can render a tooltip after scissor is disabled (not clipped)
                if (hovered && !name.isEmpty()) {
                    // Store into local variables on the stack via final copy - we'll render below after scissor
                    hoveredName = name;
                    hoverX = mouseX;
                    hoverY = mouseY;
                }
            }
            // Removed left stripe to eliminate thin sliver artifacts on card edges
        }

        graphics.disableScissor();
        
        // Render dragged item card following mouse (semi-transparent full card)
        if (isDragging && draggedBlock != null) {
            int dragCardX = (int) mouseX - (getItemWidth() / 2);
            int dragCardY = (int) mouseY - (ITEM_HEIGHT / 2);
            int dragCardWidth = getItemWidth();
            
            // Semi-transparent background
            GuiRenderUtil.drawRoundedRect(graphics, dragCardX, dragCardY, dragCardWidth, ITEM_HEIGHT, 3, 0x80FFFFFF);
            
            // Render tinted item icon
            var pose = graphics.pose();
            pose.pushPose();
            RenderSystem.setShaderColor(
                ((draggedBlock.color >> 16) & 0xFF) / 255.0f,
                ((draggedBlock.color >> 8) & 0xFF) / 255.0f,
                (draggedBlock.color & 0xFF) / 255.0f,
                0.6f  // Semi-transparent
            );
            graphics.renderItem(draggedBlock.originalBlock.asItem().getDefaultInstance(), dragCardX + 1, dragCardY + 1);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            pose.popPose();
            
            // Text (name and hex)
            String name = draggedBlock.blockName != null ? draggedBlock.blockName : "";
            String displayName = name.isEmpty() ? "Unnamed" : name;
            int nameColor = (name.isEmpty() ? 0x00FFFF : 0xFFFFFF) & 0x80FFFFFF; // semi-transparent
            String hexText = (draggedBlock.hexColor != null && !draggedBlock.hexColor.isEmpty()) ? "#" + draggedBlock.hexColor : null;
            int textLeft = dragCardX + 18;
            int textAvail = dragCardWidth - 20;
            
            String trimmedName = trimToWidthScaled(font, displayName, textAvail, NAME_TEXT_SCALE);
            drawScaledString(graphics, font, trimmedName, textLeft, dragCardY + 2, NAME_TEXT_SCALE, nameColor);
            
            if (hexText != null) {
                String trimmedHex = trimToWidthScaled(font, hexText, textAvail, HEX_TEXT_SCALE);
                drawScaledString(graphics, font, trimmedHex, textLeft, dragCardY + 10, HEX_TEXT_SCALE, 0x80AAAAAA);
            }
        }
        
        // If an item was hovered we want to render an unclipped tooltip with the full name
        if (hoveredName != null && !hoveredName.isEmpty()) {
            // renderTooltip expects integer coordinates in this MC version
            graphics.renderTooltip(font, net.minecraft.network.chat.Component.literal(hoveredName), (int) hoverX, (int) hoverY);
        }

        // Redraw title on top of scissored content to avoid artifacts
        var title = net.minecraft.network.chat.Component.literal("Recent Blocks");
        graphics.drawCenteredString(font, title, panelX + panelWidth / 2, panelY - TITLE_BAR_HEIGHT + 4, 0xFFFFFF);

        // Draw "+" button for creating new folders (to the left of the title)
            // "+" button for creating new folders (move to top-right)
            int addButtonSize = 12;
            // Check if player is in survival mode to know if the full-stack toggle is visible on right
            boolean inSurvival = false;
            var mcForAdd = net.minecraft.client.Minecraft.getInstance();
            if (mcForAdd.player != null) {
                inSurvival = !mcForAdd.player.getAbilities().instabuild;
            }
            int rightInset = INNER_PADDING;
            int fullStackToggleWidth = 24;
            int gap = 4;
            int addButtonX;
            int toggleY = panelY - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - TOGGLE_SIZE) / 2 + 1;
            if (inSurvival) {
                int fullStackToggleX = panelX + panelWidth - INNER_PADDING - fullStackToggleWidth;
                addButtonX = fullStackToggleX - gap - addButtonSize;
            } else {
                addButtonX = panelX + panelWidth - rightInset - addButtonSize;
            }
            int addButtonY = panelY - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - addButtonSize) / 2;
            boolean addButtonHovered = mouseX >= addButtonX && mouseX < addButtonX + addButtonSize &&
                                       mouseY >= addButtonY && mouseY < addButtonY + addButtonSize;
            int addButtonBg = addButtonHovered ? 0xFF555555 : 0xFF333333;
            GuiRenderUtil.drawRoundedRect(graphics, addButtonX, addButtonY, addButtonSize, addButtonSize, 3, addButtonBg);
            graphics.drawCenteredString(font, "+", addButtonX + addButtonSize / 2, addButtonY + 2, 0xFFFFFF);

        // Draw toggles in the title bar
        // Nudge toggles down by 1px to avoid overlapping the title bar's top seam, which
        // can make the top edge look uneven at small sizes.
        // toggleY computed above for the + button is reused here
        int compactToggleX = panelX + INNER_PADDING;
        
        // Draw compact/enlarged toggle on the LEFT side (always shown)
        int toggleBg = compactView ? 0xFF555555 : 0xFF808022; // different background to indicate state
        GuiRenderUtil.drawRoundedRect(graphics, compactToggleX, toggleY, TOGGLE_SIZE, TOGGLE_SIZE, 3, toggleBg);
        // draw a simple indicator: one bar for compact, two bars for enlarged
        if (compactView) {
            graphics.fill(compactToggleX + (TOGGLE_SIZE / 2) - 2, toggleY + 3, compactToggleX + (TOGGLE_SIZE / 2) + 2, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
        } else {
            graphics.fill(compactToggleX + 3, toggleY + 3, compactToggleX + 5, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
            graphics.fill(compactToggleX + TOGGLE_SIZE - 5, toggleY + 3, compactToggleX + TOGGLE_SIZE - 3, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
        }

        // Draw full stack toggle on the RIGHT side (only in survival mode)
        if (inSurvival) {
            int fullStackToggleX = panelX + panelWidth - INNER_PADDING - 24; // 24 is the toggle width
            
            // Create or update the toggle button position
            if (fullStackToggleButton == null) {
                fullStackToggleButton = new PixelatedToggleButton(
                    fullStackToggleX, 
                    toggleY, 
                    24,  // width for iOS-style toggle (wider than tall)
                    TOGGLE_SIZE, 
                    fullStackMode,
                    (state) -> fullStackMode = state
                );
            } else {
                // Update position in case panel moved, and sync state
                fullStackToggleButton = new PixelatedToggleButton(
                    fullStackToggleX, 
                    toggleY, 
                    24, 
                    TOGGLE_SIZE, 
                    fullStackMode,
                    (state) -> fullStackMode = state
                );
            }
            
            // Render the toggle button
            fullStackToggleButton.render(graphics, (int) mouseX, (int) mouseY, 0);
        } else {
            // Not in survival mode, clear the button
            fullStackToggleButton = null;
        }

        // Optional scrollbar when overflow (use the padded rows count for consistent sizing)
        int totalRows = (int) Math.ceil(displayList.size() / (double) cols);
        // Place the scrollbar rail flush with the panel's right edge so it visually aligns
        // with the content area; we've reserved SCROLLBAR_WIDTH from contentWidth above.
        int scrollBarX = panelX + panelWidth - SCROLLBAR_WIDTH;

        // Draw rail constrained to the item area (exclude the title bar). This keeps the rail and
        // thumb visually aligned with the rows they control.
        // Rail top should match the top of the item area so the thumb aligns with rows
        int railTop = panelY + CONTENT_TOP_PADDING;
        int railBottom = panelY + panelHeight;
        graphics.fill(scrollBarX, railTop, scrollBarX + SCROLLBAR_WIDTH, railBottom, 0xFF666666);

        if (totalRows <= rowsVisible) {
            // No overflow: thumb fills the rail height
            int thumbHeight = Math.max(10, effectivePanelHeight);
            graphics.fill(scrollBarX, railTop, scrollBarX + SCROLLBAR_WIDTH, railTop + thumbHeight, 0xFF4D4D4D);
        } else {
            // Normal overflow case: draw proportional thumb using the padded rows and height
            int thumbHeight = Math.max(10, (rowsVisible * effectivePanelHeight) / totalRows);
            int scrollRowOffset = scrollOffset / cols; // convert item offset to row offset
            int maxScrollRows = Math.max(1, totalRows - rowsVisible);
            int thumbY = railTop + (scrollRowOffset * (effectivePanelHeight - thumbHeight)) / maxScrollRows;
            graphics.fill(scrollBarX, thumbY, scrollBarX + SCROLLBAR_WIDTH, thumbY + thumbHeight, 0xFFCCCCCC);
        }
        
        // Render tooltips for toggles (after all other rendering so they appear on top)
        
        // Check "+" button hover (reuse variables from above)
        if (addButtonHovered) {
            graphics.renderTooltip(font, net.minecraft.network.chat.Component.literal("Create New Folder"), (int) mouseX, (int) mouseY);
        }
        
        // Check compact toggle hover
        boolean compactToggleHovered = mouseX >= compactToggleX && mouseX < compactToggleX + TOGGLE_SIZE &&
                                       mouseY >= toggleY && mouseY < toggleY + TOGGLE_SIZE;
        if (compactToggleHovered) {
            String compactTooltip = compactView ? "Switch to Large View" : "Switch to Compact View";
            graphics.renderTooltip(font, net.minecraft.network.chat.Component.literal(compactTooltip), (int) mouseX, (int) mouseY);
        }
        
        // Check full stack toggle hover (only if in survival mode)
        if (inSurvival && fullStackToggleButton != null && fullStackToggleButton.isHovered((int) mouseX, (int) mouseY)) {
            String fullStackTooltip = fullStackMode ? "Full Stack Mode (x64)" : "Single Item Mode (x1)";
            graphics.renderTooltip(font, net.minecraft.network.chat.Component.literal(fullStackTooltip), (int) mouseX, (int) mouseY);
        }
    }

    public boolean mouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        List<DisplayItem> displayList = buildDisplayList();
        Bounds b = computeBounds(screen, computePanelWidth(screen));

        // Check if player is in survival mode for full stack toggle
        boolean inSurvival = false;
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null) {
            inSurvival = !mc.player.getAbilities().instabuild;
        }

        // Compute toggle bounds in the title bar
        // Match the 1px vertical nudge applied in render() so hitboxes align visually
        int toggleY = b.y - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - TOGGLE_SIZE) / 2 + 1;

        // Check "+" button for creating folders (top-right)
        int addButtonSize = 12;
        int rightInset = INNER_PADDING;
        int fullStackToggleWidth = 24;
        int gap = 4;
        int addButtonX;
        if (inSurvival) {
            int fullStackToggleX = b.x + b.width - INNER_PADDING - fullStackToggleWidth;
            addButtonX = fullStackToggleX - gap - addButtonSize;
        } else {
            addButtonX = b.x + b.width - rightInset - addButtonSize;
        }
        int addButtonY = b.y - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - addButtonSize) / 2;
        boolean clickedAddButton = mouseX >= addButtonX && mouseX < addButtonX + addButtonSize &&
                                   mouseY >= addButtonY && mouseY < addButtonY + addButtonSize;
        if (clickedAddButton) {
            // Open folder creation dialog (name + color)
            Minecraft.getInstance().setScreen(new FolderDialogScreen((Screen) screen, (folderName, colorRgb) -> {
                String effectiveName = (folderName == null || folderName.trim().isEmpty()) ? "New Folder" : folderName.trim();
                BlockEditorHistory.createFolder(effectiveName, colorRgb);
            }));
            return true;
        }

        // Check compact/enlarged toggle (LEFT side)
        int compactToggleX = b.x + INNER_PADDING;
        boolean clickedCompactToggle = mouseX >= compactToggleX && mouseX < compactToggleX + TOGGLE_SIZE &&
                                      mouseY >= toggleY && mouseY < toggleY + TOGGLE_SIZE;
        if (clickedCompactToggle) {
            compactView = !compactView;
            // clamp scroll offset to new visible range
            int panelWidth = computePanelWidth(screen);
            try {
                LOGGER.debug("HistoryPanel compact view toggled -> {}", compactView);
            } catch (Exception ignored) {
                // intentionally ignore logging errors in environments where logging might not be available
            }
            int contentWidth = Math.max(0, panelWidth - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
            int cols = computeColumnsForLayout(screen, contentWidth);
            int rowsVisible = Math.max(1, Math.max(0, computeBounds(screen, panelWidth).height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
            int maxVisible = rowsVisible * cols;
            int maxScroll = Math.max(0, displayList.size() - maxVisible);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }

        // Check full stack toggle (RIGHT side, only in survival)
        if (inSurvival && fullStackToggleButton != null) {
            if (fullStackToggleButton.mouseClicked(mouseX, mouseY, button)) {
                fullStackMode = fullStackToggleButton.isToggled();
                try {
                    LOGGER.debug("HistoryPanel full stack mode toggled -> {}", fullStackMode);
                } catch (Exception ignored) {
                    // intentionally ignore logging errors
                }
                return true;
            }
        }

        // If the click wasn't on the toggle, require the click be within the content bounds
        if (!b.contains(mouseX, mouseY)) return false;

        int contentWidth = Math.max(0, b.width - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
        int cols = computeColumnsForLayout(screen, contentWidth);
        // Account for the top padding when computing visible rows for hit detection
        int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;

        int relX = (int) (mouseX - b.x);
        int relY = (int) (mouseY - b.y);

        // Subtract the content top padding so clicks above the first row are ignored
        relY -= CONTENT_TOP_PADDING;
        if (relY < 0) return false;

        // Adjust for the left inner padding used when rendering items
        relX -= INNER_PADDING;
        if (relX < 0) return false;

        int itemWidth = computeItemWidthForLayout(contentWidth, cols);
        int col = relX / (itemWidth + ITEM_SPACING);
        int row = relY / (ITEM_HEIGHT + ITEM_SPACING);
        if (col < 0 || col >= cols) return false;

        int visibleIndex = row * cols + col;
        if (visibleIndex < 0 || visibleIndex >= maxVisible) return false;

        int dataIndex = scrollOffset + visibleIndex;
        if (dataIndex < 0 || dataIndex >= displayList.size()) return false;

        DisplayItem item = displayList.get(dataIndex);
        
        // If it's a folder, toggle it
        if (item.isFolder) {
            item.folder.expanded = !item.folder.expanded;
            BlockEditorHistory.saveHistoryToFile();
            return true;
        }

        // Otherwise, it's a block - pass to the click handler
        if (onItemClick != null && item.block != null) {
            onItemClick.accept(item.block, button);
        }
        return true;
    }

    public boolean mouseScrolled(Screen screen, double mouseX, double mouseY, double delta) {
        List<DisplayItem> displayList = buildDisplayList();
        if (displayList.isEmpty()) return false;

        Bounds b = computeBounds(screen, computePanelWidth(screen));
        if (!b.contains(mouseX, mouseY)) return false;

        int panelWidth = b.width;
        int contentWidth = Math.max(0, panelWidth - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
        int cols = computeColumnsForLayout(screen, contentWidth);
        // Use padded rows for scroll calculations so scroll amount matches visible items
        int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;
        int maxScroll = Math.max(0, displayList.size() - maxVisible);

        if (delta > 0) {
            scrollOffset = Math.max(0, scrollOffset - cols); // scroll by a row
        } else if (delta < 0) {
            scrollOffset = Math.min(maxScroll, scrollOffset + cols);
        }
        return true;
    }

    public boolean isMouseOver(Screen screen, double mouseX, double mouseY) {
        Bounds b = computeBounds(screen, computePanelWidth(screen));
        return b.contains(mouseX, mouseY);
    }

    public void scrollUp() {
        int cols = 1; // fallback; actual step refined during scroll calls
        scrollOffset = Math.max(0, scrollOffset - cols);
    }

    public void scrollDown() {
        int size = BlockEditorHistory.getHistory().size();
        scrollOffset = Math.min(size, scrollOffset + 1);
    }

    // Helpers
    private static void drawScaledString(GuiGraphics g, Font font, String text, int x, int y, float scale, int color) {
        var pose = g.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, 1f);
        g.drawString(font, text, 0, 0, color, false);
        pose.popPose();
    }

    private static String trimToWidthScaled(Font font, String text, int widthPx, float scale) {
        if (text == null) return "";
        // Work in unscaled space by dividing allowed width by scale
        int allowed = (int) Math.floor(widthPx / Math.max(0.001f, scale));
        int w = font.width(text);
        if (w <= allowed) return text;
        String ell = "...";
        int ellW = font.width(ell);
        for (int i = text.length() - 1; i >= 0; i--) {
            String sub = text.substring(0, i);
            if (font.width(sub) + ellW <= allowed) return sub + ell;
        }
        return text;
    }

    private int computePanelWidth(Screen screen) {
        // Compute available width to the right of leftBoundX (if provided), up to a max
        int available;
        if (leftBoundX >= 0) {
            // Allow the panel to expand all the way to the right (we will render the scrollbar
            // inside the panel itself). Don't reserve extra space for an external scrollbar.
            available = Math.max(0, screen.width - leftBoundX - PANEL_MARGIN);
        } else {
            available = screen.width / 4; // fallback heuristic when no bound provided
        }
        // Use the available space (right half) directly so the panel naturally fills the right side.
        // Ensure at least ITEM_WIDTH plus inner padding is returned.
        return Math.max(getItemWidth() + INNER_PADDING * 2 + 6, available);
    }

    private int computeColumns(Screen screen, int contentWidth) {
        // contentWidth is already panelWidth minus inner padding; compute how many cells fit
        int computed = Math.max(1, (contentWidth + ITEM_SPACING) / (getItemWidth() + ITEM_SPACING));
        // If the panel is constrained (we'll place the scrollbar at the far right), allow one
        // additional column to use the freed-up layout on wide displays.
        if (this.leftBoundX >= 0) {
            computed = computed + 1;
        }

        // Cap columns based on the overall screen resolution:
        // - For 1920-wide (1080p) or smaller screens, use up to 6 columns
        // - For larger screens, allow up to 10 columns
        int dynamicCap = getDynamicCap(screen);

        return Math.min(computed, dynamicCap);
    }

    private static int getDynamicCap(Screen screen) {
        int guiWidth = (screen != null) ? screen.width : 1920;
        int windowPixelWidth;
        try {
            windowPixelWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        } catch (Exception ignored) {
            windowPixelWidth = guiWidth;
        }
        int maxObservedWidth = Math.max(guiWidth, windowPixelWidth);
        return (maxObservedWidth > 1920) ? 10 : 6;
    }

    private Bounds computeBounds(Screen screen, int panelWidth) {
        int panelX;
        if (leftBoundX >= 0) {
            // If constrained, place the panel so its right edge sits directly left of the scrollbar rail.
            panelX = screen.width - panelWidth - PANEL_MARGIN;
            // Ensure we still respect the provided left bound. If our computed X would start left of the
            // leftBoundX, clamp to leftBoundX and expand the panel width to fill to the rail.
            if (panelX < leftBoundX) {
                panelX = leftBoundX;
                panelWidth = Math.max(getItemWidth() + INNER_PADDING * 2 + 6,
                        Math.max(0, screen.width - panelX - PANEL_MARGIN));
            }
        } else {
            panelX = screen.width - panelWidth - PANEL_MARGIN;
        }
        int panelY;
        int panelHeight;
        if (this.topBoundY >= 0 && this.bottomBoundY > this.topBoundY) {
            // Interpret provided topBoundY as the top of the title bar (so callers can align the
            // title bar with other UI elements). Compute the actual content Y (panelY) by
            // shifting down by TITLE_BAR_HEIGHT. Clamp to the screen and compute the height so
            // the panel's bottom matches bottomBoundY.
            int requestedTitleTop = Math.max(0, Math.min(this.topBoundY, screen.height - 1));
            panelY = Math.max(0, requestedTitleTop + TITLE_BAR_HEIGHT);
            // Ensure we don't produce a negative height; bottomBoundY is expected to be the
            // absolute bottom coordinate (e.g., bottom of the buttons). Compute height as
            // bottomBoundY - panelY clamped to screen, and subtract a small BOTTOM_INSET
            // to avoid overlapping bottom-aligned widgets.
            panelHeight = Math.max(ITEM_HEIGHT, Math.min(screen.height - panelY, this.bottomBoundY - panelY - BOTTOM_INSET));
        } else {
            panelY = 60;
            // Subtract a small bottom inset so the panel doesn't reach all the way to the screen bottom
            panelHeight = screen.height - panelY - 20 - BOTTOM_INSET;
        }
        return new Bounds(panelX, panelY, panelWidth, panelHeight);
    }

    private static final class Bounds {
        final int x;
        final int y;
        final int width;
        final int height;

        Bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean contains(double mx, double my) {
            // Use exact panel bounds so clicks on the external scrollbar are not treated as panel clicks
            return mx >= x && mx <= x + width && my >= y && my <= y + height;
        }
    }

    // Return the current effective item width depending on compact/enlarged state
    private int getItemWidth() {
        return compactView ? ITEM_WIDTH : ITEM_WIDTH * 2;
    }

    // Layout helpers that know about compact/enlarged view rules
    private int computeColumnsForLayout(Screen screen, int contentWidth) {
        if (!compactView) {
            // Enlarged view: choose fixed columns so items line up perfectly.
            // Use 3 columns on 1080p (<= 1920 width) and 5 columns on larger displays.
            int guiWidth = (screen != null) ? screen.width : 1920;
            int windowPixelWidth;
            try {
                windowPixelWidth = Minecraft.getInstance().getWindow().getScreenWidth();
            } catch (Exception ignored) {
                windowPixelWidth = guiWidth;
            }
            int maxObservedWidth = Math.max(guiWidth, windowPixelWidth);
            return (maxObservedWidth <= 1920) ? 3 : 5;
        }
        // Compact: fall back to the previous computed columns behavior
        return computeColumns(screen, contentWidth);
    }

    private int computeItemWidthForLayout(int contentWidth, int cols) {
        if (cols <= 0) return ITEM_WIDTH;
        int totalSpacing = ITEM_SPACING * Math.max(0, cols - 1);
        int availableForItems = Math.max(0, contentWidth - totalSpacing);
        int w = (availableForItems / cols);
        // Ensure we don't shrink below the nominal compact width
        return Math.max(w, ITEM_WIDTH);
    }

    // Drag-and-drop support
    public boolean mousePressed(Screen screen, double mouseX, double mouseY, int button) {
        if (button != 0) return false; // Only left-click drag
        
        List<DisplayItem> displayList = buildDisplayList();
        Bounds b = computeBounds(screen, computePanelWidth(screen));
        
        if (!b.contains(mouseX, mouseY)) return false;

        int contentWidth = Math.max(0, b.width - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
        int cols = computeColumnsForLayout(screen, contentWidth);
        int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;

        int relX = (int) (mouseX - b.x);
        int relY = (int) (mouseY - b.y);
        relY -= CONTENT_TOP_PADDING;
        if (relY < 0) return false;
        relX -= INNER_PADDING;
        if (relX < 0) return false;

        int itemWidth = computeItemWidthForLayout(contentWidth, cols);
        int col = relX / (itemWidth + ITEM_SPACING);
        int row = relY / (ITEM_HEIGHT + ITEM_SPACING);
        if (col < 0 || col >= cols) return false;

        int visibleIndex = row * cols + col;
        if (visibleIndex < 0 || visibleIndex >= maxVisible) return false;

        int dataIndex = scrollOffset + visibleIndex;
        if (dataIndex < 0 || dataIndex >= displayList.size()) return false;

        DisplayItem item = displayList.get(dataIndex);
        
        // Prepare a drag candidate for blocks (not folders); don't start dragging yet
        if (!item.isFolder && item.block != null) {
            dragCandidateBlock = item.block;
            // Determine source folder (if any) for the candidate
            dragCandidateFromFolder = null;
            for (BlockFolder folder : BlockEditorHistory.getFolders()) {
                if (folder.blocks.contains(item.block)) {
                    dragCandidateFromFolder = folder;
                    break;
                }
            }
            // Record initial press position; defer actual drag until threshold exceeded
            mouseDownX = mouseX;
            mouseDownY = mouseY;
            dragStartX = mouseX;
            dragStartY = mouseY;
            isDragging = false;
            draggedBlock = null;
            draggedFromFolder = null;
            return true; // capture for potential drag sequence
        }
        
        return false;
    }

    public boolean mouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        // Only begin dragging after moving past the threshold from the press point
        if (!isDragging && dragCandidateBlock != null) {
            double dx = mouseX - mouseDownX;
            double dy = mouseY - mouseDownY;
            if ((dx * dx + dy * dy) >= DRAG_THRESHOLD_PX_SQ) {
                // Start actual drag
                draggedBlock = dragCandidateBlock;
                draggedFromFolder = dragCandidateFromFolder;
                isDragging = true;
            }
        }
        // Dragging is tracked, rendering happens in render()
        return isDragging && draggedBlock != null;
    }

    public boolean mouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        // If we never started a drag, clear candidate and let normal click handling proceed
        if (!isDragging || draggedBlock == null) {
            dragCandidateBlock = null;
            dragCandidateFromFolder = null;
            isDragging = false;
            draggedBlock = null;
            draggedFromFolder = null;
            return false;
        }
        
        List<DisplayItem> displayList = buildDisplayList();
        Bounds b = computeBounds(screen, computePanelWidth(screen));
        
        // Check if we dropped on a folder
        if (b.contains(mouseX, mouseY)) {
            int contentWidth = Math.max(0, b.width - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
            int cols = computeColumnsForLayout(screen, contentWidth);
            int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
            int maxVisible = rowsVisible * cols;

            int relX = (int) (mouseX - b.x);
            int relY = (int) (mouseY - b.y);
            relY -= CONTENT_TOP_PADDING;
            relX -= INNER_PADDING;

            if (relX >= 0 && relY >= 0) {
                int itemWidth = computeItemWidthForLayout(contentWidth, cols);
                int col = relX / (itemWidth + ITEM_SPACING);
                int row = relY / (ITEM_HEIGHT + ITEM_SPACING);

                if (col >= 0 && col < cols) {
                    int visibleIndex = row * cols + col;
                    if (visibleIndex >= 0 && visibleIndex < maxVisible) {
                        int dataIndex = scrollOffset + visibleIndex;
                        if (dataIndex >= 0 && dataIndex < displayList.size()) {
                            DisplayItem item = displayList.get(dataIndex);
                            
                            // Dropped on a folder - add block to it
                            if (item.isFolder) {
                                BlockEditorHistory.moveBlockToFolder(draggedBlock, item.folder);
                                // Clear drag state after successful drop
                                dragCandidateBlock = null;
                                dragCandidateFromFolder = null;
                                isDragging = false;
                                draggedBlock = null;
                                draggedFromFolder = null;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        // Dropped outside any folder - move to main list
        if (draggedFromFolder != null) {
            BlockEditorHistory.moveBlockToFolder(draggedBlock, null);
        }
        
        // Clear drag state
        dragCandidateBlock = null;
        dragCandidateFromFolder = null;
        isDragging = false;
        draggedBlock = null;
        draggedFromFolder = null;
        return true;
    }

    public void setOnItemClick(BiConsumer<CreatedBlockInfo, Integer> handler) {
        this.onItemClick = handler;
    }
}
