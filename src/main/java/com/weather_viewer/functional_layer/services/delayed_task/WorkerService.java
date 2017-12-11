package com.weather_viewer.functional_layer.services.delayed_task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.neovisionaries.i18n.CountryCode;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import com.weather_viewer.gui.general.GeneralFormDelegate;
import com.weather_viewer.gui.settings.Settings;
import com.weather_viewer.gui.settings.SettingsFormDelegate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerService implements IWorkerService {
    private static final Logger LOGGER;
    private static final long PERIOD, INITIAL_DELAY;
    private static final int EXECUTORS_NUMBER;
    private final IWeatherConnector<CurrentDay> connectorCurrentDay;
    private final IWeatherConnector<Workweek> connectorWorkweek;
    private final AtomicReference<CurrentDay> currentDay;
    private final AtomicReference<Workweek> workweek;
    private final AtomicReference<CurrentDay.SignatureCurrentDay> currentLocation;
    private final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay;
    private final List<IWeatherConnector> connectorsList;
    private final GeneralFormDelegate generalFormListener;
    private final SettingsFormDelegate settingsListener;
    private ScheduledExecutorService executorScheduled;
    private ExecutorService executorFinder;
    private ExecutorService executorsLoaders;
    private long counterResponses;
    private static WorkerService workerService;


    static {
        LOGGER = Logger.getLogger(WorkerService.class.getName());
        PERIOD = 30L;
        INITIAL_DELAY = 0L;
        EXECUTORS_NUMBER = 2;
    }

    {
        this.executorScheduled = getNewScheduledExecutor();
        this.executorFinder = getNewExecutorFinder();
        this.executorsLoaders = getExecutorsLoaders();
        this.currentLocation = new AtomicReference<>();
    }

    private WorkerService(IWeatherConnector<CurrentDay> connectorCurrentDay,
                          IWeatherConnector<Workweek> connectorWorkweek,
                          IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay, @NotNull GeneralFormDelegate generalFormListener) {
        this.connectorCurrentDay = connectorCurrentDay;
        this.connectorWorkweek = connectorWorkweek;
        this.connectorSignatureDay = connectorSignatureDay;
        this.generalFormListener = generalFormListener;
        this.settingsListener = generalFormListener.getSettingsForm();
        this.currentDay = generalFormListener.getCurrentDay();
        this.workweek = generalFormListener.getWorkweek();


        this.connectorsList = new LinkedList<>(Arrays.asList(this.connectorCurrentDay, this.connectorWorkweek));
        this.executorScheduled.scheduleWithFixedDelay(() -> {
            getAndUpdate();
            generalFormListener.onPerform();
        }, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    @NotNull
    private ExecutorService getExecutorsLoaders() {
        return Executors.newFixedThreadPool(EXECUTORS_NUMBER, new ThreadFactoryBuilder().setNameFormat("ExecutorsLoaders-%d").build());
    }

    @NotNull
    private ExecutorService getNewExecutorFinder() {
        return Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("ExecutorFinder-%d").build());
    }

    @NotNull
    private ScheduledExecutorService getNewScheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("ScheduledExecutor-%d").build());
    }

    private void getAndUpdate() {
        List<Future<IWeatherStruct>> futures;
        List<IWeatherStruct> list = new LinkedList<>();
        List<Callable<IWeatherStruct>> callableList = Arrays.asList(() -> getIWeatherStruct(connectorCurrentDay), () -> getIWeatherStruct(connectorWorkweek));
        try {
            futures = executorsLoaders.invokeAll(callableList);
            for (Future<IWeatherStruct> future : futures) {
                list.add(future.get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        currentDay.set(list.stream().filter(e -> e instanceof CurrentDay).map(e -> (CurrentDay) e).findAny().orElse(null));
        workweek.set(list.stream().filter(e -> e instanceof Workweek).map(e -> (Workweek) e).findAny().orElse(null));
        generalFormListener.onUpdateForm();
    }

    public static IWorkerService build(IWeatherConnector<CurrentDay> connectorCurrentDay,
                                       IWeatherConnector<Workweek> connectorWorkweek,
                                       IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay,
                                       GeneralFormDelegate generalFormListener) {
        if (workerService != null)
            return workerService;
        workerService = new WorkerService(connectorCurrentDay, connectorWorkweek, connectorSignatureDay, generalFormListener);
        return workerService;
    }

    @Contract(pure = true)
    public static IWorkerService getInstance() throws NullPointerException {
        if (workerService != null)
            return workerService;
        throw new NullPointerException("WorkerService is not build");
    }

    @Override
    public void resetExecutor() {
        disposeExecutorService(executorFinder);
        executorFinder = getNewExecutorFinder();
    }

    @Override
    public void resetScheduledExecutor() {
        disposeExecutorService(executorScheduled);
        executorScheduled = getNewScheduledExecutor();

    }

    private void disposeExecutorService(@NotNull ExecutorService... executorServices) {
        for (ExecutorService executorService : executorServices) {
            try {
                executorService.shutdown();
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } finally {
                if (!executorService.isTerminated()) {
                    LOGGER.log(Level.SEVERE, "Task is not terminated");
                }

                executorService.shutdownNow();
                LOGGER.log(Level.SEVERE, "Shutdown finished");
            }
        }
    }

    @Override
    public void dispose() {
        disposeExecutorService(executorFinder, executorScheduled, executorsLoaders);
        workerService = null;
    }

    @Override
    public void onSearch(Country country, City city) {
        if (city.toString().isEmpty()) {
            JOptionPane.showMessageDialog(((Settings) settingsListener), "City must be filled");
            return;
        } else if (country.toString().isEmpty()) {
            JOptionPane.showMessageDialog(((Settings) settingsListener), "Country must be filled");
            return;
        }
        connectorSignatureDay.setNewData(city, country);
        settingsListener.onFindLocation(false);
        executorFinder.execute(() -> {
            currentLocation.set(getIWeatherStruct(connectorSignatureDay));
            if (currentLocation.get() != null) settingsListener.onFindLocation(true);
            else settingsListener.onFindLocation(false);
        });
    }

    @Override
    public void onChangeLocationData() {
        CurrentDay.SignatureCurrentDay signatureCurrentDay = currentLocation.get();
        if (signatureCurrentDay != null) {
            connectorsList.forEach((a) -> a.setNewData(signatureCurrentDay.getCity(), signatureCurrentDay.getCountry()));
            executorScheduled.scheduleWithFixedDelay(this::getAndUpdate, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
            currentLocation.set(null);
        }
    }

    private <T extends IWeatherStruct> T getIWeatherStruct(IWeatherConnector<T> iWeatherConnector) {
        T iWeatherStruct = null;
        try {
            iWeatherStruct = iWeatherConnector.requestAndGetWeatherStruct();
            LOGGER.log(Level.INFO, "{0} connector response number is {1} and current location the {2} in {3}",
                    new Object[]{iWeatherConnector.getType(), ++counterResponses, iWeatherStruct.getSignature().getCity(),
                            CountryCode.getByCode(iWeatherStruct.getSignature().getCountry().toString()).getName()});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        return iWeatherStruct;
    }

}
