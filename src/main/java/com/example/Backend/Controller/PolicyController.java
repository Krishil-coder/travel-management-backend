package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.PolicyRequest;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.PolicyResponse;
import com.example.Backend.Service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<ApiResponse<PolicyResponse>> createPolicy(
            @Valid @RequestBody PolicyRequest request
    ) {
        PolicyResponse response = policyService.createPolicy(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Policy Created Successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyResponse>>> getAllPolicies() {
        List<PolicyResponse> response = policyService.getAllPolicies();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Policies Fetched Successfully")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponse>> getPolicyById(
            @PathVariable Long id
    ) {
        PolicyResponse response = policyService.getPolicyById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Policy Fetched Successfully")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponse>> updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody PolicyRequest request
    ) {
        PolicyResponse response = policyService.updatePolicy(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Policy Updated Successfully")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponse>> deletePolicy(
            @PathVariable Long id
    ) {
        PolicyResponse response = policyService.deletePolicy(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Policy Deleted Successfully")
        );
    }
}
