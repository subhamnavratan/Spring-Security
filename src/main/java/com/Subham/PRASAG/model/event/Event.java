package com.Subham.PRASAG.model.event;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "eventName")
        }
)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventName;

    private String eventVenue;
    private LocalDate date;
    private LocalTime time;

    @Column(length = 1000)
    private String description;

    public Event() {}

    public Event(String eventName, String eventVenue,
                 LocalDate date, LocalTime time, String description) {
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getEventName() { return eventName; }
    public String getEventVenue() { return eventVenue; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getDescription() { return description; }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
