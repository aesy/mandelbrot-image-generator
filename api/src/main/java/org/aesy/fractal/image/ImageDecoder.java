package org.aesy.fractal.image;

import java.awt.image.BufferedImage;

public interface ImageDecoder {
    BufferedImage decode(byte[] bytes);
}
