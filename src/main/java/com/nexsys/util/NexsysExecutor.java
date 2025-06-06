// src/main/java/com/nexsys/util/NexsysExecutor.java
package com.nexsys.util;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Singleton executor service for the Nexsys system.
 * Uses virtual threads when available.
 */
public final class NexsysExecutor {
    private static final Logger LOGGER = Logger.getLogger(NexsysExecutor.class.getName());
    private static final NexsysExecutor INSTANCE = new NexsysExecutor();

    private final ScheduledThreadPoolExecutor scheduler;
    private final ExecutorService virtualExecutor;
    private volatile boolean shuttingDown = false;

    private NexsysExecutor() {
        ThreadFactory schedulerFactory = r -> {
            Thread t = new Thread(r, "Nexsys-Scheduler");
            t.setDaemon(true);
            return t;
        };

        this.scheduler = new ScheduledThreadPoolExecutor(2, schedulerFactory);
        this.scheduler.setRemoveOnCancelPolicy(true);

        // Try to create virtual thread executor if available (Java 21+)
        ExecutorService executor;
        try {
            executor = Executors.newVirtualThreadPerTaskExecutor();
        } catch (UnsupportedOperationException e) {
            // Fall back to ForkJoinPool if virtual threads not available
            executor = ForkJoinPool.commonPool();
        }
        this.virtualExecutor = executor;
    }

    public static NexsysExecutor getInstance() {
        return INSTANCE;
    }

    /**
     * Run a task after specified delay.
     */
    public ScheduledFuture<?> runLater(Runnable task, Duration delay) {
        if (shuttingDown) {
            throw new RejectedExecutionException("Executor is shutting down");
        }
        return scheduler.schedule(() -> virtualExecutor.execute(task),
                delay.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Run a task at specific instant.
     */
    public ScheduledFuture<?> runAt(Runnable task, Instant when) {
        Duration delay = Duration.between(Instant.now(), when);
        if (delay.isNegative()) {
            delay = Duration.ZERO;
        }
        return runLater(task, delay);
    }

    /**
     * Submit a blocking operation to be executed on virtual thread.
     */
    public <T> CompletableFuture<T> submitBlocking(Callable<T> task) {
        if (shuttingDown) {
            return CompletableFuture.failedFuture(
                    new RejectedExecutionException("Executor is shutting down"));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, virtualExecutor);
    }

    /**
     * Gracefully shutdown the executor.
     */
    public void shutdown() {
        if (shuttingDown) {
            return;
        }

        shuttingDown = true;
        scheduler.shutdown();
        virtualExecutor.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOGGER.warning("Scheduler did not terminate");
                }
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}