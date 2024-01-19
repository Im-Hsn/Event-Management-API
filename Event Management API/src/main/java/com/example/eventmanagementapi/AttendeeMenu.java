package com.example.eventmanagementapi;

import com.example.eventmanagementapi.Controller.AttendeeController;
import com.example.eventmanagementapi.Controller.EventController;
import com.example.eventmanagementapi.Model.Attendee;
import java.util.Scanner;

public class AttendeeMenu {
    public void attendeeMenu(Attendee attendee) {
        int choice;
        Scanner enter = new Scanner(System.in);
        do {
            System.out.println("1 - to view list of unattended events");
            System.out.println("2 - to view list of attended events");
            System.out.println("3 - to attend an event");
            System.out.println("4 - to stop attending an event");
            System.out.println("5 - to update personal information");
            System.out.println("6 - to search for an event");
            System.out.println("7 - to log out");
            Scanner s = new Scanner(System.in);
            choice = s.nextInt();
            AttendeeController attendeeController = new AttendeeController();
            attendee=attendeeController.getAttendee(attendee.getId());
            EventController eventController = new EventController();
            switch (choice) {
                case 1:
                    attendeeController.filterAttendeeEvents(attendee, 0);
                    break;
                case 2:
                    attendeeController.filterAttendeeEvents(attendee, 1);
                    break;
                case 3:
                    attendeeController.filterAttendeeEvents(attendee, 0);
                    System.out.println("\nEnter event ID to attend");
                    String id = enter.nextLine();
                    int j = 0;
                    if (eventController.getEvent(id).getId().equals(id)) {
                        for (int i = 0; i < attendee.AttendedEventsId.size(); i++) {
                            if (id.equals(attendee.AttendedEventsId.get(i))) {
                                System.out.println("Event already in attended events list");
                                j = 1;
                                break;
                            }
                        }
                        if (j == 0) {
                            attendee.AttendedEventsId.add(id);
                            attendeeController.modifyAttendee(attendee);
                        }
                    } else System.out.println("Event ID not found");
                    break;
                case 4:
                    attendeeController.filterAttendeeEvents(attendee, 1);
                    System.out.println("\nEnter event ID to cancel");
                    String Id = enter.nextLine();
                    int k = 0;
                    if (eventController.getEvent(Id).getId().equals(Id)) {
                        for (int i = 0; i < attendee.AttendedEventsId.size(); i++) {
                            if (Id.equals(attendee.AttendedEventsId.get(i))) {
                                k = 1;
                                attendee.AttendedEventsId.remove(i);
                                attendeeController.modifyAttendee(attendee);
                                break;
                            }
                        }
                        if (k == 0) {
                            System.out.println("Event not attended in attended events list");
                        }
                    } else System.out.println("Event ID not found");
                    break;
                case 5:
                    System.out.println("\nID:" + attendee.getId());
                    System.out.println("Name:" + attendee.getName());
                    System.out.println("Email address:" + attendee.getEmailAddress());
                    System.out.println("Attended events ID:");
                    for (int i = 0; i < attendee.AttendedEventsId.size(); i++) {
                        System.out.println(attendee.AttendedEventsId.get(i));
                    }
                    System.out.println("Would you like to modify attended events? (Y/N)");
                    String yn = enter.nextLine();
                    System.out.print("Enter new attendee name:");
                    String Name = enter.nextLine();
                    attendee.setName(Name);
                    System.out.print("Enter new attendee email address:");
                    String Email = enter.nextLine();
                    attendee.setEmailAddress(Email);
                    if (yn.equalsIgnoreCase("y")) {
                        attendee.AttendedEventsId.clear();
                        System.out.println("\nEnter Exit to stop entering IDs:");
                        int n = 0;
                        String ID;
                        do {
                            n++;
                            System.out.print("Enter newly attended event ID, Nb " + n + " :");
                            ID = enter.nextLine();
                            if (eventController.getEvent(ID).getId().equals(ID)) {
                                attendee.AttendedEventsId.add(ID);
                            }
                        } while (!ID.equalsIgnoreCase("Exit"));System.out.println("\nIDs that exist of the entered IDs updated");
                    } else if (!yn.equalsIgnoreCase("n")) System.out.println("Answer taken as 'no'\n");
                    attendeeController.modifyAttendee(attendee);System.out.println("\nAttendee successfully updated\n");
                    break;
                case 6:
                    System.out.println("1 - to search by organizer ID");
                    System.out.println("2 - to search by location");
                    System.out.println("3 - to search by name");
                    int search;
                    search = enter.nextInt();
                    switch (search) {
                        case 1:
                            System.out.println("Enter events organizer ID");
                            String oid = enter.nextLine();
                            for(int i=0;i<eventController.findEventByOrganizer(oid).size();i++){
                                System.out.println("Event "+i+" ID:"+eventController.findEventByOrganizer(oid).get(i));
                            }if(eventController.findEventByOrganizer(oid).size()==0)System.out.println("\nNo events with such criteria exist\n");
                            break;
                        case 2:
                            System.out.println("Enter events location");
                            String elocation = enter.nextLine();
                            for(int i=0;i<eventController.findEventByLocation(elocation).size();i++){
                                System.out.println("Event "+i+" ID:"+eventController.findEventByLocation(elocation).get(i));
                            }if(eventController.findEventByLocation(elocation).size()==0)System.out.println("\nNo events with such criteria exist\n");
                            break;
                        case 3:
                            System.out.println("Enter events name");
                            String ename = enter.nextLine();
                            for(int i=0;i<eventController.findEventByName(ename).size();i++){
                                System.out.println("Event "+i+" ID:"+eventController.findEventByName(ename).get(i));
                            }if(eventController.findEventByName(ename).size()==0)System.out.println("\nNo events with such criteria exist\n");
                            break;
                        default:
                            System.out.println("\nError " + choice + " is not an option\n");
                    }
                    break;
                case 7:
                    break;
                default:
                    System.out.println("\nError " + choice + " is not an option\n");
            }
        } while (choice != 7);
    }
}
