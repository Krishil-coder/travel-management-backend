package com.example.Backend.DTO.Request;

import com.example.Backend.Domain.Role;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyRequest {

    @NotBlank(message = "Policy name is required")
    @Size(max = 200, message = "Policy name must not exceed 200 characters")
    private String name;

    @NotNull(message = "Role is required")
    private Role appliesToRole;

    @NotNull(message = "Max budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Max budget must be greater than 0")
    private BigDecimal maxBudget;

    @Size(max = 50, message = "Travel class must not exceed 50 characters")
    private String maxTravelClass;

    @NotNull(message = "Hard block value is required")
    private Boolean hardBlockOnViolation;

    @NotNull(message = "Active value is required")
    private Boolean active;
}
