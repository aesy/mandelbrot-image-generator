package org.aesy.fractal.fractal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.stream.IntStream;

public class MultiThreadedFractalImageGenerator implements FractalImageGenerator {
    private final EscapeTimeFractal fractal;
    private final ColorGradient colorSampler;
    private final double x1;
    private final double x2;
    private final double y1;
    private final double y2;

    public MultiThreadedFractalImageGenerator(EscapeTimeFractal fractal, ColorGradient colorSampler,
                                              double x1, double x2, double y1, double y2) {
        this.fractal = fractal;
        this.colorSampler = colorSampler;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public BufferedImage generate(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double pixelWidth = (x2 - x1) / width;
        double pixelHeight = (y2 - y1) / height;

        IntStream.range(0, width).parallel().forEach(i -> {
            IntStream.range(0, height).parallel().forEach(j -> {
                // Transform pixel coordinate to cartesian plane
                double x = (pixelWidth * i) + (pixelWidth / 2) + x1;
                double y = (pixelHeight * j) + (pixelHeight / 2) + y1;

                int iterations = fractal.sample(BigDecimal.valueOf(x), BigDecimal.valueOf(y)).intValue();
                Color color = colorSampler.sample(iterations);
                image.setRGB(i, j, color.getRGB());
            });
		});

        return image;
    }
}
