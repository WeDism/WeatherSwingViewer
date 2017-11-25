package com.weather_viewer.functional_layer.structs.location.concrete_location;

import com.weather_viewer.functional_layer.structs.location.Location;
import org.jetbrains.annotations.NotNull;

public class City extends Location{
    public City(@NotNull String city) {
        super(city);
    }
}
