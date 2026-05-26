package com.example.Backend.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApprovalActionRequest {
    @NotNull(message = "Manager is required")
    private Long managerId;

    @Size(max = 1000, message = "Comments must not exceed 1000 characters")
    private String comments;
}
