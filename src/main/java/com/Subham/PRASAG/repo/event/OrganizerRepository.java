package com.Subham.PRASAG.repo.event;

import com.Subham.PRASAG.model.event.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    Optional<Organizer> findByPosition(String position);

    void deleteByPosition(String position);

    boolean existsByPosition(String position);
}
