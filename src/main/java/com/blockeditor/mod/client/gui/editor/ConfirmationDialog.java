package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Simple confirmation dialog for destructive actions.
 */
public class ConfirmationDialog extends Screen {
    private final Screen parent;
    private final String message;
    private final Runnable onConfirm;
    
    private ButtonWidget yesButton;
    private ButtonWidget noButton;

    public ConfirmationDialog(Screen parent, String title, String message, Runnable onConfirm) {
        super(Text.literal(title));
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
        
        yesButton = ButtonWidget.builder(Text.literal("Yes"), b -> onYes())
            .position(buttonsStartX, btnY)
            .size(btnWidth, 20)
            .build();
        
        noButton = ButtonWidget.builder(Text.literal("No"), b -> onNo())
            .position(buttonsStartX + btnWidth + spacing, btnY)
            .size(btnWidth, 20)
            .build();
        
        this.addDrawableChild(yesButton);
        this.addDrawableChild(noButton);
    }

    private void onYes() {
        if (onConfirm != null) {
            onConfirm.run();
        }
        MinecraftClient.getInstance().setScreen(parent);
    }

    private void onNo() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float partialTick) {
        // Dim background
        g.fill(0, 0, this.width, this.height, 0xA0000000);

        int dialogWidth = 260;
        int dialogHeight = 100;
        int x = (this.width - dialogWidth) / 2;
        int y = (this.height - dialogHeight) / 2;

        // Panel
        g.fill(x, y, x + dialogWidth, y + dialogHeight, 0xFF2B2B2B);
        g.fill(x, y, x + dialogWidth, y + 22, 0xFF3B3B3B);
        g.drawText(this.textRenderer, this.title, x + 10, y + 7, 0xFFFFFF, false);

        // Message
        int messageY = y + 35;
        // Word wrap for long messages
        var lines = this.textRenderer.wrapLines(Text.literal(message), dialogWidth - 32);
        for (var line : lines) {
            g.drawText(this.textRenderer, line, x + 16, messageY, 0xCCCCCC, false);
            messageY += 10;
        }

        super.render(g, mouseX, mouseY, partialTick);
    }
}
