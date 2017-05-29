package com.siemens.devops.codingchallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.siemens.devops.codingchallenge.config.JettyConfig;
import com.siemens.devops.codingchallenge.converter.Converters;
import com.siemens.devops.codingchallenge.printer.ReportPrinter;
import com.siemens.devops.codingchallenge.printer.StatusReportPrinter;
import com.siemens.devops.codingchallenge.report.Report;
import com.siemens.devops.codingchallenge.report.StatusReport;

/**
 * Tests the entire application including:
 *   
 *   Lookups (one web service to another)
 *   Feature Branches (via Report)
 *   Build Statuses (Via Report)
 *   Report Printing
 *   Null Tests
 *
 *   Subdirectories also contain unit tests for specific subsystems.
 */
public final class AppTest {

    private static Server jetty;
    private Report report;
    private ReportPrinter<PrintStream> reportPrinter;

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
        report = new StatusReport();
        report.setTitle("AppTest.java: JETTY TEST DATA");
        report.setDate(new Date());

        // READ DATA FROM JETTY

        List<String> featureBranches = 
            Converters.featureBranchConverter.convert(JettyConfig.featureBranchURL);
        Map<String, String> buildStatuses = 
            Converters.buildStatusConverter.convert(JettyConfig.buildStatusURL);

        // CREATE REPORT

        report.setFeatureBranches(featureBranches);
        report.setBuildStatuses(buildStatuses);
        reportPrinter = new StatusReportPrinter();
        reportPrinter.setReport(report);
    }

    @After
    public void tearDown() {
        report = null;
        reportPrinter = null;
    }

    // LOOKUP TESTS
 
    @Test
    public void reportGetResultsEntrySetSizeEquals7(){
        assertEquals(7,report.getResults().entrySet().size());
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest121EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-121")
                         .equals("NOT BUILT"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest122EqualsSuccess() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-122")
                         .equals("SUCCESS"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest123EqualsSuccess() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-123")
                         .equals("SUCCESS"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest124EqualsFailure() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-124")
                         .equals("FAILURE"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest125EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-125")
                         .equals("NOT BUILT"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest126EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-126")
                         .equals("NOT BUILT"));
    }

    @Test
    public void reportGetResultsGetFeatureAbcTest127EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/abc/TEST-127")
                         .equals("NOT BUILT"));
    }

    // FEATURE BRANCH TESTS

    @Test
    public void reportGetFeatureBranchesSizeEquals7() {
        assertEquals(7, report.getFeatureBranches().size());
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest121() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-121"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest122() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-122"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest123() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-123"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest124() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-124"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest125() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-125"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest126() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-126"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureAbcTest127() {
        assertTrue(report.getFeatureBranches().contains("feature/abc/TEST-127"));
    }

    // BUILD STATUS TESTS

    @Test
    public void reportGetBuildStatusesKeySetSizeEquals3() {
        assertEquals(3, report.getBuildStatuses().keySet().size());
    }

    @Test
    public void reportGetBuildStatusesFeatureAbcTest122EqualsSuccess() {
        assertTrue(report.getBuildStatuses().get("feature-abc-TEST-122").equals("SUCCESS"));
    }

    @Test
    public void reportGetBuildStatusesFeatureAbcTest123EqualsSuccess() {
        assertTrue(report.getBuildStatuses().get("feature-abc-TEST-123").equals("SUCCESS"));
    }

    @Test
    public void reportGetBuildStatusesFeatureAbcTest124EqualsFailure() {
        assertTrue(report.getBuildStatuses().get("feature-abc-TEST-124").equals("FAILURE"));
    }

    // PRINTER TESTS

    @Test
    public void testPrintReport() {
        reportPrinter.printReport(System.out);
    }

    // NULL TESTS

    @Test
    public void jettyIsNotNull() {
        assertNotNull(jetty);
    }

    @Test
    public void reportIsNotNull() {
        assertNotNull(report);
    }

    @Test
    public void reportPrinterIsNotNull() {
        assertNotNull(reportPrinter);
    }

    @Test
    public void reportGetFeatureBranchesIsNotNull() {
        assertNotNull(report.getFeatureBranches());
    }

    @Test
    public void reportGetBuildStatusesIsNotNNull() {
        assertNotNull(report.getBuildStatuses());
    }

    @Test
    public void reportGetResultsIsNotNull() {
        assertNotNull(report.getResults());
    }

}
