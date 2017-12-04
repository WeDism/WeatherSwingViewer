package com.weather_viewer.gui.general;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.previews.start.StartPreview;
import helpers.TestDataPaths;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static helpers.TestData.RU_COUNTRY;
import static helpers.TestData.SAMARA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class GeneralFormSpyTest {

    private final JsonElement jsonElementCurrentDay;
    private final JsonElement jsonElementWorkweek;

    public GeneralFormSpyTest() throws Exception {
        String jsonAsString = Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_CURRENT_DAY), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining());
        jsonElementCurrentDay = new JsonParser().parse(jsonAsString);

        jsonAsString = Files.readAllLines(Paths.get(TestDataPaths.PATH_TO_WORKWEEK), StandardCharsets.UTF_8)
                .parallelStream().collect(Collectors.joining());

        jsonElementWorkweek = new JsonParser().parse(jsonAsString);

    }

    @Test
    public void counterCallsPreview() throws Exception {
        IWeatherConnector<CurrentDay> connectorWeatherForDay
                = spy(ApiConnector.build(SAMARA, RU_COUNTRY, CurrentDay.class));
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = spy(ApiConnector.build(SAMARA, RU_COUNTRY, Workweek.class));

        StartPreview startPreview = new StartPreview();
        when(connectorWeatherForDay.request()).thenReturn(jsonElementCurrentDay);
        when(connectorForecastForTheWorkWeek.request()).thenReturn(jsonElementWorkweek);
        //TODO
//        General general = new General(startPreview, connectorWeatherForDay, connectorForecastForTheWorkWeek);
//        general.resetScheduledExecutor();

//        verify(connectorWeatherForDay, times(1)).requestAndGetWeatherStruct();
//        verify(connectorForecastForTheWorkWeek, times(1)).requestAndGetWeatherStruct();

    }
}
