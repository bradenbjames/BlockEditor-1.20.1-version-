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
}

