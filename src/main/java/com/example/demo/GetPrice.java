package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GetPrice {
    private final PricingDao dao;

    public GetPrice(PricingDao dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "prices/{name}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    @ResponseBody
    public Price getPrice(@PathVariable("name") String name) {
        Price originalPrice = dao.getPrice(name);
        if (originalPrice == null) return null;
        List<Promo> promotions = dao.getPromos();
        return promotions.stream().map(originalPrice::apply).min(Price::compareTo).orElse(originalPrice);
    }
}
