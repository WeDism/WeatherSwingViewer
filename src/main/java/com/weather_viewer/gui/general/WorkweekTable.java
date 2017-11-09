package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.Workweek;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;

import static com.weather_viewer.gui.consts.Sign.*;

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
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Date";
            case 1:
                return "Weather";
            case 2:
                return "Humidity";
            case 3:
                return "Temperature";
            case 4:
                return "Pressure";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Day day = workweek.getForecast(rowIndex);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:'T'HH:mm:ss");

        switch (columnIndex) {
            case 0:
                return formatter.format(day.getDateTime());
            case 1:
                return day.getWeather();
            case 2:
                return day.getHumidity() + HUMIDITY;
            case 3:
                return day.getTemp() + CELSIUS;
            case 4:
                return String.format("%s, %s", day.getPressure(), PRESSURE);
            default:
                return null;
        }
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
