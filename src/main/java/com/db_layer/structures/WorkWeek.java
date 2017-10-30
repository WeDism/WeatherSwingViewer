package com.db_layer.structures;

import com.functional_layer.structs.location.concrete_location.City;
import com.functional_layer.structs.location.concrete_location.Country;

import java.util.List;

public class WorkWeek implements IWeatherStruct {
    private Signature signature;
    private List<Day> workWeek;

    public WorkWeek(Signature signature, List<Day> workWeek) {
        this.signature = signature;
        this.workWeek = workWeek;
    }

    public Signature getSignature() {
        return signature;
    }

    public List<Day> getWorkWeek() {
        return workWeek;
    }

    public static class SignatureWorkDay extends Signature {
        public SignatureWorkDay(City city, Country country) {
            super(city, country);
        }
    }
}
