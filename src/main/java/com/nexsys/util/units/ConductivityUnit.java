// src/main/java/com/nexsys/units/ConductivityUnit.java
package com.nexsys.util.units;

/**
 * Units of electrical conductivity measurement.
 */
public enum ConductivityUnit implements Unit {
    MICROSIEMEN_PER_CM(1.0),
    MILLISIEMEN_PER_CM(1000.0),
    SIEMEN_PER_CM(1000000.0);

    private final double toMicrosiemenPerCm;

    ConductivityUnit(double toMicrosiemenPerCm) {
        this.toMicrosiemenPerCm = toMicrosiemenPerCm;
    }

    public double toMicrosiemenPerCm() {
        return toMicrosiemenPerCm;
    }
}