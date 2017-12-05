package org.aesy.fractal.color;

import java.awt.*;

/**
 * Maps a quantitative input domain to an output color. If a given value is outside of the
 * domain range, the value is clamped. A color is then linearly interpolated, using RGB
 * color space, based on the given value.
 */
public class SimpleClampedLinearGradient implements ColorGradient {
    private double from;
    private double to;
    private Color startColor;
    private Color endColor;

    /**
     * @param from The start of the domain range.
     * @param to The end of the domain range.
     * @param startColor The color mapped to the start of the domain range.
     * @param endColor The color mapped to the end of the domain range.
     */
    public SimpleClampedLinearGradient(double from, double to, Color startColor, Color endColor) {
        this.from = from;
        this.to = to;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public SimpleClampedLinearGradient(int from, int to, Color startColor, Color endColor) {
        this((double) from, (double) to, startColor, endColor);
    }

    @Override
    public Color sample(int value) {
        return sample((double) value);
    }

    @Override
    public Color sample(double value) {
        // Transform to percent
        double p = ((value - from) / (to - from));

        // Clamp percent between [0, 1]
        p = Math.max(0, Math.min(1, p));

        // Linearly interpolate between colors
        int r = (int)(endColor.getRed() * p + startColor.getRed() * (1 - p));
        int g = (int)(endColor.getGreen() * p + startColor.getGreen() * (1 - p));
        int b = (int)(endColor.getBlue() * p + startColor.getBlue() * (1 - p));

        return new Color(r, g, b);
    }
}
