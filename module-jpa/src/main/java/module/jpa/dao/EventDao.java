package module.jpa.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import module.jpa.modles.Event;

import javax.enterprise.context.RequestScoped;

@RequestScoped
// tag::EventDao[]
public class EventDao {
    
    // tag::PersistenceContext[]
    @PersistenceContext(name = "jpa-unit")
    // end::PersistenceContext[]
    private EntityManager em;
    
    // tag::createEvent[]
    public void createEvent(Event event) {
        // tag::Persist[]
        em.persist(event);
        // end::Persist[]
    }
    // end::createEvent[]

    // tag::readEvent[]
    public Event readEvent(int eventId) {
        // tag::Find[]
        return em.find(Event.class, eventId);
        // end::Find[]
    }
    // end::readEvent[]

    // tag::updateEvent[]
    public void updateEvent(Event event) {
        // tag::Merge[]
        em.merge(event);
        // end::Merge[]
    }
    // end::updateEvent[]

    // tag::deleteEvent[]
    public void deleteEvent(Event event) {
        // tag::Remove[]
        em.remove(event);
        // end::Remove[]
    }
    // end::deleteEvent[]

    // tag::readAllEvents[]
    public List<Event> readAllEvents() {
        return em.createNamedQuery("Event.findAll", Event.class).getResultList();
    }
    // end::readAllEvents[]

    // tag::findEvent[]
    public List<Event> findEvent(String name, String location, String time) {
        return em.createNamedQuery("Event.findEvent", Event.class)
            .setParameter("name", name)
            .setParameter("location", location)
            .setParameter("time", time).getResultList();
    }
    // end::findEvent[]
}
// end::EventDao[]