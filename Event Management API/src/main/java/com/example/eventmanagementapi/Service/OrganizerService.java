package com.example.eventmanagementapi.Service;
import com.example.eventmanagementapi.AESEncryptionDecryption;
import com.example.eventmanagementapi.Model.Attendee;
import com.example.eventmanagementapi.Model.Event;
import com.example.eventmanagementapi.Model.Organizer;
import com.example.eventmanagementapi.Repository.AttendeeRepository;
import com.example.eventmanagementapi.Repository.EventRepository;
import com.example.eventmanagementapi.Repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AttendeeRepository attendeeRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    public OrganizerService(OrganizerRepository repository) {
        this.repository = repository;
    }

    public Organizer addOrganizer(Organizer organizer) {
        organizer.setOrganizerId(UUID.randomUUID().toString().split("-")[0]); //auto generate id
        if (organizer.OrganizedEvents.size() > 0) for (int i = 0; i < organizer.OrganizedEvents.size(); i++) {
            organizer.OrganizedEvents.get(i).setEventId(UUID.randomUUID().toString().split("-")[0]);
            organizer.OrganizedEvents.get(i).setOrganizerId(organizer.getId());
            eventRepository.save(organizer.OrganizedEvents.get(i));
        }
        return repository.save(organizer);
    }

    public List<Organizer> findAllOrganizer() {
        return repository.findAll();
    }

    public Organizer getOrganizerById(String organizerId) {
        return repository.findById(organizerId).get();
    }

    public List<Organizer> getOrganizerByName(String name) {
        return repository.findByName(name);
    }

    public List<Organizer> getOrganizerByEmailAddress(String EmailAddress) {
        return repository.findByEmailAddress(EmailAddress);
    }
    public Organizer getOrganizerBySignIn(String id, String password) {
        return repository.findBySignIn(id, password);
    }

    public Organizer getOrganizerByEvent(String EventId) {
        return repository.findByOrganizedEvent(EventId);
    }
    public void filterOrganizedEvents(Organizer organizer){
        if(organizer.OrganizedEvents.size()==0)System.out.println("No organized events exist:");
        else System.out.println("\nOrganized events:\n");
        for(int i=0;i<organizer.OrganizedEvents.size();i++){
            System.out.println("\n\nEvent ID:"+organizer.OrganizedEvents.get(i).getId());
            System.out.println("Name:"+organizer.OrganizedEvents.get(i).getName());
            System.out.println("Location:"+organizer.OrganizedEvents.get(i).getLocation());
            System.out.println("Date:"+organizer.OrganizedEvents.get(i).getDate());
            System.out.println("Time:"+organizer.OrganizedEvents.get(i).getTime()+"\n");
            for(int j=0;j<organizer.OrganizedEvents.get(i).attendees.size();j++){
                System.out.println("Attendee ID:"+organizer.OrganizedEvents.get(i).attendees.get(j).getId());
            }
        }
    }
    public boolean eventExist(Event event) {
        for (int i = 0; i < eventRepository.findAll().size(); i++) {
            if (eventRepository.findAll().get(i).getId().equals(event.getId()))
                return true;
        }
        return false;
    }

    public boolean oldEventCheck(Event existing, List<Event> request) {
        for (int j = 0; j < request.size(); j++) {
            if (existing.getId().equals(request.get(j).getId())) return true;
        }
        return false;
    }

    public boolean idCheck(String eventId, List<String> ids) {
        for (int j = 0; j < ids.size(); j++) {
            if (eventId.equals(ids.get(j))) return true;
        }
        return false;
    }

    public Organizer updateOrganizer(Organizer OrganizerRequest) {
        //get the existing document from db
        Organizer existingOrganizer = repository.findById(OrganizerRequest.getId()).get();
        existingOrganizer.setName(OrganizerRequest.getName()); //Sets the new name into the existing name
        existingOrganizer.setEmailAddress(OrganizerRequest.getEmailAddress());
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        existingOrganizer.setPassword(aesEncryptionDecryption.encrypt(OrganizerRequest.getPassword(), "secret"));
        if (OrganizerRequest.OrganizedEvents.size() > 0) {
            Event existingEvent;
            for (int i = 0; i < existingOrganizer.OrganizedEvents.size(); i++) {
                existingEvent = existingOrganizer.OrganizedEvents.get(i);
                if (!oldEventCheck(existingEvent, OrganizerRequest.OrganizedEvents)) {
                    existingEvent.attendees.clear();
                    eventService.updateEvent(existingEvent); //deleting all instances of attendees in old events and updating event database
                    eventRepository.deleteById(existingEvent.getId());
                    //deleting old events that are not being modified and with organizer as organizer
                } else {
                    for (int j = 0; j < existingEvent.attendees.size(); j++) {
                        for (int l = 0; l < OrganizerRequest.OrganizedEvents.size(); l++) {
                            for (int k = 0; k < existingEvent.attendees.get(j).AttendedEventsId.size(); k++) {
                                if (existingEvent.attendees.get(j).AttendedEventsId.get(k).equals(OrganizerRequest.OrganizedEvents.get(l).getId())) {
                                    existingEvent.attendees.get(j).AttendedEventsId.remove(k);
                                    attendeeService.updateAttendee(existingEvent.attendees.get(j));
                                } //removing instances of events ids from attendee arrays
                            }
                        }
                    }
                }
            }
            existingOrganizer.OrganizedEvents.clear();  //clearing all organized events in organizer
            Attendee attendee;
            for (int i = 0; i < OrganizerRequest.OrganizedEvents.size(); i++) {
                existingEvent = OrganizerRequest.OrganizedEvents.get(i);  //finding event
                for (int j = 0; j < existingEvent.attendees.size(); j++) {
                    attendee = attendeeRepository.findById(existingEvent.attendees.get(j).getId()).get();
                    OrganizerRequest.OrganizedEvents.get(i).attendees.remove(j);
                    OrganizerRequest.OrganizedEvents.get(i).attendees.add(attendee);
                    attendeeService.updateAttendee(attendee);
                }
                if (!eventExist(existingEvent)) {
                    eventService.addEvent(existingEvent);
                    existingEvent = eventRepository.findByLDT(existingEvent.getLocation(), existingEvent.getDate(), existingEvent.getTime()); //finding event by LDT
                }
                existingOrganizer.OrganizedEvents.add(existingEvent);  //updating organized events
                eventService.updateEvent(existingEvent);  //updating event database
            }
            for (int i = 0; i < existingOrganizer.OrganizedEvents.size(); i++) {
                existingEvent = existingOrganizer.OrganizedEvents.get(i);
                for (int j = 0; j < eventRepository.findAll().size(); j++) {
                    if (existingEvent.getId().equals(eventRepository.findAll().get(j).getId())) {
                        existingOrganizer.OrganizedEvents.get(i).attendees = eventRepository.findAll().get(j).attendees;
                    }
                }
            }
        }
        return repository.save(existingOrganizer);
    }

    public String deleteById(String OrganizerId) {
        repository.deleteById(OrganizerId);
        return "Deleted Organizer " + OrganizerId + " successfully";
    }
}
