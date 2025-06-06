// src/main/java/com/nexsys/units/Quantity.java
package com.nexsys.util.units;

import com.nexsys.util.UnitConverter;

/**
 * Represents a value with its associated unit.
 */
public record Quantity(double value, Unit unit) {

    /**
     * Convert this quantity to a different unit.
     *
     * @param target the target unit
     * @return a new Quantity in the target unit
     * @throws IllegalArgumentException if units are incompatible
     */
    public Quantity convertTo(Unit target) {
        if (target.getClass() != unit.getClass()) {
            throw new IllegalArgumentException("Cannot convert " + unit.getClass().getSimpleName()
                    + " to " + target.getClass().getSimpleName());
        }

        double convertedValue = UnitConverter.convert(value, unit, target);
        return new Quantity(convertedValue, target);
    }

    @Override
    public String toString() {
        return switch (unit) {
            case TemperatureUnit t -> String.format("%.1f %s", value, getTemperatureSymbol(t));
            case SpeedUnit s when s == SpeedUnit.BEAUFORT -> String.format("%.0f Bft", value);
            case RatioUnit r when r == RatioUnit.PERCENT -> String.format("%.1f%%", value);
            case UnitlessUnit u when u == UnitlessUnit.PERCENT -> String.format("%.1f%%", value);
            default -> String.format("%.2f %s", value, getUnitSymbol(unit));
        };
    }

    private String getTemperatureSymbol(TemperatureUnit unit) {
        return switch (unit) {
            case CELSIUS -> "°C";
            case FAHRENHEIT -> "°F";
            case KELVIN -> "K";
        };
    }

    private String getUnitSymbol(Unit unit) {
        return switch (unit) {
            case LengthUnit l -> switch (l) {
                case MILLIMETER -> "mm";
                case CENTIMETER -> "cm";
                case METRE -> "m";
                case KILOMETRE -> "km";
                case INCH -> "in";
                case FOOT -> "ft";
                case YARD -> "yd";
                case MILE -> "mi";
                case NAUTICAL_MILE -> "nmi";
            };
            case AreaUnit a -> switch (a) {
                case SQUARE_MILLIMETER -> "mm²";
                case SQUARE_CENTIMETER -> "cm²";
                case SQUARE_METRE -> "m²";
                case SQUARE_KILOMETRE -> "km²";
                case SQUARE_INCH -> "in²";
                case SQUARE_FOOT -> "ft²";
                case SQUARE_YARD -> "yd²";
                case SQUARE_MILE -> "mi²";
                case ACRE -> "acre";
                case HECTARE -> "ha";
            };
            case MassUnit m -> switch (m) {
                case MICROGRAM -> "μg";
                case MILLIGRAM -> "mg";
                case GRAM -> "g";
                case KILOGRAM -> "kg";
                case OUNCE -> "oz";
                case POUND -> "lb";
                case STONE -> "st";
            };
            case VolumeUnit v -> switch (v) {
                case MILLILITER -> "mL";
                case LITER -> "L";
                case CUBIC_METRE -> "m³";
                case CUBIC_FOOT -> "ft³";
                case CENTUM_CUBIC_FOOT -> "ccf";
                case GALLON -> "gal";
                case FLUID_OUNCE -> "fl oz";
            };
            case PressureUnit p -> switch (p) {
                case PASCAL -> "Pa";
                case HECTOPASCAL -> "hPa";
                case KILOPASCAL -> "kPa";
                case BAR -> "bar";
                case CENTIBAR -> "cbar";
                case MILLIBAR -> "mbar";
                case INCH_HG -> "inHg";
                case PSI -> "psi";
                case MILLIMETER_HG -> "mmHg";
            };
            case PowerUnit p -> switch (p) {
                case MILLIWATT -> "mW";
                case WATT -> "W";
                case KILOWATT -> "kW";
                case MEGAWATT -> "MW";
                case GIGAWATT -> "GW";
                case TERAWATT -> "TW";
            };
            case SpeedUnit s -> switch (s) {
                case METRE_PER_SECOND -> "m/s";
                case MILLIMETER_PER_SECOND -> "mm/s";
                case INCH_PER_SECOND -> "in/s";
                case FOOT_PER_SECOND -> "ft/s";
                case KILOMETRE_PER_HOUR -> "km/h";
                case MILE_PER_HOUR -> "mph";
                case KNOT -> "kn";
                case BEAUFORT -> "Bft";
                case INCH_PER_DAY -> "in/d";
                case INCH_PER_HOUR -> "in/h";
                case MILLIMETER_PER_DAY -> "mm/d";
                case MILLIMETER_PER_HOUR -> "mm/h";
            };
            default -> unit.toString();
        };
    }
}