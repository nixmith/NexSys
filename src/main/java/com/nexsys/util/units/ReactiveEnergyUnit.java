// src/main/java/com/nexsys/units/ReactiveEnergyUnit.java
package com.nexsys.util.units;

/**
 * Units of reactive energy measurement.
 */
public enum ReactiveEnergyUnit implements Unit {
    VOLT_AMPERE_REACTIVE_HOUR(1.0),
    KILOVOLT_AMPERE_REACTIVE_HOUR(1000.0);

    private final double toVarh;

    ReactiveEnergyUnit(double toVarh) {
        this.toVarh = toVarh;
    }

    public double toVarh() {
        return toVarh;
    }
}