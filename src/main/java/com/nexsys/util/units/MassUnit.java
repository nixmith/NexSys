// src/main/java/com/nexsys/units/MassUnit.java
package com.nexsys.util.units;

/**
 * Units of mass measurement.
 */
public enum MassUnit implements Unit {
    MICROGRAM(0.000001),
    MILLIGRAM(0.001),
    GRAM(1.0),
    KILOGRAM(1000.0),
    OUNCE(28.349523125),
    POUND(453.59237),
    STONE(6350.29318);

    private final double toGram;

    MassUnit(double toGram) {
        this.toGram = toGram;
    }

    public double toGram() {
        return toGram;
    }
}