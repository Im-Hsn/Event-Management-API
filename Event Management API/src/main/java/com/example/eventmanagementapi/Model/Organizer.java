package com.example.eventmanagementapi.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "organizers")
public class Organizer {
    @Id
    private String id;
    private String name, EmailAddress, password;
    public List<Event> OrganizedEvents = new ArrayList<>();
    public Organizer(String name, String EmailAddress, String password) {
        this.name = name;
        this.EmailAddress = EmailAddress;
        this.password = password;
    }
    public void addEvent(Event event) {OrganizedEvents.add(event);}

    public String getId() {
        return id;
    }

    public void setOrganizerId(String id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}