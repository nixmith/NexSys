// src/test/java/com/nexsys/util/ColorUtilsTest.java
package com.nexsys.util;

import com.nexsys.util.color.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void testRgbToXyAndBack() {
        Color original = new Color(1.0, 0.0, 0.0); // Pure red
        XYPoint xy = ColorUtils.rgbToXy(original, null);
        Color backToRgb = ColorUtils.xyToRgb(xy.x(), xy.y(), 255, null);

        // Should be close but not exact due to color space conversion
        assertThat(backToRgb.r()).isCloseTo(1.0, within(0.02));
        assertThat(backToRgb.g()).isCloseTo(0.0, within(0.02));
        assertThat(backToRgb.b()).isCloseTo(0.0, within(0.02));
    }

    @Test
    void testTemperatureToRgb() {
        // 6500K should be close to white
        Color white = ColorUtils.temperatureToRgb(6500);
        assertThat(white.toHex()).isEqualTo("ffffff");

        // 2700K should be warm white
        Color warm = ColorUtils.temperatureToRgb(2700);
        assertThat(warm.r()).isGreaterThan(warm.b()); // More red than blue

        // 10000K should be cool white
        Color cool = ColorUtils.temperatureToRgb(10000);
        assertThat(cool.b()).isGreaterThan(cool.r()); // More blue than red
    }

    @Test
    void testRgbToHsvAndBack() {
        Color original = new Color(0.5, 0.3, 0.8);
        double[] hsv = ColorUtils.rgbToHsv(original);
        Color backToRgb = ColorUtils.hsvToRgb(hsv[0], hsv[1], hsv[2]);

        assertThat(backToRgb.r()).isCloseTo(original.r(), within(0.001));
        assertThat(backToRgb.g()).isCloseTo(original.g(), within(0.001));
        assertThat(backToRgb.b()).isCloseTo(original.b(), within(0.001));
    }

    @Test
    void testCssColorNames() {
        assertThat(ColorUtils.nameToColor("red")).isEqualTo(new Color(1, 0, 0));
        assertThat(ColorUtils.nameToColor("GREEN")).isEqualTo(new Color(0, 128/255.0, 0));
        assertThat(ColorUtils.nameToColor("dark sea green").toHex()).isEqualTo("8fbc8f");

        assertThatThrownBy(() -> ColorUtils.nameToColor("notacolor"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGamutValidation() {
        // Valid gamut
        Gamut valid = new Gamut(
                new XYPoint(0.7, 0.3),
                new XYPoint(0.2, 0.7),
                new XYPoint(0.15, 0.05)
        );
        assertThat(valid.isValid()).isTrue();

        // Collinear points
        Gamut collinear = new Gamut(
                new XYPoint(0.0, 0.0),
                new XYPoint(0.5, 0.5),
                new XYPoint(1.0, 1.0)
        );
        assertThat(collinear.isValid()).isFalse();

        // Out of bounds
        Gamut outOfBounds = new Gamut(
                new XYPoint(1.1, 0.3),
                new XYPoint(0.2, 0.7),
                new XYPoint(0.15, 0.05)
        );
        assertThat(outOfBounds.isValid()).isFalse();
    }

    @Test
    void testColorHexConversion() {
        Color color = new Color(1.0, 0.5, 0.0);
        String hex = color.toHex();
        assertThat(hex).isEqualTo("ff8000");

        Color fromHex = Color.fromHex("#FF8000");
        assertThat(fromHex.r()).isCloseTo(1.0, within(0.01));
        assertThat(fromHex.g()).isCloseTo(0.5, within(0.01));
        assertThat(fromHex.b()).isCloseTo(0.0, within(0.01));
    }
}