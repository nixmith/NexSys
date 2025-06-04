// src/test/java/com/nexsys/util/PercentageHelperTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PercentageHelperTest {

    @Test
    void testOrderedListItemToPercentage() {
        List<String> speeds = List.of("low", "medium", "high", "very_high");

        assertEquals(25, PercentageHelper.orderedListItemToPercentage(speeds, "low").intValue());
        assertEquals(50, PercentageHelper.orderedListItemToPercentage(speeds, "medium").intValue());
        assertEquals(75, PercentageHelper.orderedListItemToPercentage(speeds, "high").intValue());
        assertEquals(100, PercentageHelper.orderedListItemToPercentage(speeds, "very_high").intValue());
    }

    @Test
    void testPercentageToOrderedListItem() {
        List<String> speeds = List.of("low", "medium", "high", "very_high");

        assertEquals("low", PercentageHelper.percentageToOrderedListItem(speeds, 1));
        assertEquals("low", PercentageHelper.percentageToOrderedListItem(speeds, 25));
        assertEquals("medium", PercentageHelper.percentageToOrderedListItem(speeds, 26));
        assertEquals("medium", PercentageHelper.percentageToOrderedListItem(speeds, 50));
        assertEquals("high", PercentageHelper.percentageToOrderedListItem(speeds, 51));
        assertEquals("high", PercentageHelper.percentageToOrderedListItem(speeds, 75));
        assertEquals("very_high", PercentageHelper.percentageToOrderedListItem(speeds, 76));
        assertEquals("very_high", PercentageHelper.percentageToOrderedListItem(speeds, 100));
    }

    @Test
    void testEmptyList() {
        List<String> empty = List.of();
        assertThrows(IllegalArgumentException.class,
                () -> PercentageHelper.orderedListItemToPercentage(empty, "item"));
        assertThrows(IllegalArgumentException.class,
                () -> PercentageHelper.percentageToOrderedListItem(empty, 50));
    }

    @Test
    void testItemNotInList() {
        List<String> list = List.of("a", "b", "c");
        assertThrows(IllegalArgumentException.class,
                () -> PercentageHelper.orderedListItemToPercentage(list, "d"));
    }

    @Test
    void testInvalidPercentage() {
        List<String> list = List.of("a", "b", "c");
        assertThrows(IllegalArgumentException.class,
                () -> PercentageHelper.percentageToOrderedListItem(list, -1));
        assertThrows(IllegalArgumentException.class,
                () -> PercentageHelper.percentageToOrderedListItem(list, 101));
    }
}