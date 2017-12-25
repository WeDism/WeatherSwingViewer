package com.weather_viewer.functional_layer.structs.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public abstract class Location {
    protected String location;

    public Location(@NotNull String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return this.location;
    }

    @Override
    public int hashCode() {
        return this.location.toLowerCase().hashCode();
    }

    @Nullable
    public static <T extends Location> T newInstance(@NotNull T location) {
        try {
            return (T) location.getClass().getConstructor(String.class).newInstance(location.toString());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
