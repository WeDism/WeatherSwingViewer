package com.weather_viewer.functional_layer.exceptions;

public class EmptyCityException extends Exception {
    public EmptyCityException() {
        super("City must be filled");
    }
}
