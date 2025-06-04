// src/main/java/com/nexsys/util/ScalingUtils.java
package com.nexsys.util;

/**
 * Utility functions for scaling values between ranges.
 */
public final class ScalingUtils {
    private ScalingUtils() {
        // Utility class
    }

    /**
     * Scale a value from one range to another, returning an integer result.
     *
     * @param sourceLow source range minimum
     * @param sourceHigh source range maximum
     * @param targetLow target range minimum
     * @param targetHigh target range maximum
     * @param value the value to scale
     * @return the scaled integer value
     */
    public static int scaleRangedValueToIntRange(double sourceLow, double sourceHigh,
                                                 double targetLow, double targetHigh,
                                                 double value) {
        double sourceOffset = sourceLow - 1;
        double targetOffset = targetLow - 1;
        return (int) ((value - sourceOffset) * statesInRange(targetLow, targetHigh) /
                statesInRange(sourceLow, sourceHigh) + targetOffset);
    }

    /**
     * Scale a value from one range to another.
     *
     * @param sourceLow source range minimum
     * @param sourceHigh source range maximum
     * @param targetLow target range minimum
     * @param targetHigh target range maximum
     * @param value the value to scale
     * @return the scaled value
     */
    public static double scaleToRangedValue(double sourceLow, double sourceHigh,
                                            double targetLow, double targetHigh,
                                            double value) {
        double sourceOffset = sourceLow - 1;
        double targetOffset = targetLow - 1;
        return (value - sourceOffset) * statesInRange(targetLow, targetHigh) /
                statesInRange(sourceLow, sourceHigh) + targetOffset;
    }

    /**
     * Calculate how many states exist in a range.
     *
     * @param low range minimum
     * @param high range maximum
     * @return number of states in the range
     */
    public static double statesInRange(double low, double high) {
        return high - low + 1;
    }

    /**
     * Calculate how many integer states exist in a range.
     *
     * @param low range minimum
     * @param high range maximum
     * @return number of integer states in the range
     */
    public static int intStatesInRange(double low, double high) {
        return (int) statesInRange(low, high);
    }
}