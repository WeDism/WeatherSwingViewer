package com.weather_viewer.functional_layer.exceptions;

public class EmptyCountryException extends Exception {
    public EmptyCountryException() {
        super("Country must be filled");
    }
}
