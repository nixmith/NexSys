// src/test/java/com/nexsys/util/TimeoutUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.*;

class TimeoutUtilsTest {

    @Test
    void testSupplyAsyncSuccess() throws Exception {
        CompletableFuture<String> future = TimeoutUtils.supplyAsync(
                () -> "success",
                Duration.ofSeconds(1)
        );

        assertThat(future.get()).isEqualTo("success");
    }

    @Test
    void testSupplyAsyncTimeout() {
        CompletableFuture<String> future = TimeoutUtils.supplyAsync(
                () -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "too late";
                },
                Duration.ofMillis(50)
        );

        assertThatThrownBy(future::get)
                .hasCauseInstanceOf(TimeoutException.class);
    }

    @Test
    void testRunWithTimeout() throws Exception {
        AtomicBoolean completed = new AtomicBoolean(false);

        CompletableFuture<Void> future = TimeoutUtils.runWithTimeout(
                () -> completed.set(true),
                Duration.ofSeconds(1)
        );

        future.get();
        assertThat(completed).isTrue();
    }

    @Test
    void testCallWithTimeoutSuccess() throws Exception {
        String result = TimeoutUtils.callWithTimeout(
                () -> "result",
                Duration.ofSeconds(1)
        );

        assertThat(result).isEqualTo("result");
    }
}