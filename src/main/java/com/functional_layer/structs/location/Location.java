package com.functional_layer.structs.location;

import com.functional_layer.structs.location.concrete_location.Country;

public abstract class Location {
    private String location;

    public Location(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Country && location.equals(((Location) o).location);

    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
