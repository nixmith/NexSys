// src/test/java/com/nexsys/util/SignalTypeTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignalTypeTest {

    @Test
    void testOf() {
        SignalType<Integer> signal = SignalType.of("test_signal");
        assertEquals("test_signal", signal.toString());
    }

    @Test
    void testFormat() {
        SignalType<String> signal = SignalType.format("signal_%d_%s", 42, "test");
        assertEquals("signal_42_test", signal.toString());
    }

    @Test
    void testEquals() {
        SignalType<Integer> signal1 = SignalType.of("test");
        SignalType<String> signal2 = SignalType.of("test");
        SignalType<Integer> signal3 = SignalType.of("other");

        assertEquals(signal1, signal2);
        assertNotEquals(signal1, signal3);
    }

    @Test
    void testHashCode() {
        SignalType<Integer> signal1 = SignalType.of("test");
        SignalType<String> signal2 = SignalType.of("test");

        assertEquals(signal1.hashCode(), signal2.hashCode());
    }

    @Test
    void testCharSequenceMethods() {
        SignalType<Integer> signal = SignalType.of("test123");
        assertEquals(7, signal.length());
        assertEquals('t', signal.charAt(0));
        assertEquals('3', signal.charAt(6));
        assertEquals("st1", signal.subSequence(2, 5).toString());
    }

    @Test
    void testNullValue() {
        assertThrows(NullPointerException.class, () -> SignalType.of(null));
    }
}