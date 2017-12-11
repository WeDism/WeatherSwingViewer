package com.weather_viewer.main;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/config.properties"));

            WeatherViewer.getInstance(properties).start();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }


    }
}
