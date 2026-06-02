package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.Policy;
import com.example.Backend.Domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PolicyResponse {
    private Long id;
    private String name;
    private Role appliesToRole;
    private BigDecimal maxBudget;
    private String maxTravelClass;
    private Boolean hardBlockOnViolation;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PolicyResponse from(Policy policy) {
        return PolicyResponse.builder()
                .id(policy.getId())
                .name(policy.getName())
                .appliesToRole(policy.getAppliesToRole())
                .maxBudget(policy.getMaxBudget())
                .maxTravelClass(policy.getMaxTravelClass())
                .hardBlockOnViolation(policy.getHardBlockOnViolation())
                .active(policy.isActive())
                .createdAt(policy.getCreatedAt())
                .updatedAt(policy.getUpdateAt())
                .build();
    }
}
