package com.weather_viewer.functional_layer.structs.location;

import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public abstract class Location {
    private String location;

    public Location(@NotNull String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return this.location;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Country && this.location.equals(((Location) o).location);

    }

    @Override
    public int hashCode() {
        return this.location.hashCode();
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
