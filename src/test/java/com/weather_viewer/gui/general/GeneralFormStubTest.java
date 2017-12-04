package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.previews.start.StartPreview;
import org.junit.Test;

import java.awt.*;

import static helpers.TestData.RU_COUNTRY;
import static helpers.TestData.SAMARA;

public class GeneralFormStubTest {
    static class PreviewFormStub extends StartPreview {
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
        //TODO
//        General general = new General(previewFormStub, connectorWeatherForDay, connectorForecastForTheWorkWeek);
//        general.resetScheduledExecutor();
//        Assert.assertTrue("StartPreview was disposed", previewFormStub.wasDisposed());
    }
}
