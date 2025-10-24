package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory.CreatedBlockInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.BiConsumer;

public final class HistoryPanel {
    // Layout constants
    private static final int PANEL_MARGIN = 10;
    private static final int ITEM_HEIGHT = 18; // keep compact height
    private static final int ITEM_WIDTH = 54;  // about half of original; icon + tight text
    private static final int ITEM_SPACING = 3;
    private static final int TITLE_BAR_HEIGHT = 16;

    // Text scales for compact entries
    private static final float NAME_TEXT_SCALE = 0.85f;
    private static final float HEX_TEXT_SCALE = 0.75f;

    private int scrollOffset = 0; // in items
    private BiConsumer<CreatedBlockInfo, Integer> onItemClick;

    // New: left bound X that the panel should not cross (to avoid overlapping main content)
    // If < 0, the panel will use full available screen width up to margin.
    private int leftBoundX = -1;

    public void setOnItemClick(BiConsumer<CreatedBlockInfo, Integer> onItemClick) {
        this.onItemClick = onItemClick;
    }

    // Allow the screen to constrain the panel to the right side of a given X
    public void setLeftBoundX(int x) {
        this.leftBoundX = x;
    }

    public void render(Screen screen, GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();

        int panelWidth = computePanelWidth(screen);
        int cols = computeColumns(panelWidth);
        Bounds bounds = computeBounds(screen, panelWidth);
        int panelX = bounds.x;
        int panelY = bounds.y;
        int panelHeight = bounds.height;

        // Background and title bar
        graphics.fill(panelX - 5, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth + 5, panelY + panelHeight + 5, 0xE0000000);
        graphics.fill(panelX - 5, panelY - TITLE_BAR_HEIGHT, panelX + panelWidth + 5, panelY - 5, 0xFF333333);

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

        int rowsVisible = Math.max(1, panelHeight / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;
        int maxScroll = Math.max(0, history.size() - maxVisible);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        graphics.enableScissor(panelX - 5, panelY, panelX + panelWidth + 5, panelY + panelHeight);

        for (int visibleIndex = 0; visibleIndex < maxVisible; visibleIndex++) {
            int dataIndex = scrollOffset + visibleIndex;
            if (dataIndex >= history.size()) break;
            CreatedBlockInfo info = history.get(dataIndex);

            int row = visibleIndex / cols;
            int col = visibleIndex % cols;

            int itemX = panelX + 4 + col * (ITEM_WIDTH + ITEM_SPACING);
            int itemY = panelY + row * (ITEM_HEIGHT + ITEM_SPACING);

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
            graphics.renderItem(info.originalBlock.asItem().getDefaultInstance(), itemX + 2, itemY + 1);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            pose.popPose();

            // Text area (scaled)
            String name = info.blockName != null ? info.blockName : "";
            String hexText = "(#" + info.hexColor + ")";
            int textLeft = itemX + 20; // leave space for icon
            int textAvail = ITEM_WIDTH - 22;

            // Name (slightly larger)
            String trimmedName = trimToWidthScaled(font, name, textAvail, NAME_TEXT_SCALE);
            drawScaledString(graphics, font, trimmedName, textLeft, itemY + 2, NAME_TEXT_SCALE, 0xFFFFFF);

            // Hex (smaller)
            String trimmedHex = trimToWidthScaled(font, hexText, textAvail, HEX_TEXT_SCALE);
            drawScaledString(graphics, font, trimmedHex, textLeft, itemY + 10, HEX_TEXT_SCALE, 0xAAAAAA);
        }

        graphics.disableScissor();

        // Optional scrollbar when overflow
        int totalRows = (int) Math.ceil(history.size() / (double) cols);
        if (totalRows > rowsVisible) {
            int scrollBarX = panelX + panelWidth + 2;
            graphics.fill(scrollBarX, panelY, scrollBarX + 3, panelY + panelHeight, 0xFF666666);

            int thumbHeight = Math.max(10, (rowsVisible * panelHeight) / totalRows);
            int scrollRowOffset = scrollOffset / cols;
            int maxScrollRows = Math.max(1, totalRows - rowsVisible);
            int thumbY = panelY + (scrollRowOffset * (panelHeight - thumbHeight)) / maxScrollRows;
            graphics.fill(scrollBarX, thumbY, scrollBarX + 3, thumbY + thumbHeight, 0xFFCCCCCC);
        }
    }

    public boolean mouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();
        if (history.isEmpty()) return false;

        Bounds b = computeBounds(screen, computePanelWidth(screen));
        if (!b.contains(mouseX, mouseY)) return false;

        int cols = computeColumns(b.width);
        int rowsVisible = Math.max(1, b.height / (ITEM_HEIGHT + ITEM_SPACING));
        int maxVisible = rowsVisible * cols;

        int relX = (int) (mouseX - b.x);
        int relY = (int) (mouseY - b.y);

        int col = relX / (ITEM_WIDTH + ITEM_SPACING);
        int row = relY / (ITEM_HEIGHT + ITEM_SPACING);
        if (col < 0 || col >= cols || row < 0) return false;

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
        int rowsVisible = Math.max(1, b.height / (ITEM_HEIGHT + ITEM_SPACING));
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
        int rightMargin = PANEL_MARGIN;
        int available;
        if (leftBoundX >= 0) {
            available = Math.max(0, screen.width - leftBoundX - rightMargin);
        } else {
            available = screen.width / 4; // fallback heuristic when no bound provided
        }
        int max = 300;
        int panelWidth = Math.min(max, Math.max(ITEM_WIDTH, available)); // allow shrinking to 1,2,3 columns
        return panelWidth;
    }

    private static int computeColumns(int panelWidth) {
        // Dynamically compute how many ITEM_WIDTH cells fit into the panel, capped at 3 columns
        int computed = Math.max(1, (panelWidth + ITEM_SPACING) / (ITEM_WIDTH + ITEM_SPACING));
        return Math.min(3, computed);
    }

    private Bounds computeBounds(Screen screen, int panelWidth) {
        int panelX = screen.width - panelWidth - PANEL_MARGIN;
        int panelY = 60;
        int panelHeight = screen.height - panelY - 20;
        // Ensure we don't cross the left bound if provided
        if (leftBoundX >= 0 && panelX < leftBoundX) {
            panelX = leftBoundX;
        }
        return new Bounds(panelX, panelY, panelWidth, panelHeight);
    }

    private record Bounds(int x, int y, int width, int height) {
        boolean contains(double mx, double my) {
            return mx >= x - 5 && mx <= x + width + 5 && my >= y && my <= y + height;
        }
    }
}
