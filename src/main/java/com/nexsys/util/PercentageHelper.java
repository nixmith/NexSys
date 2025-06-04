// src/main/java/com/nexsys/util/PercentageHelper.java
package com.nexsys.util;

import java.util.List;
import java.util.Objects;

/**
 * Helper methods for percentage calculations with ordered lists.
 */
public final class PercentageHelper {
    private PercentageHelper() {
        // Utility class
    }

    /**
     * Determine the percentage of an item in an ordered list.
     *
     * @param list the ordered list
     * @param item the item to find
     * @param <T> the type of list elements
     * @return the percentage position of the item
     * @throws IllegalArgumentException if item is not in list or list is empty
     */
    public static <T> Percentage orderedListItemToPercentage(List<T> list, T item) {
        Objects.requireNonNull(list, "List cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        int index = list.indexOf(item);
        if (index == -1) {
            throw new IllegalArgumentException("Item \"" + item + "\" is not in the list");
        }

        int listPosition = index + 1;
        int percentage = (listPosition * 100) / list.size();
        return Percentage.of(percentage);
    }

    /**
     * Find the item that most closely matches the percentage in an ordered list.
     *
     * @param list the ordered list
     * @param percentage the target percentage (0-100)
     * @param <T> the type of list elements
     * @return the item at the given percentage position
     * @throws IllegalArgumentException if list is empty or percentage is invalid
     */
    public static <T> T percentageToOrderedListItem(List<T> list, int percentage) {
        Objects.requireNonNull(list, "List cannot be null");

        if (list.isEmpty()) {
            throw new IllegalArgumentException("The ordered list is empty");
        }

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        int listLen = list.size();

        for (int offset = 0; offset < listLen; offset++) {
            int listPosition = offset + 1;
            int upperBound = (listPosition * 100) / listLen;
            if (percentage <= upperBound) {
                return list.get(offset);
            }
        }

        return list.get(listLen - 1);
    }
}