package org.aesy.fractal.fractal;

import org.apache.commons.math3.complex.Complex;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The Mandelbrot Set is defined as the set of complex numbers for which `f_c(z) = z^2 + c`
 * does not diverge when iterated from z = 0, i.e., for which the sequence f_c(0), f_c(f_c(0)),
 * etc., remains bounded in absolute value.
 */
public class MandelbrotFractal implements EscapeTimeFractal {
    private final BigInteger maxIterations;
    // private final MathContext context;

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     */
    public MandelbrotFractal(BigInteger maxIterations /*, MathContext context*/) {
        this.maxIterations = maxIterations;
        // this.context = context;
    }

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     */
    public MandelbrotFractal(int maxIterations /*, int precision*/) {
        this(BigInteger.valueOf(maxIterations) /*, new MathContext(precision)*/);
    }

    /**
     * Samples a point in the complex plane to determine the amount of iterations until the point diverges.
     *
     * @param x The real part of the point c.
     * @param y The imaginary part of the point c.
     * @return The amount of iterations until divergence.
     */
    @Override
    public BigInteger sample(BigDecimal x, BigDecimal y) {
        BigInteger iterations = BigInteger.ZERO;
        Complex z = Complex.ZERO;
        // TODO use arbitrary precision complex number
        Complex c = new Complex(x.doubleValue(), y.doubleValue());

        // Escape early if outside a radius of 2 as the point is then guaranteed to diverge
        while (iterations.compareTo(maxIterations) < 0 && z.abs() <= 2) {
            z = z.multiply(z)
                 .add(c);

            iterations = iterations.add(BigInteger.ONE);
        }

        return iterations;
    }

    @Override
    public boolean isBounded(BigDecimal x, BigDecimal y) {
        return sample(x, y).compareTo(maxIterations) >= 0;
    }
}
