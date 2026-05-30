package com.example.Backend.DTO.Request;

import com.example.Backend.Domain.Itinerary.SegmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateItineraryRequest {

    @NotNull(message = "Segment type is required ")
    private SegmentType segmentType;

    @NotNull(message = "From location is requires")
    private String fromLocation;

    @NotNull(message = "To location i required")
    private String toLocation;

    @NotNull(message = "Start time is required ")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String details;



}
