// src/main/java/com/nexsys/units/RatioUnit.java
package com.nexsys.util.units;

/**
 * Units of ratio measurement.
 */
public enum RatioUnit implements Unit {
    PERCENT(100.0),
    PARTS_PER_MILLION(1000000.0),
    PARTS_PER_BILLION(1000000000.0);

    private final double toUnitless;

    RatioUnit(double toUnitless) {
        this.toUnitless = toUnitless;
    }

    public double toUnitless() {
        return toUnitless;
    }
}