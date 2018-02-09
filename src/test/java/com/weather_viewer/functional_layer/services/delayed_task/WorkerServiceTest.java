package com.weather_viewer.functional_layer.services.delayed_task;

import com.weather_viewer.functional_layer.application.Context;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.exceptions.EmptyCityException;
import com.weather_viewer.functional_layer.exceptions.EmptyCountryException;
import com.weather_viewer.functional_layer.exceptions.ObjectContainsException;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.general.General;
import com.weather_viewer.gui.general.GeneralFormDelegate;
import com.weather_viewer.gui.previews.start.IPreview;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.ISettings;
import com.weather_viewer.gui.settings.Settings;
import org.junit.Test;

import static helpers.TestData.*;

public class WorkerServiceTest {
    final IWeatherConnector<Workweek> connectorForecastForTheWorkWeek = ApiConnector.build(LONDON, UK_COUNTRY, Workweek.class);
    final IWeatherConnector<CurrentDay> connectorWeatherForDay = ApiConnector.build(LONDON, UK_COUNTRY, CurrentDay.class);
    final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay = ApiConnector.build(LONDON, UK_COUNTRY, CurrentDay.SignatureCurrentDay.class);
    final IContext context = Context.build();
    final IContext mock = getMockContext();


    public WorkerServiceTest() {
        this.context.add(IPreview.class, new StartPreview())
                .add(ISettings.class, new Settings(this.context))
                .add(GeneralFormDelegate.class, new General(this.context));
    }

    @Test(expected = EmptyCountryException.class)
    public void onSearchEmptyCountryException() throws ObjectContainsException, EmptyCityException, EmptyCountryException {
        IWorkerService workerService =
                WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, mock);
        workerService.onSearch(new Country(""), new City("london"));
    }

    @Test(expected = EmptyCityException.class)
    public void onSearchEmptyCityException() throws ObjectContainsException, EmptyCityException, EmptyCountryException {
        IWorkerService workerService =
                WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, this.mock);
        workerService.onSearch(new Country("uk"), new City(""));
    }

    @Test
    public void onSearch() throws ObjectContainsException, EmptyCityException, EmptyCountryException {
        IWorkerService workerService =
                WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, this.context);
        workerService.onSearch(new Country("uk"), new City("london"));
    }

    @Test
    public void name() throws ObjectContainsException {
        IWorkerService workerService =
                WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, this.context);
        workerService.onChangeLocationData();

    }
}