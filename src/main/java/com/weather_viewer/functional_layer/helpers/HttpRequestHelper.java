package com.weather_viewer.functional_layer.helpers;

import org.eclipse.jetty.client.api.Request;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface HttpRequestHelper {

    /**
     * Modyfied method <br><b>org.eclipse.jetty.client.HttpRequest.param(java.lang.String, java.lang.String, boolean)</b>
     * <p>You can use if comma contains your URI.</p>
     * <p><b>Warning</b> this params is not encoded</p>
     * @param request org.eclipse.jetty.client.HttpRequest
     * @param apiParams query
     * @param cityAndCountry value
     * @return current object
     */
    static Request modifyRequest(Request request, String apiParams, String cityAndCountry) {
        try {

            Method method = request.getClass().getDeclaredMethod("param", String.class, String.class, boolean.class);
            method.setAccessible(true);
            Request invoke = (Request) method.invoke(request, apiParams, cityAndCountry, true);
            Field query = invoke.getClass().getDeclaredField("query");
            query.setAccessible(true);
            if (query.get(invoke) != null)
                query.set(invoke, query.get(invoke) + "&" + apiParams + "=" + cityAndCountry);
            else {
                Method methodBuildQuery = request.getClass().getDeclaredMethod("buildQuery");
                methodBuildQuery.setAccessible(true);
                query.set(invoke, apiParams + "=" + cityAndCountry);
            }
            Field uri = invoke.getClass().getDeclaredField("uri");
            uri.setAccessible(true);
            uri.set(invoke, null);

        } catch (Exception ex) {
            Logger.getLogger(HttpRequestHelper.class.getName()).log(Level.SEVERE, null, ex);

        }
        return request;
    }
}
