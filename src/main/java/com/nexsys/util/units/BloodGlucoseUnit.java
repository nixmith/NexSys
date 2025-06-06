// src/main/java/com/nexsys/units/BloodGlucoseUnit.java
package com.nexsys.util.units;

/**
 * Units of blood glucose concentration measurement.
 */
public enum BloodGlucoseUnit implements Unit {
    MILLIGRAM_PER_DECILITER(18.0),
    MILLIMOLE_PER_LITER(1.0);

    private final double toMmolPerL;

    BloodGlucoseUnit(double toMmolPerL) {
        this.toMmolPerL = toMmolPerL;
    }

    public double toMmolPerL() {
        return toMmolPerL;
    }
}