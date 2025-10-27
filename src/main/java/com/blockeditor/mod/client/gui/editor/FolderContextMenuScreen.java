package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Simple context menu for folder actions (right-click).
 */
public class FolderContextMenuScreen extends Screen {
    private final Screen parent;
    private final BlockEditorHistory.BlockFolder folder;
    private final double menuX;
    private final double menuY;

    private static final int MENU_WIDTH = 60;
    private static final int ITEM_HEIGHT = 14;
    private static final int CORNER_RADIUS = 3;

    public FolderContextMenuScreen(Screen parent, BlockEditorHistory.BlockFolder folder, double x, double y) {
        super(Component.literal("Folder Menu"));
        this.parent = parent;
        this.folder = folder;
        this.menuX = x;
        this.menuY = y;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void drawRoundedRect(GuiGraphics g, int x, int y, int width, int height, int radius, int color) {
        // Fill main body
        g.fill(x + radius, y, x + width - radius, y + height, color);
        g.fill(x, y + radius, x + radius, y + height - radius, color);
        g.fill(x + width - radius, y + radius, x + width, y + height - radius, color);
        
        // Fill corners with smaller pixels for rounded effect
        // Top-left corner
        g.fill(x + 1, y + radius - 1, x + radius, y + radius, color);
        g.fill(x + radius - 1, y + 1, x + radius, y + radius - 1, color);
        
        // Top-right corner
        g.fill(x + width - radius, y + radius - 1, x + width - 1, y + radius, color);
        g.fill(x + width - radius, y + 1, x + width - radius + 1, y + radius - 1, color);
        
        // Bottom-left corner
        g.fill(x + 1, y + height - radius, x + radius, y + height - radius + 1, color);
        g.fill(x + radius - 1, y + height - radius + 1, x + radius, y + height - 1, color);
        
        // Bottom-right corner
        g.fill(x + width - radius, y + height - radius, x + width - 1, y + height - radius + 1, color);
        g.fill(x + width - radius, y + height - radius + 1, x + width - radius + 1, y + height - 1, color);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Very light dim overlay so we can still see the editor underneath
        g.fill(0, 0, this.width, this.height, 0x30000000);

        // Menu panel with rounded edges - ensure it stays within screen bounds
        int x = (int) Math.min(menuX, this.width - MENU_WIDTH - 5);
        int y = (int) Math.min(menuY, this.height - ITEM_HEIGHT - 5);
        
        // "Delete" option
        boolean hovered = mouseX >= x && mouseX < x + MENU_WIDTH &&
                         mouseY >= y && mouseY < y + ITEM_HEIGHT;
        
        int bgColor = hovered ? 0xFF444444 : 0xFF2B2B2B;
        drawRoundedRect(g, x, y, MENU_WIDTH, ITEM_HEIGHT, CORNER_RADIUS, bgColor);
        
        // Center text horizontally and vertically
        var pose = g.pose();
        pose.pushPose();
        pose.scale(0.8f, 0.8f, 1.0f);
        
        String text = "Delete";
        int textWidth = (int) (this.font.width(text) * 0.8f);
        int textHeight = (int) (this.font.lineHeight * 0.8f);
        
        int centeredX = (int) ((x + MENU_WIDTH / 2.0f - textWidth / 2.0f) / 0.8f);
        int centeredY = (int) ((y + ITEM_HEIGHT / 2.0f - textHeight / 2.0f) / 0.8f);
        
        g.drawString(this.font, text, centeredX, centeredY, hovered ? 0xFFFFFF : 0xCCCCCC, false);
        pose.popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) menuX;
        int y = (int) menuY;
        
        // Check if clicked on Delete option
        if (mouseX >= x && mouseX < x + MENU_WIDTH &&
            mouseY >= y && mouseY < y + ITEM_HEIGHT) {
            // Show confirmation dialog
            int itemCount = folder.blocks.size();
            String message = "Delete folder and all " + itemCount + " item" + (itemCount == 1 ? "" : "s") + " inside?";
            Minecraft.getInstance().setScreen(new ConfirmationDialog(
                parent,
                "Delete Folder?",
                message,
                () -> BlockEditorHistory.deleteFolder(folder)
            ));
            return true;
        }
        
        // Click outside menu - close
        Minecraft.getInstance().setScreen(parent);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ESC closes menu
        if (keyCode == 256) { // ESC
            Minecraft.getInstance().setScreen(parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
