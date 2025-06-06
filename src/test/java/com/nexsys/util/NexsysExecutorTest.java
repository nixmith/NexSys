// src/test/java/com/nexsys/util/NexsysExecutorTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;

class NexsysExecutorTest {

    @Test
    void testRunLater() throws Exception {
        NexsysExecutor executor = NexsysExecutor.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        executor.runLater(latch::countDown, Duration.ofMillis(100));

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
        assertThat(completed).isTrue();
    }

    @Test
    void testSubmitBlocking() throws Exception {
        NexsysExecutor executor = NexsysExecutor.getInstance();

        CompletableFuture<String> future = executor.submitBlocking(() -> {
            Thread.sleep(50);
            return "success";
        });

        String result = future.get(1, TimeUnit.SECONDS);
        assertThat(result).isEqualTo("success");
    }
}