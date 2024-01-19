package com.example.eventmanagementapi;

import com.example.eventmanagementapi.Controller.AttendeeController;
import com.example.eventmanagementapi.Controller.EventController;
import com.example.eventmanagementapi.Controller.OrganizerController;
import com.example.eventmanagementapi.Model.Event;
import com.example.eventmanagementapi.Model.Organizer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class OrganizerMenu {
    public void organizerMenu(Organizer organizer) {
        int choice;
        Scanner enter = new Scanner(System.in);
        do {
            System.out.println("1 - to view list of created events");
            System.out.println("2 - to create a new event");
            System.out.println("3 - to update an event");
            System.out.println("4 - to delete an event");
            System.out.println("5 - to update personal information");
            System.out.println("6 - to log out");
            Scanner s = new Scanner(System.in);
            choice = s.nextInt();
            OrganizerController organizerController = new OrganizerController();
            organizer = organizerController.getOrganizer(organizer.getId());
            EventController eventController = new EventController();
            AttendeeController attendeeController = new AttendeeController();
            switch (choice) {
                case 1:
                    organizerController.filterOrganizerEvents(organizer);
                    break;
                case 2:
                    System.out.println("Creating new Event:");
                    System.out.print("Enter new event name:");
                    String Name = enter.nextLine();
                    System.out.print("Enter new event location:");
                    String Location = enter.nextLine();
                    System.out.print("Enter new event date (YYYY-MM-DD):");
                    LocalDate Date = LocalDate.parse(enter.nextLine());
                    System.out.print("Enter new event time (hh:mm:ss):");
                    LocalTime Time = LocalTime.parse(enter.nextLine());
                    Event event = new Event(Name, Location, Date, Time);
                    event.setOrganizerId(organizer.getId());
                    organizer.OrganizedEvents.add(event);
                    organizerController.modifyOrganizer(organizer);
                    break;
                case 3:
                    System.out.println("\nUpdating an Event:");
                    organizerController.filterOrganizerEvents(organizer);
                    System.out.print("\nEnter event ID:");
                    String id = enter.nextLine();
                    if (eventController.getEvent(id).getId().equals(id) && eventController.getEvent(id).getOrganizerId().equals(organizer.getId())) {
                        Event event1 = eventController.getEvent(id);
                        System.out.print("Enter new event name:");
                        String name = enter.nextLine();
                        event1.setName(name);
                        System.out.print("Enter new event location:");
                        String location = enter.nextLine();
                        event1.setLocation(location);
                        System.out.print("Enter new event date (YYYY-MM-DD):");
                        LocalDate date = LocalDate.parse(enter.nextLine());
                        event1.setDate(date);
                        System.out.print("Enter new event time (hh:mm:ss):");
                        LocalTime time = LocalTime.parse(enter.nextLine());
                        event1.setTime(time);

                        System.out.print("\nWould you like to modify attendees? (Y/N)");
                        String yn = enter.nextLine();
                        if (yn.equalsIgnoreCase("y")) {
                            event1.attendees.clear();
                            System.out.println("\nEnter Exit to stop entering IDs:");
                            int n = 0;
                            String ID;
                            do {
                                n++;
                                System.out.print("Enter new attendant ID, Nb " + n + " :");
                                ID = enter.nextLine();
                                if (attendeeController.getAttendee(ID).getId().equals(ID)) {
                                    event1.attendees.add(attendeeController.getAttendee(ID));
                                }
                            } while (!ID.equalsIgnoreCase("Exit"));
                            System.out.println("\nIDs that exist of the entered IDs updated");
                        } else if (!yn.equalsIgnoreCase("n")) System.out.println("\nAnswer taken as 'no'\n");
                        eventController.modifyEvent(event1);
                        System.out.println("\nEvent successfully updated\n");
                    } else System.out.println("Event ID not found or not authorized");
                    break;
                case 4:
                    System.out.println("\nDeleting an Event:");
                    organizerController.filterOrganizerEvents(organizer);
                    System.out.print("\nEnter event ID:");
                    String Id = enter.nextLine();
                    if (eventController.getEvent(Id).getId().equals(Id) && eventController.getEvent(Id).getOrganizerId().equals(organizer.getId())) {
                        for (int i = 0; i < organizer.OrganizedEvents.size(); i++) {
                            if (organizer.OrganizedEvents.get(i).getId().equals(Id)) {
                                organizer.OrganizedEvents.remove(i);
                                organizerController.modifyOrganizer(organizer);
                                break;
                            }
                        }
                    } else System.out.println("Event ID not found or not authorized");
                    break;
                case 5:
                    System.out.println("\nUpdating organizer:");
                    System.out.print("Enter new organizer name:");
                    String name = enter.nextLine();
                    organizer.setName(name);
                    System.out.print("Enter new organizer email address:");
                    String email = enter.nextLine();
                    organizer.setEmailAddress(email);
                    System.out.print("Enter new organizer password:");
                    String password = enter.nextLine();
                    organizer.setPassword(password);

                    System.out.print("\nWould you like to modify Events? (Y/N)");
                    String YN = enter.nextLine();

                    if (YN.equalsIgnoreCase("y")) {
                        organizer.OrganizedEvents.clear();
                        System.out.println("\nEnter Exit to stop entering IDs:");
                        int n = 0;
                        String eid;
                        do {
                            n++;
                            System.out.print("Enter new event ID, Nb " + n + " :");
                            eid = enter.nextLine();
                            if (eid.equalsIgnoreCase("exit")) break;
                            if (eventController.getEvent(eid).getId().equals(eid)) {
                                organizer.OrganizedEvents.add(eventController.getEvent(eid));
                            } else {
                                System.out.println("\nEvent not found, new event will be created, enter information.\nEnter '0' in name to cancel\n");
                                System.out.print("Enter new event name:");
                                String ename = enter.nextLine();
                                if (!ename.equals("0")) {
                                    System.out.print("Enter new event location:");
                                    String elocation = enter.nextLine();
                                    System.out.print("Enter new event date (YYYY-MM-DD):");
                                    LocalDate edate = LocalDate.parse(enter.nextLine());
                                    System.out.print("Enter new event time (hh:mm:ss):");
                                    LocalTime etime = LocalTime.parse(enter.nextLine());
                                    Event e = new Event(ename, elocation, edate, etime);
                                    organizer.OrganizedEvents.add(e);
                                    System.out.print("\nEvent ID is " + organizer.OrganizedEvents.get(n - 1).getId() + "\n");
                                }
                            }
                        } while (true);
                        System.out.println("\nUnentered events have been deleted");
                    } else if (!YN.equalsIgnoreCase("n")) System.out.println("\nAnswer taken as 'no'\n");
                    organizerController.modifyOrganizer(organizer);
                    System.out.println("\nOrganizer successfully updated\n");
                    break;
                case 6:
                    break;
                default:
                    System.out.println("\nError " + choice + " is not an option\n");
            }
        } while (choice != 6);
    }
}
