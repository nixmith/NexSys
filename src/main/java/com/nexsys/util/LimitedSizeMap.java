// src/main/java/com/nexsys/util/LimitedSizeMap.java
package com.nexsys.util;

import java.util.*;

/**
 * A LinkedHashMap with a maximum size that evicts entries in FIFO order.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class LimitedSizeMap<K, V> extends LinkedHashMap<K, V> {
    private final int sizeLimit;

    /**
     * Create a new LimitedSizeMap with the specified size limit.
     *
     * @param sizeLimit the maximum number of entries
     * @throws IllegalArgumentException if sizeLimit is less than 1
     */
    public LimitedSizeMap(int sizeLimit) {
        super(16, 0.75f, false); // false = insertion order (FIFO)
        if (sizeLimit < 1) {
            throw new IllegalArgumentException("Size limit must be at least 1");
        }
        this.sizeLimit = sizeLimit;
    }

    /**
     * Create a new LimitedSizeMap with initial entries and size limit.
     *
     * @param sizeLimit the maximum number of entries
     * @param initialEntries initial entries to add
     */
    public LimitedSizeMap(int sizeLimit, Map<? extends K, ? extends V> initialEntries) {
        this(sizeLimit);
        putAll(initialEntries);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > sizeLimit;
    }

    /**
     * Get the size limit of this map.
     *
     * @return the maximum number of entries
     */
    public int getSizeLimit() {
        return sizeLimit;
    }
}