package com.example.demo;

import com.example.demo.commands.DaoCommand;

import java.util.List;
import java.util.function.Function;

public class DataAccessService implements PricingDao {
    private final PricingDao cache;
    private final PricingDao database;

    public DataAccessService(PricingDao cache, PricingDao database) {
        this.cache = cache;
        this.database = database;
    }

    @Override
    public Price getPrice(String name) {
        return readThroughCache("price", c -> c.getPrice(name));
    }

    @Override
    public List<Promo> getPromos() {
        return readThroughCache("promo", PricingDao::getPromos);
    }

    private <T> T readThroughCache(String groupName, Function<PricingDao, T> method) {
        T fromCache = new DaoCommand<>(groupName, method, cache, database).execute();
        if (fromCache == null) {
            return new DaoCommand<>(groupName, method, database, null).execute();
        }
        return fromCache;
    }
}
