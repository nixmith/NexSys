// src/test/java/com/nexsys/util/PercentageTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PercentageTest {

    @Test
    void testValidPercentage() {
        Percentage p = Percentage.of(50);
        assertEquals(50, p.value());
        assertEquals(50, p.intValue());
        assertEquals(0.5, p.asFraction(), 0.001);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 50, 99, 100})
    void testValidRange(int value) {
        assertDoesNotThrow(() -> Percentage.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, 101, 200})
    void testInvalidRange(int value) {
        assertThrows(IllegalArgumentException.class, () -> Percentage.of(value));
    }

    @Test
    void testAsFraction() {
        assertEquals(0.0, Percentage.of(0).asFraction(), 0.001);
        assertEquals(0.25, Percentage.of(25).asFraction(), 0.001);
        assertEquals(1.0, Percentage.of(100).asFraction(), 0.001);
    }
}