package com.blockeditor.mod.client.gui.editor;

public final class BlockValidation {
    private BlockValidation() {}

    public static int parseHexColor(String hex) {
        try {
            if (hex == null) return 0xFFFFFF;
            // Remove any # prefix if present
            hex = hex.replace("#", "").trim();

            // Pad with zeros if too short
            while (hex.length() < 6) {
                hex = "0" + hex;
            }

            // Limit to 6 characters
            if (hex.length() > 6) {
                hex = hex.substring(0, 6);
            }

            // Parse hex string to integer
            return Integer.parseInt(hex, 16);
        } catch (Exception e) {
            return 0xFFFFFF; // Default to white on error
        }
    }

    public static boolean isValidBlockName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Allow letters, numbers, spaces, hyphens, underscores, and basic punctuation
        return name.matches("^[a-zA-Z0-9 _\\-'.()]+$") && name.trim().length() >= 1 && name.trim().length() <= 32;
    }
}

