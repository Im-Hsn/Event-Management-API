package com.example.eventmanagementapi.Repository;
import com.example.eventmanagementapi.Model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Repository

public interface EventRepository extends MongoRepository<Event, String>{
    @Query("{name: ?0}")
    List<Event> findByName(String name);
    @Query("{location: ?0}")
    List<Event> findByLocation(String location);
    @Query("{date: ?0}")
    List<Event> findByDate(LocalDate date);
    @Query("{time: ?0}")
    List<Event> findByTime(LocalTime time);
    @Query("{organizerId: ?0}")
    List<Event> findByOrganizer(String OrganizerId);
    @Query("{location: ?0, date: ?1, time: ?2}")
    Event findByLDT(String location, LocalDate date, LocalTime time);
}
