package com.Subham.PRASAG.model.event;

import jakarta.persistence.*;
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = "position")
)
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String position;

    @Column(nullable = false)
    private String name;

    public Organizer() {}

    public Organizer(String position, String name) {
        this.position = position;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
