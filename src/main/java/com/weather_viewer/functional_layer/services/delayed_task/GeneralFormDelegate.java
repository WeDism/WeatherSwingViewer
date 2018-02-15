package com.weather_viewer.functional_layer.services.delayed_task;

import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;

public interface GeneralFormDelegate extends EventListener {
    AtomicReference<CurrentDay> getCurrentDay();

    AtomicReference<Workweek> getWorkweek();

    void onUpdateForm();

    void onPerform();
}
