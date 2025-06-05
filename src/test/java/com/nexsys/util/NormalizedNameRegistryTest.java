// src/test/java/com/nexsys/util/NormalizedNameRegistryTest.java
package com.nexsys.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class NormalizedNameRegistryTest {

    static class TestEntry {
        final String id;
        final String name;

        TestEntry(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class TestRegistry extends NormalizedNameRegistry<TestEntry> {
        @Override
        protected String getName(TestEntry entry) {
            return entry.name;
        }

        @Override
        protected String getId(TestEntry entry) {
            return entry.id;
        }
    }

    private TestRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TestRegistry();
    }

    @Test
    void testRegisterAndRetrieve() {
        TestEntry entry = new TestEntry("id1", "Test Name");
        registry.register(entry);

        assertThat(registry.getById("id1")).isSameAs(entry);
        assertThat(registry.getByName("Test Name")).isSameAs(entry);
        assertThat(registry.getByName("test name")).isSameAs(entry);
        assertThat(registry.getByName("TESTNAME")).isSameAs(entry);
        assertThat(registry.getByName("  Test  Name  ")).isSameAs(entry);
    }

    @Test
    void testDuplicateNameRejected() {
        registry.register(new TestEntry("id1", "Test"));

        assertThatThrownBy(() -> registry.register(new TestEntry("id2", "TEST")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testRemove() {
        TestEntry entry = new TestEntry("id1", "Test");
        registry.register(entry);

        assertThat(registry.remove("id1")).isSameAs(entry);
        assertThat(registry.getById("id1")).isNull();
        assertThat(registry.getByName("Test")).isNull();
    }

    @Test
    void testContains() {
        registry.register(new TestEntry("id1", "Test Name"));

        assertThat(registry.containsId("id1")).isTrue();
        assertThat(registry.containsName("test name")).isTrue();
        assertThat(registry.containsId("id2")).isFalse();
    }
}