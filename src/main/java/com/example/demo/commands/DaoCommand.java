package com.example.demo.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.function.Function;

public class DaoCommand<Value, Dao> extends HystrixCommand<Value> {
    private final String name;
    private final Function<Dao, Value> method;
    private final Dao primary;
    private final Dao fallback;

    public DaoCommand(String name, Function<Dao, Value> method, Dao primary, Dao fallback) {
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
    protected Value run() throws Exception {
        return method.apply(primary);
    }

    @Override
    protected Value getFallback() {
        if (fallback == null) return null;
        return new DaoCommand<>(name, method, fallback, null).execute();
    }
}
