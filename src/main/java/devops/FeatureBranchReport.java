package devops;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

/**
 * Read from our SCM and CI servers and report feature branch statuses.
 *
 */
public class FeatureBranchReport {

    private final Gson gson = new Gson();

    public static void main(String[] args) throws MalformedURLException, IOException {

        final FeatureBranchReport app = new FeatureBranchReport();
        
        app.printFeatureBranchBuildReport(System.out);
    }
    
    public String getScmBaseUrl() {
        // TODO read from command line
        return "http://scm-api.example.com";
    }
    
    public String getCIBaseUrl() {
        // TODO read from command line
        return "http://ci-api.example.com";
    }

    public void printFeatureBranchBuildReport(final PrintStream out) throws MalformedURLException, IOException {

        final Collection<String> branches = getFeatureBranches();
        final Map<String, String> statuses = getCIBuildStatuses();

        out.println("+----------------------------------------+-----------+");
        out.println("| Feature Branch                         | Status    |");
        out.println("+----------------------------------------+-----------+");
        for (final String branch : branches) {
            String status = "NOT BUILT";
            if (statuses.keySet().contains(branch)) {
                status = statuses.get(branch);
            }
            String line = new String("");
            line += "| ";
            line += branch;
            line += "                                       ";
            line += "| ";
            line += status;
            line += "          ";
            line += "|";
            out.println(line);
        }
        out.println("+----------------------------------------+-----------+");
    }

    /**
     * Retrieve feature branch names from SCM server.
     * @return Collection of feature branch names
     * @throws MalformedURLException
     * @throws IOException
     */
    public Collection<String> getFeatureBranches() throws MalformedURLException, IOException {
        
        final HttpURLConnection http = (HttpURLConnection)new URL(getScmBaseUrl() + "/featureBranches").openConnection();
        http.connect();

        @SuppressWarnings("unchecked")
        Collection<Object> objects = gson.fromJson(new InputStreamReader(http.getInputStream()), Collection.class);
        final Collection<String> branches = new ArrayList<String>();
        for (final Object object : objects) {
            if (object instanceof String) {
                branches.add((String)object);
            }
        }
        
        return branches;
    }

    /**
     * Retrieve statuses of continuous integration builds.
     * @return Map of job name to status
     * @throws MalformedURLException
     * @throws IOException
     */
    public Map<String, String> getCIBuildStatuses() throws MalformedURLException, IOException {
        
        final HttpURLConnection http = (HttpURLConnection)new URL(getCIBaseUrl() + "/buildStatuses").openConnection();
        http.connect();

        @SuppressWarnings("unchecked")
        Map<String, Object> objects = gson.fromJson(new InputStreamReader(http.getInputStream()), Map.class);
        final Map<String, String> statuses = new TreeMap<String, String>();
        for (final String key : objects.keySet()) {
            if (objects.get(key) instanceof String) {
                statuses.put(key, (String)objects.get(key));
            }
        }
        
        return statuses;
    }
}
