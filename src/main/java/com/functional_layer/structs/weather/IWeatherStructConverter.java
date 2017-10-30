package com.functional_layer.structs.weather;

import com.db_layer.structures.IWeatherStruct;
import com.google.gson.JsonElement;

public interface IWeatherStructConverter<T extends IWeatherStruct> {
    T construct(JsonElement jsonElement);
}
