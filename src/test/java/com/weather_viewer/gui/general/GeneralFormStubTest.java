package com.weather_viewer.gui.general;

import com.stubs.GeneralFormStart;
import com.weather_viewer.functional_layer.services.delayed_task.WorkerService;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.Settings;
import org.jetbrains.annotations.Contract;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static helpers.TestData.RU_COUNTRY;
import static helpers.TestData.SAMARA;

public class GeneralFormStubTest {

    private final static int TIMEOUT;

    static class PreviewFormStub extends StartPreview {
        private boolean isDispose;

        private PreviewFormStub() throws HeadlessException {
        }

        @Override
        public void dispose() {
            super.dispose();
            isDispose = true;
        }

        @Contract(pure = true)
        private boolean wasDisposed() {
            return isDispose;
        }
    }

    static {
        TIMEOUT = 10;
    }

    @Test
    public void testDisposePreview() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        IWeatherConnector<CurrentDay> connectorWeatherForDay
                = ApiConnector.build(SAMARA, RU_COUNTRY, CurrentDay.class);
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = ApiConnector.build(SAMARA, RU_COUNTRY, Workweek.class);
        IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay
                = Mockito.mock(ApiConnector.class);


        final PreviewFormStub previewFormStub = new PreviewFormStub();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<GeneralFormStart> future = executorService.submit(() -> new GeneralFormStart(previewFormStub, new Settings()));
        GeneralFormStart general = future.get();
        WorkerService.build(connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, general);
        executorService.shutdown();
        LocalDateTime maxTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        while (!general.wasPerform() && LocalDateTime.now().isBefore(maxTime)) ;
        Assert.assertTrue("General form was not disposed", general.wasPerform());

        Assert.assertTrue("StartPreview was not disposed", previewFormStub.wasDisposed());
    }
}
