// src/main/java/com/nexsys/util/DateTimeUtils.java
package com.nexsys.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Utilities for date and time operations.
 */
public final class DateTimeUtils {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private DateTimeUtils() {
        // Utility class
    }

    /**
     * Get the current UTC time.
     *
     * @return current UTC instant
     */
    public static Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Get the current time in the system default zone.
     *
     * @return current zoned datetime
     */
    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    /**
     * Get the current time in a specific zone.
     *
     * @param zone the time zone
     * @return current zoned datetime
     */
    public static ZonedDateTime now(ZoneId zone) {
        Objects.requireNonNull(zone, "Zone cannot be null");
        return ZonedDateTime.now(zone);
    }

    /**
     * Parse an ISO 8601 datetime string.
     *
     * @param isoString the ISO string
     * @return the parsed instant
     * @throws DateTimeParseException if the string cannot be parsed
     */
    public static Instant parseIso(String isoString) {
        Objects.requireNonNull(isoString, "ISO string cannot be null");

        try {
            // Try parsing as instant first
            return Instant.parse(isoString);
        } catch (DateTimeParseException e) {
            // Try parsing as zoned/offset datetime
            try {
                return ZonedDateTime.parse(isoString, ISO_FORMATTER).toInstant();
            } catch (DateTimeParseException e2) {
                return OffsetDateTime.parse(isoString, ISO_FORMATTER).toInstant();
            }
        }
    }

    /**
     * Format an instant to ISO 8601 string.
     *
     * @param instant the instant
     * @return ISO formatted string
     */
    public static String formatIso(Instant instant) {
        Objects.requireNonNull(instant, "Instant cannot be null");
        return instant.toString();
    }

    /**
     * Format a zoned datetime to ISO 8601 string.
     *
     * @param dateTime the datetime
     * @return ISO formatted string
     */
    public static String formatIso(ZonedDateTime dateTime) {
        Objects.requireNonNull(dateTime, "DateTime cannot be null");
        return dateTime.format(ISO_FORMATTER);
    }

    /**
     * Calculate duration between two instants.
     *
     * @param start the start instant
     * @param end the end instant
     * @return the duration between
     */
    public static Duration between(Instant start, Instant end) {
        Objects.requireNonNull(start, "Start instant cannot be null");
        Objects.requireNonNull(end, "End instant cannot be null");
        return Duration.between(start, end);
    }

    /**
     * Convert instant to epoch milliseconds.
     *
     * @param instant the instant
     * @return epoch milliseconds
     */
    public static long toEpochMillis(Instant instant) {
        Objects.requireNonNull(instant, "Instant cannot be null");
        return instant.toEpochMilli();
    }

    /**
     * Convert epoch milliseconds to instant.
     *
     * @param epochMillis the epoch milliseconds
     * @return the instant
     */
    public static Instant fromEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }

    // TODO: add dependency to build.gradle: implementation 'org.shredzone.commons:commons-suncalc:3.7'
    // for sunrise/sunset calculations
}