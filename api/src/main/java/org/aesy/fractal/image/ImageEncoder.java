package org.aesy.fractal.image;

import java.awt.image.BufferedImage;

public interface ImageEncoder {
    byte[] encode(BufferedImage image);
}
