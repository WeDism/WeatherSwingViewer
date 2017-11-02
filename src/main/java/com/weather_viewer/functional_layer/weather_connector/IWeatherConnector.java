package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.JsonElement;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;

public interface IWeatherConnector<T extends IWeatherStruct> {
    JsonElement request() throws Exception;

    T requestAndGetWeatherStruct() throws Exception;

}
