package com.functional_layer.weather_connector;

import com.db_layer.structures.IWeatherStruct;
import com.functional_layer.helpers.HttpRequestHelper;
import com.functional_layer.structs.location.concrete_location.City;
import com.functional_layer.structs.location.concrete_location.Country;
import com.functional_layer.structs.weather.IWeatherStructConverter;
import com.functional_layer.weather_connector.consts.ApiParams;
import com.functional_layer.weather_connector.consts.Path;
import com.functional_layer.weather_connector.consts.UriScheme;
import com.functional_layer.weather_connector.consts.WeatherPlan;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public abstract class ApiConnector<T extends IWeatherStruct> implements IWeatherConnector<T> {
    private String cityAndCountry;
    private String appId;
    private HttpClient httpClient;
    protected IWeatherStructConverter<T> weatherStructConverter;

    protected ApiConnector(City city, String appId, Country country) {
        this.cityAndCountry = String.format("%s,%s", city, country);
        this.appId = appId;
        httpClient = new HttpClient();
        httpClient.setFollowRedirects(false);
    }

    protected abstract WeatherPlan getWeatherPlan();

    @Override
    public JsonElement request() throws Exception {
        httpClient.start();
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient
                        .newRequest(UriScheme.http + Path.WEATHER_URL + getWeatherPlan())
                        .method(HttpMethod.GET), ApiParams.Q, cityAndCountry)
                        .param(ApiParams.APPID, appId).send();

        httpClient.stop();

        return new JsonParser().parse(contentResponse.getContentAsString());
    }

    @Override
    public T requestAndGetWeatherStruct() throws Exception {
        return weatherStructConverter.construct(request());
    }

}
