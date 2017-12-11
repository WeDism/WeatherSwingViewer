package com.weather_viewer.functional_layer.structs.weather;


import java.util.Date;

public class Day {
    //region Data
    private long dateTimeNumber;
    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    //endregion
    //region Wind
    private int windDegrees;
    private double windSpeed;
    //endregion
    //region Weather
    private String weather;
    private String weatherDescription;
    //endregion
    //region Characteristic
    private int humidity;
    private int pressure;
    private int temp;
    private int tempMax;
    private int tempMin;
    //endregion

    public Day(long dateTimeNumber, int windDegrees, double windSpeed, String weather, String weatherDescription, int humidity, int pressure, int temp, int tempMax, int tempMin) {
        this.dateTimeNumber = dateTimeNumber;
        this.dateTime = new Date(dateTimeNumber);
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

    public long getDateTimeNumber() {
        return this.dateTimeNumber;
    }

    public int getWindDegrees() {
        return this.windDegrees;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public String getWeather() {
        return this.weather;
    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public int getPressure() {
        return this.pressure;
    }

    public int getTemp() {
        return this.temp;
    }

    public int getTempMax() {
        return this.tempMax;
    }

    public int getTempMin() {
        return this.tempMin;
    }

}
