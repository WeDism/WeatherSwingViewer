package com.weather_viewer.functional_layer.services.delayed_task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.neovisionaries.i18n.CountryCode;
import com.weather_viewer.functional_layer.application.IContext;
import com.weather_viewer.functional_layer.exceptions.EmptyCityException;
import com.weather_viewer.functional_layer.exceptions.EmptyCountryException;
import com.weather_viewer.functional_layer.exceptions.ObjectContainsException;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.IWeatherConnector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerService implements IWorkerService {
    private static final Logger LOGGER = Logger.getLogger(WorkerService.class.getName());
    private static final long PERIOD = 30L, INITIAL_DELAY = 0L;
    private static final int EXECUTORS_NUMBER = 2;
    private static final Set UNIQUE_CONNECTORS_AND_DELEGATE = new HashSet();
    private final IWeatherConnector<CurrentDay> connectorCurrentDay;
    private final IWeatherConnector<Workweek> connectorWorkweek;
    private final AtomicReference<CurrentDay> currentDay;
    private final AtomicReference<Workweek> workweek;
    private final AtomicReference<CurrentDay.SignatureCurrentDay> currentLocation = new AtomicReference<>();
    private final IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay;
    private final List<IWeatherConnector> connectorsList;
    private final GeneralFormDelegate generalFormListener;
    private final SettingsFormDelegate settingsListener;
    private ScheduledExecutorService executorScheduled = getNewScheduledExecutor();
    private ExecutorService executorFinder = getNewExecutorFinder();
    private ExecutorService executorsLoaders = getExecutorsLoaders();
    private long counterResponses;

    private WorkerService(IWeatherConnector<CurrentDay> connectorCurrentDay,
                          IWeatherConnector<Workweek> connectorWorkweek,
                          IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay, IContext context) throws ObjectContainsException {
        this.validateObjects(connectorCurrentDay);
        this.connectorCurrentDay = connectorCurrentDay;
        this.validateObjects(connectorWorkweek);
        this.connectorWorkweek = connectorWorkweek;
        this.validateObjects(connectorSignatureDay);
        this.connectorSignatureDay = connectorSignatureDay;
        this.generalFormListener = (GeneralFormDelegate) context.get(GeneralFormDelegate.class);
        this.validateObjects(this.generalFormListener);
        this.settingsListener = (SettingsFormDelegate) context.get(ISettings.class);
        this.currentDay = generalFormListener.getCurrentDay();
        this.workweek = generalFormListener.getWorkweek();

        this.connectorsList = new LinkedList<>(Arrays.asList(this.connectorCurrentDay, this.connectorWorkweek));
        this.executorScheduled.scheduleWithFixedDelay(() -> {
            getAndUpdate();
            generalFormListener.onPerform();
        }, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    private void validateObjects(Object object) throws ObjectContainsException {
        if (!WorkerService.UNIQUE_CONNECTORS_AND_DELEGATE.contains(object))
            WorkerService.UNIQUE_CONNECTORS_AND_DELEGATE.add(object);
        else throw new ObjectContainsException();
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
        List<Callable<IWeatherStruct>> callableList = Arrays.asList(() -> getIWeatherStruct(this.connectorCurrentDay), () -> getIWeatherStruct(this.connectorWorkweek));
        try {
            futures = this.executorsLoaders.invokeAll(callableList);
            for (Future<IWeatherStruct> future : futures) {
                list.add(future.get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        this.currentDay.set(list.stream().filter(e -> e instanceof CurrentDay).map(e -> (CurrentDay) e).findAny().orElse(null));
        this.workweek.set(list.stream().filter(e -> e instanceof Workweek).map(e -> (Workweek) e).findAny().orElse(null));
        this.generalFormListener.onUpdateForm();
    }

    public synchronized static IWorkerService build(IWeatherConnector<CurrentDay> connectorCurrentDay,
                                                    IWeatherConnector<Workweek> connectorWorkweek,
                                                    IWeatherConnector<CurrentDay.SignatureCurrentDay> connectorSignatureDay,
                                                    IContext context) throws ObjectContainsException {
        return new WorkerService(connectorCurrentDay, connectorWorkweek, connectorSignatureDay, context);
    }

    @Override
    public void resetExecutor() {
        disposeExecutorService(this.executorFinder);
        this.executorFinder = getNewExecutorFinder();
    }

    @Override
    public void resetScheduledExecutor() {
        disposeExecutorService(this.executorScheduled);
        this.executorScheduled = getNewScheduledExecutor();

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
        disposeExecutorService(this.executorFinder, this.executorScheduled, this.executorsLoaders);
    }

    @Override
    public void onSearch(Country country, City city) throws EmptyCountryException, EmptyCityException {
        if (city.toString().isEmpty()) {
            throw new EmptyCityException();
        } else if (country.toString().isEmpty()) {
            throw new EmptyCountryException();
        }
        this.connectorSignatureDay.setNewData(city, country);
        this.settingsListener.onFindLocation(false);
        this.executorFinder.execute(() -> {
            this.currentLocation.set(getIWeatherStruct(this.connectorSignatureDay));
            if (this.currentLocation.get() != null) this.settingsListener.onFindLocation(true);
            else this.settingsListener.onFindLocation(false);
        });
    }

    @Override
    public void onChangeLocationData() {
        CurrentDay.SignatureCurrentDay signatureCurrentDay = currentLocation.get();
        if (signatureCurrentDay != null) {
            this.connectorsList.forEach((a) -> a.setNewData(signatureCurrentDay.getCity(), signatureCurrentDay.getCountry()));
            this.executorScheduled.scheduleWithFixedDelay(this::getAndUpdate, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
            this.currentLocation.set(null);
        }
    }

    private <T extends IWeatherStruct> T getIWeatherStruct(IWeatherConnector<T> iWeatherConnector) {
        T iWeatherStruct = null;
        try {
            iWeatherStruct = iWeatherConnector.requestAndGetWeatherStruct();
            LOGGER.log(Level.INFO, "{0} connector response number is {1} and current location the {2} in {3}",
                    new Object[]{iWeatherConnector.getType(), ++this.counterResponses, iWeatherStruct.getSignature().getCity(),
                            CountryCode.getByCode(iWeatherStruct.getSignature().getCountry().toString()).getName()});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        return iWeatherStruct;
    }

}
