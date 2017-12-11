package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.JsonElement;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import org.junit.Test;

import static helpers.TestData.RU_COUNTRY;
import static helpers.TestData.SAMARA;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiConnectorTest {

    private final IWeatherConnector<CurrentDay> connector;

    public ApiConnectorTest() {
        this.connector = ApiConnector.build(SAMARA, RU_COUNTRY, CurrentDay.class);

    }

    @Test
    public void apiConnector() {
        assertNotNull("ApiConnectorWeatherForDay not null", this.connector);
    }

    @Test
    public void request() throws Exception {
        JsonElement jsonElement = this.connector.request();
        assertTrue("Request response is not JSON", jsonElement.isJsonObject());
    }

    @Test
    public void requestAndGetCurrentDay() throws Exception {
        CurrentDay currentDay = this.connector.requestAndGetWeatherStruct();
        assertNotNull("CurrentDay not null", currentDay);

    }

}