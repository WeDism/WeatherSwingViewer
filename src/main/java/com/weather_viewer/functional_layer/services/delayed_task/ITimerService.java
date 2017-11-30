package com.weather_viewer.functional_layer.services.delayed_task;

public interface ITimerService {
    void reRunService(Runnable runnable);
    void dispose();
}
