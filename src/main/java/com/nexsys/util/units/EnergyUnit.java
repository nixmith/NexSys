// src/main/java/com/nexsys/units/EnergyUnit.java
package com.nexsys.util.units;

/**
 * Units of energy measurement.
 */
public enum EnergyUnit implements Unit {
    JOULE(0.000277778),
    KILOJOULE(0.277778),
    MEGAJOULE(277.778),
    GIGAJOULE(277778.0),
    MILLIWATT_HOUR(0.001),
    WATT_HOUR(1.0),
    KILOWATT_HOUR(1000.0),
    MEGAWATT_HOUR(1000000.0),
    GIGAWATT_HOUR(1000000000.0),
    TERAWATT_HOUR(1000000000000.0),
    CALORIE(0.00116222),
    KILOCALORIE(1.16222),
    MEGACALORIE(1162.22),
    GIGACALORIE(1162220.0);

    private final double toWattHour;

    EnergyUnit(double toWattHour) {
        this.toWattHour = toWattHour;
    }

    public double toWattHour() {
        return toWattHour;
    }
}