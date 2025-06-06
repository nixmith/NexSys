// src/main/java/com/nexsys/units/LengthUnit.java
package com.nexsys.util.units;

/**
 * Units of length measurement.
 */
public enum LengthUnit implements Unit {
    MILLIMETER(0.001),
    CENTIMETER(0.01),
    METRE(1.0),
    KILOMETRE(1000.0),
    INCH(0.0254),
    FOOT(0.3048),
    YARD(0.9144),
    MILE(1609.344),
    NAUTICAL_MILE(1852.0);

    private final double toMetre;

    LengthUnit(double toMetre) {
        this.toMetre = toMetre;
    }

    public double toMetre() {
        return toMetre;
    }
}