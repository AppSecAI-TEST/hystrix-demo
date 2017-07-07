package com.example.demo;

import java.math.BigDecimal;

public class PercentOffPromo extends Promo {
    private final BigDecimal multiplier;

    public PercentOffPromo(String name, String target, BigDecimal percentOff) {
        super(name, target);
        this.multiplier = BigDecimal.ONE.subtract(percentOff.movePointLeft(2));
    }

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.multiply(multiplier);
    }
}
