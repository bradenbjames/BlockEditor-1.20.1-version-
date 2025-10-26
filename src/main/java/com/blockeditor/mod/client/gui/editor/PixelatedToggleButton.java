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
    
    // Check if mouse is hovering over the toggle
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    // Render using the project's GuiGraphics and GuiRenderUtil helpers so it matches other UI code
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;

        // Colors - iOS style
        int trackOn = 0xFF4CD964;  // iOS green
        int trackOff = 0xFFE5E5EA; // Light gray when off
        int knobColor = 0xFFFFFFFF; // White knob
        int border = 0xFF1C1C1E;   // Dark border

        int trackColor = toggled ? trackOn : trackOff;
        if (hovered) trackColor = lighten(trackColor, 0.04f);

    // Track: fill then crisp 1px outline to avoid "egg" edges
    GuiRenderUtil.drawPillFilled(graphics, x, y, width, height, trackColor);
    GuiRenderUtil.drawPillOutline(graphics, x, y, width, height, border);

        // Knob: circular knob that slides left/right
        int knobSize = height - 4; // Slightly smaller than track height for padding
        int knobY = y + 2;
        int knobX = toggled ? (x + width - knobSize - 2) : (x + 2);

        // Knob: fill + 1px outline, no extra shadows (keeps pixel-crisp)
        GuiRenderUtil.drawPillFilled(graphics, knobX, knobY, knobSize, knobSize, knobColor);
        GuiRenderUtil.drawPillOutline(graphics, knobX, knobY, knobSize, knobSize, border);
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
