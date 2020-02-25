package io.websphereapps.jaxrs;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("properties")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PropertiesResource {

    @GET
    public Properties getProperties() {
        return System.getProperties();
    }

    public void nonJaxrsMethod() {
        System.out.println("This is a non-JAXRS method and should not be accessible with a REST client!");
    }
}
