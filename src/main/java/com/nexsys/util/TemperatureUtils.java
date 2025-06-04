// src/main/java/com/nexsys/util/TemperatureUtils.java
package com.nexsys.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Temperature conversion and display utilities.
 */
public final class TemperatureUtils {
    public static final double PRECISION_HALVES = 0.5;
    public static final double PRECISION_TENTHS = 0.1;

    private TemperatureUtils() {
        // Utility class
    }

    /**
     * Display temperature with conversion and precision rounding.
     *
     * @param value the temperature value
     * @param fromUnit the source unit (C, F, or K)
     * @param toUnit the target unit (C, F, or K)
     * @param precision the rounding precision
     * @return the converted and rounded temperature
     */
    public static double displayTemp(double value, String fromUnit, String toUnit, double precision) {
        // Convert temperature
        double converted = convertTemperature(value, fromUnit, toUnit);

        // Apply precision rounding
        if (precision == 0) {
            return Math.round(converted);
        }

        // Use BigDecimal for precise rounding
        BigDecimal bd = BigDecimal.valueOf(converted);
        BigDecimal precisionBd = BigDecimal.valueOf(precision);
        BigDecimal divided = bd.divide(precisionBd, RoundingMode.HALF_UP);
        BigDecimal rounded = divided.setScale(0, RoundingMode.HALF_UP);
        return rounded.multiply(precisionBd).doubleValue();
    }

    private static double convertTemperature(double value, String from, String to) {
        if (from.equals(to)) {
            return value;
        }

        // Convert to Celsius first
        double celsius = switch (from) {
            case "C" -> value;
            case "F" -> fahrenheitToCelsius(value);
            case "K" -> kelvinToCelsius(value);
            default -> throw new IllegalArgumentException("Unknown unit: " + from);
        };

        // Convert from Celsius to target
        return switch (to) {
            case "C" -> celsius;
            case "F" -> celsiusToFahrenheit(celsius);
            case "K" -> celsiusToKelvin(celsius);
            default -> throw new IllegalArgumentException("Unknown unit: " + to);
        };
    }

    private static double celsiusToFahrenheit(double celsius) {
        return celsius * 1.8 + 32.0;
    }

    private static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) / 1.8;
    }

    private static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    private static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}