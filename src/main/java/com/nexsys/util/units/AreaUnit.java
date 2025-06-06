// src/main/java/com/nexsys/units/AreaUnit.java
package com.nexsys.util.units;

/**
 * Units of area measurement.
 */
public enum AreaUnit implements Unit {
    SQUARE_MILLIMETER(0.000001),
    SQUARE_CENTIMETER(0.0001),
    SQUARE_METRE(1.0),
    SQUARE_KILOMETRE(1000000.0),
    SQUARE_INCH(0.00064516),
    SQUARE_FOOT(0.092903),
    SQUARE_YARD(0.836127),
    SQUARE_MILE(2590000.0),
    ACRE(4046.86),
    HECTARE(10000.0);

    private final double toSquareMetre;

    AreaUnit(double toSquareMetre) {
        this.toSquareMetre = toSquareMetre;
    }

    public double toSquareMetre() {
        return toSquareMetre;
    }
}