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

import com.siemens.devops.codingchallenge.printer.ReportPrinter;
import com.siemens.devops.codingchallenge.printer.StatusReportPrinter;
import com.siemens.devops.codingchallenge.report.Report;
import com.siemens.devops.codingchallenge.report.StatusReport;

/**
 * Tests report printing to a specified output target.
 */
public final class PrinterTest {

    private Report report;
    private ReportPrinter<PrintStream> reportPrinter;

    @Before
    public void setUp() {
        report = new StatusReport();
        report.setTitle("PrinterTest.java: MANUAL TEST DATA");
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
        reportPrinter = new StatusReportPrinter();
        reportPrinter.setReport(report);
    }

    @After
    public void tearDown() {
        report = null;
        reportPrinter = null;
    }

    // PRINTER TESTS

    @Test
    public void testPrintReport() {
        reportPrinter.printReport(System.out);
    }

    // NULL TESTS

    @Test
    public void reportIsNotNull() {
        assertNotNull(report);
    }

    @Test
    public void reportPrinterIsNotNull() {
        assertNotNull(reportPrinter);
    }

}
