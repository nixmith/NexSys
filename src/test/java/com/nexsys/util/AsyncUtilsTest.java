// src/test/java/com/nexsys/util/AsyncUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import static org.assertj.core.api.Assertions.*;

class AsyncUtilsTest {

    @Test
    void testGatherWithLimit() throws Exception {
        AtomicInteger concurrent = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);

        List<Supplier<CompletableFuture<Integer>>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            tasks.add(() -> CompletableFuture.supplyAsync(() -> {
                int current = concurrent.incrementAndGet();
                maxConcurrent.updateAndGet(max -> Math.max(max, current));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                concurrent.decrementAndGet();
                return taskId;
            }));
        }

        CompletableFuture<List<Integer>> result = AsyncUtils.gatherWithLimit(3, tasks);
        List<Integer> results = result.get(2, TimeUnit.SECONDS);

        assertThat(results).hasSize(10);
        assertThat(maxConcurrent.get()).isLessThanOrEqualTo(3);
    }

    @Test
    void testSupplyEager() throws Exception {
        CountDownLatch started = new CountDownLatch(1);

        CompletableFuture<String> future = AsyncUtils.supplyEager(() -> {
            started.countDown();
            return "eager";
        }, "test-eager");

        boolean taskStarted = started.await(100, TimeUnit.MILLISECONDS);
        assertThat(taskStarted).isTrue();
        assertThat(future.get()).isEqualTo("eager");
    }
}