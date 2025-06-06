// src/main/java/com/nexsys/units/Unit.java
package com.nexsys.util.units;

/**
 * Sealed interface for all unit types in the system.
 */
public sealed interface Unit permits
        LengthUnit, AreaUnit, MassUnit, VolumeUnit, PressureUnit,
        TemperatureUnit, SpeedUnit, EnergyUnit, PowerUnit,
        DataRateUnit, InformationUnit, ElectricCurrentUnit,
        ElectricPotentialUnit, VolumeFlowRateUnit, TimeUnit,
        EnergyDistanceUnit, ReactiveEnergyUnit, BloodGlucoseUnit,
        ConductivityUnit, RatioUnit, UnitlessUnit {
}