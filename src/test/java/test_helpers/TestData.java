package test_helpers;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;

public interface TestData {
   City LONDON = new City("London");
   Country UK_COUNTRY = new Country("uk");
   String LONDON_IN_UK = String.format("%s,%s", LONDON, UK_COUNTRY);
   String APPID_VALUE = "a931917869669cee8ee1da9fb35d3dd3";
   City SAMARA = new City("Samara");
   Country RU_COUNTRY = new Country("RU_COUNTRY");


}
