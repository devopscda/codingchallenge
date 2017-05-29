package com.siemens.devops.codingchallenge.printer;

import com.siemens.devops.codingchallenge.report.Report;

/**
 * Specifies a report printer capable of writing to any destination type.
 */
public interface ReportPrinter<T> {

    public Report getReport();

    public void setReport(Report report);

    public void printReport(T t);

}
