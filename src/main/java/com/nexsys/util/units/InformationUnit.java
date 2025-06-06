// src/main/java/com/nexsys/units/InformationUnit.java
package com.nexsys.util.units;

/**
 * Units of information measurement.
 */
public enum InformationUnit implements Unit {
    BIT(1.0),
    KILOBIT(1000.0),
    MEGABIT(1000000.0),
    GIGABIT(1000000000.0),
    BYTE(8.0),
    KILOBYTE(8000.0),
    MEGABYTE(8000000.0),
    GIGABYTE(8000000000.0),
    TERABYTE(8000000000000.0),
    PETABYTE(8000000000000000.0),
    EXABYTE(8000000000000000000.0),
    ZETTABYTE(8e21),
    YOTTABYTE(8e24),
    KIBIBYTE(8192.0),
    MEBIBYTE(8388608.0),
    GIBIBYTE(8589934592.0),
    TEBIBYTE(8796093022208.0),
    PEBIBYTE(9007199254740992.0),
    EXBIBYTE(9.223372036854776E18),
    ZEBIBYTE(9.444732965739290E21),
    YOBIBYTE(9.671406556917033E24);

    private final double toBit;

    InformationUnit(double toBit) {
        this.toBit = toBit;
    }

    public double toBit() {
        return toBit;
    }
}