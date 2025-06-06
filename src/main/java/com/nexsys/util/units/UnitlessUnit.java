// src/main/java/com/nexsys/units/UnitlessUnit.java
package com.nexsys.util.units;

/**
 * Unitless measurements.
 */
public enum UnitlessUnit implements Unit {
    NONE(1.0),
    PERCENT(100.0),
    PARTS_PER_MILLION(1000000.0),
    PARTS_PER_BILLION(1000000000.0);

    private final double toUnitless;

    UnitlessUnit(double toUnitless) {
        this.toUnitless = toUnitless;
    }

    public double toUnitless() {
        return toUnitless;
    }
}