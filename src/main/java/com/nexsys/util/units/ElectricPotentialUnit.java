// src/main/java/com/nexsys/units/ElectricPotentialUnit.java
package com.nexsys.util.units;

/**
 * Units of electric potential measurement.
 */
public enum ElectricPotentialUnit implements Unit {
    MICROVOLT(0.000001),
    MILLIVOLT(0.001),
    VOLT(1.0),
    KILOVOLT(1000.0),
    MEGAVOLT(1000000.0);

    private final double toVolt;

    ElectricPotentialUnit(double toVolt) {
        this.toVolt = toVolt;
    }

    public double toVolt() {
        return toVolt;
    }
}