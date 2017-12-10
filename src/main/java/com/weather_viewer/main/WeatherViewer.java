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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherViewer {

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
    private General general;

    public WeatherViewer(Properties startUpConf) throws InterruptedException, ExecutionException {
        City samara = new City(startUpConf.getProperty("currentCity"));
        Country ru = new Country(startUpConf.getProperty("countryCode"));

        connectorForecastForTheWorkWeek = ApiConnector.build(samara, ru, Workweek.class);
        connectorWeatherForDay = ApiConnector.build(samara, ru, CurrentDay.class);
        connectorSignatureDay = ApiConnector.build(CurrentDay.SignatureCurrentDay.class);


    }

    private void createGui() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<General> futureGeneral = executorService.submit(() -> new General(new StartPreview(), new Settings()));
        general = futureGeneral.get();
        //Add this Listener because it is should to run all tests
        general.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        executorService.shutdown();
    }

    private void buildWorkerService() {
        WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, general);
    }

    public void start() throws ExecutionException, InterruptedException {
        createGui();
        buildWorkerService();
    }
}
