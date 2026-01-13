package com.Subham.PRASAG.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventCreateDto(

        @NotBlank
        String eventName,

        @NotBlank
        String eventVenue,

        @NotNull
        LocalDate date,

        @NotNull
        LocalTime time,

        String description
) {}
