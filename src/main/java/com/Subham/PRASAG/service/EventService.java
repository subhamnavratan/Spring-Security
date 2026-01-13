package com.Subham.PRASAG.service;

import com.Subham.PRASAG.dto.EventCreateDto;
import com.Subham.PRASAG.dto.EventNotificationDto;
import com.Subham.PRASAG.model.event.Event;
import com.Subham.PRASAG.repo.event.EventRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    public EventService(EventRepository repo,
                        SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    //  COMMON NOTIFICATION
    private void notifyAllUsers(Event event) {
        messagingTemplate.convertAndSend(
                "/topic/events",
                new EventNotificationDto(
                        event.getEventName(),
                        event.getEventVenue(),
                        event.getDate().toString(),
                        event.getTime().toString()
                )
        );
    }

    // CREATE EVENT
    @Transactional
    public Event create(EventCreateDto dto) {

        if (repo.existsByEventNameIgnoreCase(dto.eventName())) {
            throw new RuntimeException("Event already exists: " + dto.eventName());
        }

        Event event = new Event(
                dto.eventName(),
                dto.eventVenue(),
                dto.date(),
                dto.time(),
                dto.description()
        );

        Event saved = repo.save(event);

        notifyAllUsers(saved);

        return saved;
    }

    // READ ALL
    public List<Event> getAll() {
        return repo.findAll();
    }

    //  READ BY NAME
    public Event getByEventName(String eventName) {
        return repo.findByEventNameIgnoreCase(eventName)
                .orElseThrow(() ->
                        new RuntimeException("Event not found: " + eventName));
    }

    //  UPDATE EVENT
    @Transactional
    public Event update(String eventName, EventCreateDto dto) {

        Event existing = getByEventName(eventName);

        existing.setEventVenue(dto.eventVenue());
        existing.setDate(dto.date());
        existing.setTime(dto.time());
        existing.setDescription(dto.description());

        Event updated = repo.save(existing);

        notifyAllUsers(updated);

        return updated;
    }

    //  DELETE EVENT
    @Transactional
    public void delete(String eventName) {

        Event event = getByEventName(eventName);

        repo.delete(event);

        messagingTemplate.convertAndSend(
                "/topic/events",
                "Event deleted: " + eventName
        );
    }
}
