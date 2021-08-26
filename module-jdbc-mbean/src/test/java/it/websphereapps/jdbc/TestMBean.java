package it.websphereapps.jdbc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMBean extends TestServletClient {
    
    /**
     * Get a connection to make sure connection manager mbean is setup.
     * @throws Exception 
     */
    @BeforeAll
    public static void setup() throws Exception {
        StringBuilder response = runTestWithResponse("getConnection");
        assertTrue(response.toString().contains(SUCCESS));
    }
    
    @Test
    public void testPurge() throws Exception {
        String expected = "KJA1017 POOL CONTENTS AFTER PURGE:";
        StringBuilder response = runTestWithResponse("purge");
        assertTrue(response.toString().contains(expected));
    }
    
    @Test
    public void testPurgeImmediate() throws Exception {
        String expected = "KJA1017 POOL CONTENTS AFTER PURGE IMMEDIATE:";
        StringBuilder response = runTestWithResponse("purgeImmediate");
        assertTrue(response.toString().contains(expected));
    }
    
    @Test
    public void testPurgeAbort() throws Exception {
        String expected = "KJA1017 POOL CONTENTS AFTER PURGE ABORT:";
        StringBuilder response = runTestWithResponse("purgeAbort");
        assertTrue(response.toString().contains(expected));
    }

    @Test
    public void testPoolSize() throws Exception {
        String expected = "KJA1017 POOL SIZE:";
        StringBuilder response = runTestWithResponse("poolSize");
        assertTrue(response.toString().contains(expected));
    }

}
