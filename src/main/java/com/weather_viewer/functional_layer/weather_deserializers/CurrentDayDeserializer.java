package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.*;
import com.weather_viewer.db_layer.structures.CurrentDay;
import com.weather_viewer.db_layer.structures.Day;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class CurrentDayDeserializer implements JsonDeserializer<CurrentDay> {
    @Override
    public CurrentDay deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonObject sys = jsonObject.getAsJsonObject("sys");
        final Day day = jsonDeserializationContext.deserialize(jsonElement, Day.class);

        CurrentDay.SignatureCurrentDay signatureCurrentDay = new CurrentDay.SignatureCurrentDay(
                new City(jsonObject.get("name").getAsString()),
                new Country(sys.get("country").getAsString()),
                sys.get("sunrise").getAsLong() * TimeUnit.SECONDS.toMillis(1),
                sys.get("sunset").getAsLong() * TimeUnit.SECONDS.toMillis(1));

        return
                new CurrentDay(
                        day,
                        signatureCurrentDay);
    }
}
