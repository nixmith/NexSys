// src/main/java/com/nexsys/util/AsyncUtils.java
package com.nexsys.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Asynchronous operation utilities.
 */
public final class AsyncUtils {
    private AsyncUtils() {}

    /**
     * Execute multiple async tasks with concurrency limit.
     */
    public static <T> CompletableFuture<List<T>> gatherWithLimit(
            int limit, List<Supplier<CompletableFuture<T>>> tasks) {

        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        Semaphore semaphore = new Semaphore(limit);
        List<CompletableFuture<T>> futures = new ArrayList<>();

        for (Supplier<CompletableFuture<T>> taskSupplier : tasks) {
            CompletableFuture<T> future = CompletableFuture
                    .supplyAsync(() -> {
                        try {
                            semaphore.acquire();
                            return taskSupplier.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new CompletionException(e);
                        }
                    })
                    .thenCompose(f -> f)
                    .whenComplete((result, error) -> semaphore.release());

            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    /**
     * Create a CompletableFuture that starts executing immediately.
     */
    public static <T> CompletableFuture<T> supplyEager(Supplier<T> supplier, String name) {
        Objects.requireNonNull(supplier, "Supplier cannot be null");

        CompletableFuture<T> future = new CompletableFuture<>();

        Thread.ofVirtual()
                .name(name != null ? name : "eager-task")
                .start(() -> {
                    try {
                        T result = supplier.get();
                        future.complete(result);
                    } catch (Throwable t) {
                        future.completeExceptionally(t);
                    }
                });

        return future;
    }
}