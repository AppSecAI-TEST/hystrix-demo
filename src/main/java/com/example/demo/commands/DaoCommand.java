package com.example.demo.commands;

import com.example.demo.PricingDao;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.function.Function;

public class DaoCommand<T> extends HystrixCommand<T> {
    private final String name;
    private final Function<PricingDao, T> method;
    private final PricingDao primary;
    private final PricingDao fallback;

    public DaoCommand(String name, Function<PricingDao, T> method, PricingDao primary, PricingDao fallback) {
        super(properties(name));
        this.name = name;
        this.method = method;
        this.primary = primary;
        this.fallback = fallback;
    }

    private static Setter properties(String name) {
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey(name);
        Setter setter = Setter.withGroupKey(groupKey);
        HystrixCommandProperties.Setter props = HystrixCommandProperties.defaultSetter().
                withExecutionTimeoutInMilliseconds(3000);
        return setter.andCommandPropertiesDefaults(props);
    }

    @Override
    protected T run() throws Exception {
        return method.apply(primary);
    }

    @Override
    protected T getFallback() {
        if (fallback == null) throw new RuntimeException("Unavailable!");
        return new DaoCommand<>(name, method, fallback, null).execute();
    }
}
