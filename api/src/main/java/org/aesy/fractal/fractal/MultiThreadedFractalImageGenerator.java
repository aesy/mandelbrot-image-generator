package org.aesy.fractal.fractal;

import org.aesy.fractal.color.ColorGradient;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.IntStream;

/**
 * A multithreaded implementation of a fractal image generator.
 */
public class MultiThreadedFractalImageGenerator implements FractalImageGenerator {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private final EscapeTimeFractal fractal;
    private final ColorGradient colorSampler;
    private final BigDecimal minX;
    private final BigDecimal maxX;
    private final BigDecimal minY;
    private final BigDecimal maxY;
    private final MathContext context;

    /**
     * @param fractal The fractal.
     * @param colorSampler A gradient to sample colors from.
     * @param minX The minimum horizontal position to render.
     * @param maxX The maximum horizontal position to render.
     * @param minY The minimum vertical position to render.
     * @param maxY The maximum vertical position to render.
     * @param context
     */
    public MultiThreadedFractalImageGenerator(EscapeTimeFractal fractal, ColorGradient colorSampler,
                                              BigDecimal minX, BigDecimal maxX, BigDecimal minY, BigDecimal maxY,
                                              MathContext context) {
        this.fractal = fractal;
        this.colorSampler = colorSampler;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.context = context;
    }

    @Override
    public BufferedImage generate(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BigDecimal pixelWidth = maxX.min(minX).divide(BigDecimal.valueOf(width), context);
        BigDecimal pixelHeight = maxY.min(minY).divide(BigDecimal.valueOf(height), context);

        IntStream.range(0, width).parallel().forEach(i -> {
            IntStream.range(0, height).parallel().forEach(j -> {
                // Transform pixel coordinate to cartesian plane
                BigDecimal x = pixelWidth.multiply(BigDecimal.valueOf(i), context)
                                         .add(pixelWidth.divide(TWO, context))
                                         .add(minX, context);
                BigDecimal y = pixelHeight.multiply(BigDecimal.valueOf(j), context)
                                          .add(pixelHeight.divide(TWO, context))
                                          .add(minY, context);

                int iterations = fractal.sample(x, y).intValue();
                Color color = colorSampler.sample(iterations);
                image.setRGB(i, j, color.getRGB());
            });
		});

        return image;
    }
}
