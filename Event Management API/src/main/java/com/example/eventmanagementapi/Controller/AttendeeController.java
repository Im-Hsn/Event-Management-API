package com.example.eventmanagementapi.Controller;
import com.example.eventmanagementapi.Model.Attendee;
import com.example.eventmanagementapi.Service.AttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {
    @Autowired
    private AttendeeService service ;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attendee createAttendee(@RequestBody Attendee attendee) {
        return service.addAttendee(attendee);
    }

    @GetMapping
    public List<Attendee> getAttendee() {
        return service.findAllAttendee();
    }

    @GetMapping("/{attendeeId}")
    public Attendee getAttendee(@PathVariable String attendeeId) {
        return service.getAttendeeById(attendeeId);
    }

    @GetMapping("/name/{name}")
    public List<Attendee> findAttendeeByName(@PathVariable String name) {
        return service.getAttendeeByName(name);
    }

    @GetMapping("/EmailAddress/{EmailAddress}")
    public List<Attendee> findAttendeeByEmailAddress(@PathVariable String EmailAddress) {
        return service.getAttendeeByEmailAddress(EmailAddress);
    }
    @GetMapping("/filter/{bool}")
    public void filterAttendeeEvents(@RequestBody Attendee attendee, @PathVariable int bool) {
       service.filterEvents(attendee, bool);
    }
    @GetMapping("/eventId/{eventId}")
    public Attendee findAttendeeByEventId(@PathVariable String eventId) {
        return service.getAttendeeByEvent(eventId);
    }

    @PutMapping
    public Attendee modifyAttendee(@RequestBody Attendee attendee) {
        return service.updateAttendee(attendee);
    }

    @DeleteMapping("/{attendeeId}")
    public String deleteAttendee(@PathVariable String attendeeId) {
        service.deleteById(attendeeId);
        return "Deleted Organizer " + attendeeId + " successfully";
    }
}
