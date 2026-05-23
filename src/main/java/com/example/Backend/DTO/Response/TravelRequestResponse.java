package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.RequestStatus;
import com.example.Backend.Domain.TravelRequest;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TravelRequestResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String purpose;
    private BigDecimal estimatedCost;
    private RequestStatus status;
    private String travelClass;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TravelRequestResponse from(TravelRequest travelRequest) {
        return TravelRequestResponse.builder()
                .id(travelRequest.getId())
                .employeeId(travelRequest.getEmployee().getId())
                .employeeName(travelRequest.getEmployee().getFirstName() + " " + travelRequest.getEmployee().getLastName())
                .destination(travelRequest.getDestination())
                .startDate(travelRequest.getStartDate())
                .endDate(travelRequest.getEndDate())
                .purpose(travelRequest.getPurpose())
                .estimatedCost(travelRequest.getEstimatedCost())
                .status(travelRequest.getStatus())
                .travelClass(travelRequest.getTravelClass())
                .submittedAt(travelRequest.getSubmittedAt())
                .createdAt(travelRequest.getCreatedAt())
                .updatedAt(travelRequest.getUpdatedAt())
                .build();
    }
}
