package devops;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static devops.FeatureBranchReport.NOT_BUILT;
import static org.junit.Assert.*;

/**
 * Unit tests for Feature Branch Report App.
 */
public class FeatureBranchReportTest {

    private static final int TEST_PORT = 9999;
    private Server server = null;

    @Before
    public void setUp() throws Exception {
        server = new Server(TEST_PORT);
        new WebAppContext(server, "src/test/webapp/", "/");

        // Start Server
        server.start();
    }
    
    @After
    public void tearDown() throws Exception {
        // Stop Server
        if (server != null) {
            server.stop();
        }
    }
    
    /**
     * Test the report
     * @throws IOException during retrieval of info for report or during printing of report.
     */
    @Test
    public void testReport() throws IOException {
        
        final FeatureBranchReport app = new FeatureBranchReport() {
            public Collection<String> getFeatureBranches() {
                final Collection<String> branches = new ArrayList<>();
                
                branches.add("feature/abc/TEST-123");
                branches.add("feature/abc/TEST-124");
                branches.add("feature/xyz/TEST-125");
                branches.add("feature/xyz/TEST-126");
                branches.add("feature/xyz/TEST-127");
                
                return branches;
            }

            public Map<String, String> getCIBuildStatuses() {
                final Map<String, String> statuses = new TreeMap<>();

                // NOTE XXX see below tests
                //statuses.put("feature-abc-TEST-122", "SUCCESS");
                statuses.put("feature-abc-TEST-123", "SUCCESS");
                statuses.put("feature-abc-TEST-124", "FAILURE");
                
                return statuses;
            }
        };

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        app.printFeatureBranchBuildReport(new PrintStream(baos));
        final String report = baos.toString("UTF-8");

        testReports(app.getFeatureBranches(), app.getCIBuildStatuses(), report);
    }

    @Test
    public void testReportFromWebApp() throws IOException {
        final FeatureBranchReport app = new FeatureBranchReport() {
            public String getScmBaseUrl() {
                return "http://localhost:" + TEST_PORT + "/scm";
            }
            public String getCIBaseUrl() {
                return "http://localhost:" + TEST_PORT + "/ci";
            }
        };

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        app.printFeatureBranchBuildReport(new PrintStream(baos));
        final String report = baos.toString("UTF-8");
        testReports(app.getFeatureBranches(), app.getCIBuildStatuses(), report);
    }


    private void testReports(Collection<String> branches, Map<String, String> ciBuildStatuses, String report) {
        // sanity checks
        assertNotNull(report);
        assertFalse(report.isEmpty());

        // happy paths
        // every feature branch shows up in the report
        branches.forEach(branch -> assertTrue(report.contains(branch)));
        // every status value
        ciBuildStatuses.values().stream().distinct().forEach(status -> assertTrue(report.contains(status)));
        // number of NOT_BUILT
        int notBuiltCount = 0;
        for (String branch : branches) {
            if (!ciBuildStatuses.containsKey(FeatureBranchReport.prepareBuildName(branch))) {
                notBuiltCount++;
            }
        }
        assertEquals(notBuiltCount, StringUtils.countMatches(report, NOT_BUILT));

        // TODO confirm: is it possible to have a CI build name that does not have a known feature branch?
        ciBuildStatuses.keySet().forEach(
                b -> assertTrue(branches.contains(FeatureBranchReport.reverseBuildName(b))));

        // negative tests
        // random
        assertFalse(report.contains("feature-abc-TEST-122"));
        assertFalse(report.contains("feature/abc/TEST/122"));
        assertFalse(report.contains("feature/abc/TEST-12３"));

        // status build names not in report -- different than branch names
        ciBuildStatuses.keySet().forEach(statusBranch -> assertFalse(report.contains(statusBranch)));
        branches.forEach(
                branch -> assertFalse(report.contains(FeatureBranchReport.prepareBuildName(branch))));
    }
    
    @Test
    public void testGetFeatureBranches() throws IOException {
        final FeatureBranchReport app = new FeatureBranchReport() {
            public String getScmBaseUrl() {
                return "http://localhost:" + TEST_PORT + "/scm";
            }
        };
        
        final Collection<String> branches = app.getFeatureBranches();
        assertEquals(7, branches.size());
        assertTrue(branches.contains("feature/abc/TEST-121"));
        assertTrue(branches.contains("feature/abc/TEST-122"));
        assertTrue(branches.contains("feature/abc/TEST-123"));
        assertTrue(branches.contains("feature/abc/TEST-124"));
        assertTrue(branches.contains("feature/abc/TEST-125"));
        assertTrue(branches.contains("feature/abc/TEST-126"));
        assertTrue(branches.contains("feature/abc/TEST-127"));
    }

    @Test
    public void testGetBuildStatuses() throws IOException {
        final FeatureBranchReport app = new FeatureBranchReport() {
            public String getCIBaseUrl() {
                return "http://localhost:" + TEST_PORT + "/ci";
            }
        };
        
        final Map<String, String> statuses = app.getCIBuildStatuses();
        assertEquals(3, statuses.keySet().size());
        assertTrue(statuses.get("feature-abc-TEST-122").equals("SUCCESS"));
        assertTrue(statuses.get("feature-abc-TEST-123").equals("SUCCESS"));
        assertTrue(statuses.get("feature-abc-TEST-124").equals("FAILURE"));
    }
}
