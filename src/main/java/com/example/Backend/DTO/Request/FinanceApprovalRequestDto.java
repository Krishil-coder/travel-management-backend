package com.example.Backend.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FinanceApprovalRequestDto {

    @NotNull(message = "Finance approver is required")
    private Long financeId;

    @Size(max=1000,message = "Comments must not exceed 1000 characters ")
    private String comments;

}
