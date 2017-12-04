package com.weather_viewer.functional_layer.services.delayed_task;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;

import java.util.concurrent.atomic.AtomicReference;

public interface IWorkerService {
    void resetScheduledExecutor();
    void resetExecutor();
    void onSearch(Country country, City city);
    void onChangeLocationData();
}
