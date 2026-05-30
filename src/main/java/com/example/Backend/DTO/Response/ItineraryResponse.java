package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.Itinerary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItineraryResponse {

    private Long id;
    private Long travelRequestId;
    private String segmentType;
    private String fromLocation;
    private String toLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String details;


    public static ItineraryResponse from (Itinerary itinerary) {
        return ItineraryResponse.builder()
        .id(itinerary.getId())
                .travelRequestId(itinerary.getTravelRequest().getId())
                .segmentType(itinerary.getSegmentType().name())
                .fromLocation(itinerary.getFromLocation())
                .toLocation(itinerary.getToLocation())
                .startTime(itinerary.getStartTime())
                .endTime(itinerary.getEndTime())
                .details(itinerary.getDetails())
                .build();
    }


}
