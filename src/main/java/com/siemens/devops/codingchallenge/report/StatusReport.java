package com.siemens.devops.codingchallenge.report;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Defines a concrete implementation of the Report interface.
 */
public class StatusReport implements Report {

    private String title;
    private Date date;
    private List<String> featureBranches;
    private Map<String, String> buildStatuses;

    public List<String> getFeatureBranches() {
        return featureBranches;
    }

    public void setFeatureBranches(List<String> featureBranches) {
        this.featureBranches = featureBranches;
    }

    public Map<String, String> getBuildStatuses() {
        return buildStatuses;
    }

    public void setBuildStatuses(Map<String, String> buildStatuses) {
        this.buildStatuses = buildStatuses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Traditional lambda expression to look up a feature branch (web service #1)
     * in build statuses (web service #2), the result of which 
     * will be suitable for priting in a status report.
     */
    private Lookup lookupExpression1 = (f, b) -> {

        Map<String,String> lookupResults = new TreeMap<>();
        featureBranches.stream()
        .forEach( e -> {
            String status = "NOT BUILT"; 
            String toMatch = e.replaceAll("/", "-");
            if(buildStatuses.keySet().contains(toMatch)) {
                status = buildStatuses.get(toMatch);
            }
            lookupResults.put(e,status); 
        });

        return lookupResults;
    }; 

    /**
     * Filter-based lambda expression to look up a feature branch (web service #1)
     * in build statuses (web service #2), the result of which 
     * will be suitable for priting in a status report.
     */
    private Lookup lookupExpression2 = (f, b) -> {

        Map<String,String> lookupResults = new TreeMap<>();
        Set keySet = buildStatuses.keySet();

        // BUILT
        featureBranches.stream()
        .filter(e -> keySet.contains(e.replaceAll("/","-")))
        .forEach(e -> lookupResults.put(e,
                          buildStatuses.get(
                            e.replaceAll("/","-"))));

        // NOT BUILT
        featureBranches.stream()
        .filter(e -> !keySet.contains(e.replaceAll("/","-")))
        .forEach(e -> lookupResults.put(e,"NOT BUILT"));

        return lookupResults;
    }; 

    /**
     * Gets the results, using the chosen lambda expression from above.
     */
    public Map<String,String> getResults() {
        return lookupExpression2.lookup(featureBranches, buildStatuses);
    }

}

