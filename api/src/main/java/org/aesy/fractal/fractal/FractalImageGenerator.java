package org.aesy.fractal.fractal;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

public interface FractalImageGenerator {
    BufferedImage generate(BigDecimal xStart,
                           BigDecimal xEnd,
                           BigDecimal yStart,
                           BigDecimal yEnd,
                           int width,
                           int height);
}
