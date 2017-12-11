package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherViewer<T extends General> {

    private static final Logger LOGGER = Logger.getLogger(WeatherViewer.class.getName());

    static {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            UIManager.getLookAndFeelDefaults()
                    .put("defaultFont", new Font("Arial", Font.PLAIN, 14));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }


    private final IWeatherConnector<Workweek> connectorForecastForTheWorkWeek;
    private final IWeatherConnector<CurrentDay> connectorWeatherForDay;
    private final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay;
    private final Callable<T> callable;
    private static WeatherViewer weatherViewer;
    private T general;

    private WeatherViewer(Properties startUpConf) {
        City samara = new City(startUpConf.getProperty("currentCity"));
        Country ru = new Country(startUpConf.getProperty("countryCode"));

        this.connectorForecastForTheWorkWeek = ApiConnector.build(samara, ru, Workweek.class);
        this.connectorWeatherForDay = ApiConnector.build(samara, ru, CurrentDay.class);
        this.connectorSignatureDay = ApiConnector.build(CurrentDay.SignatureCurrentDay.class);
        this.callable = () -> (T) new General(new StartPreview(), new Settings());
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
        executorService.shutdown();
    }

    private void buildWorkerService() {
        WorkerService.build(this.connectorWeatherForDay, this.connectorForecastForTheWorkWeek, this.connectorSignatureDay, this.general);
    }

    public WeatherViewer start() throws ExecutionException, InterruptedException {
        createGui();
        buildWorkerService();
        return this;
    }

    public static WeatherViewer getInstance(Properties startUpConf) {
        if (weatherViewer != null)
            return weatherViewer;
        weatherViewer = new WeatherViewer(startUpConf);
        return weatherViewer;
    }

    public static <T extends General> WeatherViewer getInstance(IWeatherConnector<CurrentDay> connectorWeatherForDay,
                                                                IWeatherConnector<Workweek> connectorForecastForTheWorkWeek,
                                                                IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay,
                                                                Callable<T> callable) {
        if (weatherViewer != null)
            return weatherViewer;
        weatherViewer = new WeatherViewer<>(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, callable);
        return weatherViewer;
    }

    public T getGeneral() {
        return general;
    }

    public static WeatherViewer getInstance() throws IllegalStateException {
        if (weatherViewer != null)
            return weatherViewer;
        throw new IllegalStateException();
    }

    public void dispose() {
        general.dispose();
        weatherViewer = null;
    }
}
