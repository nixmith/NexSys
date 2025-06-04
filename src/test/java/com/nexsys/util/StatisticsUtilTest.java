// src/test/java/com/nexsys/util/StatisticsUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilsTest {

    @Test
    void testIgnoreVarianceInteger() {
        AtomicInteger callCount = new AtomicInteger();
        Function<String, Integer> original = s -> {
            callCount.incrementAndGet();
            return s.length() * 10;
        };

        Function<String, Integer> filtered = StatisticsUtils.ignoreVariance(original, 15);

        // First call
        assertEquals(30, filtered.apply("abc"));
        assertEquals(1, callCount.get());

        // Within threshold - returns previous value
        assertEquals(30, filtered.apply("abcd")); // Would be 40
        assertEquals(2, callCount.get());

        // Outside threshold - returns new value
        assertEquals(60, filtered.apply("abcdef"));
        assertEquals(3, callCount.get());
    }

    @Test
    void testIgnoreVarianceDouble() {
        Function<Double, Double> original = d -> d * 2.0;
        Function<Double, Double> filtered = StatisticsUtils.ignoreVariance(original, 0.5);

        assertEquals(20.0, filtered.apply(10.0));
        assertEquals(20.0, filtered.apply(10.2)); // Within 0.5 threshold
        assertEquals(21.0, filtered.apply(10.5)); // Outside threshold
    }

    @Test
    void testNullHandling() {
        assertThrows(NullPointerException.class,
                () -> StatisticsUtils.ignoreVariance(null, 10));

        Function<String, Integer> lengthFunction = s -> s.length();
        assertThrows(NullPointerException.class,
                () -> StatisticsUtils.ignoreVariance(lengthFunction, null));
    }
}