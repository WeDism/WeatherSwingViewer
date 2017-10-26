package com.weather_viewer.gui.main_form;

import com.toedter.calendar.JCalendar;
import com.weather_viewer.gui.settings_form.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class General extends JFrame {

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
    private JLabel cityLabel;
    private JLabel pictureLabel;
    private JLabel tempLabel;
    private JLabel humiditylabel;
    private JLabel pressureLabel;
    private JLabel valueTempLabel;
    private JLabel valueHumiditylabel;
    private JLabel valuePressurelabel;
    private JCalendar JCalendar1;
    private JSpinner spinnerHours;
    private JSpinner spinnerMinutes;
    private JSlider sliderMinutes;
    private JSlider sliderHours;
    private JButton installNotificationButton;
    private JLabel selectedDatestempLabel;
    private JLabel datestempLabel;
    private JSpinner spinner1;
    private JButton viewHistoryOfNotificationsButton;
    public static final Dimension MINIMUM_SIZE = new Dimension(500, 500);
    JFrame jFrame = this;

    public General() {

        setMinimumSize(MINIMUM_SIZE);
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        changeLocationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Settings(jFrame);
            }
        });
        rootPanel.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setResizable(false);
        setVisible(true);
    }


}
