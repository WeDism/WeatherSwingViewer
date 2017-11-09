package com.weather_viewer.gui.general;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorForecastForTheWorkweek;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.preview.Preview;
import helpers.TestDataPaths;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static helpers.TestData.*;
import static org.mockito.Mockito.*;

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
                = spy(new ApiConnectorWeatherForDay(SAMARA, APPID_VALUE, RU_COUNTRY, CurrentDay.class));
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = spy(new ApiConnectorForecastForTheWorkweek(SAMARA, APPID_VALUE, RU_COUNTRY, Workweek.class));

        Preview preview = new Preview();
        when(connectorWeatherForDay.request()).thenReturn(jsonElementCurrentDay);
        when(connectorForecastForTheWorkWeek.request()).thenReturn(jsonElementWorkweek);
        General general = new General(preview, connectorWeatherForDay, connectorForecastForTheWorkWeek);
        general.dispose();

        verify(connectorWeatherForDay, times(1)).requestAndGetWeatherStruct();
        verify(connectorForecastForTheWorkWeek, times(1)).requestAndGetWeatherStruct();

    }
}
