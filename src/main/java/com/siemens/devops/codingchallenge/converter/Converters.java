package com.siemens.devops.codingchallenge.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

/**
 * Defines lambda expressions for feature branch and build status converters.
 */
public class Converters {

    /**
     * Converts a list of feature branches from JSON to List.
     * This method allows the caller to pass in a lambda expression that 
     * provides the desired behavior. For example, one caller might
     * pass in code that uses the Google Gson APIs, whereas another
     * might pass in Oracle JSON APIs.
     */
    public static List<String> convertFeatureBranches
        (URL url, Converter<URL, List<String>> featureBranchConverter) {
            return featureBranchConverter.convert(url);
    }

    /**
     * Converts a list of build statuses from JSON to Map.
     * This method allows the caller to pass a lambda expression that 
     * provides the desired behavior. For example, one caller might
     * pass in code that uses the Google Gson APIs, whereas another
     * might pass in ORACLE JSON APIs.
     */
    public static Map<String,String> convertBuildStatuses
        (URL url, Converter<URL, Map<String,String>> buildStatusConverter) {
            return buildStatusConverter.convert(url);
    }

    /**
     * Gets a lambda expression that converts feature branches from JSON to ArrayList.
     * This method does not directly return a collection. Instead, it returns a
     * lambda expression which has the conversion behavior already implemented.
     * Callers can get the list of feature branches as follows:
     * featureBranches = Converters.featureBranchConverter.convert(JettyConfig.featureBranchURL); 
     */
    @SuppressWarnings("unchecked")
    public static Converter<URL, List<String>> featureBranchConverter = (f) -> {

        List<String> results = null;
        try {
            HttpURLConnection http = (HttpURLConnection) f.openConnection();
            http.connect();
            results = new Gson().fromJson(new InputStreamReader(http.getInputStream()), 
                                              ArrayList.class);
            http.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return results;

    };

    /**
     * Gets a lambda expression that converts build statuses from JSON to TreeMap.
     * This method does not directly return a collection. Instead, it returns a
     * lambda expression which has the conversion behavior already implemented.
     * Callers can get the list of build statuses as follows:
     * buildStatuses = Converters.buildStatusConverter.convert(JettyConfig.buildStatusURL);
     */
    @SuppressWarnings("unchecked")
    public static Converter<URL, Map<String, String>> buildStatusConverter = (f) -> {
        Map<String, String> results = null;
        try {
            HttpURLConnection http = (HttpURLConnection) f.openConnection();
            http.connect();
            results = new Gson().fromJson(new InputStreamReader(http.getInputStream()), 
                                              TreeMap.class);
            http.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return results;
    };
}
