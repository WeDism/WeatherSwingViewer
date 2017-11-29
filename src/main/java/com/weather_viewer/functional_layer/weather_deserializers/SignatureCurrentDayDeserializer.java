package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.*;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class SignatureCurrentDayDeserializer implements JsonDeserializer<CurrentDay.SignatureCurrentDay> {
    @Override
    public CurrentDay.SignatureCurrentDay deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonObject sys = jsonObject.getAsJsonObject("sys");

        return new CurrentDay.SignatureCurrentDay(
                new City(jsonObject.get("name").getAsString()),
                new Country(sys.get("country").getAsString()),
                sys.get("sunrise").getAsLong() * TimeUnit.SECONDS.toMillis(1),
                sys.get("sunset").getAsLong() * TimeUnit.SECONDS.toMillis(1));
    }
}
