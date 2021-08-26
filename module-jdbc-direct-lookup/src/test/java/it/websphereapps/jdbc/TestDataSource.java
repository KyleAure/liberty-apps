package it.websphereapps.jdbc;

import org.junit.jupiter.api.Test;

import io.websphereapps.jdbc.DataSourceServlet;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDataSource extends TestServletClient{

    @Test
    public void testConnection() throws Exception {
        StringBuilder response = runTestWithResponse("getConnection");
        assertTrue(response.toString().contains(SUCCESS));
    }

    @Test
    public void testConnections() throws Exception {
        StringBuilder response = runTestWithResponse("getConnections", CONNECTIONS + "=2");
        assertTrue(response.toString().contains(SUCCESS));
    }
}
