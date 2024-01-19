package com.example.eventmanagementapi.Controller;
import com.example.eventmanagementapi.AESEncryptionDecryption;
import com.example.eventmanagementapi.Model.Organizer;
import com.example.eventmanagementapi.Repository.OrganizerRepository;
import com.example.eventmanagementapi.Service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {
    @Autowired
    private OrganizerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organizer createOrganizer(@RequestBody Organizer organizer) {
        //encrypting password
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        organizer.setPassword(aesEncryptionDecryption.encrypt(organizer.getPassword(), "secret"));
        return service.addOrganizer(organizer);
    }

    @GetMapping
    public List<Organizer> getOrganizer() {
        return service.findAllOrganizer();
    }

    @GetMapping("/{organizerId}")
    public Organizer getOrganizer(@PathVariable String organizerId) {
        return service.getOrganizerById(organizerId);
    }

    @GetMapping("/name/{name}")
    public List<Organizer> findOrganizerByName(@PathVariable String name) {
        return service.getOrganizerByName(name);
    }

    @GetMapping("/EmailAddress/{EmailAddress}")
    public List<Organizer> findOrganizerByEmailAddress(@PathVariable String EmailAddress) {
        return service.getOrganizerByEmailAddress(EmailAddress);
    }

    @GetMapping("/{organizerId}/{password}")
    public Organizer findOrganizerBySignIn(@PathVariable String organizerId, @PathVariable String password) {
        //Encryption to find encrypted password
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption();
        return service.getOrganizerBySignIn(organizerId, aesEncryptionDecryption.encrypt(password, "secret"));
    }
    @GetMapping("/filter")
    public void filterOrganizerEvents(@RequestBody Organizer organizer) {
        service.filterOrganizedEvents(organizer);
    }
    @GetMapping("/eventId/{eventId}")
    public Organizer findOrganizerByEventId(@PathVariable String eventId) {
        return service.getOrganizerByEvent(eventId);
    }

    @PutMapping
    public Organizer modifyOrganizer(@RequestBody Organizer organizer) {
        return service.updateOrganizer(organizer);
    }

    @DeleteMapping("/{organizerId}")
    public String deleteOrganizer(@PathVariable String organizerId) {
        service.deleteById(organizerId);
        return "Deleted Organizer " + organizerId + " successfully";
    }
}
