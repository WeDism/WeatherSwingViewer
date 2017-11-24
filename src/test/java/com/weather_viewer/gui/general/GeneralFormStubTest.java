package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorForecastForTheWorkweek;
import com.weather_viewer.functional_layer.weather_connector.concrete_connector.ApiConnectorWeatherForDay;
import com.weather_viewer.gui.preview.Preview;
import org.junit.Assert;
import org.junit.Test;

import static helpers.TestData.*;

public class GeneralFormStubTest {
    static class PreviewFormStub extends Preview {
        private boolean isDispose;

        @Override
        public void dispose() {
            super.dispose();
            isDispose = true;
        }

        private boolean wasDisposed() {
            return isDispose;
        }
    }

    @Test
    public void testDisposePreview() throws Exception {
        IWeatherConnector<CurrentDay> connectorWeatherForDay
                = new ApiConnectorWeatherForDay<>(SAMARA, RU_COUNTRY, CurrentDay.class);
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = new ApiConnectorForecastForTheWorkweek<>(SAMARA, RU_COUNTRY, Workweek.class);

        final PreviewFormStub previewFormStub = new PreviewFormStub();
        General general = new General(previewFormStub, connectorWeatherForDay, connectorForecastForTheWorkWeek);
        general.dispose();
        Assert.assertTrue("Preview was disposed", previewFormStub.wasDisposed());
    }
}
