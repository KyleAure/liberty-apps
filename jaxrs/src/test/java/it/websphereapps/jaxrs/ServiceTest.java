package it.websphereapps.jaxrs;

import io.websphereapps.jaxrs.PropertiesResource;
import org.junit.jupiter.api.Test;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.ws.rs.ProcessingException;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@MicroShedTest
public class ServiceTest {
    @Container
    public static ApplicationContainer app = new ApplicationContainer()
            .withAppContextRoot("/jaxrs")
            .withReadinessPath("/jaxrs/system/properties");

    @RESTClient
    public static PropertiesResource propRes;

    @Test
    public void testGetRequest() {
        Properties res = propRes.getProperties();
        assertFalse("Response should not be empty", res.isEmpty());
    }

    @Test
    public void testNonJaxrsMethod() {
        assertThrows(ProcessingException.class, () -> propRes.nonJaxrsMethod());
    }
}