// src/main/java/com/nexsys/util/UnitSystem.java
package com.nexsys.util;

import com.nexsys.util.units.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a system of measurement units (metric or US customary).
 */
public final class UnitSystem {

    private static final UnitSystem METRIC = createMetricSystem();
    private static final UnitSystem US_CUSTOMARY = createUsCustomarySystem();

    private final String name;
    private final Map<String, Unit> defaultUnits;
    private final Map<DeviceClassUnitPair, Unit> conversions;

    private UnitSystem(String name, Map<String, Unit> defaultUnits, Map<DeviceClassUnitPair, Unit> conversions) {
        this.name = name;
        this.defaultUnits = Map.copyOf(defaultUnits);
        this.conversions = Map.copyOf(conversions);
    }

    /**
     * Get the metric unit system.
     */
    public static UnitSystem metric() {
        return METRIC;
    }

    /**
     * Get the US customary unit system.
     */
    public static UnitSystem usCustomary() {
        return US_CUSTOMARY;
    }

    /**
     * Convert a value to this unit system's preferred unit.
     *
     * @param value the value to convert
     * @param deviceClass the device class (e.g., "pressure", "temperature")
     * @param original the original unit
     * @return a Quantity in the preferred unit for this system
     */
    public Quantity convert(Number value, String deviceClass, Unit original) {
        Unit targetUnit = conversions.get(new DeviceClassUnitPair(deviceClass, original));
        if (targetUnit == null) {
            // No conversion specified, keep original
            return new Quantity(value.doubleValue(), original);
        }

        double convertedValue = UnitConverter.convert(value.doubleValue(), original, targetUnit);
        return new Quantity(convertedValue, targetUnit);
    }

    /**
     * Get the default units for this system as a map.
     *
     * @return map of unit type to default unit
     */
    public Map<String, Unit> asMap() {
        return Map.copyOf(defaultUnits);
    }

    public String getName() {
        return name;
    }

    private static UnitSystem createMetricSystem() {
        Map<String, Unit> defaults = Map.of(
                "LENGTH", LengthUnit.KILOMETRE,
                "AREA", AreaUnit.SQUARE_METRE,
                "MASS", MassUnit.GRAM,
                "PRESSURE", PressureUnit.PASCAL,
                "TEMPERATURE", TemperatureUnit.CELSIUS,
                "VOLUME", VolumeUnit.LITER,
                "WIND_SPEED", SpeedUnit.METRE_PER_SECOND,
                "ACCUMULATED_PRECIPITATION", LengthUnit.MILLIMETER
        );

        Map<DeviceClassUnitPair, Unit> conversions = new HashMap<>();

        // Force atmospheric pressures to hPa
        for (PressureUnit unit : PressureUnit.values()) {
            if (unit != PressureUnit.HECTOPASCAL) {
                conversions.put(new DeviceClassUnitPair("atmospheric_pressure", unit), PressureUnit.HECTOPASCAL);
            }
        }

        // Convert non-metric area
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_INCH), AreaUnit.SQUARE_CENTIMETER);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_FOOT), AreaUnit.SQUARE_METRE);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_MILE), AreaUnit.SQUARE_KILOMETRE);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_YARD), AreaUnit.SQUARE_METRE);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.ACRE), AreaUnit.HECTARE);

        // Convert non-metric distances
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.FOOT), LengthUnit.METRE);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.INCH), LengthUnit.MILLIMETER);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.MILE), LengthUnit.KILOMETRE);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.NAUTICAL_MILE), LengthUnit.KILOMETRE);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.YARD), LengthUnit.METRE);

        // Convert non-metric volumes of gas meters
        conversions.put(new DeviceClassUnitPair("gas", VolumeUnit.CENTUM_CUBIC_FOOT), VolumeUnit.CUBIC_METRE);
        conversions.put(new DeviceClassUnitPair("gas", VolumeUnit.CUBIC_FOOT), VolumeUnit.CUBIC_METRE);

        // Convert non-metric precipitation
        conversions.put(new DeviceClassUnitPair("precipitation", LengthUnit.INCH), LengthUnit.MILLIMETER);

        // Convert non-metric precipitation intensity
        conversions.put(new DeviceClassUnitPair("precipitation_intensity", SpeedUnit.INCH_PER_DAY), SpeedUnit.MILLIMETER_PER_DAY);
        conversions.put(new DeviceClassUnitPair("precipitation_intensity", SpeedUnit.INCH_PER_HOUR), SpeedUnit.MILLIMETER_PER_HOUR);

        // Convert non-metric pressure
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.PSI), PressureUnit.KILOPASCAL);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.INCH_HG), PressureUnit.HECTOPASCAL);

        // Convert non-metric speeds except knots to km/h
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.FOOT_PER_SECOND), SpeedUnit.KILOMETRE_PER_HOUR);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.INCH_PER_SECOND), SpeedUnit.MILLIMETER_PER_SECOND);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.MILE_PER_HOUR), SpeedUnit.KILOMETRE_PER_HOUR);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.INCH_PER_DAY), SpeedUnit.MILLIMETER_PER_DAY);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.INCH_PER_HOUR), SpeedUnit.MILLIMETER_PER_HOUR);

        // Convert non-metric volumes
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.CENTUM_CUBIC_FOOT), VolumeUnit.CUBIC_METRE);
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.CUBIC_FOOT), VolumeUnit.CUBIC_METRE);
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.FLUID_OUNCE), VolumeUnit.MILLILITER);
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.GALLON), VolumeUnit.LITER);

        // Convert non-metric volumes of water meters
        conversions.put(new DeviceClassUnitPair("water", VolumeUnit.CENTUM_CUBIC_FOOT), VolumeUnit.CUBIC_METRE);
        conversions.put(new DeviceClassUnitPair("water", VolumeUnit.CUBIC_FOOT), VolumeUnit.CUBIC_METRE);
        conversions.put(new DeviceClassUnitPair("water", VolumeUnit.GALLON), VolumeUnit.LITER);

        // Convert wind speeds except knots to km/h
        for (SpeedUnit unit : SpeedUnit.values()) {
            if (unit != SpeedUnit.KILOMETRE_PER_HOUR && unit != SpeedUnit.KNOT) {
                conversions.put(new DeviceClassUnitPair("wind_speed", unit), SpeedUnit.KILOMETRE_PER_HOUR);
            }
        }

        return new UnitSystem("metric", defaults, conversions);
    }

    private static UnitSystem createUsCustomarySystem() {
        Map<String, Unit> defaults = Map.of(
                "LENGTH", LengthUnit.MILE,
                "AREA", AreaUnit.SQUARE_FOOT,
                "MASS", MassUnit.POUND,
                "PRESSURE", PressureUnit.PSI,
                "TEMPERATURE", TemperatureUnit.FAHRENHEIT,
                "VOLUME", VolumeUnit.GALLON,
                "WIND_SPEED", SpeedUnit.MILE_PER_HOUR,
                "ACCUMULATED_PRECIPITATION", LengthUnit.INCH
        );

        Map<DeviceClassUnitPair, Unit> conversions = new HashMap<>();

        // Force atmospheric pressures to inHg
        for (PressureUnit unit : PressureUnit.values()) {
            if (unit != PressureUnit.INCH_HG) {
                conversions.put(new DeviceClassUnitPair("atmospheric_pressure", unit), PressureUnit.INCH_HG);
            }
        }

        // Convert non-USCS areas
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_METRE), AreaUnit.SQUARE_FOOT);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_CENTIMETER), AreaUnit.SQUARE_INCH);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_MILLIMETER), AreaUnit.SQUARE_INCH);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.SQUARE_KILOMETRE), AreaUnit.SQUARE_MILE);
        conversions.put(new DeviceClassUnitPair("area", AreaUnit.HECTARE), AreaUnit.ACRE);

        // Convert non-USCS distances
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.CENTIMETER), LengthUnit.INCH);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.KILOMETRE), LengthUnit.MILE);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.METRE), LengthUnit.FOOT);
        conversions.put(new DeviceClassUnitPair("distance", LengthUnit.MILLIMETER), LengthUnit.INCH);

        // Convert non-USCS volumes of gas meters
        conversions.put(new DeviceClassUnitPair("gas", VolumeUnit.CUBIC_METRE), VolumeUnit.CUBIC_FOOT);
        conversions.put(new DeviceClassUnitPair("gas", VolumeUnit.LITER), VolumeUnit.CUBIC_FOOT);

        // Convert non-USCS precipitation
        conversions.put(new DeviceClassUnitPair("precipitation", LengthUnit.CENTIMETER), LengthUnit.INCH);
        conversions.put(new DeviceClassUnitPair("precipitation", LengthUnit.MILLIMETER), LengthUnit.INCH);

        // Convert non-USCS precipitation intensity
        conversions.put(new DeviceClassUnitPair("precipitation_intensity", SpeedUnit.MILLIMETER_PER_DAY), SpeedUnit.INCH_PER_DAY);
        conversions.put(new DeviceClassUnitPair("precipitation_intensity", SpeedUnit.MILLIMETER_PER_HOUR), SpeedUnit.INCH_PER_HOUR);

        // Convert non-USCS pressure
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.MILLIBAR), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.CENTIBAR), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.BAR), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.PASCAL), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.HECTOPASCAL), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.KILOPASCAL), PressureUnit.PSI);
        conversions.put(new DeviceClassUnitPair("pressure", PressureUnit.MILLIMETER_HG), PressureUnit.INCH_HG);

        // Convert non-USCS speeds, except knots, to mph
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.METRE_PER_SECOND), SpeedUnit.MILE_PER_HOUR);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.MILLIMETER_PER_SECOND), SpeedUnit.INCH_PER_SECOND);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.KILOMETRE_PER_HOUR), SpeedUnit.MILE_PER_HOUR);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.MILLIMETER_PER_DAY), SpeedUnit.INCH_PER_DAY);
        conversions.put(new DeviceClassUnitPair("speed", SpeedUnit.MILLIMETER_PER_HOUR), SpeedUnit.INCH_PER_HOUR);

        // Convert non-USCS volumes
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.CUBIC_METRE), VolumeUnit.CUBIC_FOOT);
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.LITER), VolumeUnit.GALLON);
        conversions.put(new DeviceClassUnitPair("volume", VolumeUnit.MILLILITER), VolumeUnit.FLUID_OUNCE);

        // Convert non-USCS volumes of water meters
        conversions.put(new DeviceClassUnitPair("water", VolumeUnit.CUBIC_METRE), VolumeUnit.CUBIC_FOOT);
        conversions.put(new DeviceClassUnitPair("water", VolumeUnit.LITER), VolumeUnit.GALLON);

        // Convert wind speeds except knots to mph
        for (SpeedUnit unit : SpeedUnit.values()) {
            if (unit != SpeedUnit.KNOT && unit != SpeedUnit.MILE_PER_HOUR) {
                conversions.put(new DeviceClassUnitPair("wind_speed", unit), SpeedUnit.MILE_PER_HOUR);
            }
        }

        return new UnitSystem("us_customary", defaults, conversions);
    }

    private record DeviceClassUnitPair(String deviceClass, Unit unit) {}
}