package com.weather_viewer.gui.preview;

import javax.swing.*;
import java.awt.*;

public class Preview extends JFrame {
    private JPanel rootPanel;
    private JLabel gifLabel;

    public Preview() throws HeadlessException {
        setLocationRelativeTo(null);
        getContentPane().add(rootPanel);
        setUndecorated(true);

        setBackground(new Color(0, 0, 0, 0));

        pack();
        setResizable(false);
        setVisible(true);

    }
}
