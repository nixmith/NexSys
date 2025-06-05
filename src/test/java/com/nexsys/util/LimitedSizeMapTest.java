// src/test/java/com/nexsys/util/LimitedSizeMapTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class LimitedSizeMapTest {

    @Test
    void testFifoEviction() {
        LimitedSizeMap<String, Integer> map = new LimitedSizeMap<>(3);

        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        assertThat(map).containsKeys("a", "b", "c");

        // Adding fourth element should evict "a"
        map.put("d", 4);

        assertThat(map).hasSize(3);
        assertThat(map).doesNotContainKey("a");
        assertThat(map).containsKeys("b", "c", "d");

        // Adding fifth element should evict "b"
        map.put("e", 5);

        assertThat(map).hasSize(3);
        assertThat(map).doesNotContainKeys("a", "b");
        assertThat(map).containsKeys("c", "d", "e");
    }

    @Test
    void testConstructorWithInitialEntries() {
        Map<String, Integer> initial = Map.of("a", 1, "b", 2, "c", 3, "d", 4);
        LimitedSizeMap<String, Integer> map = new LimitedSizeMap<>(2, initial);

        // Should only keep last 2 entries
        assertThat(map).hasSize(2);
    }

    @Test
    void testInvalidSizeLimit() {
        assertThatThrownBy(() -> new LimitedSizeMap<String, Integer>(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}