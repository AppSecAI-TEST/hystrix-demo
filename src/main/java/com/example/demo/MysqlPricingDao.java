package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MysqlPricingDao implements PricingDao {
    private final JdbcTemplate jdbcTemplate;

    public MysqlPricingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Price getPrice(String name) {
        return jdbcTemplate.queryForObject("select * from prices where name = ?", (rs, i) ->
                new Price(rs.getString("name"), rs.getBigDecimal("price"))
        , name);
    }

    @Override
    public List<Promo> getPromos() {
        return jdbcTemplate.query("select * from promos", (rs, i) -> {
            String type = rs.getString("promo_type");
            String name = rs.getString("name");
            String target = rs.getString("target");
            switch (type) {
                case "percent_off": return new PercentOffPromo(name, target, rs.getBigDecimal("percent_off"));
                case "dollar_off": return new DollarOffPromo(name, target, rs.getBigDecimal("dollar_off"));
            }
            throw new IllegalArgumentException("Invalid promo with name " + name + " and type " + type);
        });
    }
}
