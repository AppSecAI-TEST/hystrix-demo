package com.example.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoApplication {
	@Bean("database")
	public PricingDao database(JdbcTemplate template) {
		return new MysqlPricingDao(template);
	}

	@Bean("cache")
    public PricingDao cache() {
	    return new CachePricingDao();
    }

    @Bean("das")
    public DataAccessService das(@Qualifier("database") PricingDao database,
                                 @Qualifier("cache") PricingDao cache) {
	    return new DataAccessService(database, cache);
    }

	@Bean
	public GetPrice getPrice(DataAccessService das) {
		return new GetPrice(das);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
