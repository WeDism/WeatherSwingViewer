package com.weather_viewer.functional_layer.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context implements IContext {
    private final Map<Class<?>, Object> context = new ConcurrentHashMap<>();

    private Context() {
    }

    @Override
    public IContext add(Class<?> clazz, Object object) {
        if (this.context.containsKey(clazz)) {
            throw new IllegalArgumentException();
        }
        this.context.put(clazz, object);
        return this;
    }

    @Override
    public Object get(Class<?> clazz) {
        return this.context.get(clazz);
    }

    public static IContext build(){
        return new Context();
    }
}
