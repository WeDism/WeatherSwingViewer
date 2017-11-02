package com.weather_viewer.gui.main_form;

import com.toedter.calendar.JCalendar;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.settings_form.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.weather_viewer.gui.consts.Sign.*;

public class General extends JFrame {

    private final Logger logger = Logger.getLogger(General.class.getName());
    private JPanel rootPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu settingsMenu;
    private JMenuItem saveDataPerWeekMenuItem;
    private JMenuItem changeLocationMenuItem;
    private JTabbedPane tabbedPanel;
    private JPanel menuBarPanel;
    private JPanel tabbedPanePanel;
    private JPanel weatherDayPanel;
    private JPanel forecastWeekPanel;
    private JMenuItem saveDataPerDayMenuItem;
    private JLabel locationLabel;
    private JLabel weatherLabel;
    private JLabel cityAndCountryLabel;
    private JLabel pictureLabel;
    private JLabel tempLabel;
    private JLabel humiditylabel;
    private JLabel pressureLabel;
    private JLabel valueTempLabel;
    private JLabel valueHumiditylabel;
    private JLabel valuePressurelabel;
    private JCalendar JCalendar;
    private JSpinner spinnerHours;
    private JSpinner spinnerMinutes;
    private JSlider sliderMinutes;
    private JSlider sliderHours;
    private JButton installNotificationButton;
    private JLabel selectedDatestempLabel;
    private JLabel datestempLabel;
    private JSpinner spinner1;
    private JButton viewHistoryOfNotificationsButton;
    private JLabel valueWeatherLabel;
    private JPanel notificationPanel;
    private JLabel wantedTemperatureLabel;
    private static final Dimension MINIMUM_SIZE = new Dimension(500, 500);
    private JFrame jFrame = this;
    private IWeatherConnector<CurrentDay> connector;
    private final Timer timer;
    private int counterResponses;

    public General() {

        setMinimumSize(MINIMUM_SIZE);
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        changeLocationMenuItem.addActionListener(e -> new Settings(jFrame));
        rootPanel.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        City samara = new City("Samara");
        String a931917869669cee8ee1da9fb35d3dd3 = "a931917869669cee8ee1da9fb35d3dd3";
        Country ru = new Country("ru");

        CurrentDay currentDay = getCurrentDay(samara, a931917869669cee8ee1da9fb35d3dd3, ru);
        updateWeatherDayPanel(currentDay);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateWeatherDayPanel(getCurrentDay(samara, a931917869669cee8ee1da9fb35d3dd3, ru));
            }
        }, TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS.toMillis(30));

        pack();
        setResizable(false);
        setVisible(true);
    }

    private CurrentDay getCurrentDay(City city, String appId, Country country) {
        CurrentDay currentDay = null;
        try {
            connector = new ApiConnectorWeatherForDay<>(city, appId, country, CurrentDay.class);
            currentDay = connector.requestAndGetWeatherStruct();
            logger.log(Level.INFO, "Response number is {0}", ++counterResponses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDay;
    }

    private void updateWeatherDayPanel(CurrentDay currentDay) {
        CurrentDay.SignatureCurrentDay signatureCurrentDay = currentDay.getSignatureCurrentDay();
        cityAndCountryLabel.setText(String.format("%s, %s", signatureCurrentDay.getCity(), signatureCurrentDay.getCountry()));
        valueTempLabel.setText((String.valueOf(currentDay.getTemp()) + CELSIUS));
        valueHumiditylabel.setText((String.valueOf(currentDay.getHumidity()) + HUMIDITY));
        valuePressurelabel.setText(String.format("%s, %s", String.valueOf(currentDay.getPressure()), PRESSURE));
        valueWeatherLabel.setText(String.format("%s (%s)", currentDay.getWeather(), currentDay.getWeatherDescription()));
    }


}
