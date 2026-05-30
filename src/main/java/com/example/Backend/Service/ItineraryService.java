package com.example.Backend.Service;

import com.example.Backend.DTO.Request.CreateItineraryRequest;
import com.example.Backend.DTO.Request.UpdateItineraryRequest;
import com.example.Backend.DTO.Response.ItineraryResponse;
import com.example.Backend.Domain.Itinerary;
import com.example.Backend.Domain.TravelRequest;
import com.example.Backend.Exception.ResourceConflictException;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Repository.ItineraryRepository;
import com.example.Backend.Repository.TravelRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelRequestRepository travelRequestRepository;

    @Transactional
    public ItineraryResponse addItinerary(
            Long requestId,
            CreateItineraryRequest dto
    ){
       TravelRequest travelRequest = travelRequestRepository.findById(requestId)
               .orElseThrow(()->new ResourceNotFoundException("Travel Request Not Found " + requestId));

       validateTimeRange(dto.getStartTime(), dto.getEndTime());
        Itinerary itinerary =  Itinerary.builder()
                .travelRequest(travelRequest)
                .segmentType(dto.getSegmentType())
                .fromLocation(dto.getFromLocation())
                .toLocation(dto.getToLocation())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .details(dto.getDetails())
                .build();

        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        return ItineraryResponse.from(savedItinerary);
    }
    @Transactional
    public List<ItineraryResponse> getItineraries( Long requestId){
       travelRequestRepository.findById(requestId)
               .orElseThrow(()->new ResourceNotFoundException("Travel Request Not Found " + requestId));

       List<Itinerary> itineraries = itineraryRepository.findByTravelRequestId(requestId);

       return itineraries.stream()
               .map(ItineraryResponse::from)
               .toList();
    }
    @Transactional
    public ItineraryResponse updateItinerary(
            Long requestId,
            Long itineraryId,
            UpdateItineraryRequest dto
    ) {
        validateTimeRange(dto.getStartTime(), dto.getEndTime());

        travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + requestId));

        Itinerary itinerary = itineraryRepository.findByIdAndTravelRequestId(itineraryId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary Not Found " + itineraryId));

        itinerary.setSegmentType(dto.getSegmentType());
        itinerary.setFromLocation(dto.getFromLocation());
        itinerary.setToLocation(dto.getToLocation());
        itinerary.setStartTime(dto.getStartTime());
        itinerary.setEndTime(dto.getEndTime());
        itinerary.setDetails(dto.getDetails());

        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        return ItineraryResponse.from(savedItinerary);
    }

    @Transactional
    public ItineraryResponse deleteItinerary(Long requestId, Long itineraryId) {
        travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + requestId));

        Itinerary itinerary = itineraryRepository.findByIdAndTravelRequestId(itineraryId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary Not Found " + itineraryId));

        ItineraryResponse response = ItineraryResponse.from(itinerary);
        itineraryRepository.delete(itinerary);

        return response;
    }

    private void validateTimeRange(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            throw new ResourceConflictException("End time cannot be before start time");
        }
    }
}
