package com.example.eventmanagementapi.Controller;
import com.example.eventmanagementapi.Model.Event;
import com.example.eventmanagementapi.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return service.addEvent(event);
    }

    @GetMapping
    public List<Event> getEvent() {
        return service.findAllEvents();
    }

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable String eventId) {
        return service.getEventByEventId(eventId);
    }

    @GetMapping("/name/{name}")
    public List<Event> findEventByName(@PathVariable String name) {
        return service.getEventByName(name);
    }

    @GetMapping("/location/{location}")
    public List<Event> findEventByLocation(@PathVariable String location) {
        return service.getEventByLocation(location);
    }

    @GetMapping("/date/{date}")
    public List<Event> findEventByDate(@PathVariable LocalDate date) {
        return service.getEventByDate(date);
    }

    @GetMapping("/time/{time}")
    public List<Event> findEventByTime(@PathVariable LocalTime time) {
        return service.getEventByTime(time);
    }

    @GetMapping("/organizerId/{organizerId}")
    public List<Event> findEventByOrganizer(@PathVariable String organizerId) {
        return service.getEventByOrganizer(organizerId);
    }
    @GetMapping("/{location}/{date}/{time}")
    public Event findEventByLDT(@PathVariable String location, @PathVariable LocalDate date, @PathVariable LocalTime time) {
        return service.getEventByLDT(location, date, time);
    }
    @GetMapping("/paginated/{pages}/{rows}/{sortField}")
    public Page<Event> paginateEvents(@PathVariable int pages, @PathVariable int rows, @PathVariable String sortField){
        Page<Event> eventPage=service.findAllEvents(pages, rows, sortField);
        return eventPage;
    }

    @PutMapping
    public Event modifyEvent(@RequestBody Event event) {
        return service.updateEvent(event);
    }

    @DeleteMapping("/{eventId}")
    public String deleteEvent(@PathVariable String eventId) {
        service.deleteById(eventId);
        return eventId + " Deleted successfully";
    }
}
