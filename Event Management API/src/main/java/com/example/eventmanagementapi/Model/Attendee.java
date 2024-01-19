package com.example.eventmanagementapi.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendees")
public class Attendee {
    @Id
    private String id;
    private String name, EmailAddress;
    public List<String> AttendedEventsId = new ArrayList<>();
//    public Attendee(String name, String emailAddress, String id) {
//        this.name = name;
//        this.EmailAddress = emailAddress;
//        this.id=id;
//    }
//    public Attendee(String name, String emailAddress) {
//        this.name = name;
//        this.EmailAddress = emailAddress;
//    }

    public void addAttendedEventsId(String EventId) {
        AttendedEventsId.add(EventId);
    }

    public String getId() {
        return id;
    }

    public void setAttendeeId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }
}
