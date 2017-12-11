package com.weather_viewer.functional_layer.structs.weather;

import com.weather_viewer.functional_layer.structs.location.Location;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.jetbrains.annotations.NotNull;

public class CurrentDay extends Day implements IWeatherStruct{
    private SignatureCurrentDay signatureCurrentDay;

    public CurrentDay(long dateTime, int windDegrees, double windSpeed, String weather, String weatherDescription, int humidity, int pressure, int temp, int tempMax, int tempMin, SignatureCurrentDay signatureCurrentDay) {
        super(dateTime, windDegrees, windSpeed, weather, weatherDescription, humidity, pressure, temp, tempMax, tempMin);
        this.signatureCurrentDay = signatureCurrentDay;
    }

    public CurrentDay(Day day, SignatureCurrentDay signatureCurrentDay) {
        super(day.getDateTimeNumber(),
                day.getWindDegrees(),
                day.getWindSpeed(),
                day.getWeather(),
                day.getWeatherDescription(),
                day.getHumidity(),
                day.getPressure(),
                day.getTemp(),
                day.getTempMax(),
                day.getTempMin());
        this.signatureCurrentDay = signatureCurrentDay;
    }

    public SignatureCurrentDay getSignatureCurrentDay() {
        return SignatureCurrentDay.newInstance(signatureCurrentDay);
    }

    @Override
    public Signature getSignature() {
        return getSignatureCurrentDay();
    }

    public static class SignatureCurrentDay extends Signature implements IWeatherStruct{
        private long sunriseDateTime;
        private long sunsetDateTime;

        public SignatureCurrentDay(City city, Country country, long sunriseDateTime, long sunsetDateTime) {
            super(city, country);
            this.sunriseDateTime = sunriseDateTime;
            this.sunsetDateTime = sunsetDateTime;
        }

        public long getSunriseDateTime() {
            return this.sunriseDateTime;
        }

        public long getSunsetDateTime() {
            return this.sunsetDateTime;
        }

        @Override
        public Signature getSignature() {
            return this;
        }

        @NotNull
        static SignatureCurrentDay newInstance(SignatureCurrentDay signatureCurrentDay) {
            return new SignatureCurrentDay(
                    Location.newInstance(signatureCurrentDay.getCity()),
                    Location.newInstance(signatureCurrentDay.getCountry()),
                    signatureCurrentDay.getSunriseDateTime(),
                    signatureCurrentDay.getSunsetDateTime());
        }
    }

}
