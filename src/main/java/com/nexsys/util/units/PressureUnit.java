// src/main/java/com/nexsys/units/PressureUnit.java
package com.nexsys.util.units;

/**
 * Units of pressure measurement.
 */
public enum PressureUnit implements Unit {
    PASCAL(1.0),
    HECTOPASCAL(100.0),
    KILOPASCAL(1000.0),
    BAR(100000.0),
    CENTIBAR(1000.0),
    MILLIBAR(100.0),
    INCH_HG(3386.389),
    PSI(6894.757),
    MILLIMETER_HG(133.3224);

    private final double toPascal;

    PressureUnit(double toPascal) {
        this.toPascal = toPascal;
    }

    public double toPascal() {
        return toPascal;
    }
}