// src/main/java/com/nexsys/units/TimeUnit.java
package com.nexsys.util.units;

/**
 * Units of time measurement.
 */
public enum TimeUnit implements Unit {
    MICROSECOND(0.000001),
    MILLISECOND(0.001),
    SECOND(1.0),
    MINUTE(60.0),
    HOUR(3600.0),
    DAY(86400.0),
    WEEK(604800.0);

    private final double toSecond;

    TimeUnit(double toSecond) {
        this.toSecond = toSecond;
    }

    public double toSecond() {
        return toSecond;
    }
}