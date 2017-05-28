package com.siemens.devops.codingchallenge.report;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface Lookup {

    /**
     * Specifies lookup behavior across two collections, 
     * generating results suitable for printing in a report.
     */
    public Map<String,String> lookup(List<String> featureBranches, Map<String,String> buildStatuses);

}
