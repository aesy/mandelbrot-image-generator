package org.aesy.fractal.fractal;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Fractal {
    BigInteger sample(BigDecimal x, BigDecimal y);
}
