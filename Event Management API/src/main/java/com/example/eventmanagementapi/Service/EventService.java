package com.example.eventmanagementapi.Service;

import com.example.eventmanagementapi.Model.Attendee;
import com.example.eventmanagementapi.Model.Event;
import com.example.eventmanagementapi.Model.Organizer;
import com.example.eventmanagementapi.Repository.EventRepository;
import com.example.eventmanagementapi.Repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventRepository repository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private AttendeeService attendeeService;

    public Event addEvent(Event event) {
        event.setEventId(UUID.randomUUID().toString().split("-")[0]);
        return repository.save(event);
    }

    public List<Event> findAllEvents() {
        return repository.findAll();
    }
    public Page<Event> findAllEvents(int page, int size, String sortField) {
        Sort.Direction direction= Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return repository.findAll(pageable);
    }

    public Event getEventByEventId(String eventId) {
        return repository.findById(eventId).get();
    }

    public List<Event> getEventByName(String name) {
        return repository.findByName(name);
    }

    public List<Event> getEventByLocation(String location) {
        return repository.findByLocation(location);
    }

    public List<Event> getEventByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<Event> getEventByTime(LocalTime time) {
        return repository.findByTime(time);
    }

    public List<Event> getEventByOrganizer(String organizerId) {
        return repository.findByOrganizer(organizerId);
    }

    public Event getEventByLDT(String location, LocalDate date, LocalTime time) {
        return repository.findByLDT(location, date, time);
    }
    public Event updateEvent(Event eventRequest) {
        //get the existing document from db
        Event existingEvent = repository.findById(eventRequest.getId()).get();
        existingEvent.setName(eventRequest.getName()); //Sets the new name into the existing event name
        existingEvent.setLocation(eventRequest.getLocation());
        existingEvent.setDate(eventRequest.getDate());
        existingEvent.setTime(eventRequest.getTime());
        existingEvent.setOrganizerId(eventRequest.getOrganizerId());

            Attendee attendee;
            for (int i = 0; i < existingEvent.attendees.size(); i++) {
                attendee = existingEvent.attendees.get(i);
                    for (int j = 0; j < attendee.AttendedEventsId.size(); j++) {
                        if (attendee.AttendedEventsId.get(j).equals(existingEvent.getId())) {
                            attendee.AttendedEventsId.remove(j);
                            attendeeService.updateAttendee(attendee);     //deleting old instances of event ids in AttendedEventIds in attendees
                        }
                    }
            }
            existingEvent.attendees.clear();     //clearing attendees list in event

            for (int i = 0; i < eventRequest.attendees.size(); i++) {
                attendee = eventRequest.attendees.get(i);
                existingEvent.attendees.add(attendee);             //updating attendees in event array of attendees
                attendee.AttendedEventsId.add(eventRequest.getId());
                attendeeService.updateAttendee(attendee);  //updating event ids in attendee
            }

        ////////////// Organizer update part
        Organizer organizer;
            organizer = organizerRepository.findById(existingEvent.getOrganizerId()).get();
            for (int i = 0; i < organizer.OrganizedEvents.size(); i++) {
                if (organizer.OrganizedEvents.get(i).getId().equals(eventRequest.getId())) {
                    organizer.OrganizedEvents.remove(i);
                    organizerRepository.save(organizer); //removing unwanted event in old organizer
                }
            }
            organizer = organizerRepository.findById(eventRequest.getOrganizerId()).get();
            for (int i = 0; i < organizer.OrganizedEvents.size(); i++) {
                if (organizer.OrganizedEvents.get(i).getId().equals(eventRequest.getId())) {
                    organizer.OrganizedEvents.remove(i);
                    organizer.OrganizedEvents.add(eventRequest);
                }else organizer.OrganizedEvents.add(eventRequest);
                organizerRepository.save(organizer); //adding event in new organizer
            }

        return repository.save(existingEvent);
    }

    public String deleteById(String eventId) {
        repository.deleteById(eventId);
        return eventId + " Deleted successfully";
    }
}
