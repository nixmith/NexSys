// src/main/java/com/nexsys/units/ElectricCurrentUnit.java
package com.nexsys.util.units;

/**
 * Units of electric current measurement.
 */
public enum ElectricCurrentUnit implements Unit {
    AMPERE(1.0),
    MILLIAMPERE(0.001);

    private final double toAmpere;

    ElectricCurrentUnit(double toAmpere) {
        this.toAmpere = toAmpere;
    }

    public double toAmpere() {
        return toAmpere;
    }
}