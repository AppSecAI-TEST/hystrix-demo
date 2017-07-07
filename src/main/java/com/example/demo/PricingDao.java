package com.example.demo;

import java.util.List;

public interface PricingDao {
    Price getPrice(String name);

    List<Promo> getPromos();
}
