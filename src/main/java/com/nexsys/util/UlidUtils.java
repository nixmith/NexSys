// src/main/java/com/nexsys/util/UlidUtils.java
package com.nexsys.util;

import java.time.Instant;
// TODO: Add dependency to build.gradle: implementation 'de.huxhorn.sulky:ulid:8.2.0'

/**
 * ULID utility functions.
 */
public final class UlidUtils {
    private UlidUtils() {
        // Utility class
    }

    /**
     * Generate a new ULID for the current time.
     *
     * @return a ULID string
     */
    public static String newUlid() {
        // TODO: Implement using de.huxhorn.sulky.ulid.ULID.new()
        throw new UnsupportedOperationException("ULID dependency not yet added");
    }

    /**
     * Generate a ULID for a specific instant.
     *
     * @param instant the timestamp
     * @return a ULID string
     */
    public static String ulidAt(Instant instant) {
        // TODO: Implement using de.huxhorn.sulky.ulid.ULID.fromMillis(instant.toEpochMilli())
        throw new UnsupportedOperationException("ULID dependency not yet added");
    }
}