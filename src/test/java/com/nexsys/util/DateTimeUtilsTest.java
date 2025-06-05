// src/test/java/com/nexsys/util/DateTimeUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.time.*;
import static org.assertj.core.api.Assertions.*;

class DateTimeUtilsTest {

    @Test
    void testNowUtc() {
        Instant before = Instant.now();
        Instant result = DateTimeUtils.nowUtc();
        Instant after = Instant.now();

        assertThat(result).isAfterOrEqualTo(before);
        assertThat(result).isBeforeOrEqualTo(after);
    }

    @Test
    void testParseIso() {
        // Test various ISO formats
        String instant = "2023-12-25T10:30:00Z";
        String withOffset = "2023-12-25T10:30:00+01:00";
        String withMillis = "2023-12-25T10:30:00.123Z";

        assertThat(DateTimeUtils.parseIso(instant)).isNotNull();
        assertThat(DateTimeUtils.parseIso(withOffset)).isNotNull();
        assertThat(DateTimeUtils.parseIso(withMillis)).isNotNull();
    }

    @Test
    void testFormatIso() {
        Instant instant = Instant.parse("2023-12-25T10:30:00Z");
        String formatted = DateTimeUtils.formatIso(instant);

        assertThat(formatted).isEqualTo("2023-12-25T10:30:00Z");
    }

    @Test
    void testBetween() {
        Instant start = Instant.parse("2023-12-25T10:00:00Z");
        Instant end = Instant.parse("2023-12-25T11:30:45Z");

        Duration duration = DateTimeUtils.between(start, end);

        assertThat(duration.toHours()).isEqualTo(1);
        assertThat(duration.toMinutes()).isEqualTo(90);
        assertThat(duration.getSeconds()).isEqualTo(5445);
    }

    @Test
    void testRoundTripIsoString() {
        Instant original = Instant.now();
        String iso = DateTimeUtils.formatIso(original);
        Instant parsed = DateTimeUtils.parseIso(iso);

        assertThat(parsed).isEqualTo(original);
    }
}