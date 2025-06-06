// src/test/java/com/nexsys/util/LoopGuardTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class LoopGuardTest {

    @Test
    void testProtectOnNormalThread() {
        assertThatCode(() ->
                LoopGuard.protect("test-op", () -> {})
        ).doesNotThrowAnyException();
    }

    @Test
    void testProtectOnEventLoopThread() {
        LoopGuard.markAsEventLoopThread();
        try {
            assertThatThrownBy(() ->
                    LoopGuard.protect("blocking-op", () -> {})
            ).isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("event-loop thread");
        } finally {
            LoopGuard.unmarkEventLoopThread();
        }
    }
}