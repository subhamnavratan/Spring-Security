package com.Subham.PRASAG.service;

import com.Subham.PRASAG.model.event.Organizer;
import com.Subham.PRASAG.repo.event.OrganizerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizerService {

    private final OrganizerRepository repo;

    public OrganizerService(OrganizerRepository repo) {
        this.repo = repo;
    }

    // USER + ADMIN
    public List<Organizer> getAll() {
        return repo.findAll();
    }

    // USER + ADMIN
    public Organizer getByPosition(String position) {
        return repo.findByPosition(position)
                .orElseThrow(() ->
                        new RuntimeException("Organizer not found for position: " + position));
    }

    // ADMIN
    public Organizer add(Organizer organizer) {

        if (repo.existsByPosition(organizer.getPosition())) {
            throw new RuntimeException("Position already exists");
        }

        return repo.save(organizer);
    }

    // ADMIN
    public Organizer update(String position, Organizer updated) {

        Organizer existing = getByPosition(position);

        existing.setName(updated.getName());


        return repo.save(existing);
    }


        @Transactional
        public void delete(String position) {
            repo.deleteByPosition(position);
        }
    }

