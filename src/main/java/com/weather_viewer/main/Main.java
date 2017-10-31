package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.db_layer.structures.CurrentDay;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.main_form.General;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
        }
        General general = new General();
        try {
            IWeatherConnector<CurrentDay> connector =
                    new ApiConnectorWeatherForDay<>
                            (new City("London"), "a931917869669cee8ee1da9fb35d3dd3", new Country("uk"), CurrentDay.class);

            System.out.println(connector.requestAndGetWeatherStruct());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
