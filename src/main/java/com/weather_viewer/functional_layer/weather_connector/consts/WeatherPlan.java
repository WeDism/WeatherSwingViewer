package com.weather_viewer.functional_layer.weather_connector.consts;

public enum WeatherPlan  {
    Weather("weather"), ForecastForTheWorkWeek("forecast");
    private String name;

    WeatherPlan(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
