package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.JsonElement;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;

public interface IWeatherConnector<T extends IWeatherStruct> {
    void setNewData(City city, Country country);

    JsonElement request() throws Exception;

    T requestAndGetWeatherStruct() throws Exception;

}
