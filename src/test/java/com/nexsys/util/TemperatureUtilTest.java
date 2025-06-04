// src/test/java/com/nexsys/util/TemperatureUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureUtilsTest {

    @Test
    void testConstants() {
        assertEquals(0.5, TemperatureUtils.PRECISION_HALVES);
        assertEquals(0.1, TemperatureUtils.PRECISION_TENTHS);
    }

    @ParameterizedTest
    @CsvSource({
            "0, C, F, 1, 32",
            "100, C, F, 1, 212",
            "32, F, C, 1, 0",
            "212, F, C, 1, 100",
            "0, C, K, 1, 273",
            "273.15, K, C, 1, 0"
    })
    void testDisplayTemp(double value, String from, String to, double precision, double expected) {
        double result = TemperatureUtils.displayTemp(value, from, to, precision);
        assertEquals(expected, result, 0.01);
    }

    @Test
    void testPrecisionRounding() {
        // Test halves precision
        assertEquals(20.5, TemperatureUtils.displayTemp(20.4, "C", "C", 0.5));
        assertEquals(20.5, TemperatureUtils.displayTemp(20.6, "C", "C", 0.5));
        assertEquals(21.0, TemperatureUtils.displayTemp(20.8, "C", "C", 0.5));

        // Test tenths precision
        assertEquals(20.4, TemperatureUtils.displayTemp(20.44, "C", "C", 0.1));
        assertEquals(20.5, TemperatureUtils.displayTemp(20.45, "C", "C", 0.1));

        // Test whole number precision
        assertEquals(20, TemperatureUtils.displayTemp(20.4, "C", "C", 0));
        assertEquals(21, TemperatureUtils.displayTemp(20.6, "C", "C", 0));
    }

    @Test
    void testUnknownUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> TemperatureUtils.displayTemp(20, "X", "C", 1));
        assertThrows(IllegalArgumentException.class,
                () -> TemperatureUtils.displayTemp(20, "C", "X", 1));
    }
}