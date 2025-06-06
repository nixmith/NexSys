// src/main/java/com/nexsys/util/SystemInfo.java
package com.nexsys.util;

import java.io.IOException;
import java.lang.management.*;
import java.nio.file.*;
import java.util.*;

/**
 * System information utilities.
 */
public final class SystemInfo {
    private static final OperatingSystemMXBean OS_BEAN =
            ManagementFactory.getOperatingSystemMXBean();
    private static final MemoryMXBean MEMORY_BEAN =
            ManagementFactory.getMemoryMXBean();

    private SystemInfo() {}

    /**
     * Get current CPU load percentage.
     */
    public static double getCpuLoad() {
        if (OS_BEAN instanceof com.sun.management.OperatingSystemMXBean) {
            return ((com.sun.management.OperatingSystemMXBean) OS_BEAN)
                    .getProcessCpuLoad() * 100;
        }
        return -1;
    }

    /**
     * Get memory usage info.
     */
    public static Map<String, Long> getMemoryInfo() {
        MemoryUsage heapUsage = MEMORY_BEAN.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = MEMORY_BEAN.getNonHeapMemoryUsage();

        Map<String, Long> info = new HashMap<>();
        info.put("heapUsed", heapUsage.getUsed());
        info.put("heapMax", heapUsage.getMax());
        info.put("nonHeapUsed", nonHeapUsage.getUsed());
        return info;
    }

    /**
     * Check if running in Docker container.
     */
    public static boolean isDockerEnv() {
        return Files.exists(Paths.get("/.dockerenv")) ||
                Files.exists(Paths.get("/run/.containerenv")) ||
                System.getenv("KUBERNETES_SERVICE_HOST") != null ||
                checkCgroupForDocker();
    }

    private static boolean checkCgroupForDocker() {
        try {
            String cgroup = Files.readString(Paths.get("/proc/self/cgroup"));
            return cgroup.contains("docker") || cgroup.contains("kubepods");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Check if running in virtual environment.
     */
    public static boolean isVirtualEnv() {
        String javaHome = System.getProperty("java.home");
        String userHome = System.getProperty("user.home");

        return javaHome != null && userHome != null &&
                !javaHome.startsWith("/usr") &&
                !javaHome.startsWith("/System") &&
                !javaHome.startsWith("C:\\Program Files");
    }
}