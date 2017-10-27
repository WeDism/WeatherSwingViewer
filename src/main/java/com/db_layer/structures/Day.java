package com.db_layer.structures;

import com.google.gson.annotations.SerializedName;

public class Day implements IWeatherStruct {
    @SerializedName("name")
    String city;


}
