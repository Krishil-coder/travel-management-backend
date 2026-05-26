package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.ApprovalActionRequest;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.ManagerRequestDetailsResponse;
import com.example.Backend.Service.ManagerApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})
public class ManagerApprovalController {
    private final ManagerApprovalService managerApprovalService;

    @GetMapping("/{managerId}/requests/pending")
    public ResponseEntity<ApiResponse<List<ManagerRequestDetailsResponse>>> getPendingRequests(
            @PathVariable Long managerId
    ) {
        List<ManagerRequestDetailsResponse> response = managerApprovalService.getPendingRequests(managerId);

        return ResponseEntity.ok(ApiResponse.success(response, "Pending Requests Fetched Successfully"));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ApiResponse<ManagerRequestDetailsResponse>> getRequestDetails(
            @PathVariable Long requestId
    ) {
        ManagerRequestDetailsResponse response = managerApprovalService.getRequestDetails(requestId);

        return ResponseEntity.ok(ApiResponse.success(response, "Travel Request Found Successfully"));
    }

    @PutMapping("/requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<ManagerRequestDetailsResponse>> approveRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody ApprovalActionRequest request
    ) {
        ManagerRequestDetailsResponse response = managerApprovalService.approveRequest(requestId, request);

        return ResponseEntity.ok(ApiResponse.success(response, "Travel Request Approved Successfully"));
    }

    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<ApiResponse<ManagerRequestDetailsResponse>> rejectRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody ApprovalActionRequest request
    ) {
        ManagerRequestDetailsResponse response = managerApprovalService.rejectRequest(requestId, request);

        return ResponseEntity.ok(ApiResponse.success(response, "Travel Request Rejected Successfully"));
    }
}
