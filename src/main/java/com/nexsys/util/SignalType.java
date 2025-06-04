// src/main/java/com/nexsys/util/SignalType.java
package com.nexsys.util;

import java.util.Objects;

/**
 * Generic wrapper around a String signal type.
 *
 * @param <P> the parameter type for the signal
 */
public final class SignalType<P> implements CharSequence {
    private final String value;

    private SignalType(String value) {
        this.value = Objects.requireNonNull(value, "Signal value cannot be null");
    }

    /**
     * Creates a new SignalType with the given value.
     *
     * @param value the signal value
     * @param <P> the parameter type
     * @return a new SignalType instance
     */
    public static <P> SignalType<P> of(String value) {
        return new SignalType<>(value);
    }

    /**
     * Creates a formatted SignalType using the given format and arguments.
     *
     * @param format the format string
     * @param args the format arguments
     * @param <P> the parameter type
     * @return a new formatted SignalType instance
     */
    public static <P> SignalType<P> format(String format, Object... args) {
        return new SignalType<>(String.format(format, args));
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
        if (!(obj instanceof SignalType<?> other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}