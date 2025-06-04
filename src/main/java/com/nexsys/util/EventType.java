// src/main/java/com/nexsys/util/EventType.java
package com.nexsys.util;

import java.util.Map;
import java.util.Objects;

/**
 * Generic wrapper around a String event type.
 *
 * @param <D> the data type, must extend Map
 */
public final class EventType<D extends Map<String, ?>> implements CharSequence {
    private final String value;

    private EventType(String value) {
        this.value = Objects.requireNonNull(value, "Event value cannot be null");
    }

    /**
     * Creates a new EventType with the given value.
     *
     * @param value the event value
     * @param <D> the data type
     * @return a new EventType instance
     */
    public static <D extends Map<String, ?>> EventType<D> of(String value) {
        return new EventType<>(value);
    }

    /**
     * Creates a formatted EventType using the given format and arguments.
     *
     * @param format the format string
     * @param args the format arguments
     * @param <D> the data type
     * @return a new formatted EventType instance
     */
    public static <D extends Map<String, ?>> EventType<D> format(String format, Object... args) {
        return new EventType<>(String.format(format, args));
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventType<?> other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}