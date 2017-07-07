package com.example.demo;


import java.math.BigDecimal;

public class Price implements Comparable<Price> {
    private final String name;
    private final BigDecimal price;

    public Price(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Price apply(Promo promo) {
        if (promo.isEligible(name)) {
            return new Price(name, promo.apply(price));
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(Price o) {
        return price.compareTo(o.price);
    }
}
