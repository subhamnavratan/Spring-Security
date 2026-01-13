package com.Subham.PRASAG.controller;

import com.Subham.PRASAG.dto.EventCreateDto;
import com.Subham.PRASAG.model.event.Event;
import com.Subham.PRASAG.service.EventService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // DEBUG (OPTIONAL)
    @GetMapping("/debug")
    public Object debug(Authentication auth) {
        return auth == null ? "NO AUTH" : auth.getAuthorities();
    }

    // USER + ADMIN
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_READ','ADMIN_READ')")
    public List<Event> getAll() {
        return service.getAll();
    }

    // USER + ADMIN
    @GetMapping("/by-name/{eventName}")
    @PreAuthorize("hasAnyAuthority('USER_READ','ADMIN_READ')")
    public Event getByName(@PathVariable String eventName) {
        return service.getByEventName(eventName);
    }

    // ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public Event create(@Valid @RequestBody EventCreateDto dto) {
        return service.create(dto);
    }

    // ADMIN ONLY
    @PutMapping("/by-name/{eventName}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public Event update(
            @PathVariable String eventName,
            @Valid @RequestBody EventCreateDto dto) {
        return service.update(eventName, dto);
    }

    // ADMIN ONLY
    @DeleteMapping("/by-name/{eventName}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public void delete(@PathVariable String eventName) {
        service.delete(eventName);
    }
}

