package com.siemens.devops.codingchallenge.config;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility class that defines common Jetty configuration options.
 */
public class JettyConfig {

    public static final int PORT = 9999;
    public static final String WEB_APP = "src/test/webapp/";
    public static final String CONTEXT_PATH = "/";
    public static final String FEATURE_BRANCH_URL = "http://localhost:9999/scm/featureBranches";
    public static final String BUILD_STATUS_URL = "http://localhost:9999/ci/buildStatuses";
    public static URL featureBranchURL;
    public static URL buildStatusURL;

    static {
        try {
            featureBranchURL = new URL(FEATURE_BRANCH_URL);
            buildStatusURL = new URL(BUILD_STATUS_URL);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
    }
}
