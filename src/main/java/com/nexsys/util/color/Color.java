// src/main/java/com/nexsys/util/Color.java
package com.nexsys.util.color;

/**
 * Represents an RGB color with values in range 0.0-1.0.
 */
public record Color(double r, double g, double b) {

    /**
     * Clamp all color components to range [0.0, 1.0].
     *
     * @return a new Color with clamped values
     */
    public Color clamp() {
        return new Color(
                Math.max(0, Math.min(1, r)),
                Math.max(0, Math.min(1, g)),
                Math.max(0, Math.min(1, b))
        );
    }

    /**
     * Convert to hex string (six lowercase characters).
     *
     * @return hex color string without # prefix
     */
    public String toHex() {
        int red = (int) Math.round(r * 255);
        int green = (int) Math.round(g * 255);
        int blue = (int) Math.round(b * 255);
        return String.format("%02x%02x%02x", red, green, blue);
    }

    /**
     * Create Color from hex string.
     *
     * @param hex hex string (with or without # prefix)
     * @return Color instance
     * @throws IllegalArgumentException if hex string is invalid
     */
    public static Color fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 6 characters");
        }

        try {
            int red = Integer.parseInt(hex.substring(0, 2), 16);
            int green = Integer.parseInt(hex.substring(2, 4), 16);
            int blue = Integer.parseInt(hex.substring(4, 6), 16);

            return new Color(red / 255.0, green / 255.0, blue / 255.0);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color: " + hex, e);
        }
    }
}