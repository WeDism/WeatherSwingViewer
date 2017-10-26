package com.functional_layer.weather_connector.concrete_connector;

import com.functional_layer.weather_connector.ApiConnector;
import com.functional_layer.weather_connector.consts.WeatherPlan;

public class ApiConnectorForecastForTheWorkWeek extends ApiConnector {
    public ApiConnectorForecastForTheWorkWeek(String city, String appId) {
        super(city, appId);
    }

    @Override
    protected WeatherPlan getWeatherPlan() {
        return WeatherPlan.ForecastForTheWorkWeek;
    }
}
