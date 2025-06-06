// src/main/java/com/nexsys/util/Gamut.java
package com.nexsys.util.color;

/**
 * Represents the color gamut of a light source.
 */
public record Gamut(XYPoint red, XYPoint green, XYPoint blue) {

    /**
     * Check if this gamut is valid.
     *
     * @return true if all points are within [0,1] and not collinear
     */
    public boolean isValid() {
        // Check if all coordinates are within [0,1]
        if (!isPointValid(red) || !isPointValid(green) || !isPointValid(blue)) {
            return false;
        }

        // Check if points are not collinear using cross product
        XYPoint v1 = new XYPoint(green.x() - red.x(), green.y() - red.y());
        XYPoint v2 = new XYPoint(blue.x() - red.x(), blue.y() - red.y());
        double crossProduct = v1.x() * v2.y() - v1.y() * v2.x();

        return Math.abs(crossProduct) > 0.0001;
    }

    private boolean isPointValid(XYPoint point) {
        return point.x() >= 0 && point.x() <= 1 && point.y() >= 0 && point.y() <= 1;
    }
}