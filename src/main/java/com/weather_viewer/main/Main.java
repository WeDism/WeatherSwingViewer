package com.weather_viewer.main;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.gui.main_form.General;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(General.class.getName());

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        General general = new General();


    }
}
