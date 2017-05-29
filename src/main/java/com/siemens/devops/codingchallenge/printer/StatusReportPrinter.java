package com.siemens.devops.codingchallenge.printer;

import java.io.PrintStream;

import com.siemens.devops.codingchallenge.report.Report;

/**
 * Defines a concrete status report printer that writes to a PrintStream object.
 */
public class StatusReportPrinter implements ReportPrinter<PrintStream> {

    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void printReport(PrintStream out) {

        out.println();
        out.println("STATUS REPORT: " + report.getTitle());
        out.println("DATE: " + report.getDate());

        out.println("+----------------------------------------+--------------+");
        out.println("| FEATURE BRANCH                         | BUILD STATUS |");
        out.println("+----------------------------------------+--------------+");

        report.getResults().entrySet()
            .stream()
            .forEach(e -> {
                String branch = e.getKey();
                String status = e.getValue();
                out.print("| ");
                out.print(branch);
                out.print("                   ");
                out.print("| ");
                out.print(status);
                if (status.equals("NOT BUILT")) {
                    out.print("    ");
                } else {
                    out.print("      ");
                }
                out.println("|");
            });
        out.println("+----------------------------------------+--------------+");
    }

}

