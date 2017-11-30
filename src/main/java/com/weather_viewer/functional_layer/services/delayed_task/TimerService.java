package com.weather_viewer.functional_layer.services.delayed_task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerService implements ITimerService {
    private final long period;
    private Timer timer;

    private TimerService() {
        timer = new Timer();
        period = TimeUnit.SECONDS.toMillis(30);
    }

    private TimerService(Runnable runnable) {
        this();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, 0, period);
    }

    public static ITimerService build(Runnable runnable) {
        return new TimerService(runnable);
    }

    @Override
    public void reRunService(Runnable runnable) {
        timer.purge();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, 0, period);
    }

    @Override
    public void dispose() {
        timer.cancel();
    }
}
