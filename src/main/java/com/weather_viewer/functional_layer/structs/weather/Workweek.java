package com.weather_viewer.functional_layer.structs.weather;

import com.weather_viewer.functional_layer.structs.location.Location;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Workweek implements IWeatherStruct {
    private SignatureWorkDay signatureWorkDay;
    private List<Day> listForecasts;

    public Workweek(SignatureWorkDay signatureWorkDay, List<Day> workweek) {
        this.signatureWorkDay = signatureWorkDay;
        this.listForecasts = workweek;
    }

    public SignatureWorkDay getSignatureWorkDay() {
        return SignatureWorkDay.newInstance(signatureWorkDay);
    }

    @Override
    public Signature getSignature() {
        return this.getSignatureWorkDay();
    }

    public List<Day> getListForecasts() {
        return Collections.unmodifiableList(this.listForecasts);
    }

    public Day getForecast(int forecast) {
        return this.listForecasts.get(forecast);
    }

    public int sizeListForecasts() {
        return this.listForecasts.size();
    }

    public static class SignatureWorkDay extends Signature {
        private int countForecasts;

        public SignatureWorkDay(City city, Country country, int countForecasts) {
            super(city, country);
            this.countForecasts = countForecasts;
        }

        public int getCountForecasts() {
            return this.countForecasts;
        }

        @NotNull
        static SignatureWorkDay newInstance(SignatureWorkDay signatureWorkDay) {
            return new SignatureWorkDay(
                    Location.newInstance(signatureWorkDay.getCity()),
                    Location.newInstance(signatureWorkDay.getCountry()),
                    signatureWorkDay.getCountForecasts());
        }

    }
}
