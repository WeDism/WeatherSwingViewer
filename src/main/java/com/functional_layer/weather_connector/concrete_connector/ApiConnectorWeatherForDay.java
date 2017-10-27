package com.functional_layer.weather_connector.concrete_connector;

import com.db_layer.structures.Day;
import com.functional_layer.struct_constructor.concrete_constructor.WeatherDayConverter;
import com.functional_layer.weather_connector.ApiConnector;
import com.functional_layer.weather_connector.consts.Country;
import com.functional_layer.weather_connector.consts.WeatherPlan;

public class ApiConnectorWeatherForDay<T extends Day> extends ApiConnector<T> {
    public ApiConnectorWeatherForDay(String city, String appId, Country country) {
        super(city, appId, country);
        weatherStructConverter = new WeatherDayConverter<>();
    }

    @Override
    protected WeatherPlan getWeatherPlan() {
        return WeatherPlan.Weather;
    }

}
