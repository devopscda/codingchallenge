package com.siemens.devops.codingchallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import com.siemens.devops.codingchallenge.config.JettyConfig;
import com.siemens.devops.codingchallenge.converter.Converters;

/**
 * Tests the conversion of JSON data (read from Jetty) to Collections.
 */
public final class ConverterTest {

    private static Server jetty;
    private List<String> featureBranches;
    private Map<String,String> buildStatuses;

    @BeforeClass
    public static void openResources() throws Exception {
        jetty = new Server(JettyConfig.PORT);
        new WebAppContext(jetty, JettyConfig.WEB_APP, JettyConfig.CONTEXT_PATH);
        jetty.start();
    }

    @AfterClass
    public static void closeResources() throws Exception {
        jetty.stop();
    }

    @Before
    public void setUp() {
        featureBranches = Converters.featureBranchConverter.convert(JettyConfig.featureBranchURL);
        buildStatuses = Converters.buildStatusConverter.convert(JettyConfig.buildStatusURL);
    }

    @After
    public void tearDown() {
        featureBranches = null;
        buildStatuses = null;
    }

    // FEATURE BRANCH UNIT TESTS

    @Test
    public void featureBranchesSizeEquals7() {
        assertEquals(7, featureBranches.size());
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest121() {
        assertTrue(featureBranches.contains("feature/abc/TEST-121"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest122() {
        assertTrue(featureBranches.contains("feature/abc/TEST-122"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest123() {
        assertTrue(featureBranches.contains("feature/abc/TEST-123"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest124() {
        assertTrue(featureBranches.contains("feature/abc/TEST-124"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest125() {
        assertTrue(featureBranches.contains("feature/abc/TEST-125"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest126() {
        assertTrue(featureBranches.contains("feature/abc/TEST-126"));
    }

    @Test
    public void featureBranchesContainsFeatureAbcTest127() {
        assertTrue(featureBranches.contains("feature/abc/TEST-127"));
    }

    // BUILD STATUS TESTS

    @Test
    public void buildStatusesKeySetSizeEquals3() {
        assertEquals(3, buildStatuses.keySet().size());
    }

    @Test
    public void buildStatusesFeatureAbcTest122EqualsSuccess() {
        assertTrue(buildStatuses.get("feature-abc-TEST-122").equals("SUCCESS"));
    }

    @Test
    public void buildStatusesFeatureAbcTest123EqualsSuccess() {
        assertTrue(buildStatuses.get("feature-abc-TEST-123").equals("SUCCESS"));
    }

    @Test
    public void buildStatusesFeatureAbcTest124EqualsFailure() {
        assertTrue(buildStatuses.get("feature-abc-TEST-124").equals("FAILURE"));
    }

    // NULL TESTS

    @Test
    public void jettyIsNotNull() {
        assertNotNull(jetty);
    }

    @Test
    public void featureBranchesIsNotNull() {
        assertNotNull(featureBranches);
    }

    @Test
    public void buildStatusesIsNotNNull() {
        assertNotNull(buildStatuses);
    }

   // LAMBDA EXPRESSION TESTS

    @Test
    public void convertFeatureBranchesEqualsKnownFeatureBranches() {
   
        List<String> convertedFeatureBranches = 
                     Converters.convertFeatureBranches(JettyConfig.featureBranchURL,
            (f) -> {

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
            }
        );
        assertEquals(featureBranches, convertedFeatureBranches);
    }

    @Test
    public void convertBuildStatusesEqualsKnownBuildStatuses() {
   
        Map<String,String> convertedBuildStatuses = 
                           Converters.convertBuildStatuses(JettyConfig.buildStatusURL,
            (f) -> {

                Map<String,String> results = null;
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
            }
        );

        assertEquals(buildStatuses, convertedBuildStatuses);
    }
}
