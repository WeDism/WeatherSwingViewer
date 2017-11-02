package com.weather_viewer.functional_layer.helpers;

import com.weather_viewer.functional_layer.structs.location.concrete_location.City;
import com.weather_viewer.functional_layer.structs.location.concrete_location.Country;
import com.weather_viewer.functional_layer.weather_connector.consts.ApiParams;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.weather_viewer.functional_layer.weather_connector.consts.Path.WEATHER_URL;
import static com.weather_viewer.functional_layer.weather_connector.consts.UriScheme.http;
import static com.weather_viewer.functional_layer.weather_connector.consts.WeatherPlan.Weather;
import static test_helpers.AssertHelper.assertTrue;

public class HttpRequestHelperTest {

    private HttpClient httpClient;
    private City city = new City("London");
    private Country country = new Country("uk");
    private String cityAndCountry = String.format("%s,%s", city, country);
    private static final String APPID_VALUE = "a931917869669cee8ee1da9fb35d3dd3";

    public HttpRequestHelperTest() {
        httpClient = new HttpClient();
        //<editor-fold desc="Enable for debug settings for fiddler" defaultstate="collapsed">
        //ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
        //HttpProxy proxy = new HttpProxy("127.0.0.1", 8888);
        //proxyConfig.getProxies().add(proxy);
        //</editor-fold>
    }

    @Before
    public void setUp() throws Exception {
        httpClient.start();
    }


    @Test
    public void insertParamsAfter() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient
                        .newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET), ApiParams.Q, cityAndCountry)
                        .param(ApiParams.APPID, APPID_VALUE).send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void insertParamsBefore() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient.newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET)
                        .param(ApiParams.APPID, APPID_VALUE), ApiParams.Q, cityAndCountry)
                        .send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }
}
