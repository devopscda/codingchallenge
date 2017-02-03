package devops;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Read from our SCM and CI servers and report feature branch statuses.
 *
 */
public class FeatureBranchReport {

    private final Gson gson = new Gson();
    static final String NOT_BUILT = "NOT BUILT";

    public static void main(String[] args) throws IOException {

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

    public void printFeatureBranchBuildReport(final PrintStream out) throws IOException {

        final Collection<String> branches = getFeatureBranches();
        final Map<String, String> statuses = getCIBuildStatuses();

        out.println("+----------------------------------------+-----------+");
        out.println("| Feature Branch                         | Status    |");
        out.println("+----------------------------------------+-----------+");
        for (final String branch : branches) {
            String status = NOT_BUILT;
            if (statuses.keySet().contains(prepareBuildName(branch))) {
                status = statuses.get(prepareBuildName(branch));
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("| ");
            sb.append(branch);
            sb.append("                                       | ".substring(branch.length()));
            sb.append(status);
            sb.append("          |".substring(status.length()));
            out.println(sb.toString());
        }
        out.println("+----------------------------------------+-----------+");
    }

    /**
     * Prepare branch names to match build names.
     * @param branch name of branch
     * @return replaces all '/' with '-'
     */
    static String prepareBuildName(String branch) {
        return branch.replace('/', '-');
    }

    // not really a good idea after all but for consistencies sake
    static String reverseBuildName(String build) {
        int lastHyphen = build.lastIndexOf('-');
        String branchName = build.replace('-', '/').substring(0, lastHyphen);
        branchName += '-';
        branchName += build.replace('-', '/').substring(lastHyphen + 1);
        return branchName;
    }

    /**
     * Retrieve feature branch names from SCM server.
     * @return Collection of feature branch names
     * @throws IOException when data cannot be retrieved from the URL
     */
    public Collection<String> getFeatureBranches() throws IOException {
        
        final HttpURLConnection http = (HttpURLConnection)new URL(getScmBaseUrl() + "/featureBranches").openConnection();
        http.connect();

        @SuppressWarnings("unchecked")
        Collection<Object> objects = gson.fromJson(new InputStreamReader(http.getInputStream()), Collection.class);
        final Collection<String> branches = new ArrayList<>();
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
     * @throws IOException when data cannot be retrieved from the URL
     */
    public Map<String, String> getCIBuildStatuses() throws IOException {
        
        final HttpURLConnection http = (HttpURLConnection)new URL(getCIBaseUrl() + "/buildStatuses").openConnection();
        http.connect();

        @SuppressWarnings("unchecked")
        Map<String, Object> objects = gson.fromJson(new InputStreamReader(http.getInputStream()), Map.class);
        final Map<String, String> statuses = new TreeMap<>();
        for (final String key : objects.keySet()) {
            if (objects.get(key) instanceof String) {
                statuses.put(key, (String)objects.get(key));
            }
        }
        
        return statuses;
    }
}
