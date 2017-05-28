package com.siemens.devops.codingchallenge;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.siemens.devops.codingchallenge.config.JettyConfig;
import com.siemens.devops.codingchallenge.converter.Converters;
import com.siemens.devops.codingchallenge.printer.ReportPrinter;
import com.siemens.devops.codingchallenge.printer.StatusReportPrinter;
import com.siemens.devops.codingchallenge.report.Report;
import com.siemens.devops.codingchallenge.report.StatusReport;

/**
 * USAGE: mvn exec:java
 *        Hit enter to accept default server URLs
 *        or enter new URLs when prompted 
 *        (for example http://127.0.0.1:9999).
 */
public class App {

    private static final String DEFAULT_SCM_BASE_URL = "http://localhost:9999";
    private static final String DEFAULT_CI_BASE_URL = "http://localhost:9999";
    private static final String SCM_SUFFIX = "/scm/featureBranches";
    private static final String CI_SUFFIX = "/ci/buildStatuses";

    private Scanner scanner = new Scanner(System.in);
    private String scmUrl;
    private String ciUrl;
    private Server jetty;
    private URL featureBranchURL;
    private URL buildStatusURL;
    private Report report;
    private ReportPrinter<PrintStream> reportPrinter;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.startServer();
        app.printBanner();
        app.getScmUrlUserInput();
        app.getCiUrlUserInput();
        app.printServerUrls();
        app.initReportData();
        app.printStatusReport();
        app.stopServer();
    }

    private void printBanner() {
        System.out.println("+==================================================+");
        System.out.println("| STATUS REPORTER V 1.0                            |");
        System.out.println("+==================================================+");
        System.out.println("| ENTER SERVER URLs, OR RETURN TO ACCEPT DEFAULTS. |");
        System.out.println("+==================================================+");
    }

    private void startServer() throws Exception {
        jetty = new Server(JettyConfig.PORT);
        new WebAppContext(jetty, JettyConfig.WEB_APP, JettyConfig.CONTEXT_PATH);
        jetty.start();
    }

    private void stopServer() throws Exception {
        jetty.stop();
    }

    private void getScmUrlUserInput() {
        System.out.println("SCM BASE URL: [http://localhost:9999] ");
        scmUrl = scanner.nextLine();
        if (scmUrl.isEmpty()) {
            scmUrl = DEFAULT_SCM_BASE_URL + SCM_SUFFIX;
        } else {
            scmUrl = scmUrl + SCM_SUFFIX;
        }
    }

    private void getCiUrlUserInput() {
        System.out.println("CI BASE URL: [http://localhost:9999] ");
        ciUrl = scanner.nextLine();
        if (ciUrl.isEmpty()) {
            ciUrl = DEFAULT_CI_BASE_URL + CI_SUFFIX;
        } else {
            ciUrl = ciUrl + CI_SUFFIX;
        }
    }

    public void initReportData() {
        try {
            featureBranchURL = new URL(scmUrl);
            buildStatusURL = new URL(ciUrl);
        } catch (MalformedURLException e) {
            System.err.println("BAD URL: " + e.getMessage());
            System.exit(0);
        }
        report = new StatusReport();
        report.setTitle("App.java: JETTY WEB SERVICE");
        report.setDate(new Date());
        List<String> featureBranches = Converters.featureBranchConverter.convert(featureBranchURL);
        Map<String, String> buildStatuses = Converters.buildStatusConverter.convert(buildStatusURL);
        report.setFeatureBranches(featureBranches);
        report.setBuildStatuses(buildStatuses);
        reportPrinter = new StatusReportPrinter();
        reportPrinter.setReport(report);
    }

    private void printServerUrls() {
        System.out.println("CONNECTING TO SCM SERVER AT: " + scmUrl);
        System.out.println("CONNECTING TO CI SERVER AT: " + ciUrl);
    }

    private void printStatusReport() {
        reportPrinter.printReport(System.out);
    }
        
}
