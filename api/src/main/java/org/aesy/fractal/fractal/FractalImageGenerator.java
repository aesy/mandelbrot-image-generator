package org.aesy.fractal.fractal;

import java.awt.image.BufferedImage;

/**
 * Generates an image of a fractal with a specific resolution.
 */
public interface FractalImageGenerator {
    /**
     * Generates a buffered image.
     *
     * @param width The width of the image, in pixels.
     * @param height The height of the image, in pixels.
     * @return The image.
     */
    BufferedImage generate(int width, int height);
}
