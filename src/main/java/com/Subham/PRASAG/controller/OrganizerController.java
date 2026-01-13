


package com.Subham.PRASAG.controller;

import com.Subham.PRASAG.model.event.Organizer;
import com.Subham.PRASAG.repo.event.OrganizerRepository;
import com.Subham.PRASAG.service.OrganizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    private final OrganizerService service;

    public OrganizerController(OrganizerService service) {
        this.service = service;
    }
    @GetMapping("/debug")
    public Object debug(Authentication auth) {
        System.out.println(auth == null ? "NO AUTH" : auth.getAuthorities());
        return auth == null ? "NO AUTH" : auth.getAuthorities();
    }
    //  USER + ADMIN
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_READ','ADMIN_READ')")
    public List<Organizer> getAll() {
        return service.getAll();
    }

    //  USER + ADMIN (POSITION-BASED)
    @GetMapping("/position/{position}")
    @PreAuthorize("hasAnyAuthority('USER_READ','ADMIN_READ')")
    public Organizer getByPosition(@PathVariable String position) {
        return service.getByPosition(position);
    }

    //  ADMIN ONLY
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public Organizer add(@RequestBody Organizer organizer) {
        return service.add(organizer);
    }

    //  ADMIN ONLY (UPDATE BY POSITION)
    @PutMapping("/position/{position}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public Organizer update(
            @PathVariable String position,
            @RequestBody Organizer organizer) {

        return service.update(position, organizer);
    }

    //  ADMIN ONLY (DELETE BY POSITION)
    @DeleteMapping("/position/{position}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public void delete(@PathVariable String position) {
        service.delete(position);
    }
}


