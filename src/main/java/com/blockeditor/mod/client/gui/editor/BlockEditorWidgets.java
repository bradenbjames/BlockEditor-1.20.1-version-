package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.function.Consumer;

public final class BlockEditorWidgets {
    private BlockEditorWidgets() {}

    public static EditBox createHexBox(Font font, int x, int y, String initialHex) {
        // Size the hex box to exactly fit 6 hex characters plus a small padding
        int hexTextWidth = font.width("FFFFFF");
        int hexPadding = 8; // left+right padding in pixels
        EditBox box = new EditBox(font, x, y, Math.max(40, hexTextWidth + hexPadding), 20, Component.literal("Hex"));
        box.setMaxLength(6);
        if (initialHex != null) {
            box.setValue(sanitizeHex(initialHex));
        }
        // Allow only hex characters while typing
        box.setFilter(s -> s != null && s.matches("[0-9A-Fa-f]{0,6}"));
        box.setHint(Component.literal("Hex (RRGGBB)"));
        return box;
    }

    // Now accepts an explicit desired width so the screen can ensure the name box doesn't overlap
    // nearby UI (for example, the block preview). The method will still enforce a small minimum width.
    public static EditBox createNameBox(Font font, int x, int y, int width) {
        int minWidth = 40; // very small fallback to avoid zero/negative widths
        int usedWidth = Math.max(minWidth, width);
        EditBox box = new EditBox(font, x, y, usedWidth, 20, Component.literal("Name"));
        box.setMaxLength(32);
        // Make the placeholder text light blue so it's visually distinct
        box.setHint(Component.literal("new block name").withStyle(s -> s.withColor(0xADD8E6)));
        return box;
    }

    public static Button createButton(String label, int x, int y, int width, Runnable onClick) {
        return Button.builder(Component.literal(label), btn -> {
            if (onClick != null) onClick.run();
        }).bounds(x, y, width, 20).build();
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
