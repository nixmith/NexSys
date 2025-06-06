// src/test/java/com/nexsys/util/UnitSystemTest.java
package com.nexsys.util;

import com.nexsys.util.units.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UnitSystemTest {

    @Test
    void testMetricSystemPressureConversion() {
        UnitSystem metric = UnitSystem.metric();

        // PSI should convert to kPa in metric
        Quantity psi10 = new Quantity(10, PressureUnit.PSI);
        Quantity converted = metric.convert(psi10.value(), "pressure", psi10.unit());

        assertThat(converted.unit()).isEqualTo(PressureUnit.KILOPASCAL);
        assertThat(converted.value()).isCloseTo(68.95, within(0.01));
    }

    @Test
    void testMetricSystemDefaults() {
        UnitSystem metric = UnitSystem.metric();
        var defaults = metric.asMap();

        assertThat(defaults.get("TEMPERATURE")).isEqualTo(TemperatureUnit.CELSIUS);
        assertThat(defaults.get("LENGTH")).isEqualTo(LengthUnit.KILOMETRE);
        assertThat(defaults.get("PRESSURE")).isEqualTo(PressureUnit.PASCAL);
    }

    @Test
    void testUsCustomarySystemConversion() {
        UnitSystem usCustomary = UnitSystem.usCustomary();

        // Celsius should convert to Fahrenheit
        Quantity celsius = new Quantity(20, TemperatureUnit.CELSIUS);
        Quantity converted = usCustomary.convert(celsius.value(), "temperature", celsius.unit());

        // US customary doesn't have temperature conversions, so it should stay the same
        assertThat(converted.unit()).isEqualTo(TemperatureUnit.CELSIUS);

        // Test pressure: hPa to inHg for atmospheric
        Quantity hpa = new Quantity(1013.25, PressureUnit.HECTOPASCAL);
        converted = usCustomary.convert(hpa.value(), "atmospheric_pressure", hpa.unit());

        assertThat(converted.unit()).isEqualTo(PressureUnit.INCH_HG);
        assertThat(converted.value()).isCloseTo(29.92, within(0.01));
    }

    @Test
    void testUsCustomaryDefaults() {
        UnitSystem usCustomary = UnitSystem.usCustomary();
        var defaults = usCustomary.asMap();

        assertThat(defaults.get("TEMPERATURE")).isEqualTo(TemperatureUnit.FAHRENHEIT);
        assertThat(defaults.get("LENGTH")).isEqualTo(LengthUnit.MILE);
        assertThat(defaults.get("PRESSURE")).isEqualTo(PressureUnit.PSI);
    }

    @Test
    void testNoConversionForUnknownDeviceClass() {
        UnitSystem metric = UnitSystem.metric();

        // Unknown device class should keep original unit
        Quantity psi = new Quantity(10, PressureUnit.PSI);
        Quantity converted = metric.convert(psi.value(), "unknown_device", psi.unit());

        assertThat(converted.unit()).isEqualTo(PressureUnit.PSI);
        assertThat(converted.value()).isEqualTo(10);
    }
}