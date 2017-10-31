package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.JsonElement;
import com.weather_viewer.db_layer.structures.IWeatherStruct;

public interface IWeatherConnector<T extends IWeatherStruct> {
    JsonElement request() throws Exception;

    T requestAndGetWeatherStruct() throws Exception;

}
