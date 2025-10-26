package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Simple modal dialog to create a folder: lets user enter a name and choose a tint color.
 */
public class FolderDialogScreen extends Screen {
    private final Screen parent;
    private final BiConsumer<String, Integer> onConfirm;

    private EditBox nameBox;
    private EditBox colorBox; // hex RRGGBB
    private Button createBtn;
    private Button cancelBtn;

    // Expanded preset palette (48 colors) chosen to be visually distinct across hue/saturation
    private final List<Integer> presets = Arrays.asList(
        // Row 1
        0x66CCFF, 0xFFD966, 0x8FD14F, 0xFF9AA2, 0xB39DDB, 0x80CBC4, 0xFFCCFF, 0xC5E1A5,
        0xFF8A65, 0x90CAF9, 0xA5D6A7, 0xFFAB91,
        // Row 2
        0x4FC3F7, 0xFFB74D, 0x81C784, 0xE57373, 0x9575CD, 0x4DB6AC, 0xF48FB1, 0xAED581,
        0xFFCC80, 0x64B5F6, 0xBA68C8, 0x4DD0E1,
        // Row 3
        0x29B6F6, 0xFFA726, 0x66BB6A, 0xEF5350, 0x7E57C2, 0x26A69A, 0xEC407A, 0x9CCC65,
        0xFF7043, 0x42A5F5, 0x9FA8DA, 0x26C6DA,
        // Row 4
        0x039BE5, 0xFB8C00, 0x43A047, 0xE53935, 0x5E35B1, 0x00897B, 0xD81B60, 0x7CB342,
        0xF4511E, 0x1E88E5, 0x8E99F3, 0x00ACC1
    );

    // Palette UI geometry
    private int paletteX, paletteY;
    private int swatchSize = 14;
    private int swatchGap = 6;
    private int dialogWidth = 360;
    private int dialogHeight = 220;

    public FolderDialogScreen(Screen parent, BiConsumer<String, Integer> onConfirm) {
        super(Component.literal("Create Folder"));
        this.parent = parent;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void init() {
        super.init();
    int x = (this.width - dialogWidth) / 2;
    int y = (this.height - dialogHeight) / 2;

    // Name input (tighter vertical spacing to free room for palette)
    nameBox = new EditBox(this.font, x + 16, y + 44, dialogWidth - 32, 20, Component.literal("Folder Name"));
        nameBox.setMaxLength(48);
        nameBox.setValue("");
        this.addRenderableWidget(nameBox);

        // Color input (hex)
    colorBox = new EditBox(this.font, x + 16, y + 90, 110, 20, Component.literal("RRGGBB"));
        colorBox.setMaxLength(6);
        colorBox.setValue(String.format("%06X", presets.get(0)));
        this.addRenderableWidget(colorBox);

        int btnWidth = 90;
        int btnY = y + dialogHeight - 28;
        createBtn = Button.builder(Component.literal("Create"), b -> onCreate())
            .pos(x + dialogWidth - btnWidth - 16, btnY)
            .size(btnWidth, 20)
            .build();
        cancelBtn = Button.builder(Component.literal("Cancel"), b -> onCancel())
            .pos(x + 16, btnY)
            .size(btnWidth, 20)
            .build();
        this.addRenderableWidget(createBtn);
        this.addRenderableWidget(cancelBtn);

    // Palette position (grid under color input, full-width content area)
    paletteX = x + 16;
    paletteY = y + 132; // consistent 16px below the Palette label
    }

    private void onCreate() {
        String name = nameBox != null ? nameBox.getValue().trim() : "";
        String hex = colorBox != null ? colorBox.getValue().trim().toUpperCase() : "66CCFF";
        hex = hex.replaceAll("[^0-9A-F]", "");
        if (hex.length() != 6) {
            hex = "66CCFF";
        }
        int rgb = Integer.parseInt(hex, 16) & 0xFFFFFF;
        if (onConfirm != null) {
            onConfirm.accept(name, rgb);
        }
        Minecraft.getInstance().setScreen(parent);
    }

    private void onCancel() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (nameBox != null) nameBox.tick();
        if (colorBox != null) {
            String v = colorBox.getValue().toUpperCase().replaceAll("[^0-9A-F]", "");
            if (!v.equals(colorBox.getValue())) colorBox.setValue(v);
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Dim background
        g.fill(0, 0, this.width, this.height, 0xA0000000);

        int x = (this.width - dialogWidth) / 2;
        int y = (this.height - dialogHeight) / 2;

        // Panel
        g.fill(x, y, x + dialogWidth, y + dialogHeight, 0xFF2B2B2B);
        g.fill(x, y, x + dialogWidth, y + 22, 0xFF3B3B3B);
        g.drawString(this.font, this.title, x + 10, y + 7, 0xFFFFFF, false);

    // Labels (reduced spacing to keep palette above buttons)
    g.drawString(this.font, Component.literal("Name:"), x + 16, y + 28, 0xCCCCCC, false);
    g.drawString(this.font, Component.literal("Color (hex):"), x + 16, y + 74, 0xCCCCCC, false);
    g.drawString(this.font, Component.literal("Palette:"), x + 16, y + 116, 0xCCCCCC, false);

        // Draw palette as a grid that wraps within the dialog width
        int contentLeft = x + 16;
        int contentRight = x + dialogWidth - 16;
        int maxWidth = contentRight - contentLeft;
        int cols = Math.max(1, (maxWidth + swatchGap) / (swatchSize + swatchGap));
        int cx = contentLeft;
        int cy = paletteY;
        for (int i = 0; i < presets.size(); i++) {
            int rgb = presets.get(i) & 0xFFFFFF;
            int argb = 0xFF000000 | rgb;
            g.fill(cx, cy, cx + swatchSize, cy + swatchSize, argb);
            // border
            g.fill(cx, cy, cx + swatchSize, cy + 1, 0xFF000000);
            g.fill(cx, cy + swatchSize - 1, cx + swatchSize, cy + swatchSize, 0xFF000000);
            g.fill(cx, cy, cx + 1, cy + swatchSize, 0xFF000000);
            g.fill(cx + swatchSize - 1, cy, cx + swatchSize, cy + swatchSize, 0xFF000000);

            boolean hovered = mouseX >= cx && mouseX < cx + swatchSize && mouseY >= cy && mouseY < cy + swatchSize;
            if (hovered) {
                g.fill(cx, cy, cx + swatchSize, cy + swatchSize, 0x40FFFFFF);
            }

            // advance
            int colIndex = (i + 1) % cols;
            if (colIndex == 0) {
                cx = contentLeft;
                cy += swatchSize + swatchGap;
            } else {
                cx += swatchSize + swatchGap;
            }
        }

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Palette click handling (grid with wrapping)
        int x = (this.width - dialogWidth) / 2;
        int contentLeft = x + 16;
        int contentRight = x + dialogWidth - 16;
        int maxWidth = contentRight - contentLeft;
        int cols = Math.max(1, (maxWidth + swatchGap) / (swatchSize + swatchGap));
        int cx = contentLeft;
        int cy = paletteY;
        for (int i = 0; i < presets.size(); i++) {
            if (mouseX >= cx && mouseX < cx + swatchSize && mouseY >= cy && mouseY < cy + swatchSize) {
                int rgb = presets.get(i) & 0xFFFFFF;
                if (colorBox != null) {
                    colorBox.setValue(String.format("%06X", rgb));
                }
                return true;
            }
            int colIndex = (i + 1) % cols;
            if (colIndex == 0) {
                cx = contentLeft;
                cy += swatchSize + swatchGap;
            } else {
                cx += swatchSize + swatchGap;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
