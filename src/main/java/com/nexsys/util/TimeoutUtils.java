// src/main/java/com/nexsys/util/TimeoutUtils.java
package com.nexsys.util;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Utilities for operations with timeouts.
 */
public final class TimeoutUtils {
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1, r -> {
                Thread t = new Thread(r, "TimeoutUtils-Scheduler");
                t.setDaemon(true);
                return t;
            });

    private TimeoutUtils() {
        // Utility class
    }

    /**
     * Execute a supplier asynchronously with a timeout.
     *
     * @param supplier the supplier to execute
     * @param timeout the timeout duration
     * @param <T> the result type
     * @return a CompletableFuture that completes with the result or times out
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Duration timeout) {
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        Objects.requireNonNull(timeout, "Timeout cannot be null");

        CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier);

        ScheduledFuture<?> timeoutFuture = scheduler.schedule(
                () -> future.completeExceptionally(new TimeoutException("Operation timed out")),
                timeout.toMillis(),
                TimeUnit.MILLISECONDS
        );

        future.whenComplete((result, error) -> timeoutFuture.cancel(false));

        return future;
    }

    /**
     * Run a task with a timeout.
     *
     * @param task the task to run
     * @param timeout the timeout duration
     * @return a CompletableFuture that completes when the task finishes or times out
     */
    public static CompletableFuture<Void> runWithTimeout(Runnable task, Duration timeout) {
        Objects.requireNonNull(task, "Task cannot be null");
        Objects.requireNonNull(timeout, "Timeout cannot be null");

        return supplyAsync(() -> {
            task.run();
            return null;
        }, timeout);
    }

    /**
     * Execute a callable with a timeout, blocking until completion.
     *
     * @param callable the callable to execute
     * @param timeout the timeout duration
     * @param <T> the result type
     * @return the result
     * @throws TimeoutException if the operation times out
     * @throws ExecutionException if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted
     */
    public static <T> T callWithTimeout(Callable<T> callable, Duration timeout)
            throws TimeoutException, ExecutionException, InterruptedException {
        Objects.requireNonNull(callable, "Callable cannot be null");
        Objects.requireNonNull(timeout, "Timeout cannot be null");

        FutureTask<T> task = new FutureTask<>(callable);
        Thread thread = new Thread(task, "TimeoutUtils-Worker");
        thread.start();

        try {
            return task.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } finally {
            if (!task.isDone()) {
                thread.interrupt();
            }
        }
    }
}