package com.weather_viewer.functional_layer.weather_deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather_viewer.db_layer.structures.CurrentDay;
import com.weather_viewer.db_layer.structures.Day;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class CurrentDayDeserializerTest {

    @Test
    public void createCurrentDayObject() throws Exception {
        final String jsonAsString = Files.readAllLines(Paths.get("src//test//resources//CurrentDay.json"), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining());

        Assert.assertNotNull("Test file not find", jsonAsString);
        Assert.assertTrue("String is empty", jsonAsString.length() > 0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrentDay.class, new CurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        Gson gson = gsonBuilder.create();

        Object o = gson.fromJson(jsonAsString, Object.class);
        Assert.assertFalse("String is not json", o instanceof String);


        CurrentDay currentDay = gson.fromJson(jsonAsString, CurrentDay.class);
        Assert.assertNotNull("Current Day is null", currentDay);

    }
}