package com.weather_viewer.functional_layer.structs.weather;


public class Day implements IWeatherStruct {
    //<editor-fold defaultstate="collapsed" desc="Data">
    private long dateTime;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Wind">
    private int windDegrees;
    private double windSpeed;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Weather">
    private String weather;
    private String weatherDescription;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Main Characteristic">
    private int humidity;
    private int pressure;
    private int temp;
    private int tempMax;
    private int tempMin;
    //</editor-fold>

    public Day(long dateTime, int windDegrees, double windSpeed, String weather, String weatherDescription, int humidity, int pressure, int temp, int tempMax, int tempMin) {
        this.dateTime = dateTime;
        this.windDegrees = windDegrees;
        this.windSpeed = windSpeed;
        this.weather = weather;
        this.weatherDescription = weatherDescription;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temp = temp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public long getDateTime() {
        return dateTime;
    }

    public int getWindDegrees() {
        return windDegrees;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeather() {
        return weather;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getTemp() {
        return temp;
    }

    public int getTempMax() {
        return tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

}
