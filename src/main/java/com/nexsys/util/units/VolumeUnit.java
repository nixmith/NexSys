// src/main/java/com/nexsys/units/VolumeUnit.java
package com.nexsys.util.units;

/**
 * Units of volume measurement.
 */
public enum VolumeUnit implements Unit {
    MILLILITER(0.000001),
    LITER(0.001),
    CUBIC_METRE(1.0),
    CUBIC_FOOT(0.028316846592),
    CENTUM_CUBIC_FOOT(2.8316846592),
    GALLON(0.003785411784),
    FLUID_OUNCE(0.000029573529563);

    private final double toCubicMetre;

    VolumeUnit(double toCubicMetre) {
        this.toCubicMetre = toCubicMetre;
    }

    public double toCubicMetre() {
        return toCubicMetre;
    }
}