// src/test/java/com/nexsys/util/ThreadUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import static org.assertj.core.api.Assertions.*;

class ThreadUtilsTest {

    @Test
    void testDeadlockSafeShutdown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Thread worker = new Thread(() -> {
            try {
                latch.await();  // waits until latch.countDown()
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "BlockingWorker");
        worker.start();

        Thread shutdownThread = new Thread(() ->
                ThreadUtils.deadlockSafeShutdown(Duration.ofSeconds(2))
        );
        shutdownThread.start();

        // Let shutdownThread attempt to join the worker for up to 2 seconds
        Thread.sleep(100);
        latch.countDown();  // now worker will become unblocked, finish run(), and exit

        shutdownThread.join(3000);
        assertThat(shutdownThread.isAlive()).isFalse();
    }
}