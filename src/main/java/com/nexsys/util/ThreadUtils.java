// src/main/java/com/nexsys/util/ThreadUtils.java
package com.nexsys.util;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Thread management utilities.
 */
public final class ThreadUtils {
    private static final Logger LOGGER = Logger.getLogger(ThreadUtils.class.getName());
    private static final sun.misc.Unsafe UNSAFE;

    static {
        try {
            Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) f.get(null);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ThreadUtils() {}

    /**
     * Shutdown all non-daemon threads safely within timeout.
     *
     * @param timeout maximum time to wait for thread termination
     */
    public static void deadlockSafeShutdown(Duration timeout) {
        List<Thread> remainingThreads = new ArrayList<>();
        Thread mainThread = Thread.currentThread();

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t != mainThread && !t.isDaemon() && t.isAlive()) {
                remainingThreads.add(t);
            }
        }

        if (remainingThreads.isEmpty()) {
            return;
        }

        long timeoutMillis = timeout.toMillis();
        long timePerThread = timeoutMillis / remainingThreads.size();

        for (Thread t : remainingThreads) {
            try {
                t.join(timePerThread);
                if (t.isAlive()) {
                    LOGGER.warning("Thread " + t.getName() + " still running at shutdown");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warning("Interrupted while joining thread: " + t.getName());
            }
        }
    }

    /**
     * Politely interrupts the given thread, causing it to receive an interrupt signal.
     * In Java, there is no safe way to forcibly throw an arbitrary exception in another thread. Instead,
     * we set the target thread's interrupt flag. It's up to that thread's logic to check Thread.interrupted()
     * or catch InterruptedException and exit cleanly.
     *
     * @param thread the thread to interrupt; must not be null
     * @throws NullPointerException if {@code thread} is null
     */
    public static void interruptThread(Thread thread) {
        Objects.requireNonNull(thread, "Thread to interrupt cannot be null");
        thread.interrupt();
    }
}