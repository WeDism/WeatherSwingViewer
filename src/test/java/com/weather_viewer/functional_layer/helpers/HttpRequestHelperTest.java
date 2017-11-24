package com.weather_viewer.functional_layer.helpers;

import com.weather_viewer.functional_layer.weather_connector.ApiConnector;
import com.weather_viewer.functional_layer.weather_connector.consts.ApiParams;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.weather_viewer.functional_layer.weather_connector.consts.Path.WEATHER_URL;
import static com.weather_viewer.functional_layer.weather_connector.consts.UriScheme.http;
import static com.weather_viewer.functional_layer.weather_connector.consts.WeatherPlan.Weather;
import static helpers.AssertHelper.assertTrue;
import static helpers.TestData.LONDON_IN_UK;

public class HttpRequestHelperTest {

    private final static String APP_ID;
    private static final Logger LOGGER = Logger.getLogger(HttpRequestHelperTest.class.getName());
    private HttpClient httpClient;

    static {
        Properties properties = new Properties();
        try {
            properties.load(ApiConnector.class.getResourceAsStream("/config.properties"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        APP_ID = properties.getProperty("appId");
    }

    public HttpRequestHelperTest() {
        httpClient = new HttpClient();
        //region Enable for debug settings for fiddler
        //ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
        //HttpProxy proxy = new HttpProxy("127.0.0.1", 8888);
        //proxyConfig.getProxies().add(proxy);
        //endregion
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
                        .param(ApiParams.APPID, APP_ID).send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @Test
    public void insertParamsBefore() throws Exception {
        ContentResponse contentResponse =
                HttpRequestHelper.modifyRequest(httpClient.newRequest(http + WEATHER_URL + Weather)
                        .method(HttpMethod.GET)
                        .param(ApiParams.APPID, APP_ID), ApiParams.Q, LONDON_IN_UK)
                        .send();

        assertTrue("Status code: %s but should be %s", contentResponse.getStatus(), HttpStatus.OK_200);
    }

    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }
}
