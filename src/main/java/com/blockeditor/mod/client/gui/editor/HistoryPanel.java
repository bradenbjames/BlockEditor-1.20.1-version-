package com.blockeditor.mod.client.gui.editor;

import com.blockeditor.mod.client.gui.editor.BlockEditorHistory.CreatedBlockInfo;
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

        if (history.isEmpty()) {
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
        int maxScroll = Math.max(0, history.size() - maxVisible);
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
            if (dataIndex >= history.size()) break;
            CreatedBlockInfo info = history.get(dataIndex);

            int row = visibleIndex / cols;
            int col = visibleIndex % cols;

            // No extra per-item shift here: panelX is already adjusted in computeBounds when constrained.
            int itemX = panelX + INNER_PADDING + col * (itemWidth + ITEM_SPACING);
            // Place the first row at the content-top plus the configured top padding
            int itemY = panelY + CONTENT_TOP_PADDING + row * (ITEM_HEIGHT + ITEM_SPACING);

            boolean hovered = mouseX >= itemX && mouseX < itemX + itemWidth && mouseY >= itemY && mouseY < itemY + ITEM_HEIGHT;
            int bg = hovered ? 0x80CCCCCC : ((row % 2 == 0) ? 0x40FFFFFF : 0x20FFFFFF);
            GuiRenderUtil.drawRoundedRect(graphics, itemX, itemY, itemWidth, ITEM_HEIGHT, 3, bg);

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

        graphics.disableScissor();
        // If an item was hovered we want to render an unclipped tooltip with the full name
        if (hoveredName != null && !hoveredName.isEmpty()) {
            // renderTooltip expects integer coordinates in this MC version
            graphics.renderTooltip(font, net.minecraft.network.chat.Component.literal(hoveredName), (int) hoverX, (int) hoverY);
        }

        // Redraw title on top of scissored content to avoid artifacts
        var title = net.minecraft.network.chat.Component.literal("Recent Blocks");
        graphics.drawCenteredString(font, title, panelX + panelWidth / 2, panelY - TITLE_BAR_HEIGHT + 4, 0xFFFFFF);

        // Check if player is in survival mode to show the full stack toggle
        boolean inSurvival = false;
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null) {
            inSurvival = !mc.player.getAbilities().instabuild; // instabuild is false in survival
        }

        // Draw toggles in the title bar (top-right area)
        int toggleSpacing = TOGGLE_SIZE + 4; // spacing between toggles
        int baseToggleX = panelX + panelWidth - INNER_PADDING - TOGGLE_SIZE;

        // Draw compact/enlarged toggle (always shown)
        int compactToggleX = baseToggleX;
        int toggleY = panelY - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - TOGGLE_SIZE) / 2;
        int toggleBg = compactView ? 0xFF555555 : 0xFF808022; // different background to indicate state
        GuiRenderUtil.drawRoundedRect(graphics, compactToggleX, toggleY, TOGGLE_SIZE, TOGGLE_SIZE, 3, toggleBg);
        // draw a simple indicator: one bar for compact, two bars for enlarged
        if (compactView) {
            graphics.fill(compactToggleX + (TOGGLE_SIZE / 2) - 2, toggleY + 3, compactToggleX + (TOGGLE_SIZE / 2) + 2, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
        } else {
            graphics.fill(compactToggleX + 3, toggleY + 3, compactToggleX + 5, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
            graphics.fill(compactToggleX + TOGGLE_SIZE - 5, toggleY + 3, compactToggleX + TOGGLE_SIZE - 3, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
        }

        // Draw full stack toggle (only in survival mode)
        if (inSurvival) {
            int fullStackToggleX = baseToggleX - toggleSpacing;
            int fullStackBg = fullStackMode ? 0xFF226622 : 0xFF555555; // green when enabled
            GuiRenderUtil.drawRoundedRect(graphics, fullStackToggleX, toggleY, TOGGLE_SIZE, TOGGLE_SIZE, 3, fullStackBg);

            // Draw stack indicator: single block for x1, stack symbol for x64
            if (fullStackMode) {
                // Draw a "64" or stack symbol - using two stacked rectangles
                graphics.fill(fullStackToggleX + 2, toggleY + 2, fullStackToggleX + TOGGLE_SIZE - 2, toggleY + 6, 0xFFFFFFFF);
                graphics.fill(fullStackToggleX + 3, toggleY + 7, fullStackToggleX + TOGGLE_SIZE - 1, toggleY + TOGGLE_SIZE - 1, 0xFFFFFFFF);
            } else {
                // Draw single block symbol
                graphics.fill(fullStackToggleX + 3, toggleY + 3, fullStackToggleX + TOGGLE_SIZE - 3, toggleY + TOGGLE_SIZE - 3, 0xFFFFFFFF);
            }
        }

        // Optional scrollbar when overflow (use the padded rows count for consistent sizing)
        int totalRows = (int) Math.ceil(history.size() / (double) cols);
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
    }

    public boolean mouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        List<CreatedBlockInfo> history = BlockEditorHistory.getHistory();
        if (history.isEmpty()) return false;

        Bounds b = computeBounds(screen, computePanelWidth(screen));

        // Check if player is in survival mode for full stack toggle
        boolean inSurvival = false;
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null) {
            inSurvival = !mc.player.getAbilities().instabuild;
        }

        // Compute toggle bounds in the title bar
        int toggleSpacing = TOGGLE_SIZE + 4;
        int baseToggleX = b.x + b.width - INNER_PADDING - TOGGLE_SIZE;
        int toggleY = b.y - TITLE_BAR_HEIGHT + (TITLE_BAR_HEIGHT - TOGGLE_SIZE) / 2;

        // Check compact/enlarged toggle (rightmost)
        int compactToggleX = baseToggleX;
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
            int maxScroll = Math.max(0, history.size() - maxVisible);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            return true;
        }

        // Check full stack toggle (left of compact toggle, only in survival)
        if (inSurvival) {
            int fullStackToggleX = baseToggleX - toggleSpacing;
            boolean clickedFullStackToggle = mouseX >= fullStackToggleX && mouseX < fullStackToggleX + TOGGLE_SIZE &&
                                            mouseY >= toggleY && mouseY < toggleY + TOGGLE_SIZE;
            if (clickedFullStackToggle) {
                fullStackMode = !fullStackMode;
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

        int panelWidth = b.width;
        int contentWidth = Math.max(0, panelWidth - (INNER_PADDING * 2) - SCROLLBAR_WIDTH);
        int cols = computeColumnsForLayout(screen, contentWidth);
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

    public void setOnItemClick(BiConsumer<CreatedBlockInfo, Integer> handler) {
        this.onItemClick = handler;
    }
}
