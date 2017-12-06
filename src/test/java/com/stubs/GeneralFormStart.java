package com.stubs;

import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.Settings;
import org.jetbrains.annotations.Contract;

import java.awt.*;

public class GeneralFormStart extends General {
    private boolean isPerform;

    public GeneralFormStart(StartPreview startPreview, Settings settings) throws HeadlessException {
        super(startPreview, settings);
    }

    @Override
    public void onPerform() {
        super.onPerform();
        isPerform = true;
        dispose();
    }

    @Contract(pure = true)
    public boolean wasPerform() {
        return isPerform;
    }
}