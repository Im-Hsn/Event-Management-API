package com.example.eventmanagementapi;

import com.example.eventmanagementapi.Controller.AttendeeController;
import com.example.eventmanagementapi.Controller.OrganizerController;
import com.example.eventmanagementapi.Model.Attendee;
import com.example.eventmanagementapi.Model.Organizer;
import com.example.eventmanagementapi.Repository.EventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Scanner;

@SpringBootApplication
public class EventManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementApiApplication.class, args);
//		ConfigurableApplicationContext context=SpringApplication.run(EventManagementApiApplication.class, args);
		int choice;
		Scanner enter = new Scanner(System.in);
		do {
			System.out.println("1 - to create organizer account");
			System.out.println("2 - to create attendee account");
			System.out.println("3 - to login to an account");
			System.out.println("4 - to exit");
			Scanner s = new Scanner(System.in);
			choice = s.nextInt();
			OrganizerController organizerController = new OrganizerController();
			AttendeeController attendeeController = new AttendeeController();
			AttendeeMenu attendeeMenu = new AttendeeMenu();
			OrganizerMenu organizerMenu = new OrganizerMenu();
			switch (choice) {
				case 1:
					System.out.println("Creating organizer account:");
					System.out.print("Enter new organizer name:");
					String name = enter.nextLine();
					System.out.print("Enter new organizer email address:");
					String email = enter.nextLine();
					System.out.print("Enter new organizer password:");
					String password = enter.nextLine();
					Organizer organizer = new Organizer(name, email, password);
					organizerController.createOrganizer(organizer);
					System.out.print("Organizer created successfully. Your ID is " + organizer.getId());
					break;
				case 2:
					System.out.println("Creating attendee account:");
					System.out.print("Enter new attendee name:");
					String Name = enter.nextLine();
					System.out.print("Enter new attendee email address:");
					String Email = enter.nextLine();
//					Attendee attendee = new Attendee(Name, Email);
//					attendeeController.createAttendee(attendee);
//					System.out.print("Attendee created successfully. Your ID is " + attendee.getId());
					break;
				case 3:
					System.out.println("Logging in account:");
					System.out.print("Enter your ID:");
					String id = enter.nextLine();
					if (attendeeController.getAttendee(id).getId().equals(id)) {
						System.out.print("Attendee successfully logged in");
						attendeeMenu.attendeeMenu(attendeeController.getAttendee(id));
						break;
					} else if (organizerController.getOrganizer(id).getId().equals(id)) {
						organizerMenu.organizerMenu(organizerController.getOrganizer(id));
						System.out.print("Enter organizer password:");
						String pass = enter.nextLine();
						if (organizerController.findOrganizerBySignIn(id, pass).getId().equals(id)) {
							System.out.print("Organizer successfully logged in");
							organizerMenu.organizerMenu(organizerController.getOrganizer(id));
						}
						break;
					} else System.out.print("ID not found");
					break;
				case 4:
					System.exit(0);
					break;
				default:
					System.out.println("\nError " + choice + " is not an option\n");
			}
		} while (true);
	}
}
