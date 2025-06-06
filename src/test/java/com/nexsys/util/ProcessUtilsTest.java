// src/test/java/com/nexsys/util/ProcessUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Verify that ProcessUtils.kill(...) actually terminates a live subprocess.
 */
class ProcessUtilsTest {

    @Test
    void testKillNullProcess() {
        // kill(null) must not throw an exception
        assertThatCode(() -> ProcessUtils.kill(null))
                .doesNotThrowAnyException();
    }

    @Test
    void testKillProcess() throws InterruptedException {
        // Attempt to start a dummy long‐running process in a cross‐platform way:
        Process process = ProcessUtils.startDummyLongRunningProcess();
        if (process == null) {
            // In the rare case neither "ping" nor "sleep" could be launched, skip test:
            return;
        }

        // At this point the subprocess should still be alive:
        assertThat(process.isAlive()).isTrue();

        // Now kill it:
        ProcessUtils.kill(process);

        // Give JVM a brief moment to reflect process termination:
        Thread.sleep(200);
        assertThat(process.isAlive()).isFalse();
    }
}
