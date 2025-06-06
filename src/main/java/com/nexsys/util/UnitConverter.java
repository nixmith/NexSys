// src/main/java/com/nexsys/util/UnitConverter.java
package com.nexsys.util;

import com.nexsys.util.units.*;

/**
 * Utility class for converting between units.
 */
// TODO add dependency tech.units:indriya:3.2 to build.gradle for future JSR-385 swap
public final class UnitConverter {

    private UnitConverter() {} // Prevent instantiation

    /**
     * Convert a value from one unit to another.
     *
     * @param value the value to convert
     * @param from the source unit
     * @param to the target unit
     * @return the converted value
     * @throws IllegalArgumentException if units are incompatible
     */
    public static double convert(double value, Unit from, Unit to) {
        if (from.equals(to)) {
            return value;
        }

        if (!from.getClass().equals(to.getClass())) {
            throw new IllegalArgumentException("Cannot convert between different unit types");
        }

        return switch (from) {
            case TemperatureUnit fromTemp -> convertTemperature(value, fromTemp, (TemperatureUnit) to);
            case SpeedUnit fromSpeed -> convertSpeed(value, fromSpeed, (SpeedUnit) to);
            case EnergyDistanceUnit fromED -> convertEnergyDistance(value, fromED, (EnergyDistanceUnit) to);
            case LengthUnit fromLength -> {
                double metres = value * fromLength.toMetre();
                yield metres / ((LengthUnit) to).toMetre();
            }
            case AreaUnit fromArea -> {
                double squareMetres = value * fromArea.toSquareMetre();
                yield squareMetres / ((AreaUnit) to).toSquareMetre();
            }
            case MassUnit fromMass -> {
                double grams = value * fromMass.toGram();
                yield grams / ((MassUnit) to).toGram();
            }
            case VolumeUnit fromVolume -> {
                double cubicMetres = value * fromVolume.toCubicMetre();
                yield cubicMetres / ((VolumeUnit) to).toCubicMetre();
            }
            case PressureUnit fromPressure -> {
                double pascals = value * fromPressure.toPascal();
                yield pascals / ((PressureUnit) to).toPascal();
            }
            case EnergyUnit fromEnergy -> {
                double wattHours = value * fromEnergy.toWattHour();
                yield wattHours / ((EnergyUnit) to).toWattHour();
            }
            case PowerUnit fromPower -> {
                double watts = value * fromPower.toWatt();
                yield watts / ((PowerUnit) to).toWatt();
            }
            case DataRateUnit fromDataRate -> {
                double bitsPerSecond = value * fromDataRate.toBitPerSecond();
                yield bitsPerSecond / ((DataRateUnit) to).toBitPerSecond();
            }
            case InformationUnit fromInfo -> {
                double bits = value * fromInfo.toBit();
                yield bits / ((InformationUnit) to).toBit();
            }
            case ElectricCurrentUnit fromCurrent -> {
                double amperes = value * fromCurrent.toAmpere();
                yield amperes / ((ElectricCurrentUnit) to).toAmpere();
            }
            case ElectricPotentialUnit fromPotential -> {
                double volts = value * fromPotential.toVolt();
                yield volts / ((ElectricPotentialUnit) to).toVolt();
            }
            case VolumeFlowRateUnit fromFlow -> {
                double cubicMetresPerHour = value * fromFlow.toCubicMetrePerHour();
                yield cubicMetresPerHour / ((VolumeFlowRateUnit) to).toCubicMetrePerHour();
            }
            case TimeUnit fromTime -> {
                double seconds = value * fromTime.toSecond();
                yield seconds / ((TimeUnit) to).toSecond();
            }
            case ReactiveEnergyUnit fromReactive -> {
                double varh = value * fromReactive.toVarh();
                yield varh / ((ReactiveEnergyUnit) to).toVarh();
            }
            case BloodGlucoseUnit fromGlucose -> {
                double mmolPerL = value * fromGlucose.toMmolPerL();
                yield mmolPerL / ((BloodGlucoseUnit) to).toMmolPerL();
            }
            case ConductivityUnit fromConductivity -> {
                double microsiemenPerCm = value * fromConductivity.toMicrosiemenPerCm();
                yield microsiemenPerCm / ((ConductivityUnit) to).toMicrosiemenPerCm();
            }
            case RatioUnit fromRatio -> {
                double unitless = value * fromRatio.toUnitless();
                yield unitless / ((RatioUnit) to).toUnitless();
            }
            case UnitlessUnit fromUnitless -> {
                double unitless = value * fromUnitless.toUnitless();
                yield unitless / ((UnitlessUnit) to).toUnitless();
            }
        };
    }

    /**
     * Get the floored base-10 logarithm of the conversion ratio.
     *
     * @param from the source unit
     * @param to the target unit
     * @return floor(log10(ratio))
     */
    public static double floorLog10Ratio(Unit from, Unit to) {
        if (!from.getClass().equals(to.getClass())) {
            throw new IllegalArgumentException("Cannot compare different unit types");
        }

        double ratio = getRatio(from, to);
        return Math.floor(Math.max(0, Math.log10(ratio)));
    }

    private static double getRatio(Unit from, Unit to) {
        return switch (from) {
            case LengthUnit fromLength -> fromLength.toMetre() / ((LengthUnit) to).toMetre();
            case AreaUnit fromArea -> fromArea.toSquareMetre() / ((AreaUnit) to).toSquareMetre();
            case MassUnit fromMass -> fromMass.toGram() / ((MassUnit) to).toGram();
            case VolumeUnit fromVolume -> fromVolume.toCubicMetre() / ((VolumeUnit) to).toCubicMetre();
            case PressureUnit fromPressure -> fromPressure.toPascal() / ((PressureUnit) to).toPascal();
            case EnergyUnit fromEnergy -> fromEnergy.toWattHour() / ((EnergyUnit) to).toWattHour();
            case PowerUnit fromPower -> fromPower.toWatt() / ((PowerUnit) to).toWatt();
            case DataRateUnit fromDataRate -> fromDataRate.toBitPerSecond() / ((DataRateUnit) to).toBitPerSecond();
            case InformationUnit fromInfo -> fromInfo.toBit() / ((InformationUnit) to).toBit();
            case ElectricCurrentUnit fromCurrent -> fromCurrent.toAmpere() / ((ElectricCurrentUnit) to).toAmpere();
            case ElectricPotentialUnit fromPotential -> fromPotential.toVolt() / ((ElectricPotentialUnit) to).toVolt();
            case VolumeFlowRateUnit fromFlow -> fromFlow.toCubicMetrePerHour() / ((VolumeFlowRateUnit) to).toCubicMetrePerHour();
            case TimeUnit fromTime -> fromTime.toSecond() / ((TimeUnit) to).toSecond();
            case ReactiveEnergyUnit fromReactive -> fromReactive.toVarh() / ((ReactiveEnergyUnit) to).toVarh();
            case BloodGlucoseUnit fromGlucose -> fromGlucose.toMmolPerL() / ((BloodGlucoseUnit) to).toMmolPerL();
            case ConductivityUnit fromConductivity -> fromConductivity.toMicrosiemenPerCm() / ((ConductivityUnit) to).toMicrosiemenPerCm();
            case RatioUnit fromRatio -> fromRatio.toUnitless() / ((RatioUnit) to).toUnitless();
            case UnitlessUnit fromUnitless -> fromUnitless.toUnitless() / ((UnitlessUnit) to).toUnitless();
            default -> 1.0;
        };
    }

    private static double convertTemperature(double value, TemperatureUnit from, TemperatureUnit to) {
        // Convert to Celsius first
        double celsius = from.multiplier() * value + from.offset();

        // Convert from Celsius to target
        return (celsius - to.offset()) / to.multiplier();
    }

    private static double convertSpeed(double value, SpeedUnit from, SpeedUnit to) {
        if (from == SpeedUnit.BEAUFORT) {
            // Beaufort to m/s using HA formula
            double ms = 0.836 * Math.pow(value, 1.5);
            return ms * to.toMetrePerSecond();
        } else if (to == SpeedUnit.BEAUFORT) {
            // Convert to m/s first
            double ms = value * from.toMetrePerSecond();
            // m/s to Beaufort using HA formula
            return Math.round(Math.pow(ms / 0.836, 2.0/3.0));
        } else {
            // Normal linear conversion
            double ms = value * from.toMetrePerSecond();
            return ms / to.toMetrePerSecond();
        }
    }

    private static double convertEnergyDistance(double value, EnergyDistanceUnit from, EnergyDistanceUnit to) {
        boolean fromInverse = from.isInverse();
        boolean toInverse = to.isInverse();

        double fromRatio = from.toKwhPer100Km();
        double toRatio = to.toKwhPer100Km();

        if (fromInverse != toInverse) {
            // One is inverse, the other is not
            return toRatio / (value / fromRatio);
        } else {
            // Both same type
            return (value / fromRatio) * toRatio;
        }
    }
}