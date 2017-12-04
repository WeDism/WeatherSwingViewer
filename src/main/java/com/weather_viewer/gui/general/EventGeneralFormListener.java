package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.gui.settings.EventSettingsListener;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicReference;

public interface EventGeneralFormListener extends EventListener {
    AtomicReference<CurrentDay> getCurrentDay();

    AtomicReference<Workweek> getWorkweek();

    void onUpdateForm();

    void onPerform();

    EventSettingsListener getSettingsForm();
}
