package com.weather_viewer.functional_layer.helpers;

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
import static test_helpers.TestData.*;

public class HttpRequestHelperTest {

    private HttpClient httpClient;

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
                        .method(HttpMethod.GET), ApiParams.Q, LONDON_IN_UK)
                        .param(ApiParams.APPID, APPID_VALUE).send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void insertParamsBefore() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient.newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET)
                        .param(ApiParams.APPID, APPID_VALUE), ApiParams.Q, LONDON_IN_UK)
                        .send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }
}
