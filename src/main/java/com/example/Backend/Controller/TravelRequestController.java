package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.CreateTravelRequestDto;
import com.example.Backend.DTO.Request.UpdateTravelRequestDto;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.TravelRequestResponse;
import com.example.Backend.Service.TravelRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})

public class TravelRequestController {
    private final TravelRequestService travelRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<TravelRequestResponse>> createTravelRequest(
            @Valid @RequestBody CreateTravelRequestDto dto
    ) {
        TravelRequestResponse response = travelRequestService.createTravelRequest(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Travel Request Created Successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> updateDraftTravelRequest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTravelRequestDto dto
    ) {
        TravelRequestResponse response = travelRequestService.updateDraftTravelRequest(id, dto);

        return ResponseEntity
                .ok(ApiResponse.success(response, "Draft Travel Request Updated Successfully"));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> submitTravelRequest(
            @PathVariable Long id
    ) {
        TravelRequestResponse response = travelRequestService.submitTravelRequest(id);

        return ResponseEntity
                .ok(ApiResponse.success(response, "Travel Request Submitted Successfully"));
    }
}
