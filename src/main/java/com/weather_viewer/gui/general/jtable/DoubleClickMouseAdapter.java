package com.weather_viewer.gui.general.jtable;

import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

public class DoubleClickMouseAdapter extends MouseAdapter {
    private final JTable workweekJTable;
    private final JFrame general;
    private final AtomicReference<Workweek> workweek;

    public DoubleClickMouseAdapter(JTable workweekJTable, JFrame general, AtomicReference<Workweek> workweek) {
        this.workweekJTable = workweekJTable;
        this.general = general;
        this.workweek = workweek;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            super.mouseClicked(e);
            Day day = ((WorkweekTable) this.workweekJTable.getModel()).getValueAt(this.workweekJTable.rowAtPoint(e.getPoint()));
            new DayView(this.general, this.workweek.get(), day);
        }
    }
}
