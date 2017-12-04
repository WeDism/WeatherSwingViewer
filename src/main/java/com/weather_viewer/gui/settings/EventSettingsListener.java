package com.weather_viewer.gui.settings;

import java.util.EventListener;

public interface EventSettingsListener extends EventListener{
    void onFindLocation(boolean find);
    void onOK();
}
