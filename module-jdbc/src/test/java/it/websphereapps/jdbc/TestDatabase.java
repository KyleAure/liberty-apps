package it.websphereapps.jdbc;

import io.websphereapps.jdbc.KeyValueResource;

import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestDatabase {

    @Inject
    KeyValueResource kvr;

    @Test
    public void testPost() {
        int key = 3;
        String expected = "test";

        try {
            kvr.insert(key, expected);
        } catch (SQLException e) {
            fail();
        }

        try {
            Response result = kvr.getValue(key);
            assertEquals(expected, result.toString().contains(expected));
        } catch (SQLException e) {
            fail();
        }
    }
}
