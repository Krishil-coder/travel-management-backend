package com.example.Backend.DTO.Request;

import com.example.Backend.Domain.Itinerary.SegmentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateItineraryRequest {

    @NotNull(message = "Segment type is required")
    private SegmentType segmentType;

    @NotNull(message = "From location is required")
    @Size(max = 200, message = "From location must not exceed 200 characters")
    private String fromLocation;

    @NotNull(message = "To location is required")
    @Size(max = 200, message = "To location must not exceed 200 characters")
    private String toLocation;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;
}
