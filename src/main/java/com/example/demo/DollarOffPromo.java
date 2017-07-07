package com.example.demo;


import java.math.BigDecimal;

public class DollarOffPromo extends Promo {
    private final BigDecimal subtractor;

    public DollarOffPromo(String name, String target, BigDecimal dollarOff) {
        super(name, target);
        this.subtractor = dollarOff;
    }

    @Override
    public BigDecimal apply(BigDecimal price) {
        return price.subtract(subtractor);
    }
}
