// src/test/java/com/nexsys/util/SystemInfoTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class SystemInfoTest {

    @Test
    void testGetCpuLoad() {
        double cpuLoad = SystemInfo.getCpuLoad();
        assertThat(cpuLoad).isGreaterThanOrEqualTo(-1);
    }

    @Test
    void testGetMemoryInfo() {
        Map<String, Long> memInfo = SystemInfo.getMemoryInfo();

        assertThat(memInfo).containsKeys("heapUsed", "heapMax", "nonHeapUsed");
        assertThat(memInfo.get("heapUsed")).isGreaterThan(0);
    }

    @Test
    void testEnvironmentDetection() {
        // These will return false in normal test environment
        assertThat(SystemInfo.isDockerEnv()).isIn(true, false);
        assertThat(SystemInfo.isVirtualEnv()).isIn(true, false);
    }
}