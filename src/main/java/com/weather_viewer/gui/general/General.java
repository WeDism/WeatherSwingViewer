package com.weather_viewer.gui.general;

import com.toedter.calendar.JCalendar;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorForecastForTheWorkWeek;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.preview.Preview;
import com.weather_viewer.gui.settings.Settings;
import com.weather_viewer.main.MainPaths;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.weather_viewer.gui.consts.Sign.*;

public class General extends JFrame {

    //region Fields
    private final Logger logger = Logger.getLogger(General.class.getName());
    //region Labels
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
    private JLabel selectedDatestempLabel;
    private JLabel datestempLabel;
    private JLabel wantedTemperatureLabel;
    private JLabel valueWeatherLabel;
    private JLabel forecastLocationLabel;
    private JLabel forecastLocationValueLabel;
    private JPanel forecastWorkweekPanelForJTable;
    //endregion
    //region JPanels
    private JPanel rootPanel;
    private JPanel menuBarPanel;
    private JPanel tabbedPanePanel;
    private JPanel weatherDayPanel;
    private JPanel forecastWorkweekPanel;
    private JPanel notificationPanel;
    //endregion
    private JMenu fileMenu;
    private JMenu settingsMenu;
    private JMenuItem saveDataPerWeekMenuItem;
    private JMenuItem changeLocationMenuItem;
    private JTabbedPane tabbedPanel;
    private JMenuItem saveDataPerDayMenuItem;
    private JCalendar JCalendar;
    private JSpinner spinnerHours;
    private JSpinner spinnerMinutes;
    private JSlider sliderMinutes;
    private JSlider sliderHours;
    private JButton installNotificationButton;
    private JButton viewHistoryOfNotificationsButton;
    private JSpinner spinner1;
    private JMenuBar menuBar;
    private JFrame jFrame = this;
    private IWeatherConnector<CurrentDay> connectorCurrentDay;
    private IWeatherConnector<Workweek> connectorWorkweek;
    private final Timer timer;
    private int counterResponses;
    private CurrentDay currentDay;
    private Workweek workweek;
    private JTable workweekJTable;
    private JScrollPane workweekJScroollPane;
    //endregion

    public General() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(MainPaths.CONFIG_PATH));
        City samara = new City(properties.getProperty("currentCity"));
        String valueAppId = properties.getProperty("appId");
        Country ru = new Country(properties.getProperty("countryCode"));

        timer = new Timer();
        initTimer(samara, valueAppId, ru);

        initGeneral();
    }

    private void initGeneral() {

        Preview preview = new Preview();
        addListeners();

        setMinimumSize(rootPanel.getMinimumSize());
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        for (int i = 0; workweek == null && currentDay == null && i < 10_000; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
        preview.dispose();
        Workweek.SignatureWorkDay signatureWorkDay = workweek.getSignatureWorkDay();
        forecastLocationValueLabel.setText(String.format("%s, %s", signatureWorkDay.getCity(), signatureWorkDay.getCountry()));
        initJTable();

        pack();
        setResizable(false);
        setVisible(true);
    }

    private void initJTable() {
        workweekJTable = new JTable(new WorkweekTable(workweek));
        workweekJScroollPane = new JScrollPane(workweekJTable);
        forecastWorkweekPanelForJTable.add(workweekJScroollPane);
    }

    private void addListeners() {
        changeLocationMenuItem.addActionListener(e -> new Settings(jFrame));

        rootPanel.registerKeyboardAction(e -> {
            timer.cancel();
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initTimer(City city, String valueAppId, Country country) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateWeatherDayPanel(getCurrentDay(city, valueAppId, country));
                workweek = getWorkweek(city, valueAppId, country);
            }
        }, 0, TimeUnit.SECONDS.toMillis(30));
    }

    private CurrentDay getCurrentDay(City city, String appId, Country country) {
        CurrentDay currentDay = null;
        try {
            connectorCurrentDay = new ApiConnectorWeatherForDay<>(city, appId, country, CurrentDay.class);
            currentDay = connectorCurrentDay.requestAndGetWeatherStruct();
            logger.log(Level.INFO, "Current Day connector response number is {0}", ++counterResponses);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return currentDay;
    }

    private Workweek getWorkweek(City city, String appId, Country country) {
        Workweek workweek = null;
        try {
            connectorWorkweek = new ApiConnectorForecastForTheWorkWeek<>(city, appId, country, Workweek.class);
            workweek = connectorWorkweek.requestAndGetWeatherStruct();
            logger.log(Level.INFO, "Workweek connector response number is {0}", ++counterResponses);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return workweek;
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
