package com.weather_viewer.gui.general.jtable;

import com.weather_viewer.functional_layer.application.Context;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

public class DoubleClickMouseAdapter extends MouseAdapter {
    private final JTable workweekJTable;
    private final AtomicReference<Workweek> workweek;
    private final DayView dayView;

    @SuppressWarnings("unchecked")
    public DoubleClickMouseAdapter(IContext context) {
        this.workweekJTable = ((JTable) context.get(JTable.class));
        this.workweek = ((AtomicReference<Workweek>) context.get(Workweek.class));
        this.dayView = new DayView(context);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            super.mouseClicked(e);
            Day day = ((WorkweekTable) this.workweekJTable.getModel()).getValueAt(this.workweekJTable.rowAtPoint(e.getPoint()));
            this.dayView.updateData(this.workweek.get(), day);
        }
    }
}
