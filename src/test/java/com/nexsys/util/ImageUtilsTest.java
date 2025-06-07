package com.nexsys.util;

import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {

    @Test
    void testDrawBoundingBox_VerifyPixelColor() {
        // Create a 200x100 image
        BufferedImage img = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);

        // Fill with white background
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 100);
        g.dispose();

        // Define bounding box: yMin=0.1, xMin=0.2, yMax=0.5, xMax=0.9
        Rectangle2D.Float box = new Rectangle2D.Float(0.1f, 0.2f, 0.5f, 0.9f);

        // Draw red box
        ImageUtils.drawBoundingBox(img, box, "Test", Color.RED);

        // Expected pixel coordinates:
        // left = 0.2 * 200 = 40
        // top = 0.1 * 100 = 10
        // So pixel at (40, 10) should be red (part of the line)

        int pixelColor = img.getRGB(40, 10);
        Color actualColor = new Color(pixelColor);

        // Check if pixel is red (allowing some tolerance due to antialiasing)
        assertEquals(255, actualColor.getRed());
        assertEquals(0, actualColor.getGreen());
        assertEquals(0, actualColor.getBlue());
    }

    @Test
    void testDrawBoundingBox_NoText() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Rectangle2D.Float box = new Rectangle2D.Float(0.2f, 0.2f, 0.8f, 0.8f);

        // Should not throw exception
        assertDoesNotThrow(() -> ImageUtils.drawBoundingBox(img, box, null, Color.BLUE));
        assertDoesNotThrow(() -> ImageUtils.drawBoundingBox(img, box, "", Color.BLUE));
    }

    @Test
    void testDrawBoundingBox_FullImage() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Rectangle2D.Float box = new Rectangle2D.Float(0.0f, 0.0f, 1.0f, 1.0f);

        assertDoesNotThrow(() -> ImageUtils.drawBoundingBox(img, box, "Full", Color.GREEN));
    }

    @Test
    void testDrawBoundingBox_NullArguments() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Rectangle2D.Float box = new Rectangle2D.Float(0.5f, 0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class,
                () -> ImageUtils.drawBoundingBox(null, box, "Test", Color.RED));
        assertThrows(IllegalArgumentException.class,
                () -> ImageUtils.drawBoundingBox(img, null, "Test", Color.RED));
        assertThrows(IllegalArgumentException.class,
                () -> ImageUtils.drawBoundingBox(img, box, "Test", null));
    }
}