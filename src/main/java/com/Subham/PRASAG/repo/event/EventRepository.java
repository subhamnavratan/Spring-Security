package com.Subham.PRASAG.repo.event;

import com.Subham.PRASAG.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByEventNameIgnoreCase(String eventName);

    boolean existsByEventNameIgnoreCase(String eventName);
}
