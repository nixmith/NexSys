// src/main/java/com/nexsys/units/EnergyDistanceUnit.java
package com.nexsys.util.units;

/**
 * Units of energy per distance measurement.
 */
public enum EnergyDistanceUnit implements Unit {
    KILOWATT_HOUR_PER_100_KM(1.0, false),
    WATT_HOUR_PER_KM(10.0, false),
    MILE_PER_KILOWATT_HOUR(160.9344, true),
    KM_PER_KILOWATT_HOUR(100.0, true);

    private final double toKwhPer100Km;
    private final boolean inverse;

    EnergyDistanceUnit(double toKwhPer100Km, boolean inverse) {
        this.toKwhPer100Km = toKwhPer100Km;
        this.inverse = inverse;
    }

    public double toKwhPer100Km() {
        return toKwhPer100Km;
    }

    public boolean isInverse() {
        return inverse;
    }
}