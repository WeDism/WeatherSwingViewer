package com.weather_viewer.gui.general.jtable;

import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;

import static com.weather_viewer.gui.consts.Sign.CELSIUS;

public class WorkweekTable implements TableModel {
    private Workweek workweek;

    public WorkweekTable(Workweek workweek) {
        this.workweek = workweek;
    }

    @Override
    public int getRowCount() {
        return workweek.sizeListForecasts();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Date";
            case 1:
                return "Weather";
            case 2:
                return "Temperature";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Day.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Day day = workweek.getForecast(rowIndex);

        switch (columnIndex) {
            case 0:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(day.getDateTime());
            case 1:
                return day.getWeather();
            case 2:
                return day.getTemp() + CELSIUS;
            default:
                return null;
        }
    }

    public Day getValueAt(int rowIndex) {
        return workweek.getForecast(rowIndex);
    }

        @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
