package it.websphereapps.jaxrs.jdbcderby.io.openliberty.guides.event;

import io.openliberty.guides.event.resources.EventResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.jupiter.MicroShedTest;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@MicroShedTest
@SharedContainerConfig(Containers.class)
public class EventEntityTest {

    private static final String JSONFIELD_LOCATION = "location";
    private static final String JSONFIELD_NAME = "name";
    private static final String JSONFIELD_TIME = "time";
    private static final String JSONFIELD_ID = "id";
    private static final String EVENT_TIME = "12:00 PM, January 1 2018";
    private static final String EVENT_LOCATION = "IBM";
    private static final String EVENT_NAME = "JPA Guide";
    private static final String UPDATE_EVENT_TIME = "12:00 PM, February 1 2018";
    private static final String UPDATE_EVENT_LOCATION = "IBM Updated";
    private static final String UPDATE_EVENT_NAME = "JPA Guide Updated";
    private static final int NO_CONTENT_CODE = Status.NO_CONTENT.getStatusCode();
    private static final int NOT_FOUND_CODE = Status.NOT_FOUND.getStatusCode();
    @Inject
    public static EventResource eventRes;

    @AfterEach
    public void tearDown() {
        JsonArray events = eventRes.getEvents();
        for (int i = 0; i < events.size(); i++) {
            JsonObject event = events.getJsonObject(i);
            eventRes.deleteEvent(event.getInt("id"));
        }
    }

    @Test
    public void testConnection() {
        JsonArray arr = eventRes.getEvents();
        System.out.println(arr);
    }

    @Test
    public void testInvalidRead() {
        assertTrue("Reading an event that does not exist should return an empty list", eventRes.getEvent(-1).isEmpty());
    }

    @Test
    public void testInvalidDelete() {
        int deleteResponse = eventRes.deleteEvent(-1).getStatus();
        assertEquals("Trying to delete an event that does not exist should return the "
                + "HTTP response code " + NOT_FOUND_CODE, NOT_FOUND_CODE, deleteResponse);
    }

    @Test
    public void testInvalidUpdate() {
        int updateResponse = eventRes.updateEvent(EVENT_NAME, EVENT_TIME, EVENT_LOCATION, -1).getStatus();
        assertEquals("Trying to update an event that does not exist should return the "
                + "HTTP response code " + NOT_FOUND_CODE, NOT_FOUND_CODE, updateResponse);
    }

    @Test
    public void testReadIndividualEvent() {
        int postResponse = eventRes.addNewEvent(EVENT_NAME, EVENT_TIME, EVENT_LOCATION).getStatus();
        assertEquals("Creating an event should return the HTTP response code " +
                NO_CONTENT_CODE, NO_CONTENT_CODE, postResponse);

        JsonObject event = eventRes.getEvent(1);
        assertData(event);

        int deleteResponse = eventRes.deleteEvent(event.getInt("id")).getStatus();
        assertEquals("Deleting an event should return the HTTP response code " +
                NO_CONTENT_CODE, NO_CONTENT_CODE, deleteResponse);
    }

    // //@Test
    // public void testCRUD() {
    //     int eventCount = getRequest().size();
    //     int postResponse = postRequest(eventForm);
    //     assertEquals("Creating an event should return the HTTP response code " + 
    //         NO_CONTENT_CODE, NO_CONTENT_CODE, postResponse);

    //     Event e = new Event(EVENT_NAME, EVENT_LOCATION, EVENT_TIME);
    //     JsonObject event = findEvent(e);
    //     assertData(event, EVENT_NAME, EVENT_LOCATION, EVENT_TIME);

    //     eventForm.put(JSONFIELD_NAME, UPDATE_EVENT_NAME);
    //     eventForm.put(JSONFIELD_LOCATION, UPDATE_EVENT_LOCATION);
    //     eventForm.put(JSONFIELD_TIME, UPDATE_EVENT_TIME);
    //     int updateResponse = updateRequest(eventForm, event.getInt("id"));
    //     assertEquals("Updating an event should return the HTTP response code " + 
    //         NO_CONTENT_CODE, NO_CONTENT_CODE, updateResponse);

    //     e = new Event(UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);
    //     event = findEvent(e);
    //     assertData(event, UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);

    //     int deleteResponse = deleteRequest(event.getInt("id"));
    //     assertEquals("Deleting an event should return the HTTP response code " + 
    //         NO_CONTENT_CODE, NO_CONTENT_CODE, deleteResponse);
    //     assertEquals("Total number of events stored should be the same after testing "
    //         + "CRUD operations.", eventCount, getRequest().size());
    // }

    /**
     * Asserts event fields (name, location, time) equal the provided name, location
     * and date
     */
    protected void assertData(JsonObject event) {
        assertEquals(event.getString("name"), EventEntityTest.EVENT_NAME);
        assertEquals(event.getString("location"), EventEntityTest.EVENT_LOCATION);
        assertEquals(event.getString("time"), EventEntityTest.EVENT_TIME);
    }
}