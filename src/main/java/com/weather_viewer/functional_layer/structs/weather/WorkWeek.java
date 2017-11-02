package com.weather_viewer.functional_layer.structs.weather;

import com.weather_viewer.functional_layer.structs.location.Location;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkWeek implements IWeatherStruct {
    private SignatureWorkDay signatureWorkDay;
    private List<Day> workWeek;

    public WorkWeek(SignatureWorkDay signatureWorkDay, List<Day> workWeek) {
        this.signatureWorkDay = signatureWorkDay;
        this.workWeek = workWeek;
    }

    public Signature getSignatureWorkDay() {
        return SignatureWorkDay.newInstance(signatureWorkDay);
    }

    public List<Day> getWorkWeek() {
        return workWeek;
    }

    public static class SignatureWorkDay extends Signature {
        public SignatureWorkDay(City city, Country country) {
            super(city, country);
        }

        @NotNull
        static SignatureWorkDay newInstance(SignatureWorkDay signatureWorkDay) {
            return new SignatureWorkDay(
                    Location.newInstance(signatureWorkDay.getCity()),
                    Location.newInstance(signatureWorkDay.getCountry()));
        }

    }
}
