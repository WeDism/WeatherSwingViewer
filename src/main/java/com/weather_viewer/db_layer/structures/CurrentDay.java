package com.weather_viewer.db_layer.structures;

import com.weather_viewer.functional_layer.structs.location.Location;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import org.jetbrains.annotations.NotNull;

public class CurrentDay extends Day {
    private SignatureCurrentDay signatureCurrentDay;

    public CurrentDay(long dateTime, int windDegrees, double windSpeed, String weather, String weatherDescription, int humidity, int pressure, int temp, int tempMax, int tempMin, SignatureCurrentDay signatureCurrentDay) {
        super(dateTime, windDegrees, windSpeed, weather, weatherDescription, humidity, pressure, temp, tempMax, tempMin);
        this.signatureCurrentDay = signatureCurrentDay;
    }

    public CurrentDay(Day day, SignatureCurrentDay signatureCurrentDay) {
        super(day.getDateTime(),
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

    public static class SignatureCurrentDay extends Signature {
        private long sunriseDateTime;
        private long sunsetDateTime;

        public SignatureCurrentDay(City city, Country country, long sunriseDateTime, long sunsetDateTime) {
            super(city, country);
            this.sunriseDateTime = sunriseDateTime;
            this.sunsetDateTime = sunsetDateTime;
        }

        public long getSunriseDateTime() {
            return sunriseDateTime;
        }

        public long getSunsetDateTime() {
            return sunsetDateTime;
        }

        @NotNull
        public static SignatureCurrentDay newInstance(SignatureCurrentDay signatureCurrentDay) {
            return new SignatureCurrentDay(
                    Location.newInstance(signatureCurrentDay.getCity()),
                    Location.newInstance(signatureCurrentDay.getCountry()),
                    signatureCurrentDay.getSunriseDateTime(),
                    signatureCurrentDay.getSunsetDateTime());
        }
    }

}
