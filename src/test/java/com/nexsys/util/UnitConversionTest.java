// src/test/java/com/nexsys/util/UnitConversionTest.java
package com.nexsys.util;

import com.nexsys.util.units.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UnitConversionTest {

    @Test
    void testLengthConversion() {
        // Metres to miles
        double miles = UnitConverter.convert(1609.344, LengthUnit.METRE, LengthUnit.MILE);
        assertThat(miles).isCloseTo(1.0, within(0.001));

        // Miles to metres
        double metres = UnitConverter.convert(1.0, LengthUnit.MILE, LengthUnit.METRE);
        assertThat(metres).isCloseTo(1609.344, within(0.001));

        // Round trip
        double original = 42.5;
        double converted = UnitConverter.convert(original, LengthUnit.KILOMETRE, LengthUnit.MILE);
        double backAgain = UnitConverter.convert(converted, LengthUnit.MILE, LengthUnit.KILOMETRE);
        assertThat(backAgain).isCloseTo(original, within(0.001));
    }

    @Test
    void testTemperatureConversion() {
        // Celsius to Fahrenheit
        assertThat(UnitConverter.convert(0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT))
                .isCloseTo(32, within(0.01));
        assertThat(UnitConverter.convert(100, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT))
                .isCloseTo(212, within(0.01));

        // Fahrenheit to Celsius
        assertThat(UnitConverter.convert(32, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS))
                .isCloseTo(0, within(0.01));
        assertThat(UnitConverter.convert(212, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS))
                .isCloseTo(100, within(0.01));

        // Celsius to Kelvin
        assertThat(UnitConverter.convert(0, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN))
                .isCloseTo(273.15, within(0.01));

        // Kelvin to Celsius
        assertThat(UnitConverter.convert(273.15, TemperatureUnit.KELVIN, TemperatureUnit.CELSIUS))
                .isCloseTo(0, within(0.01));

        // Round trip F -> C -> K -> F
        double fahrenheit = 75.0;
        double celsius = UnitConverter.convert(fahrenheit, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS);
        double kelvin = UnitConverter.convert(celsius, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN);
        double backToF = UnitConverter.convert(kelvin, TemperatureUnit.KELVIN, TemperatureUnit.FAHRENHEIT);
        assertThat(backToF).isCloseTo(fahrenheit, within(0.01));
    }

    @Test
    void testPressureConversion() {
        // PSI to kPa
        double kpa = UnitConverter.convert(10, PressureUnit.PSI, PressureUnit.KILOPASCAL);
        assertThat(kpa).isCloseTo(68.9476, within(0.01));

        // kPa to PSI
        double psi = UnitConverter.convert(100, PressureUnit.KILOPASCAL, PressureUnit.PSI);
        assertThat(psi).isCloseTo(14.5038, within(0.01));
    }

    @Test
    void testSpeedConversion() {
        // Test Beaufort scale
        double beaufort5 = UnitConverter.convert(5, SpeedUnit.BEAUFORT, SpeedUnit.METRE_PER_SECOND);
        assertThat(beaufort5).isCloseTo(9.35, within(0.1));

        // Convert back
        double backToBeaufort = UnitConverter.convert(9.35, SpeedUnit.METRE_PER_SECOND, SpeedUnit.BEAUFORT);
        assertThat(backToBeaufort).isCloseTo(5, within(0.5));
    }

    @Test
    void testIncompatibleUnits() {
        assertThatThrownBy(() ->
                UnitConverter.convert(1.0, LengthUnit.METRE, TemperatureUnit.CELSIUS)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFloorLog10Ratio() {
        // km to m: ratio = 1000, log10 = 3
        assertThat(UnitConverter.floorLog10Ratio(LengthUnit.KILOMETRE, LengthUnit.METRE))
                .isEqualTo(3.0);

        // m to km: ratio = 0.001, log10 should be 0 (clamped)
        assertThat(UnitConverter.floorLog10Ratio(LengthUnit.METRE, LengthUnit.KILOMETRE))
                .isEqualTo(0.0);
    }
}