package com.example.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachePricingDao implements PricingDao {
    private final Cache<String, Price> priceCache =
            CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    private final Cache<String, List<Promo>> promoCache =
            CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    @Override
    public Price getPrice(String name) {
        return priceCache.getIfPresent(name);
    }

    private static final String ALL = "all";

    @Override
    public List<Promo> getPromos() {
        return promoCache.getIfPresent(ALL);
    }

    public void put(Price price) {
        priceCache.put(price.getName(), price);
    }

    public void put(List<Promo> promos) {
        promoCache.put(ALL, promos);
    }
}
