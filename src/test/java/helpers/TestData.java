package helpers;

import com.neovisionaries.i18n.CountryCode;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.services.delayed_task.GeneralFormDelegate;
import com.weather_viewer.functional_layer.services.delayed_task.ISettings;
import com.weather_viewer.gui.settings.Settings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public interface TestData {
    City LONDON = new City("London");
    Country UK_COUNTRY = new Country(CountryCode.UK.getAlpha2());
    String LONDON_IN_UK = String.format("%s,%s", LONDON, UK_COUNTRY);
    City SAMARA = new City("Samara");
    Country RU_COUNTRY = new Country(CountryCode.RU.getAlpha2());

    static IContext getMockContext() {
        IContext mock = mock(IContext.class);
        when(mock.get(GeneralFormDelegate.class)).thenReturn(mock(GeneralFormDelegate.class));
        when(mock.get(ISettings.class)).thenReturn(mock(Settings.class));
        return mock;
    }

}
