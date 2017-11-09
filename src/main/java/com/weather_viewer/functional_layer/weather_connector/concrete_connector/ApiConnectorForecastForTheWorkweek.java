package com.weather_viewer.functional_layer.weather_connector.concrete_connector;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.consts.WeatherPlan;

public class ApiConnectorForecastForTheWorkweek<T extends Workweek> extends ApiConnector<T> {
    public ApiConnectorForecastForTheWorkweek(City city, String appId, Country country, Class<T> typeParameterClass) {
        super(city, appId, country, typeParameterClass);
    }

    @Override
    protected WeatherPlan getWeatherPlan() {
        return WeatherPlan.ForecastForTheWorkWeek;
    }

}
