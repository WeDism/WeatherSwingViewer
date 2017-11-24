package helpers;

import com.neovisionaries.i18n.CountryCode;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;

public interface TestData {
   City LONDON = new City("London");
   Country UK_COUNTRY = new Country(CountryCode.UK.getAlpha2());
   String LONDON_IN_UK = String.format("%s,%s", LONDON, UK_COUNTRY);
   City SAMARA = new City("Samara");
   Country RU_COUNTRY = new Country(CountryCode.RU.getAlpha2());


}
