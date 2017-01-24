package devops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Feature Branch Report App.
 */
public class FeatureBranchReportTest {

    Server server = null;

    @Before
    public void setUp() throws Exception {
        server = new Server(9999);
        new WebAppContext(server, "src/test/webapp/", "/");

        // Start Server
        server.start();
    }
    
    @After
    public void tearDown() throws Exception {
        // Stop Server
        server.stop();
    }
    
    /**
     * Test the report
     * @throws IOException 
     * @throws MalformedURLException 
     */
    @Test
    public void testReport() throws MalformedURLException, IOException {
        
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
        // Could use some more tests
    }
    
    @Test
    public void testGetFeatureBranches() throws MalformedURLException, IOException {
        final FeatureBranchReport app = new FeatureBranchReport() {
            public String getScmBaseUrl() {
                return "http://localhost:9999/scm";
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
    public void testGetBuildStatuses() throws MalformedURLException, IOException {
        final FeatureBranchReport app = new FeatureBranchReport() {
            public String getCIBaseUrl() {
                return "http://localhost:9999/ci";
            }
        };
        
        final Map<String, String> statuses = app.getCIBuildStatuses();
        assertEquals(3, statuses.keySet().size());
        assertTrue(statuses.get("feature/abc/TEST-122").equals("SUCCESS"));
        assertTrue(statuses.get("feature-abc/TEST-123").equals("SUCCESS"));
        assertTrue(statuses.get("feature/abc/TEST-124").equals("FAILURE"));
    }
}
