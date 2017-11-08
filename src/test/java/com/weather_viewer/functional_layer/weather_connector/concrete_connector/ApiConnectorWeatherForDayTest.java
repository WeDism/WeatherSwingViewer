package com.weather_viewer.functional_layer.weather_connector.concrete_connector;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import test_helpers.TestDataPaths;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ApiConnectorWeatherForDayTest {

    @Test
    public void request() throws Exception {
        final IWeatherConnector<CurrentDay> weatherConnector = Mockito.mock(ApiConnectorWeatherForDay.class);

        JsonElement jsonElement = new JsonParser().parse(Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_CURRENT_DAY), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining()));

        Mockito.when(weatherConnector.request()).thenReturn(jsonElement);
        Assert.assertTrue(weatherConnector.request().isJsonObject());
        Mockito.verify(weatherConnector, Mockito.atLeastOnce()).request();

    }
}