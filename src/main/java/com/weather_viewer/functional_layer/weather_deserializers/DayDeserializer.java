package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.*;
import com.weather_viewer.functional_layer.structs.weather.Day;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class DayDeserializer implements JsonDeserializer<Day> {
    @Override
    public Day deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonObject wind = jsonObject.getAsJsonObject("wind");
        final JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        final JsonObject main = jsonObject.getAsJsonObject("main");
        return
                new Day(
                        jsonObject.get("dt").getAsLong() * TimeUnit.SECONDS.toMillis(1),
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
