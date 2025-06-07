package com.nexsys.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Utility methods for image manipulation and processing.
 */
public final class ImageUtils {

    private ImageUtils() {
        // Utility class
    }

    /**
     * Read an image from a file path.
     *
     * @param path Path to the image file
     * @return BufferedImage or null if unable to read
     */
    public static BufferedImage readImage(Path path) {
        if (path == null || !Files.exists(path)) {
            return null;
        }

        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read an image from an input stream.
     *
     * @param stream Input stream containing image data
     * @return BufferedImage or null if unable to read
     */
    public static BufferedImage readImage(InputStream stream) {
        if (stream == null) {
            return null;
        }

        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read an image from a byte array.
     *
     * @param bytes Image data as byte array
     * @return BufferedImage or null if unable to read
     */
    public static BufferedImage readImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return readImage(new ByteArrayInputStream(bytes));
    }

    /**
     * Write an image to a file path.
     *
     * @param image  Image to write
     * @param format Image format (e.g., "png", "jpg", "jpeg")
     * @param path   Path to write the image to
     * @return true if successful, false otherwise
     */
    public static boolean writeImage(BufferedImage image, String format, Path path) {
        if (image == null || format == null || path == null) {
            return false;
        }

        try {
            Files.createDirectories(path.getParent());
            return ImageIO.write(image, format, path.toFile());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Write an image to an output stream.
     *
     * @param image  Image to write
     * @param format Image format (e.g., "png", "jpg", "jpeg")
     * @param stream Output stream to write to
     * @return true if successful, false otherwise
     */
    public static boolean writeImage(BufferedImage image, String format, OutputStream stream) {
        if (image == null || format == null || stream == null) {
            return false;
        }

        try {
            return ImageIO.write(image, format, stream);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Convert an image to a byte array.
     *
     * @param image  Image to convert
     * @param format Image format (e.g., "png", "jpg", "jpeg")
     * @return Byte array or null on error
     */
    public static byte[] toByteArray(BufferedImage image, String format) {
        if (image == null || format == null) {
            return null;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (ImageIO.write(image, format, baos)) {
                return baos.toByteArray();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Convert an image to a Base64 encoded string.
     *
     * @param image  Image to convert
     * @param format Image format (e.g., "png", "jpg", "jpeg")
     * @return Base64 encoded string or null on error
     */
    public static String toBase64(BufferedImage image, String format) {
        byte[] bytes = toByteArray(image, format);
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    /**
     * Read an image from a Base64 encoded string.
     *
     * @param base64 Base64 encoded image data
     * @return BufferedImage or null if unable to decode
     */
    public static BufferedImage fromBase64(String base64) {
        if (base64 == null || base64.isEmpty()) {
            return null;
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return readImage(bytes);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Resize an image to the specified dimensions.
     *
     * @param image     Source image
     * @param width     Target width
     * @param height    Target height
     * @param keepRatio If true, maintains aspect ratio (may result in smaller dimensions)
     * @return Resized image
     */
    public static BufferedImage resize(BufferedImage image, int width, int height, boolean keepRatio) {
        if (image == null || width <= 0 || height <= 0) {
            return null;
        }

        int targetWidth = width;
        int targetHeight = height;

        if (keepRatio) {
            double ratio = Math.min(
                    (double) width / image.getWidth(),
                    (double) height / image.getHeight()
            );
            targetWidth = (int) (image.getWidth() * ratio);
            targetHeight = (int) (image.getHeight() * ratio);
        }

        BufferedImage resized = new BufferedImage(targetWidth, targetHeight,
                image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType());

        Graphics2D g2d = resized.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        } finally {
            g2d.dispose();
        }

        return resized;
    }

    /**
     * Crop an image to the specified region.
     *
     * @param image  Source image
     * @param x      X coordinate of top-left corner
     * @param y      Y coordinate of top-left corner
     * @param width  Width of cropped region
     * @param height Height of cropped region
     * @return Cropped image or null if invalid parameters
     */
    public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
        if (image == null || x < 0 || y < 0 || width <= 0 || height <= 0) {
            return null;
        }

        // Ensure crop region is within image bounds
        if (x + width > image.getWidth() || y + height > image.getHeight()) {
            return null;
        }

        return image.getSubimage(x, y, width, height);
    }

    /**
     * Rotate an image by the specified angle.
     *
     * @param image Image to rotate
     * @param angle Rotation angle in degrees (positive = clockwise)
     * @return Rotated image
     */
    public static BufferedImage rotate(BufferedImage image, double angle) {
        if (image == null) {
            return null;
        }

        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));

        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = (int) (width * cos + height * sin);
        int newHeight = (int) (width * sin + height * cos);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight,
                image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType());

        Graphics2D g2d = rotated.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Translate to center, rotate, then translate back
            g2d.translate(newWidth / 2, newHeight / 2);
            g2d.rotate(radians);
            g2d.translate(-width / 2, -height / 2);
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }

        return rotated;
    }

    /**
     * Draw a bounding box on an image.
     * <p>
     * The bounding box is defined by normalized coordinates (0.0-1.0) in the format
     * (yMin, xMin, yMax, xMax) relative to the image dimensions. For example,
     * if an image is 100x200 pixels (width x height) and the box is (0.1, 0.2, 0.5, 0.9),
     * the upper-left and bottom-right coordinates will be (40, 20) to (180, 100).
     *
     * @param img   Image to draw on
     * @param box   Bounding box coordinates (yMin, xMin, yMax, xMax) normalized to 0.0-1.0
     * @param text  Optional text to display above the box
     * @param color Color for the box and text
     */
    public static void drawBoundingBox(BufferedImage img,
                                       Rectangle2D.Float box,
                                       String text,
                                       Color color) {
        if (img == null || box == null || color == null) {
            throw new IllegalArgumentException("Image, box, and color must not be null");
        }

        Graphics2D g2d = img.createGraphics();
        try {
            // Enable antialiasing for better quality
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Constants matching Home Assistant's implementation
            final int LINE_WIDTH = 3;
            final int FONT_HEIGHT = 20;

            // Extract normalized coordinates (0.0-1.0)
            float yMin = box.x;      // Rectangle2D uses x for first coordinate
            float xMin = box.y;      // Rectangle2D uses y for second coordinate
            float yMax = box.width;  // Rectangle2D uses width for third coordinate
            float xMax = box.height; // Rectangle2D uses height for fourth coordinate

            // Convert to pixel coordinates
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            int left = Math.round(xMin * imgWidth);
            int right = Math.round(xMax * imgWidth);
            int top = Math.round(yMin * imgHeight);
            int bottom = Math.round(yMax * imgHeight);

            // Set color and stroke
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(LINE_WIDTH));

            // Draw rectangle
            g2d.drawRect(left, top, right - left, bottom - top);

            // Draw text if provided
            if (text != null && !text.isEmpty()) {
                // Set font
                Font font = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_HEIGHT);
                g2d.setFont(font);

                // Calculate text position (above the box)
                int textX = left + LINE_WIDTH;
                int textY = Math.abs(top - LINE_WIDTH - FONT_HEIGHT);

                // Ensure text stays within image bounds
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                if (textX + textWidth > imgWidth) {
                    textX = imgWidth - textWidth - LINE_WIDTH;
                }
                if (textY < fm.getAscent()) {
                    textY = fm.getAscent();
                }

                g2d.drawString(text, textX, textY);
            }
        } finally {
            g2d.dispose();
        }
    }
}