package com.example.eventmanagementapi.Repository;
import com.example.eventmanagementapi.Model.Organizer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrganizerRepository extends MongoRepository<Organizer, String> {
    @Query("{name: ?0}")
    List<Organizer> findByName(String name);
    @Query("{EmailAddress: ?0}")
    List<Organizer> findByEmailAddress(String EmailAddress);
    @Query("{OrganizedEvents: ?0}")
    Organizer findByOrganizedEvent(String EventId);
    @Query("{_id: ?0, password: ?1}")
    Organizer findBySignIn(String id, String password);


}
