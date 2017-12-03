package org.aesy.fractal.fractal;

import java.awt.image.BufferedImage;

public interface FractalImageGenerator {
    BufferedImage generate(int width, int height);
}
