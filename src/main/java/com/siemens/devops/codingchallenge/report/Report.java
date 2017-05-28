package com.siemens.devops.codingchallenge.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Specifies a report of feature branches and build statuses.
 */
public interface Report {

    public String getTitle();
    public void setTitle(String title);

    public Date getDate();
    public void setDate(Date date);

    /**
     * Feature branches, read from SCM server.
     */
    public List<String> getFeatureBranches();
    public void setFeatureBranches(List<String> featureBranches);

    /**
     * Build statuses, read from CI server.
     */
    public Map<String, String> getBuildStatuses();
    public void setBuildStatuses(Map<String, String> buildStatuses);

    /**
     * Looks up all feature branches against the map of build statuses, 
     * the results of which can be printed out in a report.
     */
    public Map<String,String> getResults();

}

