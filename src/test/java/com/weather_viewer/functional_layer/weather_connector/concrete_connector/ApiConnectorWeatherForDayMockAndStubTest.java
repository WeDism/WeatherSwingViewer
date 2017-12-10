package com.weather_viewer.functional_layer.weather_connector.concrete_connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stubs.GeneralFormStart;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_deserializers.CurrentDayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.DayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.SignatureCurrentDayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.WorkWeekDeserializer;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.Settings;
import helpers.TestDataPaths;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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


        JsonElement jsonElementCurrentDay = new JsonParser().parse(Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_CURRENT_DAY), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining()));

        JsonElement jsonElementWorkweek = new JsonParser().parse(Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_WORKWEEK), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining()));

        CurrentDay currentDay = gson.fromJson(jsonElementCurrentDay, CurrentDay.class);
        Workweek workweek = gson.fromJson(jsonElementWorkweek, Workweek.class);

        Mockito.when(connectorWeatherForDay.requestAndGetWeatherStruct()).thenReturn(currentDay);
        Mockito.when(connectorForecastForTheWorkWeek.requestAndGetWeatherStruct()).thenReturn(workweek);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<GeneralFormStart> future = executorService.submit(() -> new GeneralFormStart(new StartPreview(), new Settings()));
        GeneralFormStart general = future.get();
        WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, general);
        executorService.shutdown();

        LocalDateTime maxTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        while (!general.wasPerform() && LocalDateTime.now().isBefore(maxTime)) ;

        Assert.assertTrue("General form was not disposed", general.wasPerform());

        Mockito.verify(connectorWeatherForDay, Mockito.atLeastOnce()).requestAndGetWeatherStruct();
        Mockito.verify(connectorForecastForTheWorkWeek, Mockito.atLeastOnce()).requestAndGetWeatherStruct();

    }
}