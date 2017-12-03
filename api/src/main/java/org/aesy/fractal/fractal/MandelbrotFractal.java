package org.aesy.fractal.fractal;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MandelbrotFractal implements Fractal {
    private final int maxIterations;
    private final int precision;

    public MandelbrotFractal(int maxIterations, int precision) {
        this.maxIterations = maxIterations;
        this.precision = precision;
    }

    public BigInteger sample(BigDecimal x, BigDecimal y) {
        throw new RuntimeException("Not implemented");
    }
}
