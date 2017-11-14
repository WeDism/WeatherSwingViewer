package com.weather_viewer.gui.general;

import com.toedter.calendar.JCalendar;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.preview.Preview;
import com.weather_viewer.gui.settings.Settings;

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

    //region Fields
    private final Logger LOGGER = Logger.getLogger(General.class.getName());
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
    private JCalendar jCalendar;
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
    private Preview preview;
    //endregion

    public General(Preview preview,
                   IWeatherConnector<CurrentDay> connectorCurrentDay,
                   IWeatherConnector<Workweek> connectorWorkweek) throws Exception {
        this.connectorCurrentDay = connectorCurrentDay;
        this.connectorWorkweek = connectorWorkweek;
        timer = new Timer();


        initTimer(connectorCurrentDay, connectorWorkweek, null, null, null);
        initGeneral(preview);
    }

    private void initGeneral(Preview preview) {
        addListeners();
        initJPanelForecast();
        initJCalendar();

        setMinimumSize(rootPanel.getMinimumSize());
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        for (int i = 0; (workweek == null || currentDay == null) && i < 10_000; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        preview.dispose();
        if (workweek != null && currentDay != null) {
            pack();
            setResizable(false);
            setVisible(true);
        } else exit();
    }

    private void addListeners() {
        changeLocationMenuItem.addActionListener(e -> new Settings(jFrame));

        rootPanel.registerKeyboardAction(e -> exit()
                , KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initTimer(
            IWeatherConnector<CurrentDay> connectorCurrentDay, IWeatherConnector<Workweek> connectorWorkweek,
            City city, String valueAppId, Country country) {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentDay = getIWeatherStruct(connectorCurrentDay, CurrentDay.class, city, valueAppId, country);
                updateWeatherDayPanel(currentDay);
                workweek = getIWeatherStruct(connectorWorkweek, Workweek.class, city, valueAppId, country);
                updateJPanelForecast(workweek);
            }
        }, 0, TimeUnit.SECONDS.toMillis(30));
    }

    private <T extends IWeatherStruct> T getIWeatherStruct(IWeatherConnector<T> iWeatherConnector, Class<T> clazz,
                                                           City city, String appId, Country country) {
        T iWeatherStruct = null;
        try {
            if (city != null && appId != null && country != null) iWeatherConnector.setNewData(city, country);
            iWeatherStruct = iWeatherConnector.requestAndGetWeatherStruct();
            LOGGER.log(Level.INFO, "{0} connector response number is {1}", new Object[]{clazz.getSimpleName(), ++counterResponses});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            System.exit(0);
        }
        return iWeatherStruct;
    }

    private void initJPanelForecast() {
        workweekJTable = new JTable();
        workweekJTable.setShowVerticalLines(false);
        workweekJTable.setFont(new Font("Arial", Font.PLAIN, 22));
        workweekJTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        workweekJTable.setRowHeight(workweekJTable.getRowHeight() + (workweekJTable.getRowHeight() / 2));
        workweekJScroollPane = new JScrollPane(workweekJTable);
        forecastWorkweekPanelForJTable.add(workweekJScroollPane);
    }

    private void initJCalendar() {
        jCalendar.setFont(new Font("Arial", Font.BOLD, 18));
    }

    private void updateJPanelForecast(Workweek workweek) {
        Workweek.SignatureWorkDay signatureWorkDay = workweek.getSignatureWorkDay();
        forecastLocationValueLabel.setText(String.format("%s, %s", signatureWorkDay.getCity(), signatureWorkDay.getCountry()));
        workweekJTable.setModel(new WorkweekTable(workweek));
        workweekJTable.getColumnModel().getColumn(0)
                .setMinWidth(String.valueOf(workweekJTable.getModel().getValueAt(0, 0)).length() + 100);
    }

    private void updateWeatherDayPanel(CurrentDay currentDay) {
        CurrentDay.SignatureCurrentDay signatureCurrentDay = currentDay.getSignatureCurrentDay();
        cityAndCountryLabel.setText(String.format("%s, %s", signatureCurrentDay.getCity(), signatureCurrentDay.getCountry()));
        valueTempLabel.setText((String.valueOf(currentDay.getTemp()) + CELSIUS));
        valueHumiditylabel.setText((String.valueOf(currentDay.getHumidity()) + HUMIDITY));
        valuePressurelabel.setText(String.format("%s, %s", String.valueOf(currentDay.getPressure()), PRESSURE));
        valueWeatherLabel.setText(String.format("%s (%s)", currentDay.getWeather(), currentDay.getWeatherDescription()));
    }

    private void exit() {
        timer.cancel();
        this.dispose();
    }
}
