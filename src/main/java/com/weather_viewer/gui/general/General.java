package com.weather_viewer.gui.general;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.toedter.calendar.JCalendar;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.gui.general.jtable.DoubleClickMouseAdapter;
import com.weather_viewer.gui.general.jtable.WorkweekTable;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.Settings;
import com.weather_viewer.gui.settings.SettingsFormDelegate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static com.weather_viewer.gui.consts.Sign.*;

public class General extends JFrame implements GeneralFormDelegate {

    //region Fields
    //region consts
    private static final Logger LOGGER;
    private final AtomicReference<CurrentDay> currentDay;
    private final AtomicReference<Workweek> workweek;
    //endregion
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
    //region JMenuBar elements
    private JMenu settingsMenu;
    private JMenu fileMenu;
    private JMenuItem changeLocationMenuItem;
    private JMenuItem saveDataPerDayMenuItem;
    private JMenuItem saveDataPerWeekMenuItem;
    private JMenuBar menuBar;
    private JTabbedPane tabbedPanel;
    //endregion
    private JCalendar jCalendar;
    private JSpinner spinnerHours;
    private JSpinner spinnerMinutes;
    private JSlider sliderMinutes;
    private JSlider sliderHours;
    private JButton installNotificationButton;
    private JButton viewHistoryOfNotificationsButton;
    private JSpinner spinner1;
    private JTable workweekJTable;
    private final Settings settings;
    private final StartPreview startPreview;
    //endregion


    static {
        LOGGER = Logger.getLogger(General.class.getName());
    }

    public General(StartPreview startPreview, Settings settings) throws HeadlessException {
        this.settings = settings;
        this.startPreview = startPreview;
        currentDay = new AtomicReference<>();
        workweek = new AtomicReference<>();

        initGeneral();
    }

    private void initGeneral() {
        addListeners();
        initJPanelForecast();
        initJCalendar();
        initJFrameSettings();

        pack();
        setResizable(false);
        setVisible(false);
    }


    private void addListeners() {
        changeLocationMenuItem.addActionListener(e -> {
            settings.setLocationRelativeTo(this);
            settings.resetUI();
        });




        rootPanel.registerKeyboardAction(e -> dispose()
                , KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public void dispose() {
        super.dispose();
        settings.dispose();
        WorkerService.getInstance().dispose();
    }

    private void initJPanelForecast() {
        workweekJTable = new JTable();
        workweekJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workweekJTable.addMouseListener(new DoubleClickMouseAdapter(workweekJTable, this, workweek));
        workweekJTable.setShowVerticalLines(false);
        workweekJTable.setFont(new Font("Arial", Font.PLAIN, 22));
        workweekJTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        workweekJTable.setRowHeight(workweekJTable.getRowHeight() + (workweekJTable.getRowHeight() / 2));
        JScrollPane workweekJSchoolPane = new JScrollPane(workweekJTable);
        forecastWorkweekPanelForJTable.add(workweekJSchoolPane);
    }

    private void initJFrameSettings() {
        setTitle("Weather viewer");
        setIconImage(startPreview.getIconImage());
        setMinimumSize(rootPanel.getMinimumSize());
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initJCalendar() {
        jCalendar.setFont(new Font("Arial", Font.BOLD, 18));
    }

    @Override
    public void onUpdateForm() {
        if (currentDay.get() != null && workweek != null) {
            updateWeatherDayPanel(currentDay.get());
            updateJPanelForecast(workweek.get());
        }
    }

    private void updateJPanelForecast(@NotNull Workweek workweek) {
        Workweek.SignatureWorkDay signatureWorkDay = workweek.getSignatureWorkDay();
        forecastLocationValueLabel.setText(String.format("%s, %s", signatureWorkDay.getCity(), signatureWorkDay.getCountry()));
        workweekJTable.setModel(new WorkweekTable(workweek));
        workweekJTable.getColumnModel().getColumn(0)
                .setMinWidth(String.valueOf(workweekJTable.getModel().getValueAt(0, 0)).length() + 100);
    }

    private void updateWeatherDayPanel(@NotNull CurrentDay currentDay) {
        CurrentDay.SignatureCurrentDay signatureCurrentDay = currentDay.getSignatureCurrentDay();
        cityAndCountryLabel.setText(String.format("%s, %s", signatureCurrentDay.getCity(), signatureCurrentDay.getCountry()));
        valueTempLabel.setText(currentDay.getTemp() + CELSIUS);
        valueHumiditylabel.setText(currentDay.getHumidity() + HUMIDITY);
        valuePressurelabel.setText(String.format("%s, %s", currentDay.getPressure(), PRESSURE));
        valueWeatherLabel.setText(String.format("%s (%s)", currentDay.getWeather(), currentDay.getWeatherDescription()));
    }

    @Override
    public void onPerform() {
        if (!isVisible()) {
            setVisible(true);
            startPreview.dispose();
        }
    }

    @Override
    public AtomicReference<CurrentDay> getCurrentDay() {
        return currentDay;
    }

    @Override
    public AtomicReference<Workweek> getWorkweek() {
        return workweek;
    }

    @Override
    public SettingsFormDelegate getSettingsForm() {
        return settings;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.setMinimumSize(new Dimension(600, 600));
        rootPanel.setPreferredSize(new Dimension(600, 600));
        menuBarPanel = new JPanel();
        menuBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        rootPanel.add(menuBarPanel, BorderLayout.NORTH);
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        menuBar.setOpaque(false);
        menuBar.setPreferredSize(new Dimension(490, 35));
        menuBarPanel.add(menuBar);
        fileMenu = new JMenu();
        fileMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Font fileMenuFont = this.$$$getFont$$$("Arial", -1, 20, fileMenu.getFont());
        if (fileMenuFont != null) fileMenu.setFont(fileMenuFont);
        fileMenu.setText("File");
        menuBar.add(fileMenu);
        saveDataPerWeekMenuItem = new JMenuItem();
        saveDataPerWeekMenuItem.setText("Save data per week");
        fileMenu.add(saveDataPerWeekMenuItem);
        saveDataPerDayMenuItem = new JMenuItem();
        saveDataPerDayMenuItem.setSelected(false);
        saveDataPerDayMenuItem.setText("Save data per day");
        fileMenu.add(saveDataPerDayMenuItem);
        settingsMenu = new JMenu();
        settingsMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Font settingsMenuFont = this.$$$getFont$$$("Arial", -1, 20, settingsMenu.getFont());
        if (settingsMenuFont != null) settingsMenu.setFont(settingsMenuFont);
        settingsMenu.setText("Settings");
        menuBar.add(settingsMenu);
        changeLocationMenuItem = new JMenuItem();
        changeLocationMenuItem.setText("Change location");
        settingsMenu.add(changeLocationMenuItem);
        tabbedPanePanel = new JPanel();
        tabbedPanePanel.setLayout(new BorderLayout(0, 0));
        rootPanel.add(tabbedPanePanel, BorderLayout.CENTER);
        tabbedPanel = new JTabbedPane();
        Font tabbedPanelFont = this.$$$getFont$$$("Arial", -1, 22, tabbedPanel.getFont());
        if (tabbedPanelFont != null) tabbedPanel.setFont(tabbedPanelFont);
        tabbedPanePanel.add(tabbedPanel, BorderLayout.CENTER);
        weatherDayPanel = new JPanel();
        weatherDayPanel.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        Font weatherDayPanelFont = this.$$$getFont$$$("Arial", Font.BOLD, 36, weatherDayPanel.getFont());
        if (weatherDayPanelFont != null) weatherDayPanel.setFont(weatherDayPanelFont);
        weatherDayPanel.setMinimumSize(new Dimension(600, 500));
        weatherDayPanel.setPreferredSize(new Dimension(600, 500));
        tabbedPanel.addTab("Weather for the Day", weatherDayPanel);
        locationLabel = new JLabel();
        Font locationLabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, locationLabel.getFont());
        if (locationLabelFont != null) locationLabel.setFont(locationLabelFont);
        locationLabel.setText("Location");
        weatherDayPanel.add(locationLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        weatherLabel = new JLabel();
        Font weatherLabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, weatherLabel.getFont());
        if (weatherLabelFont != null) weatherLabel.setFont(weatherLabelFont);
        weatherLabel.setText("Weather");
        weatherDayPanel.add(weatherLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        weatherDayPanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cityAndCountryLabel = new JLabel();
        Font cityAndCountryLabelFont = this.$$$getFont$$$("Arial", -1, 28, cityAndCountryLabel.getFont());
        if (cityAndCountryLabelFont != null) cityAndCountryLabel.setFont(cityAndCountryLabelFont);
        cityAndCountryLabel.setText("City And Country");
        weatherDayPanel.add(cityAndCountryLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        weatherDayPanel.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        pictureLabel = new JLabel();
        pictureLabel.setText("Picture");
        weatherDayPanel.add(pictureLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        humiditylabel = new JLabel();
        Font humiditylabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, humiditylabel.getFont());
        if (humiditylabelFont != null) humiditylabel.setFont(humiditylabelFont);
        humiditylabel.setText("Humidity");
        weatherDayPanel.add(humiditylabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        weatherDayPanel.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pressureLabel = new JLabel();
        Font pressureLabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, pressureLabel.getFont());
        if (pressureLabelFont != null) pressureLabel.setFont(pressureLabelFont);
        pressureLabel.setText("Pressure");
        weatherDayPanel.add(pressureLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        weatherDayPanel.add(spacer4, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        valuePressurelabel = new JLabel();
        Font valuePressurelabelFont = this.$$$getFont$$$("Arial", -1, 28, valuePressurelabel.getFont());
        if (valuePressurelabelFont != null) valuePressurelabel.setFont(valuePressurelabelFont);
        valuePressurelabel.setText("value");
        weatherDayPanel.add(valuePressurelabel, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueHumiditylabel = new JLabel();
        Font valueHumiditylabelFont = this.$$$getFont$$$("Arial", -1, 28, valueHumiditylabel.getFont());
        if (valueHumiditylabelFont != null) valueHumiditylabel.setFont(valueHumiditylabelFont);
        valueHumiditylabel.setText("value");
        weatherDayPanel.add(valueHumiditylabel, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueTempLabel = new JLabel();
        Font valueTempLabelFont = this.$$$getFont$$$("Arial", -1, 28, valueTempLabel.getFont());
        if (valueTempLabelFont != null) valueTempLabel.setFont(valueTempLabelFont);
        valueTempLabel.setText("value");
        weatherDayPanel.add(valueTempLabel, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tempLabel = new JLabel();
        Font tempLabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, tempLabel.getFont());
        if (tempLabelFont != null) tempLabel.setFont(tempLabelFont);
        tempLabel.setText("Temperature");
        weatherDayPanel.add(tempLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        weatherDayPanel.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        valueWeatherLabel = new JLabel();
        Font valueWeatherLabelFont = this.$$$getFont$$$("Arial", -1, 28, valueWeatherLabel.getFont());
        if (valueWeatherLabelFont != null) valueWeatherLabel.setFont(valueWeatherLabelFont);
        valueWeatherLabel.setText("value");
        weatherDayPanel.add(valueWeatherLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forecastWorkweekPanel = new JPanel();
        forecastWorkweekPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPanel.addTab("Forecast for the Workweek", forecastWorkweekPanel);
        forecastLocationLabel = new JLabel();
        Font forecastLocationLabelFont = this.$$$getFont$$$("Arial", Font.BOLD, 28, forecastLocationLabel.getFont());
        if (forecastLocationLabelFont != null) forecastLocationLabel.setFont(forecastLocationLabelFont);
        forecastLocationLabel.setText("Location");
        forecastWorkweekPanel.add(forecastLocationLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        forecastWorkweekPanel.add(spacer6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        forecastLocationValueLabel = new JLabel();
        Font forecastLocationValueLabelFont = this.$$$getFont$$$("Arial", -1, 28, forecastLocationValueLabel.getFont());
        if (forecastLocationValueLabelFont != null) forecastLocationValueLabel.setFont(forecastLocationValueLabelFont);
        forecastLocationValueLabel.setText("value");
        forecastWorkweekPanel.add(forecastLocationValueLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forecastWorkweekPanelForJTable = new JPanel();
        forecastWorkweekPanelForJTable.setLayout(new BorderLayout(0, 0));
        forecastWorkweekPanel.add(forecastWorkweekPanelForJTable, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new GridLayoutManager(12, 7, new Insets(0, 0, 0, 0), -1, -1));
        notificationPanel.setFocusable(true);
        tabbedPanel.addTab("Notification", notificationPanel);
        final Spacer spacer7 = new Spacer();
        notificationPanel.add(spacer7, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        spinnerHours = new JSpinner();
        Font spinnerHoursFont = this.$$$getFont$$$("Arial", -1, 28, spinnerHours.getFont());
        if (spinnerHoursFont != null) spinnerHours.setFont(spinnerHoursFont);
        notificationPanel.add(spinnerHours, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerMinutes = new JSpinner();
        Font spinnerMinutesFont = this.$$$getFont$$$("Arial", -1, 28, spinnerMinutes.getFont());
        if (spinnerMinutesFont != null) spinnerMinutes.setFont(spinnerMinutesFont);
        notificationPanel.add(spinnerMinutes, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        notificationPanel.add(spacer8, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectedDatestempLabel = new JLabel();
        selectedDatestempLabel.setText("");
        notificationPanel.add(selectedDatestempLabel, new GridConstraints(8, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        notificationPanel.add(spacer9, new GridConstraints(10, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        datestempLabel = new JLabel();
        Font datestempLabelFont = this.$$$getFont$$$("Arial", -1, 28, datestempLabel.getFont());
        if (datestempLabelFont != null) datestempLabel.setFont(datestempLabelFont);
        datestempLabel.setText("Datestamp");
        notificationPanel.add(datestempLabel, new GridConstraints(8, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewHistoryOfNotificationsButton = new JButton();
        Font viewHistoryOfNotificationsButtonFont = this.$$$getFont$$$("Arial", -1, 24, viewHistoryOfNotificationsButton.getFont());
        if (viewHistoryOfNotificationsButtonFont != null)
            viewHistoryOfNotificationsButton.setFont(viewHistoryOfNotificationsButtonFont);
        viewHistoryOfNotificationsButton.setText("History");
        notificationPanel.add(viewHistoryOfNotificationsButton, new GridConstraints(9, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        installNotificationButton = new JButton();
        Font installNotificationButtonFont = this.$$$getFont$$$("Arial", -1, 24, installNotificationButton.getFont());
        if (installNotificationButtonFont != null) installNotificationButton.setFont(installNotificationButtonFont);
        installNotificationButton.setText("Install Notification");
        notificationPanel.add(installNotificationButton, new GridConstraints(11, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wantedTemperatureLabel = new JLabel();
        Font wantedTemperatureLabelFont = this.$$$getFont$$$("Arial", -1, 28, wantedTemperatureLabel.getFont());
        if (wantedTemperatureLabelFont != null) wantedTemperatureLabel.setFont(wantedTemperatureLabelFont);
        wantedTemperatureLabel.setText("Wanted temperature");
        notificationPanel.add(wantedTemperatureLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        notificationPanel.add(spacer10, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        spinner1 = new JSpinner();
        Font spinner1Font = this.$$$getFont$$$("Arial", -1, 28, spinner1.getFont());
        if (spinner1Font != null) spinner1.setFont(spinner1Font);
        notificationPanel.add(spinner1, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        notificationPanel.add(spacer11, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Arial", -1, 28, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("°C");
        notificationPanel.add(label1, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        notificationPanel.add(spacer12, new GridConstraints(2, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        notificationPanel.add(spacer13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        sliderMinutes = new JSlider();
        sliderMinutes.setMaximum(59);
        sliderMinutes.setOrientation(1);
        sliderMinutes.setValue(0);
        notificationPanel.add(sliderMinutes, new GridConstraints(0, 5, 6, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sliderHours = new JSlider();
        sliderHours.setMaximum(59);
        sliderHours.setOrientation(1);
        sliderHours.setValue(0);
        notificationPanel.add(sliderHours, new GridConstraints(0, 2, 6, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jCalendar = new JCalendar();
        notificationPanel.add(jCalendar, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 300), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
