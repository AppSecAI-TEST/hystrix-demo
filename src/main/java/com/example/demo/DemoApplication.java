package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoApplication {
	@Bean
	public MysqlPricingDao database(JdbcTemplate template) {
		return new MysqlPricingDao(template);
	}

	@Bean
    public CachePricingDao cache() {
	    return new CachePricingDao();
    }

    @Bean
    public DataAccessService das(MysqlPricingDao database,
                                 CachePricingDao cache) {
	    return new DataAccessService(cache, database);
    }

	@Bean
	public GetPrice getPrice(DataAccessService das) {
		return new GetPrice(das);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
