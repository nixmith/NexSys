// src/main/java/com/nexsys/util/ReadOnlyMap.java
package com.nexsys.util;

import java.util.*;

/**
 * Utilities for creating read-only maps.
 */
public final class ReadOnlyMap {
    private ReadOnlyMap() {
        // Utility class
    }

    /**
     * Wrap a map to make it read-only.
     *
     * @param map the map to wrap
     * @param <K> the key type
     * @param <V> the value type
     * @return an unmodifiable view of the map
     */
    public static <K, V> Map<K, V> wrap(Map<K, V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Create a read-only copy of a map.
     *
     * @param map the map to copy
     * @param <K> the key type
     * @param <V> the value type
     * @return an unmodifiable copy of the map
     */
    public static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new HashMap<>(map));
    }
}