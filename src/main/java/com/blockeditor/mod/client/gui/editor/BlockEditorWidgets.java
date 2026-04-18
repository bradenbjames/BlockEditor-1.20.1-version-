package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.function.Consumer;

public final class BlockEditorWidgets {
    private BlockEditorWidgets() {}

    public static TextFieldWidget createHexBox(TextRenderer textRenderer, int x, int y, String initialHex) {
        // Size the hex box to exactly fit 6 hex characters plus a small padding
        int hexTextWidth = textRenderer.getWidth("FFFFFF");
        int hexPadding = 8; // left+right padding in pixels
        TextFieldWidget box = new TextFieldWidget(textRenderer, x, y, Math.max(40, hexTextWidth + hexPadding), 20, Text.literal("Hex"));
        box.setMaxLength(6);
        if (initialHex != null) {
            box.setText(sanitizeHex(initialHex));
        }
        // Allow only hex characters - but be permissive during typing/pasting
        box.setTextPredicate(s -> {
            if (s == null) return false;
            // Allow empty string
            if (s.isEmpty()) return true;
            // Remove any # prefix if present
            String cleaned = s.startsWith("#") ? s.substring(1) : s;
            // Allow hex characters up to 6 chars
            return cleaned.matches("[0-9A-Fa-f]{0,6}");
        });
        box.setSuggestion("Hex (RRGGBB)");
        box.setChangedListener(text -> box.setSuggestion(text.isEmpty() ? "Hex (RRGGBB)" : null));
        return box;
    }

    // Now accepts an explicit desired width so the screen can ensure the name box doesn't overlap
    // nearby UI (for example, the block preview). The method will still enforce a small minimum width.
    public static TextFieldWidget createNameBox(TextRenderer textRenderer, int x, int y, int width) {
        int minWidth = 40; // very small fallback to avoid zero/negative widths
        int usedWidth = Math.max(minWidth, width);
        TextFieldWidget box = new TextFieldWidget(textRenderer, x, y, usedWidth, 20, Text.literal("Name"));
        box.setMaxLength(32);
        // Make the placeholder text light blue so it's visually distinct
        box.setSuggestion("new block name");
        box.setChangedListener(text -> box.setSuggestion(text.isEmpty() ? "new block name" : null));
        return box;
    }

    public static ButtonWidget createButton(String label, int x, int y, int width, Runnable onClick) {
        return ButtonWidget.builder(Text.literal(label), btn -> {
            if (onClick != null) onClick.run();
        }).dimensions(x, y, width, 20).build();
    }

    // Factory for the Apple-style pixelated toggle
    public static PixelatedToggleButton createAppleStyleToggle(int x, int y, boolean initialState, Consumer<Boolean> onToggle) {
        // width chosen to approximate iOS-like toggle proportions while matching pixel-art style
        return new PixelatedToggleButton(x, y, 36, 20, initialState, onToggle);
    }

    private static String sanitizeHex(String hex) {
        if (hex == null) return "";
        String v = hex.replace("#", "").trim().toUpperCase(Locale.ROOT);
        if (v.length() > 6) v = v.substring(0, 6);
        if (!v.matches("[0-9A-F]*")) v = v.replaceAll("[^0-9A-F]", "");
        return v;
    }
}
