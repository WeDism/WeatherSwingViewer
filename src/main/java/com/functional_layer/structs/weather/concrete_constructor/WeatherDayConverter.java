package com.functional_layer.structs.weather.concrete_constructor;

import com.db_layer.structures.Day;
import com.functional_layer.structs.weather.IWeatherStructConverter;
import com.google.gson.JsonElement;

public class WeatherDayConverter<T extends Day> implements IWeatherStructConverter<T> {
    @Override
    public T construct(JsonElement jsonElement) {
        return null;
    }
}
