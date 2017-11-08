package com.weather_viewer.functional_layer.weather_connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.weather_viewer.functional_layer.structs.weather.CurrentDay;
import com.weather_viewer.functional_layer.structs.weather.Day;
import com.weather_viewer.functional_layer.structs.weather.IWeatherStruct;
import com.weather_viewer.functional_layer.helpers.HttpRequestHelper;
import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.structs.weather.Workweek;
import com.weather_viewer.functional_layer.weather_connector.consts.ApiParams;
import com.weather_viewer.functional_layer.weather_connector.consts.Path;
import com.weather_viewer.functional_layer.weather_connector.consts.UriScheme;
import com.weather_viewer.functional_layer.weather_connector.consts.WeatherPlan;
import com.weather_viewer.functional_layer.weather_deserializers.CurrentDayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.DayDeserializer;
import com.weather_viewer.functional_layer.weather_deserializers.WorkWeekDeserializer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public abstract class ApiConnector<T extends IWeatherStruct> implements IWeatherConnector<T> {
    final private String cityAndCountry;
    final private String appId;
    final private HttpClient httpClient;
    final private GsonBuilder gsonBuilder;
    final private Gson gson;
    final private Class<T> typeParameterClass;

    protected ApiConnector(City city, String appId, Country country, Class<T> typeParameterClass) {
        this.cityAndCountry = String.format("%s,%s", city, country);
        this.appId = appId;
        httpClient = new HttpClient();
        httpClient.setFollowRedirects(false);
        this.typeParameterClass = typeParameterClass;

        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CurrentDay.class, new CurrentDayDeserializer());
        gsonBuilder.registerTypeAdapter(Workweek.class, new WorkWeekDeserializer());
        gsonBuilder.registerTypeAdapter(Day.class, new DayDeserializer());
        gson = gsonBuilder.create();
    }

    protected abstract WeatherPlan getWeatherPlan();

    @Override
    public JsonElement request() throws Exception {
        httpClient.start();
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient
                        .newRequest(UriScheme.http + Path.WEATHER_URL + getWeatherPlan())
                        .method(HttpMethod.GET), ApiParams.Q, cityAndCountry)
                        .param(ApiParams.APPID, appId)
                        .param(ApiParams.UNITS,ApiParams.UNITS_METRIC_VALUE).send();

        httpClient.stop();

        return new JsonParser().parse(contentResponse.getContentAsString());
    }

    @Override
    public T requestAndGetWeatherStruct() throws Exception {
        return gson.fromJson(request(), typeParameterClass);
    }

}
