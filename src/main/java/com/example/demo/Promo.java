package com.example.demo;

import java.math.BigDecimal;

public abstract class Promo {
    private final String name;
    private final String target;

    public Promo(String name, String target) {
        this.name = name;
        this.target = target;
    }

    public boolean isEligible(String name) {
        return target.equals(name);
    }

    public abstract BigDecimal apply(BigDecimal price);
}
