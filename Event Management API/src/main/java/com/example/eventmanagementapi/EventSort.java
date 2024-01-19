package com.example.eventmanagementapi;
import com.example.eventmanagementapi.Model.Event;

import java.util.Comparator;

public class EventSort implements Comparator<Event> {
    public int compare(Event e1, Event e2){
        return e1.getLocation().compareTo(e2.getLocation());
    }
}
