package com.weather_viewer.main;

import com.db_layer.structures.Day;
import com.functional_layer.structs.location.concrete_location.City;
import com.functional_layer.structs.location.concrete_location.Country;
import com.functional_layer.weather_connector.IWeatherConnector;
import com.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
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
        IWeatherConnector<Day> connector = new ApiConnectorWeatherForDay<>(new City("London"), "", new Country("uk"));
        try {
            System.out.println(connector.requestAndGetWeatherStruct());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
