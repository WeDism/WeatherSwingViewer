package com.functional_layer.weather_connector.consts;

public enum Country {
    USA("us"), Russia("ru"), UK("uk");
    private String name;

    Country(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
