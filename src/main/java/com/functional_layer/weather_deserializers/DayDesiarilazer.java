package com.functional_layer.weather_deserializers;

import com.db_layer.structures.Day;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DayDesiarilazer implements JsonDeserializer<Day> {
    @Override
    public Day deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
