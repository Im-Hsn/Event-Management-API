package com.example.eventmanagementapi.Repository;
import com.example.eventmanagementapi.Model.Attendee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface AttendeeRepository extends MongoRepository<Attendee, String> {
    @Query("{name: ?0}")
    List<Attendee> findByName(String name);
    @Query("{EmailAddress: ?0}")
    List<Attendee> findByEmailAddress(String EmailAddress);
    @Query("{AttendedEventsId: ?0}")
    Attendee findByAttendedEvent(String EventId);
}
