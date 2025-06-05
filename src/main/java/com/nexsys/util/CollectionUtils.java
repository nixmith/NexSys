// src/main/java/com/nexsys/util/CollectionUtils.java
package com.nexsys.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Utility methods for working with collections.
 */
public final class CollectionUtils {
    private CollectionUtils() {
        // Utility class
    }

    /**
     * Return first n items of the iterable as a list.
     *
     * @param iterable the source iterable
     * @param n number of items to take
     * @param <T> the element type
     * @return list containing up to n items
     */
    public static <T> List<T> take(Iterable<T> iterable, int n) {
        Objects.requireNonNull(iterable, "Iterable cannot be null");
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }

        List<T> result = new ArrayList<>();
        int count = 0;
        for (T item : iterable) {
            if (count >= n) {
                break;
            }
            result.add(item);
            count++;
        }
        return result;
    }

    /**
     * Break collection into chunks of specified size.
     *
     * @param list the source list
     * @param chunkSize the size of each chunk
     * @param <T> the element type
     * @return list of chunks
     * @throws IllegalArgumentException if chunkSize is less than 1
     */
    public static <T> List<List<T>> chunk(List<T> list, int chunkSize) {
        Objects.requireNonNull(list, "List cannot be null");
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Chunk size must be at least 1");
        }

        // TODO: add dependency to build.gradle: implementation 'com.google.guava:guava:32.1.3-jre'
        // Can use Guava's Lists.partition(list, chunkSize) once available

        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, list.size());
            chunks.add(new ArrayList<>(list.subList(i, end)));
        }
        return chunks;
    }

    /**
     * Break collection into chunks or return as single chunk if smaller than chunk size.
     *
     * @param collection the source collection
     * @param chunkSize the size of each chunk
     * @param <T> the element type
     * @return list containing either the original collection or chunks
     */
    public static <T> List<Collection<T>> chunkedOrAll(Collection<T> collection, int chunkSize) {
        Objects.requireNonNull(collection, "Collection cannot be null");
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Chunk size must be at least 1");
        }

        if (collection.size() <= chunkSize) {
            return Collections.singletonList(collection);
        }

        List<T> list = new ArrayList<>(collection);
        return chunk(list, chunkSize).stream()
                .map(chunk -> (Collection<T>) chunk)
                .collect(Collectors.toList());
    }

    /**
     * Deep merge two maps, with conflict resolution.
     *
     * @param target the target map to merge into
     * @param source the source map to merge from
     * @param conflictResolver function to resolve value conflicts
     * @param <K> the key type
     * @param <V> the value type
     * @return the merged map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> deepMerge(Map<K, V> target, Map<K, V> source,
                                             BinaryOperator<V> conflictResolver) {
        Objects.requireNonNull(target, "Target map cannot be null");
        Objects.requireNonNull(source, "Source map cannot be null");
        Objects.requireNonNull(conflictResolver, "Conflict resolver cannot be null");

        Map<K, V> result = new HashMap<>(target);

        source.forEach((key, sourceValue) -> {
            V targetValue = result.get(key);

            if (targetValue == null) {
                result.put(key, sourceValue);
            } else if (targetValue instanceof Map && sourceValue instanceof Map) {
                // Recursive merge for nested maps
                Map<Object, Object> merged = deepMerge(
                        (Map<Object, Object>) targetValue,
                        (Map<Object, Object>) sourceValue,
                        (BinaryOperator<Object>) conflictResolver
                );
                result.put(key, (V) merged);
            } else {
                // Use conflict resolver for non-map values
                result.put(key, conflictResolver.apply(targetValue, sourceValue));
            }
        });

        return result;
    }
}