package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.preview.Preview;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

import static helpers.TestData.*;

public class GeneralFormStubTest {
    static class PreviewFormStub extends Preview {
        private boolean isDispose;

        private PreviewFormStub() throws HeadlessException {
        }

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
                = ApiConnector.build(SAMARA, RU_COUNTRY, CurrentDay.class);
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = ApiConnector.build(SAMARA, RU_COUNTRY, Workweek.class);

        final PreviewFormStub previewFormStub = new PreviewFormStub();
        General general = new General(previewFormStub, connectorWeatherForDay, connectorForecastForTheWorkWeek);
        general.dispose();
        Assert.assertTrue("Preview was disposed", previewFormStub.wasDisposed());
    }
}
