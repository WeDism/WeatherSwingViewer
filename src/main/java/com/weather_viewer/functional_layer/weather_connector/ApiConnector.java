package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.helpers.HttpRequestHelper;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.consts.ApiParams;
import com.weather_viewer.functional_layer.weather_connector.consts.Path;
import com.weather_viewer.functional_layer.weather_connector.consts.UriScheme;
import com.weather_viewer.functional_layer.weather_connector.consts.WeatherPlan;
import com.weather_viewer.functional_layer.weather_deserializers.CurrentDayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.DayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.SignatureCurrentDayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.WorkWeekDeserializer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.xml.ws.ProtocolException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiConnector<T extends IWeatherStruct> implements IWeatherConnector<T> {
    private static final Logger LOGGER = Logger.getLogger(ApiConnector.class.getName());
    private static final String APP_ID;
    private static final Map<Type, WeatherPlan> WEATHER_PLAN_HASH_MAP = new HashMap<>();
    final private HttpClient httpClient;
    final private Gson gson;
    private City city;
    private Country country;
    private Class<T> typeParameterClass;

    static {
        WEATHER_PLAN_HASH_MAP.put(CurrentDay.class, WeatherPlan.Weather);
        WEATHER_PLAN_HASH_MAP.put(CurrentDay.SignatureCurrentDay.class, WeatherPlan.Weather);
        WEATHER_PLAN_HASH_MAP.put(Workweek.class, WeatherPlan.ForecastForTheWorkWeek);

        Properties properties = new Properties();
        try {
            properties.load(ApiConnector.class.getResourceAsStream("/config.properties"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        APP_ID = properties.getProperty("appId");

    }

    private ApiConnector(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;

        httpClient = new HttpClient();
        httpClient.setFollowRedirects(false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrentDay.SignatureCurrentDay.class, new SignatureCurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(CurrentDay.class, new CurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(Workweek.class, new WorkWeekDeserializer());
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        gson = gsonBuilder.create();
    }

    private ApiConnector(City city, Country country, Class<T> typeParameterClass) {
        this(typeParameterClass);
        this.setNewData(city, country);
    }

    public static <T extends IWeatherStruct> IWeatherConnector<T> build(@NotNull City city, @NotNull Country country, @NotNull Class<T> typeParameterClass) {
        return new ApiConnector<>(city, country, typeParameterClass);
    }

    public static <T extends IWeatherStruct> IWeatherConnector<T> build(@NotNull Class<T> typeParameterClass) {
        return new ApiConnector<>(typeParameterClass);
    }

    @Override
    public void setNewData(@NotNull City city, @NotNull Country country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public Class<T> getType() {
        return typeParameterClass;
    }

    @Override
    public JsonElement request() throws Exception {
        if (city == null || country == null) throw new NullPointerException("City or Country are null");
        String cityAndCountry = String.format("%s,%s", city, country);

        httpClient.start();
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient
                        .newRequest(UriScheme.http + Path.WEATHER_URL + WEATHER_PLAN_HASH_MAP.get(typeParameterClass))
                        .method(HttpMethod.GET), ApiParams.Q, cityAndCountry)
                        .param(ApiParams.APPID, APP_ID)
                        .param(ApiParams.UNITS, ApiParams.UNITS_METRIC_VALUE).send();

        httpClient.stop();
        if (contentResponse.getStatus() != HttpStatus.OK_200)
            throw new ProtocolException(String.format("Status is %s but not 200", contentResponse.getStatus()));
        return new JsonParser().parse(contentResponse.getContentAsString());
    }

    @Override
    public T requestAndGetWeatherStruct() throws Exception {
        T json = null;
        JsonElement request = null;
        try {
            request = request();
            json = gson.fromJson(request, typeParameterClass);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, request != null ? request().toString() : null, ex);
            throw new Exception(ex);
        }
        return json;
    }

}
