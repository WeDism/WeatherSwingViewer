package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.*;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkWeekDeserializer implements JsonDeserializer<Workweek> {
    @Override
    public Workweek deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonObject cityObject = jsonObject.getAsJsonObject("city");
        final JsonArray listObject = jsonObject.getAsJsonArray("list");

        List<Day> list = new ArrayList<>(jsonObject.getAsJsonPrimitive("cnt").getAsInt());
        for (JsonElement jsonElement : listObject) {
            list.add(context.deserialize(jsonElement, Day.class));
        }

        return new Workweek(new Workweek.SignatureWorkDay(
                new City(cityObject.getAsJsonPrimitive("name").getAsString()),
                new Country(cityObject.getAsJsonPrimitive("country").getAsString()),
                jsonObject.getAsJsonPrimitive("cnt").getAsInt()),
                list);
    }
}
