// src/test/java/com/nexsys/util/ScalingUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class ScalingUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "1, 255, 1, 100, 255, 100",
            "1, 255, 1, 100, 127, 49",
            "1, 255, 1, 100, 10, 3"
    })
    void testScaleRangedValueToIntRange(double sourceLow, double sourceHigh,
                                        double targetLow, double targetHigh,
                                        double value, int expected) {
        int result = ScalingUtils.scaleRangedValueToIntRange(
                sourceLow, sourceHigh, targetLow, targetHigh, value);
        assertEquals(expected, result);
    }

    @Test
    void testScaleToRangedValue() {
        double result = ScalingUtils.scaleToRangedValue(1, 255, 1, 100, 127.5);
        assertEquals(50, result, 0.00001);
    }

    @Test
    void testStatesInRange() {
        assertEquals(10.0, ScalingUtils.statesInRange(1, 10));
        assertEquals(255.0, ScalingUtils.statesInRange(1, 255));
        assertEquals(100.0, ScalingUtils.statesInRange(1, 100));
    }

    @Test
    void testIntStatesInRange() {
        assertEquals(10, ScalingUtils.intStatesInRange(1, 10));
        assertEquals(255, ScalingUtils.intStatesInRange(1, 255));
    }
}