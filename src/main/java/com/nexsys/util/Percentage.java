// src/main/java/com/nexsys/util/Percentage.java
package com.nexsys.util;

/**
 * Represents a percentage value between 0 and 100 inclusive.
 */
public record Percentage(int value) {

    /**
     * Canonical constructor with validation.
     *
     * @param value the percentage value (0-100)
     * @throws IllegalArgumentException if value is outside valid range
     */
    public Percentage {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100 inclusive, got: " + value);
        }
    }

    /**
     * Static factory method to create a Percentage.
     *
     * @param value the percentage value (0-100)
     * @return a new Percentage instance
     */
    public static Percentage of(int value) {
        return new Percentage(value);
    }

    /**
     * Get the percentage as an integer value.
     *
     * @return the percentage value
     */
    public int intValue() {
        return value;
    }

    /**
     * Get the percentage as a fraction (0.0 to 1.0).
     *
     * @return the percentage as a decimal fraction
     */
    public double asFraction() {
        return value / 100.0;
    }
}