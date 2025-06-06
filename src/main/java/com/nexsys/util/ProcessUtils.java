// src/main/java/com/nexsys/util/ProcessUtils.java
package com.nexsys.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Process management utilities.
 */
public final class ProcessUtils {
    private ProcessUtils() {}

    /**
     * Force-kills a subprocess and waits up to 5 seconds for it to exit.
     *
     * If the process is null or not alive, this method does nothing.
     *
     * @param process the Process to kill (may be null)
     */
    public static void kill(Process process) {
        if (process == null || !process.isAlive()) {
            return;
        }
        process.destroyForcibly();
        try {
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // Restore interrupted status and continue
            Thread.currentThread().interrupt();
        }
    }

    /**
     * (Optional convenience for testing purposes) Start a simple long-running process
     * without needing to know the exact command syntax on each OS.
     *
     * On Windows, this will start "ping 127.0.0.1 -n 10".
     * On Unix-like systems, this will start "sleep 10".
     *
     * @return a Process that should remain alive for at least 8–10 seconds,
     *         or null if an IOException occurred.
     */
    public static Process startDummyLongRunningProcess() {
        ProcessBuilder pb;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Windows: ping localhost 10 times (one second apart)
            pb = new ProcessBuilder("ping", "127.0.0.1", "-n", "10");
        } else {
            // macOS / Linux / Unix: sleep for 10 seconds
            pb = new ProcessBuilder("sleep", "10");
        }
        try {
            return pb.start();
        } catch (IOException e) {
            return null;
        }
    }
}
