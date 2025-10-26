package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.gui.GuiGraphics;

public final class GuiRenderUtil {
    private GuiRenderUtil() {}

    public static void drawRoundedRect(GuiGraphics graphics, int x, int y, int width, int height, int radius, int color) {
        radius = Math.min(radius, Math.min(width / 2, height / 2));
        if (radius <= 0) {
            graphics.fill(x, y, x + width, y + height, color);
            return;
        }
        graphics.fill(x + radius, y, x + width - radius, y + height, color);
        graphics.fill(x, y + radius, x + radius, y + height - radius, color);
        graphics.fill(x + width - radius, y + radius, x + width, y + height - radius, color);

        // Top-left
        graphics.fill(x + 1, y + 1, x + radius, y + 2, color);
        graphics.fill(x + 1, y + 2, x + 3, y + radius, color);
        if (radius >= 3) graphics.fill(x + 2, y + 1, x + 3, y + 2, color);
        // Top-right
        graphics.fill(x + width - radius, y + 1, x + width - 1, y + 2, color);
        graphics.fill(x + width - 3, y + 2, x + width - 1, y + radius, color);
        if (radius >= 3) graphics.fill(x + width - 3, y + 1, x + width - 2, y + 2, color);
        // Bottom-left
        graphics.fill(x + 1, y + height - 2, x + radius, y + height - 1, color);
        graphics.fill(x + 1, y + height - radius, x + 3, y + height - 2, color);
        if (radius >= 3) graphics.fill(x + 2, y + height - 2, x + 3, y + height - 1, color);
        // Bottom-right
        graphics.fill(x + width - radius, y + height - 2, x + width - 1, y + height - 1, color);
        graphics.fill(x + width - 3, y + height - radius, x + width - 1, y + height - 2, color);
        if (radius >= 3) graphics.fill(x + width - 3, y + height - 2, x + width - 2, y + height - 1, color);
    }
    
    // Draw a smooth pill shape (filled). Keeps perfectly straight top/bottom across the center segment.
    public static void drawPillFilled(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        int r = height / 2;
        if (r <= 0) {
            graphics.fill(x, y, x + width, y + height, color);
            return;
        }
        int left = x;
        int right = x + width - 1; // inclusive
        int top = y;
        int bottom = y + height - 1; // inclusive
        int cxLeft = left + r;            // integer center of left cap
        int cxRight = right - r;          // integer center of right cap
        int cy = y + r;                   // integer center Y

        // Iterate each column inclusive, compute vertical span; ensures straight top/bottom across center
        for (int xCol = left; xCol <= right; xCol++) {
            int dx;
            if (xCol < cxLeft) dx = cxLeft - xCol;
            else if (xCol > cxRight) dx = xCol - cxRight;
            else dx = 0; // inside center span -> full height

            int dy;
            if (dx == 0) {
                dy = r; // full height; gives perfectly straight top/bottom
            } else {
                int rad2 = r * r - dx * dx;
                if (rad2 < 0) rad2 = 0;
                dy = (int) Math.floor(Math.sqrt(rad2));
            }
            int yTop = cy - dy;
            int yBottom = cy + dy;
            graphics.fill(xCol, yTop, xCol + 1, yBottom + 1, color);
        }
    }

    // 1px outline for a pill. Drawn after the fill for a crisp border with straight top/bottom edges.
    public static void drawPillOutline(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        int r = height / 2;
        if (r <= 0) return;

        int left = x;
        int right = x + width - 1; // inclusive
        int top = y;
        int bottom = y + height - 1; // inclusive
        int cxLeft = left + r;
        int cxRight = right - r;
        int cy = y + r;

        // Straight top/bottom across the center span (inclusive)
        int midLeft = cxLeft;
        int midRight = cxRight;
        if (midRight >= midLeft) {
            graphics.fill(midLeft, top, midRight + 1, top + 1, color);
            graphics.fill(midLeft, bottom, midRight + 1, bottom + 1, color);
        }

        // Left arc outline (exclusive of center to avoid double-draw)
        for (int xCol = left; xCol < cxLeft; xCol++) {
            int dx = cxLeft - xCol;
            int rad2 = r * r - dx * dx;
            if (rad2 < 0) rad2 = 0;
            int dy = (int) Math.floor(Math.sqrt(rad2));
            int yTop = cy - dy;
            int yBottom = cy + dy;
            graphics.fill(xCol, yTop, xCol + 1, yTop + 1, color);
            graphics.fill(xCol, yBottom, xCol + 1, yBottom + 1, color);
        }

        // Right arc outline (exclusive of center)
        for (int xCol = cxRight + 1; xCol <= right; xCol++) {
            int dx = xCol - cxRight;
            int rad2 = r * r - dx * dx;
            if (rad2 < 0) rad2 = 0;
            int dy = (int) Math.floor(Math.sqrt(rad2));
            int yTop = cy - dy;
            int yBottom = cy + dy;
            graphics.fill(xCol, yTop, xCol + 1, yTop + 1, color);
            graphics.fill(xCol, yBottom, xCol + 1, yBottom + 1, color);
        }
    }

    // Backwards-compatible: draw a filled pill (used by older call sites)
    public static void drawPill(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        drawPillFilled(graphics, x, y, width, height, color);
    }

    // Draw a tiny 7x7 pixel arrow icon at (x,y). If down==true, draws a down triangle; otherwise right triangle.
    // This avoids font glyph inconsistencies so arrows are always the same size.
    public static void drawSmallArrow(GuiGraphics g, int x, int y, boolean down, int color) {
        // 7x7 bitmap masks (bit 6 is leftmost). 1 = pixel on
        final int[] RIGHT = new int[]{
            0b0001000,
            0b0001100,
            0b0001110,
            0b0001111,
            0b0001110,
            0b0001100,
            0b0001000
        };
        final int[] DOWN = new int[]{
            0b0000000,
            0b0001000,
            0b0011100,
            0b0111110,
            0b1111111,
            0b0000000,
            0b0000000
        };
        int[] bmp = down ? DOWN : RIGHT;
        for (int r = 0; r < 7; r++) {
            int bits = bmp[r];
            for (int c = 0; c < 7; c++) {
                if (((bits >> (6 - c)) & 1) == 1) {
                    g.fill(x + c, y + r, x + c + 1, y + r + 1, color);
                }
            }
        }
    }
}
