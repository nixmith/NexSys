// src/main/java/com/nexsys/units/VolumeFlowRateUnit.java
package com.nexsys.util.units;

/**
 * Units of volume flow rate measurement.
 */
public enum VolumeFlowRateUnit implements Unit {
    CUBIC_METRE_PER_HOUR(1.0),
    CUBIC_METRE_PER_SECOND(3600.0),
    CUBIC_FOOT_PER_MINUTE(1.6990108),
    LITER_PER_HOUR(0.001),
    LITER_PER_MINUTE(0.06),
    LITER_PER_SECOND(3.6),
    GALLON_PER_MINUTE(0.22712471),
    MILLILITER_PER_SECOND(0.0036);

    private final double toCubicMetrePerHour;

    VolumeFlowRateUnit(double toCubicMetrePerHour) {
        this.toCubicMetrePerHour = toCubicMetrePerHour;
    }

    public double toCubicMetrePerHour() {
        return toCubicMetrePerHour;
    }
}