package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorForecastForTheWorkweek;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.preview.Preview;

import javax.swing.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/config.properties"));
            City samara = new City(properties.getProperty("currentCity"));
            Country ru = new Country(properties.getProperty("countryCode"));

            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            IWeatherConnector<CurrentDay> connectorWeatherForDay
                    = new ApiConnectorWeatherForDay<>(samara, ru, CurrentDay.class);
            IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                    = new ApiConnectorForecastForTheWorkweek<>(samara, ru, Workweek.class);
            new General(new Preview(), connectorWeatherForDay, connectorForecastForTheWorkWeek);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }


    }
}
