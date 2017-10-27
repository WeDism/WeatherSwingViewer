package com.functional_layer.weather_connector.consts;

public enum UriScheme {
    http("http://"), https("https://");
    private String name;

    UriScheme(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
