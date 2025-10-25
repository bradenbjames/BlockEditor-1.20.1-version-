package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory.CreatedBlockInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.BiConsumer;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class HistoryPanel {
    // Layout constants
    private static final int PANEL_MARGIN = 10;
    private static final int INNER_PADDING = 8; // equal space on left/right inside the panel
    private static final int ITEM_HEIGHT = 18; // compact height
    private static final int ITEM_WIDTH = 46;  // slightly narrower to allow more columns on wide panels
    private static final int ITEM_SPACING = 2; // tighter spacing
    private static final int TITLE_BAR_HEIGHT = 16;
    // Increase top padding so there is a visible gap under the "Recent Blocks" header
    private static final int CONTENT_TOP_PADDING = 10; // space between title bar and first row of items
    // Larger bottom inset to avoid the panel overlapping bottom-aligned buttons/widgets
    private static final int BOTTOM_INSET = 6; // small inset so panel doesn't overlap bottom-aligned widgets
    private static final int SCROLLBAR_WIDTH = 6; // width reserved for the external scrollbar

    // Text scales for compact entries
    private static final float NAME_TEXT_SCALE = 0.65f; // even smaller
    private static final float HEX_TEXT_SCALE = 0.40f;  // much smaller

    private int scrollOffset = 0; // in items
    private BiConsumer<CreatedBlockInfo, Integer> onItemClick;

    // New: left bound X that the panel should not cross (to avoid overlapping main content)
    // If < 0, the panel will use full available screen width up to margin.
    private int leftBoundX = -1;
    // Optional vertical constraints so the screen can align the panel with other UI elements
    // If topBoundY or bottomBoundY < 0 they are ignored and defaults are used
    private int topBoundY = -1;
    private int bottomBoundY = -1;

    private static final Logger LOGGER = LogUtils.getLogger();

    public void setOnItemClick(BiConsumer<CreatedBlockInfo, Integer> onItemClick) {
        this.onItemClick = onItemClick;
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
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();

        int panelWidth = computePanelWidth(screen);
        // Compute effective content width accounting for symmetric inner padding and the
        // internal scrollbar width so items never draw under the rail.
        int contentWidth = Math.max(0, panelWidth - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);

        int cols = computeColumns(contentWidth);
        Bounds bounds = computeBounds(screen, panelWidth);
        int panelX = bounds.x;
        int panelY = bounds.y;
        int panelHeight = bounds.height;

        // Background and title bar (use exact panel bounds so we don't overlap the external scrollbar)
        graphics.fill(panelX, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth, panelY + panelHeight, 0xE0000000);
        graphics.fill(panelX, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth, panelY, 0xFF333333);

        // Title centered and slightly scaled
        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(panelX + panelWidth / 2f, panelY - 13, 0);
        pose.scale(0.6f, 0.6f, 1.0f);
        graphics.drawCenteredString(font, "§eRecent Blocks", 0, 0, 0xFFFFFF);
        pose.popPose();

        if (history.isEmpty()) {
            graphics.drawCenteredString(font, "§7No blocks yet", panelX + panelWidth / 2, panelY + 20, 0xAAAAAA);
            return;
        }

        // Compute visible rows and scrolling using the padded content height so layout, scrollbar
        // and interaction all agree about where items are placed.
        int effectivePanelHeight = Math.max(0, panelHeight - CONTENT_TOP_PADDING);
        int rowsVisible = Math.max(1, effectivePanelHeight / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;
        int maxScroll = Math.max(0, history.size() - maxVisible);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        // Use exact panel bounds for scissor to avoid drawing under the external scrollbar
        graphics.enableScissor(panelX, panelY, panelX + panelWidth, panelY + panelHeight);

        // Account for top padding inside the panel so there is space between the title and the first row
        // (variables computed above are reused here)

        for (int visibleIndex = 0; visibleIndex < maxVisible; visibleIndex++) {
            int dataIndex = scrollOffset + visibleIndex;
            if (dataIndex >= history.size()) break;
            CreatedBlockInfo info = history.get(dataIndex);

            int row = visibleIndex / cols;
            int col = visibleIndex % cols;

            // No extra per-item shift here: panelX is already adjusted in computeBounds when constrained.
            int itemX = panelX + INNER_PADDING + col * (ITEM_WIDTH + ITEM_SPACING);
            // Add top padding so the first row is offset from the title bar
            int itemY = panelY + CONTENT_TOP_PADDING + row * (ITEM_HEIGHT + ITEM_SPACING);

            boolean hovered = mouseX >= itemX && mouseX < itemX + ITEM_WIDTH && mouseY >= itemY && mouseY < itemY + ITEM_HEIGHT;
            int bg = hovered ? 0x80CCCCCC : ((row % 2 == 0) ? 0x40FFFFFF : 0x20FFFFFF);
            GuiRenderUtil.drawRoundedRect(graphics, itemX, itemY, ITEM_WIDTH, ITEM_HEIGHT, 3, bg);

            // Render tinted item icon (16x16) with tight padding
            pose = graphics.pose();
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
            String hexText = "(#" + info.hexColor + ")";
            int textLeft = itemX + 18; // thinner left padding
            int textAvail = ITEM_WIDTH - 20; // balance margins left/right

            // Name (smaller)
            String trimmedName = trimToWidthScaled(font, name, textAvail, NAME_TEXT_SCALE);
            drawScaledString(graphics, font, trimmedName, textLeft, itemY + 2, NAME_TEXT_SCALE, 0xFFFFFF);

            // Hex (smaller)
            String trimmedHex = trimToWidthScaled(font, hexText, textAvail, HEX_TEXT_SCALE);
            drawScaledString(graphics, font, trimmedHex, textLeft, itemY + 10, HEX_TEXT_SCALE, 0xAAAAAA);
        }

        graphics.disableScissor();

        // Optional scrollbar when overflow (use the padded rows count for consistent sizing)
        int totalRows = (int) Math.ceil(history.size() / (double) cols);
        // Place the scrollbar rail flush with the panel's right edge so it visually aligns
        // with the content area; we've reserved SCROLLBAR_WIDTH from contentWidth above.
        int scrollBarX = panelX + panelWidth - SCROLLBAR_WIDTH;

        // Draw rail constrained to the item area (exclude the title bar). This keeps the rail and
        // thumb visually aligned with the rows they control.
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
    }

    public boolean mouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();
        if (history.isEmpty()) return false;

        Bounds b = computeBounds(screen, computePanelWidth(screen));
        if (!b.contains(mouseX, mouseY)) return false;

        int cols = computeColumns(b.width);
        // Account for the top padding when computing visible rows for hit detection
        int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;

        int relX = (int) (mouseX - b.x);
        int relY = (int) (mouseY - b.y);

        // Subtract the content top padding so clicks above the first row are ignored
        relY -= CONTENT_TOP_PADDING;
        if (relY < 0) return false;

        int col = relX / (ITEM_WIDTH + ITEM_SPACING);
        int row = relY / (ITEM_HEIGHT + ITEM_SPACING);
        if (col < 0 || col >= cols) return false;

        int visibleIndex = row * cols + col;
        if (visibleIndex < 0 || visibleIndex >= maxVisible) return false;

        int dataIndex = scrollOffset + visibleIndex;
        if (dataIndex < 0 || dataIndex >= history.size()) return false;

        if (onItemClick != null) {
            onItemClick.accept(history.get(dataIndex), button);
        }
        return true;
    }

    public boolean mouseScrolled(Screen screen, double mouseX, double mouseY, double delta) {
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();
        if (history.isEmpty()) return false;

        Bounds b = computeBounds(screen, computePanelWidth(screen));
        if (!b.contains(mouseX, mouseY)) return false;

        int cols = computeColumns(b.width);
        // Use padded rows for scroll calculations so scroll amount matches visible items
        int rowsVisible = Math.max(1, Math.max(0, b.height - CONTENT_TOP_PADDING) / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;
        int maxScroll = Math.max(0, history.size() - maxVisible);

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
        return Math.max(ITEM_WIDTH + INNER_PADDING * 2 + 6, available);
    }

    private int computeColumns(int contentWidth) {
        // contentWidth is already panelWidth minus inner padding; compute how many cells fit
        int computed = Math.max(1, (contentWidth + ITEM_SPACING) / (ITEM_WIDTH + ITEM_SPACING));
        // If the panel is constrained (we'll place the scrollbar at the far right), allow one
        // additional column to use the freed-up layout on wide displays. Keep an upper cap.
        if (this.leftBoundX >= 0) {
            computed = computed + 1;
        }
        int upperCap = 8; // allow up to 8 columns on very wide panels
        return Math.min(computed, upperCap);
    }

    private Bounds computeBounds(Screen screen, int panelWidth) {
        int panelX;
        if (leftBoundX >= 0) {
            // If constrained, place the panel so its right edge sits directly left of the scrollbar rail.
            panelX = screen.width - panelWidth - PANEL_MARGIN;
            // Ensure we still respect the provided left bound. If our computed X would start left of the
            // requested leftBoundX, clamp the X to leftBoundX and expand the width so the panel still
            // reaches the scrollbar rail if possible.
            if (panelX < leftBoundX) {
                panelX = leftBoundX;
                // Recompute panelWidth so the panel extends to the scrollbar rail (no gap)
                panelWidth = Math.max(ITEM_WIDTH + INNER_PADDING * 2 + 6,
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
            LOGGER.info("HistoryPanel.computeBounds: requestedTitleTop={}, panelY={}, panelHeight={}, bottomBoundY={}", requestedTitleTop, panelY, panelHeight, this.bottomBoundY);
        } else {
            panelY = 60;
            // Subtract a small bottom inset so the panel doesn't reach all the way to the screen bottom
            panelHeight = screen.height - panelY - 20 - BOTTOM_INSET;
        }
        return new Bounds(panelX, panelY, panelWidth, panelHeight);
    }

    private record Bounds(int x, int y, int width, int height) {
        boolean contains(double mx, double my) {
            // Use exact panel bounds so clicks on the external scrollbar are not treated as panel clicks
            return mx >= x && mx <= x + width && my >= y && my <= y + height;
        }
    }
}
