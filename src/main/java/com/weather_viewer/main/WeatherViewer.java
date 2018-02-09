package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.functional_layer.application.Context;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.exceptions.ObjectContainsException;
import com.weather_viewer.functional_layer.services.delayed_task.IWorkerService;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.general.GeneralFormDelegate;
import com.weather_viewer.gui.previews.start.IPreview;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.ISettings;
import com.weather_viewer.gui.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherViewer<T extends General> {

    private static final Logger LOGGER = Logger.getLogger(WeatherViewer.class.getName());
    private final IWeatherConnector<Workweek> connectorForecastForTheWorkWeek;
    private final IWeatherConnector<CurrentDay> connectorWeatherForDay;
    private final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay;
    private final Callable<T> callable;
    private static WeatherViewer weatherViewer;
    private T general;
    private final IContext context = Context.build();
    private volatile static Thread capturedThread;


    static {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            UIManager.getLookAndFeelDefaults()
                    .put("defaultFont", new Font("Arial", Font.PLAIN, 14));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    {
        this.context.add(IPreview.class, new StartPreview())
                .add(ISettings.class, new Settings(this.context));
    }

    private WeatherViewer(Properties startUpConf) {
        City samara = new City(startUpConf.getProperty("currentCity"));
        Country ru = new Country(startUpConf.getProperty("countryCode"));

        this.connectorForecastForTheWorkWeek = ApiConnector.build(samara, ru, Workweek.class);
        this.connectorWeatherForDay = ApiConnector.build(samara, ru, CurrentDay.class);
        this.connectorSignatureDay = ApiConnector.build(CurrentDay.SignatureCurrentDay.class);
        this.callable = () -> (T) new General(this.context);
    }

    private WeatherViewer(IWeatherConnector<CurrentDay> connectorWeatherForDay,
                          IWeatherConnector<Workweek> connectorForecastForTheWorkWeek,
                          IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay, Callable<T> callable) {
        this.connectorForecastForTheWorkWeek = connectorForecastForTheWorkWeek;
        this.connectorWeatherForDay = connectorWeatherForDay;
        this.connectorSignatureDay = connectorSignatureDay;
        this.callable = callable;
    }

    private void createGui() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<T> futureGeneral = executorService.submit(this.callable);
        this.general = futureGeneral.get();
        this.context.add(GeneralFormDelegate.class, this.general);
        executorService.shutdown();
    }

    private void buildWorkerService() throws ObjectContainsException {
        this.context.add(IWorkerService.class,
                WorkerService.build(this.connectorWeatherForDay, this.connectorForecastForTheWorkWeek, this.connectorSignatureDay, this.context));
    }

    public WeatherViewer start() throws ExecutionException, InterruptedException, ObjectContainsException {
        this.createGui();
        this.buildWorkerService();
        return this;
    }

    public synchronized static WeatherViewer getInstance(Properties startUpConf) throws InterruptedException {
        previewGetInstance();
        WeatherViewer.weatherViewer = new WeatherViewer(startUpConf);
        LOGGER.log(Level.INFO, "WeatherViewer was created");
        return weatherViewer;
    }

    public synchronized static <T extends General> WeatherViewer getInstance(IWeatherConnector<CurrentDay> connectorWeatherForDay,
                                                                             IWeatherConnector<Workweek> connectorForecastForTheWorkWeek,
                                                                             IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay,
                                                                             Callable<T> callable) throws InterruptedException {
        previewGetInstance();
        WeatherViewer.weatherViewer = new WeatherViewer<>(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, callable);
        LOGGER.log(Level.INFO, "WeatherViewer was created");
        return weatherViewer;

    }

    private static void previewGetInstance() throws InterruptedException {
        if (WeatherViewer.capturedThread == null) WeatherViewer.capturedThread = Thread.currentThread();
        else if (!WeatherViewer.capturedThread.equals(Thread.currentThread())) {
            LOGGER.log(Level.INFO, String.format("Thread %s is wait", Thread.currentThread().getName()));
            WeatherViewer.class.wait();
            LOGGER.log(Level.INFO, String.format("Thread %s is run", Thread.currentThread().getName()));
        } else weatherViewer.dispose();
    }

    public T getGeneral() {
        return general;
    }

    public void dispose() {
        synchronized (WeatherViewer.class) {
            weatherViewer = null;
            general.dispose();
            WeatherViewer.class.notify();
        }
    }
}
