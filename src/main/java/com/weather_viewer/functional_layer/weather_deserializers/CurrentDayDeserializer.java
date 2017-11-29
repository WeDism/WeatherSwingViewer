package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.*;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class CurrentDayDeserializer implements JsonDeserializer<CurrentDay> {
    @Override
    public CurrentDay deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Day day = context.deserialize(json, Day.class);
        final CurrentDay.SignatureCurrentDay signatureCurrentDay = context.deserialize(json, CurrentDay.SignatureCurrentDay.class);

        return
                new CurrentDay(
                        day,
                        signatureCurrentDay);
    }
}
