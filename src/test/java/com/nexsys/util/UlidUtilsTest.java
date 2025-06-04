// src/test/java/com/nexsys/util/UlidUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class UlidUtilsTest {

    @Test
    @Disabled("ULID dependency not yet added")
    void testNewUlid() {
        // Will be implemented when dependency is added
        assertThrows(UnsupportedOperationException.class, UlidUtils::newUlid);
    }

    @Test
    @Disabled("ULID dependency not yet added")
    void testUlidAt() {
        // Will be implemented when dependency is added
        assertThrows(UnsupportedOperationException.class,
                () -> UlidUtils.ulidAt(Instant.now()));
    }
}