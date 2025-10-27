package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Simple confirmation dialog for destructive actions.
 */
public class ConfirmationDialog extends Screen {
    private final Screen parent;
    private final String message;
    private final Runnable onConfirm;
    
    private Button yesButton;
    private Button noButton;

    public ConfirmationDialog(Screen parent, String title, String message, Runnable onConfirm) {
        super(Component.literal(title));
        this.parent = parent;
        this.message = message;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void init() {
        super.init();
        
        int dialogWidth = 260;
        int dialogHeight = 100;
        int x = (this.width - dialogWidth) / 2;
        int y = (this.height - dialogHeight) / 2;

        int btnWidth = 80;
        int btnY = y + dialogHeight - 28;
        int spacing = 10;
        
        // Center buttons in dialog
        int totalButtonWidth = btnWidth * 2 + spacing;
        int buttonsStartX = x + (dialogWidth - totalButtonWidth) / 2;
        
        yesButton = Button.builder(Component.literal("Yes"), b -> onYes())
            .pos(buttonsStartX, btnY)
            .size(btnWidth, 20)
            .build();
        
        noButton = Button.builder(Component.literal("No"), b -> onNo())
            .pos(buttonsStartX + btnWidth + spacing, btnY)
            .size(btnWidth, 20)
            .build();
        
        this.addRenderableWidget(yesButton);
        this.addRenderableWidget(noButton);
    }

    private void onYes() {
        if (onConfirm != null) {
            onConfirm.run();
        }
        Minecraft.getInstance().setScreen(parent);
    }

    private void onNo() {
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Dim background
        g.fill(0, 0, this.width, this.height, 0xA0000000);

        int dialogWidth = 260;
        int dialogHeight = 100;
        int x = (this.width - dialogWidth) / 2;
        int y = (this.height - dialogHeight) / 2;

        // Panel
        g.fill(x, y, x + dialogWidth, y + dialogHeight, 0xFF2B2B2B);
        g.fill(x, y, x + dialogWidth, y + 22, 0xFF3B3B3B);
        g.drawString(this.font, this.title, x + 10, y + 7, 0xFFFFFF, false);

        // Message
        int messageY = y + 35;
        // Word wrap for long messages
        var lines = this.font.split(Component.literal(message), dialogWidth - 32);
        for (var line : lines) {
            g.drawString(this.font, line, x + 16, messageY, 0xCCCCCC, false);
            messageY += 10;
        }

        super.render(g, mouseX, mouseY, partialTick);
    }
}
