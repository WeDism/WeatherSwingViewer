package com.weather_viewer.functional_layer.application;

public interface IContext {
    IContext add(Class<?> clazz, Object object);

    Object get(Class<?> clazz);
}
