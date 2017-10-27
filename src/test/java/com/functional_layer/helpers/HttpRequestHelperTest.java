package com.functional_layer.helpers;

import com.functional_layer.weather_connector.consts.*;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.functional_layer.weather_connector.consts.Path.*;
import static com.functional_layer.weather_connector.consts.UriScheme.*;
import static com.functional_layer.weather_connector.consts.WeatherPlan.*;
import static test_helpers.AssertHelper.assertTrue;

public class HttpRequestHelperTest {

    private HttpClient httpClient = new HttpClient();
    private String city = "London";
    private Country country = Country.UK;
    private String cityAndCountry = String.format("%s,%s", city, country);
    private String appId = "a931917869669cee8ee1da9fb35d3dd3";

    @Before
    public void setUp() throws Exception {
        //Debug settings for fiddler
        ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
        HttpProxy proxy = new HttpProxy("127.0.0.1", 8888);
        proxyConfig.getProxies().add(proxy);

        httpClient.start();
    }


    @Test
    public void insertParamsAfter() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient
                        .newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET), ApiParams.Q, cityAndCountry)
                        .param(ApiParams.APPID, appId).send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void insertParamsBefore() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient.newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET)
                        .param(ApiParams.APPID, appId), ApiParams.Q, cityAndCountry)
                        .send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }
}
