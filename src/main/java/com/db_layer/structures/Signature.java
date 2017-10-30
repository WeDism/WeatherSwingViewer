package com.db_layer.structures;

import com.functional_layer.structs.location.concrete_location.City;
import com.functional_layer.structs.location.concrete_location.Country;

public abstract class Signature {
    private City city;
    private Country country;

    protected Signature(City city, Country country) {
        this.city = city;
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }
}
