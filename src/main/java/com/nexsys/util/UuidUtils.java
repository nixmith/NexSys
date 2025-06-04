// src/main/java/com/nexsys/util/UuidUtils.java
package com.nexsys.util;

import java.util.UUID;

/**
 * UUID utility functions.
 */
public final class UuidUtils {
    private UuidUtils() {
        // Utility class
    }

    /**
     * Generate a random UUID hex string without hyphens.
     * Not cryptographically secure.
     *
     * @return a 32-character hex string
     */
    public static String randomUuidHex() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}