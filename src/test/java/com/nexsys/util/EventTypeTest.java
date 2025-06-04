// src/test/java/com/nexsys/util/EventTypeTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    @Test
    void testOf() {
        EventType<Map<String, Object>> event = EventType.of("state_changed");
        assertEquals("state_changed", event.toString());
    }

    @Test
    void testFormat() {
        EventType<Map<String, String>> event = EventType.format("event_%s_%d", "sensor", 123);
        assertEquals("event_sensor_123", event.toString());
    }

    @Test
    void testEquals() {
        EventType<Map<String, Object>> event1 = EventType.of("test");
        EventType<Map<String, String>> event2 = EventType.of("test");
        EventType<Map<String, Object>> event3 = EventType.of("other");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
    }

    @Test
    void testCharSequenceMethods() {
        EventType<Map<String, Object>> event = EventType.of("homeassistant_start");
        assertEquals(19, event.length());
        assertEquals('h', event.charAt(0));
        assertEquals("assistant", event.subSequence(4, 13).toString());
    }
}