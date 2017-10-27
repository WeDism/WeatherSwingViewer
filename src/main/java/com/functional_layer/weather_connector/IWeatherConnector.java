package com.functional_layer.weather_connector;

import com.db_layer.structures.IWeatherStruct;
import com.google.gson.JsonElement;

public interface IWeatherConnector<T extends IWeatherStruct> {
    JsonElement request() throws Exception;

    T requestAndGetWeatherStruct() throws Exception;

}
