package com.functional_layer.weather_connector;

import com.functional_layer.weather_connector.consts.ApiParams;
import com.functional_layer.weather_connector.consts.Path;
import com.functional_layer.weather_connector.consts.WeatherPlan;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public abstract class ApiConnector {
    private String city;
    private String appId;
    private HttpClient httpClient;

    public ApiConnector(String city, String appId) {
        this.city = city;
        this.appId = appId;
        httpClient = new HttpClient();
        httpClient.setFollowRedirects(false);

    }

    protected abstract WeatherPlan getWeatherPlan();

    public JsonElement request() throws Exception {
        httpClient.start();
        ContentResponse contentResponse = httpClient
                .newRequest(Path.WEATHER_URL + getWeatherPlan())
                .param(ApiParams.P, city)
                .method(HttpMethod.GET).send();
        httpClient.stop();

        JsonElement jsonElement = new JsonParser().parse(contentResponse.getContentAsString());
        return jsonElement;
    }

}
