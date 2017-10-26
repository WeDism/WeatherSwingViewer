package com.weather_viewer.main;

import com.weather_viewer.gui.main_form.General;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.weather_viewer.gui.settings_form.Settings;

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
    }
}
