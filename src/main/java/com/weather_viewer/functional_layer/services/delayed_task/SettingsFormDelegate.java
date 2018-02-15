package com.weather_viewer.functional_layer.services.delayed_task;

import java.util.EventListener;

public interface SettingsFormDelegate extends EventListener{
    void onFindLocation(boolean find);
    void onOK();
}
