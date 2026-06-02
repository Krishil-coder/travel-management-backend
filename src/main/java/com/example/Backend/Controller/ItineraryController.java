package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.CreateItineraryRequest;
import com.example.Backend.DTO.Request.UpdateItineraryRequest;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.ItineraryResponse;
import com.example.Backend.Service.ItineraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@RequiredArgsConstructor
public class ItineraryController {
    private final ItineraryService itineraryService;

    @PostMapping("/{requestId}/itineraries")
    public ResponseEntity<ApiResponse<ItineraryResponse>> addItinerary(
          @PathVariable Long requestId,
          @Valid @RequestBody CreateItineraryRequest request
    ){
        ItineraryResponse response =  itineraryService.addItinerary(requestId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Itinerary  successfully added"));
    }

    @GetMapping("/{requestId}/itineraries")
    public ResponseEntity<ApiResponse<List<ItineraryResponse>>>getItinerary(
            @PathVariable Long requestId
    ){
        List<ItineraryResponse> response = itineraryService.getItineraries(requestId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Itinerary successfully retrieved")
        );
    }

    @PutMapping("/{requestId}/itineraries/{itineraryId}")
    public ResponseEntity<ApiResponse<ItineraryResponse>> updateItinerary(
            @PathVariable Long requestId,
            @PathVariable Long itineraryId,
            @Valid @RequestBody UpdateItineraryRequest request
    ) {
        ItineraryResponse response = itineraryService.updateItinerary(requestId, itineraryId, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Itinerary successfully updated")
        );
    }

    @DeleteMapping("/{requestId}/itineraries/{itineraryId}")
    public ResponseEntity<ApiResponse<ItineraryResponse>> deleteItinerary(
            @PathVariable Long requestId,
            @PathVariable Long itineraryId
    ) {
        ItineraryResponse response = itineraryService.deleteItinerary(requestId, itineraryId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Itinerary successfully deleted")
        );
    }

}
