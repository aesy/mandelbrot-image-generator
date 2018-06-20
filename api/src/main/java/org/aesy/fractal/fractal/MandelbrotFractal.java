package org.aesy.fractal.fractal;

import ch.obermuhlner.math.big.BigComplex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * The Mandelbrot Set is defined as the set of complex numbers for which `f_c(z) = z^2 + c`
 * does not diverge when iterated from z = 0, i.e., for which the sequence f_c(0), f_c(f_c(0)),
 * etc., remains bounded in absolute value.
 */
public class MandelbrotFractal implements EscapeTimeFractal {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private final BigInteger maxIterations;
    private final MathContext context;

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     * @param context The math context.
     */
    public MandelbrotFractal(BigInteger maxIterations , MathContext context) {
        this.maxIterations = maxIterations;
        this.context = context;
    }

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     * @param context The math context.
     */
    public MandelbrotFractal(int maxIterations , MathContext context) {
        this(BigInteger.valueOf(maxIterations), context);
    }

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     * @param precision The precision of the calculations.
     */
    public MandelbrotFractal(BigInteger maxIterations , int precision) {
        this(maxIterations, new MathContext(precision));
    }

    /**
     * @param maxIterations The maximum amount of iterations permitted before escaping.
     * @param precision The precision of the calculations.
     */
    public MandelbrotFractal(int maxIterations , int precision) {
        this(BigInteger.valueOf(maxIterations) , new MathContext(precision));
    }

    /**
     * Samples a point in the complex plane to determine the amount of iterations until the point diverges, or the
     * maximum amount of allowed iterations, whichever is smaller.
     *
     * @param real The real part of the point c.
     * @param imaginary The imaginary part of the point c.
     * @return The amount of iterations until divergence.
     */
    @Override
    public BigInteger sample(BigDecimal real, BigDecimal imaginary) {
        BigInteger iterations = BigInteger.ZERO;
        BigComplex z = BigComplex.ZERO;
        BigComplex c = BigComplex.valueOf(real, imaginary);

        // Escape early if 'z' is outside a radius of 2 as the point is then guaranteed to diverge
        while (iterations.compareTo(maxIterations) < 0 && z.abs(context).compareTo(TWO) <= 0) {
            z = z.multiply(z, context)
                 .add(c, context);

            iterations = iterations.add(BigInteger.ONE);
        }

        return iterations;
    }

    /**
     * Samples a point to determine whether the point remains bounded.
     *
     * @param real The real part of the point c.
     * @param imaginary The imaginary part of the point c.
     * @return An indication whether the point remains bounded.
     */
    @Override
    public boolean isBounded(BigDecimal real, BigDecimal imaginary) {
        return sample(real, imaginary).compareTo(maxIterations) >= 0;
    }
}
