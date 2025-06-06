// src/main/java/com/nexsys/units/DataRateUnit.java
package com.nexsys.util.units;

/**
 * Units of data rate measurement.
 */
public enum DataRateUnit implements Unit {
    BIT_PER_SECOND(1.0),
    KILOBIT_PER_SECOND(1000.0),
    MEGABIT_PER_SECOND(1000000.0),
    GIGABIT_PER_SECOND(1000000000.0),
    BYTE_PER_SECOND(8.0),
    KILOBYTE_PER_SECOND(8000.0),
    MEGABYTE_PER_SECOND(8000000.0),
    GIGABYTE_PER_SECOND(8000000000.0),
    KIBIBYTE_PER_SECOND(8192.0),
    MEBIBYTE_PER_SECOND(8388608.0),
    GIBIBYTE_PER_SECOND(8589934592.0);

    private final double toBitPerSecond;

    DataRateUnit(double toBitPerSecond) {
        this.toBitPerSecond = toBitPerSecond;
    }

    public double toBitPerSecond() {
        return toBitPerSecond;
    }
}