// src/main/java/com/nexsys/util/LoopGuard.java
package com.nexsys.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Guards against blocking operations on event-loop threads.
 */
public final class LoopGuard {
    private static final Logger LOGGER = Logger.getLogger(LoopGuard.class.getName());
    private static final Set<Long> EVENT_LOOP_THREADS = ConcurrentHashMap.newKeySet();
    private static final Set<String> REPORTED_VIOLATIONS = ConcurrentHashMap.newKeySet();

    private LoopGuard() {}

    /**
     * Mark current thread as event-loop thread.
     */
    public static void markAsEventLoopThread() {
        EVENT_LOOP_THREADS.add(Thread.currentThread().threadId());
    }

    /**
     * Unmark thread as event-loop thread.
     */
    public static void unmarkEventLoopThread() {
        EVENT_LOOP_THREADS.remove(Thread.currentThread().threadId());
    }

    /**
     * Protect against blocking operations on event-loop thread.
     */
    public static void protect(String operationName, Runnable operation) {
        if (isEventLoopThread()) {
            String key = operationName + "-" + getCallerInfo();
            if (REPORTED_VIOLATIONS.add(key)) {
                LOGGER.severe("Blocking operation '" + operationName +
                        "' called on event-loop thread at " + getCallerInfo());
            }
            throw new IllegalStateException(
                    "Blocking operation '" + operationName + "' called on event-loop thread");
        }

        operation.run();
    }

    private static boolean isEventLoopThread() {
        return EVENT_LOOP_THREADS.contains(Thread.currentThread().threadId());
    }

    private static String getCallerInfo() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length >= 4) {
            StackTraceElement caller = stack[3];
            return caller.getClassName() + "." + caller.getMethodName() +
                    "(" + caller.getFileName() + ":" + caller.getLineNumber() + ")";
        }
        return "unknown";
    }
}