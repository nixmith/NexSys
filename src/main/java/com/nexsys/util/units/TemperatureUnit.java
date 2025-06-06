// src/main/java/com/nexsys/units/TemperatureUnit.java
package com.nexsys.util.units;

/**
 * Units of temperature measurement.
 * Uses linear formula: celsius = m * value + b
 */
public enum TemperatureUnit implements Unit {
    CELSIUS(1.0, 0.0),
    FAHRENHEIT(5.0/9.0, -160.0/9.0),
    KELVIN(1.0, -273.15);

    private final double m;
    private final double b;

    TemperatureUnit(double m, double b) {
        this.m = m;
        this.b = b;
    }

    public double multiplier() {
        return m;
    }

    public double offset() {
        return b;
    }
}