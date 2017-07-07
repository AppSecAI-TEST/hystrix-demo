package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class GetPrice {
    private final PricingDao dao;

    public GetPrice(PricingDao dao) {
        this.dao = dao;
    }
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static Price NOT_FOUND = new Price("not found", null);

    @RequestMapping(value = "prices/{name}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    @ResponseBody
    public Price getPrice(@PathVariable("name") String name) throws ExecutionException, InterruptedException {
        return executor.submit(() -> {
            Price originalPrice = dao.getPrice(name);
            if (originalPrice == null) return NOT_FOUND;
            List<Promo> promotions = dao.getPromos();
            if (promotions == null) return originalPrice;
            return promotions.stream().map(originalPrice::apply).min(Price::compareTo).
                    orElse(NOT_FOUND);
        }).get();
    }
}
