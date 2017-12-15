package com.weather_viewer.functional_layer.services.delayed_task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static helpers.TestDataPaths.PATH_TO_CURRENT_DAY;
import static helpers.TestDataPaths.PATH_TO_WORKWEEK;

public class WorkweekConcurrentTest {
    private Logger LOGGER = Logger.getLogger(WorkweekConcurrentTest.class.getName());

    @Test(timeout = 3000L)
    public void ConcurrentCreateInstance() throws Exception {
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

        Thread thread1 = new Thread(() ->
                run(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay));
        thread1.start();

        Thread thread2 = new Thread(() ->
                run(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay)
        );
        thread2.start();

        thread1.join();
        thread2.join();

    }

    private void run(IWeatherConnector<CurrentDay> connectorWeatherForDay, IWeatherConnector<Workweek> connectorForecastForTheWorkWeek, IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay) {
        boolean isMade = true;
        while (isMade) {
            try {
                IWorkerService buildSecondThread = WorkerService.build(
                        connectorWeatherForDay,
                        connectorForecastForTheWorkWeek,
                        connectorSignatureDay,
                        Mockito.mock(General.class));
                buildSecondThread.dispose();
                isMade = false;
            } catch (InterruptedException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, null, e);
                isMade = true;
            }
        }
    }
}