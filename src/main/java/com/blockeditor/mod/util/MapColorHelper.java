package com.blockeditor.mod.util;

import net.minecraft.block.MapColor;

/**
 * Finds the nearest MapColor preset for a given RGB color.
 * Used to give custom-colored blocks approximate map/minimap colors.
 */
public final class MapColorHelper {

    private static final MapColor[] ALL_COLORS;

    static {
        // Collect all non-CLEAR MapColor entries from the COLORS array (IDs 0-63)
        MapColor[] temp = new MapColor[64];
        int count = 0;
        for (int i = 1; i <= 63; i++) {
            MapColor mc = MapColor.get(i);
            if (mc != null && mc != MapColor.CLEAR) {
                temp[count++] = mc;
            }
        }
        ALL_COLORS = new MapColor[count];
        System.arraycopy(temp, 0, ALL_COLORS, 0, count);
    }

    private MapColorHelper() {}

    /**
     * Returns the MapColor whose base color is closest to the given RGB value.
     */
    public static MapColor nearest(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        MapColor best = MapColor.STONE_GRAY;
        int bestDist = Integer.MAX_VALUE;

        for (MapColor mc : ALL_COLORS) {
            int mr = (mc.color >> 16) & 0xFF;
            int mg = (mc.color >> 8) & 0xFF;
            int mb = mc.color & 0xFF;

            // Weighted Euclidean distance (human eye is more sensitive to green)
            int dr = r - mr;
            int dg = g - mg;
            int db = b - mb;
            int dist = 2 * dr * dr + 4 * dg * dg + 3 * db * db;

            if (dist < bestDist) {
                bestDist = dist;
                best = mc;
            }
        }

        return best;
    }
}
