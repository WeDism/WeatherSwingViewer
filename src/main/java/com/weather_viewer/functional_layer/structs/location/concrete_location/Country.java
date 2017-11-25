package com.weather_viewer.functional_layer.structs.location.concrete_location;

import com.weather_viewer.functional_layer.structs.location.Location;
import org.jetbrains.annotations.NotNull;

public class Country extends Location{
    public Country(@NotNull String country) {
        super(country);
    }
}
