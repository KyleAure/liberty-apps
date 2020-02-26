package it.websphereapps.jpa;

import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;


public class Containers implements SharedContainerConfiguration {

    @Container
    public static ApplicationContainer app = new ApplicationContainer()
            .withAppContextRoot("/jpa")
            .withReadinessPath("/jpa/events");


}