package com.weather_viewer.gui.general;

import com.weather_viewer.functional_layer.application.Context;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.previews.start.IPreview;
import com.weather_viewer.gui.previews.start.StartPreview;
import com.weather_viewer.gui.settings.ISettings;
import com.weather_viewer.gui.settings.Settings;
import com.weather_viewer.main.WeatherViewer;
import org.jetbrains.annotations.Contract;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static helpers.TestData.RU_COUNTRY;
import static helpers.TestData.SAMARA;
import static org.mockito.Mockito.*;

public class GeneralFormSpyStubTest {

    private final static int TIMEOUT;

    private static class GeneralFormStart extends General {
        private boolean isPerform;

        GeneralFormStart(IContext context) {
            super(context);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    //This code should be and application do not exit before end test
                }
            });
        }

        @Override
        public void onPerform() {
            super.onPerform();
            isPerform = true;
            super.dispose();
        }

        @Contract(pure = true)
        private boolean wasPerform() {
            return isPerform;
        }
    }

    private static class PreviewFormStub extends StartPreview {
        private boolean isDispose;

        private PreviewFormStub() throws HeadlessException {
        }

        @Override
        public void dispose() {
            super.dispose();
            this.isDispose = true;
        }

        @Contract(pure = true)
        private boolean wasDisposed() {
            return this.isDispose;
        }
    }


    static {
        TIMEOUT = 7;
    }

    @Test
    public void counterCallsPreview() throws Exception {
        IWeatherConnector<CurrentDay> connectorWeatherForDay
                = spy(ApiConnector.build(SAMARA, RU_COUNTRY, CurrentDay.class));
        IWeatherConnector<Workweek> connectorForecastForTheWorkWeek
                = spy(ApiConnector.build(SAMARA, RU_COUNTRY, Workweek.class));
        IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay
                = Mockito.mock(ApiConnector.class);

        final IContext context = Context.build();
        context.add(IPreview.class, new PreviewFormStub())
                .add(ISettings.class, new Settings(context));


        Callable<GeneralFormStart> generalFormStartCallable = () -> new GeneralFormStart(context);
        WeatherViewer<GeneralFormStart> start = WeatherViewer.getInstance
                (connectorWeatherForDay, connectorForecastForTheWorkWeek, connectorSignatureDay, generalFormStartCallable).start();
        GeneralFormStart general = start.getGeneral();

        LocalDateTime maxTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        while (!general.wasPerform() && LocalDateTime.now().isBefore(maxTime)) ;

        Assert.assertTrue("General form was not disposed", general.wasPerform());
        Assert.assertTrue("StartPreview was not disposed", ((PreviewFormStub) context.get(IPreview.class)).wasDisposed());

        verify(connectorWeatherForDay, times(1)).requestAndGetWeatherStruct();
        verify(connectorForecastForTheWorkWeek, times(1)).requestAndGetWeatherStruct();


    }
}
