package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.FinanceApprovalRequestDto;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.FinanceRequestResponse;
import com.example.Backend.Service.FinanceApprovalService;
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
@RequestMapping("/api/financer")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})
public class FinanceApprovalController {

    private final FinanceApprovalService financeApprovalService;

    @GetMapping("/{financeId}/requests/pending")
    public ResponseEntity<ApiResponse<List<FinanceRequestResponse>>> getPendingRequests(
            @PathVariable Long financeId
    ) {
        List<FinanceRequestResponse> response =
                financeApprovalService.getPendingRequests(financeId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Finance Pending Requests fetched Successfully")
        );
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ApiResponse<FinanceRequestResponse>> getRequestDetails(
            @PathVariable Long requestId
    ) {
        FinanceRequestResponse response =
                financeApprovalService.getRequestDetails(requestId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Travel Request Details fetched Successfully")
        );
    }

    @PutMapping("/requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<FinanceRequestResponse>> approveRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody FinanceApprovalRequestDto request
    ) {
        FinanceRequestResponse response =
                financeApprovalService.approveRequest(requestId, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Request Approved Successfully")
        );
    }

    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<ApiResponse<FinanceRequestResponse>> rejectRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody FinanceApprovalRequestDto request
    ) {
        FinanceRequestResponse response =
                financeApprovalService.rejectRequest(requestId, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Request Rejected Successfully")
        );
    }
}
