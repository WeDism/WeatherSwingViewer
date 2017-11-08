package com.weather_viewer.functional_layer.structs.weather;


import java.util.Date;

public class Day implements IWeatherStruct {
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
    //region Main Characteristic
    private int humidity;
    private int pressure;
    private int temp;
    private int tempMax;
    private int tempMin;
    //endregion

    public Day(long dateTimeNumber, int windDegrees, double windSpeed, String weather, String weatherDescription, int humidity, int pressure, int temp, int tempMax, int tempMin) {
        this.dateTimeNumber = dateTimeNumber;
        dateTime = new Date(dateTimeNumber);
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
        return dateTimeNumber;
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
