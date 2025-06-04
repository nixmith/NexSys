// src/test/java/com/nexsys/util/UuidUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UuidUtilsTest {

    @Test
    void testRandomUuidHex() {
        String uuid = UuidUtils.randomUuidHex();
        assertEquals(32, uuid.length());
        assertTrue(uuid.matches("[0-9a-f]{32}"));
        assertFalse(uuid.contains("-"));
    }

    @Test
    void testUniqueness() {
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 10_000; i++) {
            uuids.add(UuidUtils.randomUuidHex());
        }
        assertEquals(10_000, uuids.size(), "All generated UUIDs should be unique");
    }
}