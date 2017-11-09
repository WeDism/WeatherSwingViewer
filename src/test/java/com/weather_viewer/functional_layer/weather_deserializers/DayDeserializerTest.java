package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather_viewer.functional_layer.structs.weather.Day;
import helpers.TestDataPaths;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DayDeserializerTest {

    @Test
    public void createDayObject() throws Exception {
        final String jsonAsString = Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_CURRENT_DAY), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining());

        assertNotNull("Test file not find", jsonAsString);
        assertTrue("String is empty", jsonAsString.length() > 0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        Gson gson = gsonBuilder.create();

        Object o = gson.fromJson(jsonAsString, Object.class);
        assertFalse("String is not json", o instanceof String);


        Day currentDay = gson.fromJson(jsonAsString, Day.class);
        assertNotNull("Day is null", currentDay);

    }

}