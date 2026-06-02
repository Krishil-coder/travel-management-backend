package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.CreateTravelRequestDto;
import com.example.Backend.DTO.Request.UpdateTravelRequestDto;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.TravelRequestResponse;
import com.example.Backend.DTO.Response.UserResponse;
import com.example.Backend.Domain.Role;
import com.example.Backend.Service.TravelRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> getTravelRequestById(
            @PathVariable long id
    ){
        TravelRequestResponse response = travelRequestService.getTravelRequestById(id);

        return ResponseEntity
                .ok(ApiResponse.success(response, "Travel Request Found Successfully"));
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
   @GetMapping
public ResponseEntity<ApiResponse<List<TravelRequestResponse>>> getAllTravelRequests(
        @RequestParam(required = false) Long employeeId
) {

    List<TravelRequestResponse> response;

    if (employeeId != null) {
        response = travelRequestService.getEmployeeRequests(employeeId);
    } else {
        response = travelRequestService.getAllTravelRequests();
    }

    return ResponseEntity.ok(
            ApiResponse.success(
                    response,
                    "Travel Requests Fetched Successfully"
            )
    );
}

@GetMapping("/managers")
public ResponseEntity<ApiResponse<List<UserResponse>>> getManagersByDepartment(
        @RequestParam Role role,
        @RequestParam String department
) {
    List<UserResponse> response = travelRequestService.getManagersByDepartment(role, department);

    return ResponseEntity.ok(
            ApiResponse.success(response, "Managers Fetched Successfully")
    );
}

@DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelRequestResponse>> deleteTravelRequest(
            @PathVariable Long id){

        TravelRequestResponse response =travelRequestService.deleteDraftTravelRequest(id);

        return  ResponseEntity.ok(ApiResponse.success(response, "Travel Request Deleted Successfully"));

}

}
