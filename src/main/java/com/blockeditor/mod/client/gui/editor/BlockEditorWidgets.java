package com.blockeditor.mod.client.gui.editor;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public final class BlockEditorWidgets {
    private BlockEditorWidgets() {}

    public static EditBox createHexBox(Font font, int x, int y, String initialHex) {
        EditBox box = new EditBox(font, x, y, 90, 20, Component.literal("Hex"));
        box.setMaxLength(6);
        if (initialHex != null) {
            box.setValue(sanitizeHex(initialHex));
        }
        // Allow only hex characters while typing
        box.setFilter(s -> s != null && s.matches("[0-9A-Fa-f]{0,6}"));
        box.setHint(Component.literal("Hex (RRGGBB)"));
        return box;
    }

    public static EditBox createNameBox(Font font, int x, int y) {
        EditBox box = new EditBox(font, x, y, 140, 20, Component.literal("Name"));
        box.setMaxLength(32);
        box.setHint(Component.literal("Enter block name"));
        return box;
    }

    public static Button createButton(String label, int x, int y, int width, Runnable onClick) {
        return Button.builder(Component.literal(label), btn -> {
            if (onClick != null) onClick.run();
        }).bounds(x, y, width, 20).build();
    }

    private static String sanitizeHex(String hex) {
        if (hex == null) return "";
        String v = hex.replace("#", "").trim().toUpperCase(Locale.ROOT);
        if (v.length() > 6) v = v.substring(0, 6);
        if (!v.matches("[0-9A-F]*")) v = v.replaceAll("[^0-9A-F]", "");
        return v;
    }
}
