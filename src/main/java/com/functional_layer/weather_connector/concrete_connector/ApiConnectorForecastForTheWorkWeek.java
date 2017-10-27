package com.functional_layer.weather_connector.concrete_connector;

import com.db_layer.structures.WorkWeek;
import com.functional_layer.struct_constructor.concrete_constructor.WorkWeekConverter;
import com.functional_layer.weather_connector.ApiConnector;
import com.functional_layer.weather_connector.consts.Country;
import com.functional_layer.weather_connector.consts.WeatherPlan;

public class ApiConnectorForecastForTheWorkWeek<T extends WorkWeek> extends ApiConnector<T> {
    public ApiConnectorForecastForTheWorkWeek(String city, String appId, Country country) {
        super(city, appId, country);
        weatherStructConverter = new WorkWeekConverter<>();
    }

    @Override
    protected WeatherPlan getWeatherPlan() {
        return WeatherPlan.ForecastForTheWorkWeek;
    }

}
