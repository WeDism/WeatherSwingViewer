package com.weather_viewer.gui.settings;

import java.util.EventListener;

public interface SettingsFormDelegate extends EventListener{
    void onFindLocation(boolean find);
    void onOK();
}
