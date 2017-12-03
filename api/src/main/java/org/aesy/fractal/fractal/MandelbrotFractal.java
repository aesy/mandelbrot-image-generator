package org.aesy.fractal.fractal;

import org.apache.commons.math3.complex.Complex;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MandelbrotFractal implements EscapeTimeFractal {
    private final BigInteger maxIterations;
    // private final MathContext context;

    public MandelbrotFractal(BigInteger maxIterations /*, MathContext context*/) {
        this.maxIterations = maxIterations;
        // this.context = context;
    }

    public MandelbrotFractal(int maxIterations /*, int precision*/) {
        this(BigInteger.valueOf(maxIterations) /*, new MathContext(precision)*/);
    }

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
    public boolean isBound(BigDecimal x, BigDecimal y) {
        return sample(x, y).compareTo(maxIterations) >= 0;
    }
}
