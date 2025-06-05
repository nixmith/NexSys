// src/test/java/com/nexsys/util/CollectionUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    void testTake() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        assertThat(CollectionUtils.take(list, 3)).containsExactly(1, 2, 3);
        assertThat(CollectionUtils.take(list, 0)).isEmpty();
        assertThat(CollectionUtils.take(list, 10)).containsExactly(1, 2, 3, 4, 5);
        assertThat(CollectionUtils.take(Collections.emptyList(), 5)).isEmpty();
    }

    @Test
    void testChunk() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<List<Integer>> chunks = CollectionUtils.chunk(list, 3);
        assertThat(chunks).hasSize(3);
        assertThat(chunks.get(0)).containsExactly(1, 2, 3);
        assertThat(chunks.get(1)).containsExactly(4, 5, 6);
        assertThat(chunks.get(2)).containsExactly(7, 8, 9);

        chunks = CollectionUtils.chunk(list, 4);
        assertThat(chunks).hasSize(3);
        assertThat(chunks.get(0)).containsExactly(1, 2, 3, 4);
        assertThat(chunks.get(1)).containsExactly(5, 6, 7, 8);
        assertThat(chunks.get(2)).containsExactly(9);
    }

    @Test
    void testChunkedOrAll() {
        List<Integer> small = Arrays.asList(1, 2, 3);
        List<Integer> large = Arrays.asList(1, 2, 3, 4, 5, 6);

        // Small collection returns as-is
        List<Collection<Integer>> result = CollectionUtils.chunkedOrAll(small, 5);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isSameAs(small);

        // Large collection gets chunked
        result = CollectionUtils.chunkedOrAll(large, 2);
        assertThat(result).hasSize(3);
    }

    @Test
    void testDeepMerge() {
        Map<String, Object> target = new HashMap<>();
        target.put("a", 1);
        target.put("b", new HashMap<>(Map.of("x", 10, "y", 20)));

        Map<String, Object> source = new HashMap<>();
        source.put("b", new HashMap<>(Map.of("y", 30, "z", 40)));
        source.put("c", 3);

        Map<String, Object> result = CollectionUtils.deepMerge(target, source, (a, b) -> b);

        assertThat(result.get("a")).isEqualTo(1);
        assertThat(result.get("c")).isEqualTo(3);

        @SuppressWarnings("unchecked")
        Map<String, Object> merged = (Map<String, Object>) result.get("b");
        assertThat(merged.get("x")).isEqualTo(10);
        assertThat(merged.get("y")).isEqualTo(30); // Source wins
        assertThat(merged.get("z")).isEqualTo(40);
    }
}