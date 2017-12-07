package org.aesy.fractal.fractal;

import org.aesy.fractal.color.ColorGradient;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.stream.IntStream;

/**
 * A multithreaded implementation of a fractal image generator.
 */
public class MultiThreadedFractalImageGenerator implements FractalImageGenerator {
    private final EscapeTimeFractal fractal;
    private final ColorGradient colorSampler;
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    /**
     * @param fractal The fractal.
     * @param colorSampler A gradient to sample colors from.
     * @param minX The minimum horizontal position to render.
     * @param maxX The maximum horizontal position to render.
     * @param minY The minimum vertical position to render.
     * @param maxY The maximum vertical position to render.
     */
    public MultiThreadedFractalImageGenerator(EscapeTimeFractal fractal, ColorGradient colorSampler,
                                              double minX, double maxX, double minY, double maxY) {
        this.fractal = fractal;
        this.colorSampler = colorSampler;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public BufferedImage generate(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double pixelWidth = (maxX - minX) / width;
        double pixelHeight = (maxY - minY) / height;

        IntStream.range(0, width).parallel().forEach(i -> {
            IntStream.range(0, height).parallel().forEach(j -> {
                // Transform pixel coordinate to cartesian plane
                double x = (pixelWidth * i) + (pixelWidth / 2) + minX;
                double y = (pixelHeight * j) + (pixelHeight / 2) + minY;

                int iterations = fractal.sample(BigDecimal.valueOf(x), BigDecimal.valueOf(y)).intValue();
                Color color = colorSampler.sample(iterations);
                image.setRGB(i, j, color.getRGB());
            });
		});

        return image;
    }
}
