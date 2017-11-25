package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.preview.Preview;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            UIManager.getLookAndFeelDefaults()
                    .put("defaultFont", new Font("Arial", Font.PLAIN, 14));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/config.properties"));
            City samara = new City(properties.getProperty("currentCity"));
            Country ru = new Country(properties.getProperty("countryCode"));

            IWeatherConnector<Workweek> connectorForecastForTheWorkWeek = ApiConnector.build(samara, ru, Workweek.class);
            IWeatherConnector<CurrentDay> connectorWeatherForDay = ApiConnector.build(samara, ru, CurrentDay.class);

            new General(new Preview(), connectorWeatherForDay, connectorForecastForTheWorkWeek);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }


    }
}
