package com.example.eventmanagementapi.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String name, location;
    private LocalDate date;
    private LocalTime time;
    private String organizerId;
    public List<Attendee> attendees = new ArrayList<>();

    public Event(String name, String location, LocalDate date, LocalTime time) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEventId(String id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
}
