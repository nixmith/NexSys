// src/main/java/com/nexsys/util/StatisticsUtils.java
package com.nexsys.util;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Statistical utility functions.
 */
public final class StatisticsUtils {
    private StatisticsUtils() {
        // Utility class
    }

    /**
     * Create a function that ignores variance within a threshold.
     * Returns the previous value if the new value doesn't vary enough.
     *
     * @param func the original function
     * @param threshold the variance threshold
     * @param <T> the input type
     * @param <N> the numeric output type
     * @return a memoizing function that suppresses small variations
     */
    public static <T, N extends Number & Comparable<N>> Function<T, N> ignoreVariance(
            Function<T, N> func, N threshold) {
        Objects.requireNonNull(func, "Function cannot be null");
        Objects.requireNonNull(threshold, "Threshold cannot be null");

        AtomicReference<N> lastValue = new AtomicReference<>();

        return input -> {
            N newValue = func.apply(input);
            N previousValue = lastValue.get();

            if (previousValue != null && isWithinThreshold(previousValue, newValue, threshold)) {
                return previousValue;
            }

            lastValue.set(newValue);
            return newValue;
        };
    }

    private static <N extends Number & Comparable<N>> boolean isWithinThreshold(
            N previous, N current, N threshold) {
        double diff = Math.abs(current.doubleValue() - previous.doubleValue());
        return diff < threshold.doubleValue();
    }
}