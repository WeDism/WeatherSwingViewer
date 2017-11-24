package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.JsonElement;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import org.junit.Test;

import static helpers.TestData.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiConnectorTest {

    private final IWeatherConnector<CurrentDay> connector;

    public ApiConnectorTest() {
        connector = new ApiConnectorWeatherForDay<>
                (SAMARA, RU_COUNTRY, CurrentDay.class);

    }

    @Test
    public void apiConnector() throws Exception {
        assertNotNull("ApiConnectorWeatherForDay not null", connector);

    }

    @Test
    public void request() throws Exception {
        JsonElement jsonElement = connector.request();
        assertTrue("Request response is not JSON", jsonElement.isJsonObject());
    }

    @Test
    public void requestAndGetCurrentDay() throws Exception {
        CurrentDay currentDay = connector.requestAndGetWeatherStruct();
        assertNotNull("CurrentDay not null", currentDay);

    }

}