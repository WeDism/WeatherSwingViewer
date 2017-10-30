package com.db_layer.structures;


import com.functional_layer.structs.location.concrete_location.City;
import com.functional_layer.structs.location.concrete_location.Country;

public class Day implements IWeatherStruct {
    private Signature signature;
    private Data data;
    private Weather weather;
    private Wind wind;

    public Day(Signature signature, Data data, Weather weather, Wind wind) {
        this.signature = signature;
        this.data = data;
        this.weather = weather;
        this.wind = wind;
    }

    public Signature getSignature() {
        return signature;
    }

    public Data getData() {
        return data;
    }

    public Weather getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public class Wind {
        private int degrees;
        private double speed;

        public Wind(int degrees, double speed) {
            this.degrees = degrees;
            this.speed = speed;
        }
    }

    public static class Weather {
        private String weather;
        private String description;

        public Weather(String weather, String description) {
            this.weather = weather;
            this.description = description;
        }

        public String getWeather() {
            return weather;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Data {
        private int humidity;
        private int pressure;
        private int temp;
        private int tempMax;
        private int tempMin;
        private long dateTime;

        public Data(int humidity, int pressure, int temp, int tempMax, int tempMin, long dateTime) {
            this.humidity = humidity;
            this.pressure = pressure;
            this.temp = temp;
            this.tempMax = tempMax;
            this.tempMin = tempMin;
            this.dateTime = dateTime;
        }
    }

    public static class SignatureDay extends Signature {
        private long sunriseDateTime;
        private long sunsetDateTime;

        public SignatureDay(City city, Country country, long sunriseDateTime, long sunsetDateTime) {
            super(city, country);
            this.sunriseDateTime = sunriseDateTime;
            this.sunsetDateTime = sunsetDateTime;
        }

        public long getSunriseDateTime() {
            return sunriseDateTime;
        }

        public long getSunsetDateTime() {
            return sunsetDateTime;
        }
    }
}
