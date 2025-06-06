// src/main/java/com/nexsys/units/SpeedUnit.java
package com.nexsys.util.units;

/**
 * Units of speed measurement.
 */
public enum SpeedUnit implements Unit {
    METRE_PER_SECOND(1.0),
    MILLIMETER_PER_SECOND(0.001),
    INCH_PER_SECOND(0.0254),
    FOOT_PER_SECOND(0.3048),
    KILOMETRE_PER_HOUR(1.0/3.6),
    MILE_PER_HOUR(0.44704),
    KNOT(0.514444),
    BEAUFORT(1.0),
    INCH_PER_DAY(0.0254/86400.0),
    INCH_PER_HOUR(0.0254/3600.0),
    MILLIMETER_PER_DAY(0.001/86400.0),
    MILLIMETER_PER_HOUR(0.001/3600.0);

    private final double toMetrePerSecond;

    SpeedUnit(double toMetrePerSecond) {
        this.toMetrePerSecond = toMetrePerSecond;
    }

    public double toMetrePerSecond() {
        return toMetrePerSecond;
    }
}