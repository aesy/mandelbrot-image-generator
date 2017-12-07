package org.aesy.fractal.color;

import java.awt.*;

/**
 * Maps a quantitative input to an output color.
 */
public interface ColorGradient {
    /**
     * Samples a color based on a given value.
     *
     * @param value
     * @return A color
     */
    Color sample(int value);

    /**
     * Samples a color based on a given value.
     *
     * @param value
     * @return A color
     */
    Color sample(double value);
}
