package com.weather_viewer.functional_layer.weather_connector.concrete_connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.services.delayed_task.IWorkerService;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_deserializers.*;
import com.weather_viewer.gui.general.General;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static helpers.TestDataPaths.PATH_TO_CURRENT_DAY;
import static helpers.TestDataPaths.PATH_TO_WORKWEEK;

public class ApiConnectorWeatherForDayMockAndStubTest {
    private final static int TIMEOUT;

    static {
        TIMEOUT = 3;
    }

    @Test
    public void request() throws Exception {
        final IWeatherConnector<CurrentDay> connectorWeatherForDay = Mockito.mock(ApiConnector.class);
        final IWeatherConnector<Workweek> connectorForecastForTheWorkWeek = Mockito.mock(ApiConnector.class);
        final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay = Mockito.mock(ApiConnector.class);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrentDay.SignatureCurrentDay.class, new SignatureCurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(CurrentDay.class, new CurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(Workweek.class, new WorkWeekDeserializer());
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        Gson gson = gsonBuilder.create();


        JsonElement jsonElementCurrentDay = new JsonParser().parse(Files.readAllLines(
                Paths.get(CurrentDayDeserializerTest.class.getResource(PATH_TO_CURRENT_DAY).toURI()))
                .parallelStream().collect(Collectors.joining()));

        JsonElement jsonElementWorkweek = new JsonParser().parse(Files.readAllLines(
                Paths.get(CurrentDayDeserializerTest.class.getResource(PATH_TO_WORKWEEK).toURI()))
                .parallelStream().collect(Collectors.joining()));

        CurrentDay currentDay = gson.fromJson(jsonElementCurrentDay, CurrentDay.class);
        Workweek workweek = gson.fromJson(jsonElementWorkweek, Workweek.class);

        Mockito.when(connectorWeatherForDay.requestAndGetWeatherStruct()).thenReturn(currentDay);
        Mockito.when(connectorForecastForTheWorkWeek.requestAndGetWeatherStruct()).thenReturn(workweek);

        IWorkerService build = WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, Mockito.mock(General.class));
        build.dispose();

        Mockito.verify(connectorWeatherForDay, Mockito.atLeastOnce()).requestAndGetWeatherStruct();
        Mockito.verify(connectorForecastForTheWorkWeek, Mockito.atLeastOnce()).requestAndGetWeatherStruct();

    }
}