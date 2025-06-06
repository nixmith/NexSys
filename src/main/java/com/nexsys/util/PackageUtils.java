// src/main/java/com/nexsys/util/PackageUtils.java
package com.nexsys.util;

import java.util.*;

/**
 * Package management utilities.
 */
public final class PackageUtils {
    private PackageUtils() {}

    /**
     * Check if a package/module is installed.
     */
    public static boolean isPackageInstalled(String spec) {
        String moduleName = extractModuleName(spec);

        return ModuleLayer.boot().modules().stream()
                .anyMatch(m -> m.getName().equals(moduleName));
    }

    /**
     * Get installed versions of specified packages.
     */
    public static Set<String> installedVersions(Set<String> specs) {
        Set<String> installed = new HashSet<>();

        for (String spec : specs) {
            if (isPackageInstalled(spec)) {
                installed.add(spec);
            }
        }

        return installed;
    }

    private static String extractModuleName(String spec) {
        int idx = spec.indexOf("==");
        if (idx > 0) {
            return spec.substring(0, idx);
        }
        idx = spec.indexOf(">=");
        if (idx > 0) {
            return spec.substring(0, idx);
        }
        return spec;
    }
}