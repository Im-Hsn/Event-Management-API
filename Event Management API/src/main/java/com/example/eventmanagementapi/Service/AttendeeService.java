package com.example.eventmanagementapi.Service;
import com.example.eventmanagementapi.EventSort;
import com.example.eventmanagementapi.Model.Attendee;
import com.example.eventmanagementapi.Model.Event;
import com.example.eventmanagementapi.Model.Organizer;
import com.example.eventmanagementapi.Repository.AttendeeRepository;
import com.example.eventmanagementapi.Repository.EventRepository;
import com.example.eventmanagementapi.Repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AttendeeService {
    @Autowired
    private AttendeeRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    public Attendee addAttendee(Attendee attendee) {
        attendee.setAttendeeId(UUID.randomUUID().toString().split("-")[0]); //auto generate id
        return repository.save(attendee);
    }
    public List<Attendee> findAllAttendee() {
        return repository.findAll();
    }

    public Attendee getAttendeeById(String attendeeId) {
        return repository.findById(attendeeId).get();
    }

    public List<Attendee> getAttendeeByName(String name) {
        return repository.findByName(name);
    }

    public List<Attendee> getAttendeeByEmailAddress(String EmailAddress) {
        return repository.findByEmailAddress(EmailAddress);
    }

    public Attendee getAttendeeByEvent(String EventId) {
        return repository.findByAttendedEvent(EventId);
    }

    public boolean attendeeExist(List<Attendee> attendees, Attendee attendee) {
        for (int j = 0; j < attendees.size(); j++) {
            if (attendee.getId().equals(attendees.get(j).getId())) return true;
        }
        return false;
    }
    public boolean eventAttended(String event, List<String> events){
        for(int i=0;i<events.size();i++){
            if(event.equals(events.get(i))) return true;
        }return false;
    }
    public void filterEvents(Attendee attendee, int bool) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < eventRepository.findAll().size(); i++) {
            if (!eventAttended(eventRepository.findAll().get(i).getId(), attendee.AttendedEventsId) && bool == 0)
                events.add(eventRepository.findAll().get(i));
            else if (eventAttended(eventRepository.findAll().get(i).getId(), attendee.AttendedEventsId) && bool == 1)
                events.add(eventRepository.findAll().get(i));
        }
        if (attendee.AttendedEventsId.size() == 0) System.out.println("\nNot attended in any event:\n");

        else {
            System.out.println("Events sorted by location:");
            Collections.sort(events, new EventSort()); //Sorting Events by location before displaying to attendee

            System.out.println("\nAttended events:\n");
            for (int j = 0; j < events.size(); j++) {
                System.out.println("Event ID:" + events.get(j).getId());
                System.out.println("Name:" + events.get(j).getName());
                System.out.println("Location:" + events.get(j).getLocation());
                System.out.println("Date:" + events.get(j).getDate());
                System.out.println("Time:" + events.get(j).getTime());
                System.out.println("Organizer ID:" + events.get(j).getOrganizerId() + "\n");
            }
        }
    }

    public Attendee updateAttendee(Attendee AttendeeRequest) {
        //get the existing document from db
        Attendee existingAttendee = repository.findById(AttendeeRequest.getId()).get();
        existingAttendee.setName(AttendeeRequest.getName()); //Sets the new name into the existing name
        existingAttendee.setEmailAddress(AttendeeRequest.getEmailAddress());

        Event existingEvent;
        Organizer organizer;
        for (int i = 0; i < existingAttendee.AttendedEventsId.size(); i++) {
            existingEvent = eventRepository.findById(existingAttendee.AttendedEventsId.get(i)).get();
            for (int j = 0; j < existingEvent.attendees.size(); j++) {
                if (existingEvent.attendees.get(j).getId().equals(existingAttendee.getId())) {
                    existingEvent.attendees.remove(j);
                    eventRepository.save(existingEvent);
                }  //removing old instances of attendee in events
            }
            organizer = organizerRepository.findById(existingEvent.getOrganizerId()).get();
            for (int j = 0; j < organizer.OrganizedEvents.size(); j++) {
                for (int t = 0; t < organizer.OrganizedEvents.get(j).attendees.size(); t++) {
                    if (organizer.OrganizedEvents.get(j).attendees.get(t).getId().equals(AttendeeRequest.getId())) {
                        organizer.OrganizedEvents.get(j).attendees.remove(t);
                        organizerRepository.save(organizer); //deleting old attendees in organizer database
                    }
                }
            }
        }
        existingAttendee.AttendedEventsId.clear(); //clearing existing list before updating

        for (int i = 0; i < AttendeeRequest.AttendedEventsId.size(); i++) {
            existingEvent = eventRepository.findById(AttendeeRequest.AttendedEventsId.get(i)).get();
            existingAttendee.AttendedEventsId.add(existingEvent.getId()); //adding updated event ids

            for (int j = 0; j < organizerRepository.findAll().size(); j++) {
                organizer = organizerRepository.findAll().get(j);
                for (int t = 0; t < organizer.OrganizedEvents.size(); t++) {
                    if (organizer.OrganizedEvents.get(t).getId().equals(existingEvent.getId()) && !attendeeExist(organizer.OrganizedEvents.get(t).attendees, AttendeeRequest)) {
                        organizer.OrganizedEvents.get(t).attendees.add(AttendeeRequest);
                        organizerRepository.save(organizer); //adding new attendee in organizer database
                    } else if (organizer.OrganizedEvents.get(t).getId().equals(existingEvent.getId()))
                        for (int k = 0; k < organizer.OrganizedEvents.get(t).attendees.size(); k++) {
                            if (organizer.OrganizedEvents.get(t).attendees.get(k).getId().equals(AttendeeRequest.getId()))
                                organizer.OrganizedEvents.get(t).attendees.remove(k);
                            organizer.OrganizedEvents.get(t).attendees.add(AttendeeRequest);
                            organizerRepository.save(organizer);
                        }
                }
            }
        }
        //event part
        for (int i = 0; i < eventRepository.findAll().size(); i++) {
            existingEvent = eventRepository.findAll().get(i);
            for (int j = 0; j < existingAttendee.AttendedEventsId.size(); j++) {
                if (existingEvent.getId().equals(existingAttendee.AttendedEventsId.get(j)) && !attendeeExist(existingEvent.attendees, AttendeeRequest)) {
                    existingEvent.attendees.add(AttendeeRequest);
                    eventRepository.save(existingEvent);
                } else if (existingEvent.getId().equals(existingAttendee.AttendedEventsId.get(j)))
                    for (int k = 0; k < existingEvent.attendees.size(); k++) {
                        if (existingEvent.attendees.get(k).getId().equals(existingAttendee.getId())) {
                            existingEvent.attendees.remove(k);
                            existingEvent.attendees.add(AttendeeRequest);
                            eventRepository.save(existingEvent);
                        }
                    }
            } //adding attendees in events arrays
        }
        return repository.save(existingAttendee);
    }

    public String deleteById(String AttendeeId) {
        repository.deleteById(AttendeeId);
        return "Deleted Organizer " + AttendeeId + " successfully";
    }
}
