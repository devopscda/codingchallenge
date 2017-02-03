package devops;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                final Collection<String> branches = new ArrayList<String>();
                
                branches.add("feature/abc/TEST-123");
                branches.add("feature/abc/TEST-124");
                branches.add("feature/xyz/TEST-125");
                branches.add("feature/xyz/TEST-126");
                branches.add("feature/xyz/TEST-127");
                
                return branches;
            }

            public Map<String, String> getCIBuildStatuses() {
                final Map<String, String> statuses = new TreeMap<String, String>();
                
                statuses.put("feature-abc-TEST-122", "SUCCESS");
                statuses.put("feature-abc-TEST-123", "SUCCESS");
                statuses.put("feature-abc-TEST-124", "FAILURE");
                
                return statuses;
            }
        };
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        app.printFeatureBranchBuildReport(new PrintStream(baos));
        
        final String report = baos.toString("UTF-8");
        
        assertTrue(report.contains("feature/abc/TEST-123"));
        // TODO Could use some more tests
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
