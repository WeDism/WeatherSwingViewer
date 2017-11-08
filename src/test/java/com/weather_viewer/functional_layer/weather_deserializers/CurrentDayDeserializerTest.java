package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import org.junit.Test;
import test_helpers.TestDataPaths;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CurrentDayDeserializerTest {

    @Test
    public void createCurrentDayObject() throws Exception {
        final String jsonAsString = Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_CURRENT_DAY), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining());

        assertNotNull("Test file not find", jsonAsString);
        assertTrue("String is empty", jsonAsString.length() > 0);

        JsonElement jsonElement = new JsonParser().parse(jsonAsString);
        assertTrue("String is not JSON", jsonElement.isJsonObject());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrentDay.class, new CurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(Workweek.class, new WorkWeekDeserializer());
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        Gson gson = gsonBuilder.create();

        Object o = gson.fromJson(jsonAsString, Object.class);
        assertFalse("String is not json", o instanceof String);


        CurrentDay currentDay = gson.fromJson(jsonAsString, CurrentDay.class);
        assertNotNull("Current Day is null", currentDay);

    }
}