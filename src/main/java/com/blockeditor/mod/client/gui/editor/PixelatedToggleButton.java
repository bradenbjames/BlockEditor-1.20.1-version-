package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;

public final class PixelatedToggleButton {
    private final int x, y, width, height;
    private boolean toggled;
    private final Consumer<Boolean> onToggle;

    public PixelatedToggleButton(int x, int y, int width, int height, boolean initialState, Consumer<Boolean> onToggle) {
        this.x = x;
        this.y = y;
        this.width = Math.max(8, width);
        this.height = Math.max(8, height);
        this.toggled = initialState;
        this.onToggle = onToggle;
    }

    // Render using the project's GuiGraphics and GuiRenderUtil helpers so it matches other UI code
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;

        // Colors
        int trackOn = 0xFF4CD964;
        int trackOff = 0xFF2F3437;
        int knob = 0xFFFFFFFF;
        int knobInner = hovered ? 0xFFFFFFFF : 0xFFF2F2F2;
        int border = 0xFF1F1F1F;

        int trackColor = toggled ? trackOn : trackOff;
        if (hovered) trackColor = lighten(trackColor, 0.06f);

        // Draw pixelated rounded track. Use a small radius to achieve "pixelated rounded" look
        int radius = Math.max(1, height / 4);
        GuiRenderUtil.drawRoundedRect(graphics, x, y, width, height, radius, trackColor);

        // subtle top/bottom border lines for clarity
        graphics.fill(x + 1, y, x + width - 1, y + 1, border);
        graphics.fill(x + 1, y + height - 1, x + width - 1, y + height, border);

        // Knob geometry: square-ish pixelated knob with small padding
        int knobSize = Math.max(4, height - 4);
        int knobY = y + 2;
        int knobX = toggled ? (x + width - knobSize - 2) : (x + 2);

        // Draw knob background (outer)
        graphics.fill(knobX, knobY, knobX + knobSize, knobY + knobSize, knob);
        // Inner highlight/depth
        graphics.fill(knobX + 1, knobY + 1, knobX + knobSize - 1, knobY + knobSize - 1, knobInner);
        // subtle shadow bottom edge
        graphics.fill(knobX, knobY + knobSize - 1, knobX + knobSize, knobY + knobSize, 0x33000000);
    }

    // Mouse click handling: return true if the click was handled
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false; // only left click toggles
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            toggled = !toggled;
            if (onToggle != null) onToggle.accept(toggled);
            return true;
        }
        return false;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean state) {
        this.toggled = state;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Basic color helper to lightly brighten an ARGB color
    private static int lighten(int color, float amount) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = clamp((int) (r + (255 - r) * amount));
        g = clamp((int) (g + (255 - g) * amount));
        b = clamp((int) (b + (255 - b) * amount));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int clamp(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }
}
