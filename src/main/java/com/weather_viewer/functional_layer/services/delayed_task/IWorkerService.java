package com.weather_viewer.functional_layer.services.delayed_task;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;

public interface IWorkerService {
    void resetScheduledExecutor();
    void resetExecutor();
    void onSearch(Country country, City city);
    void onChangeLocationData();
    void dispose();
}
