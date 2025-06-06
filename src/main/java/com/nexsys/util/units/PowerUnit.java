// src/main/java/com/nexsys/units/PowerUnit.java
package com.nexsys.util.units;

/**
 * Units of power measurement.
 */
public enum PowerUnit implements Unit {
    MILLIWATT(0.001),
    WATT(1.0),
    KILOWATT(1000.0),
    MEGAWATT(1000000.0),
    GIGAWATT(1000000000.0),
    TERAWATT(1000000000000.0);

    private final double toWatt;

    PowerUnit(double toWatt) {
        this.toWatt = toWatt;
    }

    public double toWatt() {
        return toWatt;
    }
}