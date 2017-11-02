package com.weather_viewer.functional_layer.weather_deserializers;

import com.weather_viewer.functional_layer.structs.weather.Day;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DayDeserializer implements JsonDeserializer<Day> {
    @Override
    public Day deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonObject wind = jsonObject.getAsJsonObject("wind");
        final JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        final JsonObject main = jsonObject.getAsJsonObject("main");
        return
                new Day(
                        jsonObject.get("dt").getAsLong(),
                        wind.get("deg").getAsInt(),
                        wind.get("speed").getAsInt(),
                        weather.get("main").getAsString(),
                        weather.get("description").getAsString(),
                        main.get("humidity").getAsInt(),
                        main.get("pressure").getAsInt(),
                        main.get("temp").getAsInt(),
                        main.get("temp_max").getAsInt(),
                        main.get("temp_min").getAsInt());
    }
}
