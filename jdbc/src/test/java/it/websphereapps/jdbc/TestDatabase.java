package it.websphereapps.jdbc;

import io.websphereapps.jdbc.TestResource;
import org.junit.jupiter.api.Test;
import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@MicroShedTest
public class TestDatabase implements SharedContainerConfiguration {

    @Container
    public static OracleContainer oracle = new OracleContainer("wnameless/oracle-xe-11g-r2");

    @Container
    public static ApplicationContainer app = new ApplicationContainer()
            .withAppContextRoot("/jdbcoracle")
            .withReadinessPath("/jdbcoracle/app/resource")
            .withStartupTimeout(Duration.ofMinutes(1));
    @Inject
    private static TestResource dbo;

    @Override
    public void startContainers() {
        oracle.start();
        app.withEnv("JDBC_DRIVER", "ojdbc8_g.jar")
                .withEnv("JDBC_URL", oracle.getJdbcUrl())
                .withEnv("USER", oracle.getUsername())
                .withEnv("PASSWORD", oracle.getPassword());
        app.start();
    }

    @Test
    public void testPost() {
        int key = 3;
        String expected = "test";

        try {
            dbo.insert(key, expected);
        } catch (SQLException e) {
            fail();
        }

        try {
            Response result = dbo.getValue(key);
            assertEquals(expected, result.toString().contains(expected));
        } catch (SQLException e) {
            fail();
        }
    }
}
