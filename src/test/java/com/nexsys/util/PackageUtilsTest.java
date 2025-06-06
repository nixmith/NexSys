// src/test/java/com/nexsys/util/PackageUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

class PackageUtilsTest {

    @Test
    void testIsPackageInstalled() {
        // java.base should always be present
        boolean installed = PackageUtils.isPackageInstalled("java.base");
        assertThat(installed).isTrue();

        installed = PackageUtils.isPackageInstalled("non.existent.module");
        assertThat(installed).isFalse();
    }

    @Test
    void testInstalledVersions() {
        Set<String> specs = Set.of("java.base", "java.logging", "fake.module");
        Set<String> installed = PackageUtils.installedVersions(specs);

        assertThat(installed).contains("java.base", "java.logging");
        assertThat(installed).doesNotContain("fake.module");
    }
}