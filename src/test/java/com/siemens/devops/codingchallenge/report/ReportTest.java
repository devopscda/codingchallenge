package com.siemens.devops.codingchallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.siemens.devops.codingchallenge.report.Report;
import com.siemens.devops.codingchallenge.report.StatusReport;

/**
 * Tests the Report data type, without connecting to Jetty.
 */
public final class ReportTest {

    private Report report;

    @Before
    public void setUp() {
        report = new StatusReport();
        report.setTitle("REPORT TEST");
        report.setDate(new Date());

        // MANUALLY CREATE FEATURE BRANCHES

        List<String> featureBranches = new ArrayList<>();
        featureBranches.add("feature/abc/TEST-122");
        featureBranches.add("feature/abc/TEST-123");
        featureBranches.add("feature/abc/TEST-124");
        featureBranches.add("feature/xyz/TEST-125");
        featureBranches.add("feature/xyz/TEST-126");
        featureBranches.add("feature/xyz/TEST-127");

        // MANUALLY CREATE BUILD STATUSES

        Map<String, String> buildStatuses = new TreeMap<>();
        buildStatuses.put("feature-abc-TEST-122", "SUCCESS");
        buildStatuses.put("feature-abc-TEST-123", "SUCCESS");
        buildStatuses.put("feature-abc-TEST-124", "FAILURE");

        // CREATE REPORT

        report.setFeatureBranches(featureBranches);
        report.setBuildStatuses(buildStatuses);
    }

    @After
    public void tearDown() {
        report = null;
    }

    // LOOKUP TESTS (COMBINES TWO WEB SERVICES)

    @Test
    public void reportGetResultsEntrySetSizeEquals6(){
        assertEquals(6,report.getResults().entrySet().size());
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
    public void reportGetResultsGetFeatureXyzTest125EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/xyz/TEST-125")
                         .equals("NOT BUILT"));
    }

    @Test
    public void reportGetResultsGetFeatureXyzTest126EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/xyz/TEST-126")
                         .equals("NOT BUILT"));
    }

    @Test
    public void reportGetResultsGetFeatureXyzTest127EqualsNotBuilt() {
        assertTrue(report.getResults()
                         .get("feature/xyz/TEST-127")
                         .equals("NOT BUILT"));
    }

    // FEATURE BRANCH TESTS

    @Test
    public void reportGetFeatureBranchesSizeEquals6() {
        assertEquals(6, report.getFeatureBranches().size());
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
    public void reportGetFeatureBranchesContainsFeatureXyzTest125() {
        assertTrue(report.getFeatureBranches().contains("feature/xyz/TEST-125"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureXyzTest126() {
        assertTrue(report.getFeatureBranches().contains("feature/xyz/TEST-126"));
    }

    @Test
    public void reportGetFeatureBranchesContainsFeatureXyzTest127() {
        assertTrue(report.getFeatureBranches().contains("feature/xyz/TEST-127"));
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

    // NULL TESTS

    @Test
    public void reportIsNotNull() {
        assertNotNull(report);
    }

    @Test
    public void reportGetFeatureBranchesIsNotNull() {
        assertNotNull(report.getFeatureBranches());
    }

    @Test
    public void reportGetBuildStatusesIsNotNNull() {
        assertNotNull(report.getBuildStatuses());
    }

}
