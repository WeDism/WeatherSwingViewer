package com.weather_viewer.functional_layer.services.delayed_task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimerService implements ITimerService {
    private static final Logger LOGGER;
    private static final long PERIOD;
    private ScheduledExecutorService executor;

    static {
        LOGGER = Logger.getLogger(TimerService.class.getName());
        PERIOD = 10;
    }

    private TimerService() {
        executor = Executors.newScheduledThreadPool(1);
    }

    private TimerService(Runnable runnable) {
        this();
        executor.scheduleWithFixedDelay(runnable, 0, PERIOD, TimeUnit.SECONDS);
    }

    public static ITimerService build(Runnable runnable) {
        return new TimerService(runnable);
    }

    @Override
    public void reRunService(Runnable runnable) {
        executor.scheduleWithFixedDelay(runnable, 0, PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void dispose() {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            if (!executor.isTerminated())
                LOGGER.log(Level.SEVERE, "Task is not terminated");

            executor.shutdownNow();
            LOGGER.log(Level.SEVERE, "Shutdown finished");
        }
    }
}
