package com.example.demo;

import com.example.demo.commands.DaoCommand;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataAccessService implements PricingDao {
    private final CachePricingDao cache;
    private final PricingDao database;

    public DataAccessService(CachePricingDao cache, PricingDao database) {
        this.cache = cache;
        this.database = database;
    }

    @Override
    public Price getPrice(String name) {
        return readThroughCache("price", c -> c.getPrice(name), cache::put);
    }

    @Override
    public List<Promo> getPromos() {
        return readThroughCache("promo", PricingDao::getPromos, cache::put);
    }

    private <T> T readThroughCache(String groupName, Function<PricingDao, T> method, Consumer<T> cachePut) {
        T fromCache = new DaoCommand<>(groupName, method, cache, database).execute();
        if (fromCache == null) {
            T fromDatabase = new DaoCommand<>(groupName, method, database, null).execute();
            if (fromDatabase != null) {
                cachePut.accept(fromDatabase);
            }
            return fromDatabase;
        }
        return fromCache;
    }
}
