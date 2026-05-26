package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.RequestStatus;
import com.example.Backend.Domain.TravelRequest;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ManagerRequestDetailsResponse {
    private Long requestId;
    private String employeeName;
    private String department;
    private String destination;
    private String travelClass;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal estimatedCost;
    private String purpose;
    private String status;

    public static ManagerRequestDetailsResponse from(TravelRequest travelRequest) {
        return ManagerRequestDetailsResponse.builder()
                .requestId(travelRequest.getId())
                .employeeName(travelRequest.getEmployee().getFirstName() + " " + travelRequest.getEmployee().getLastName())
                .department(travelRequest.getEmployee().getDepartment())
                .destination(travelRequest.getDestination())
                .travelClass(travelRequest.getTravelClass())
                .startDate(travelRequest.getStartDate())
                .endDate(travelRequest.getEndDate())
                .estimatedCost(travelRequest.getEstimatedCost())
                .purpose(travelRequest.getPurpose())
                .status(toManagerStatus(travelRequest.getStatus()))
                .build();
    }

    private static String toManagerStatus(RequestStatus status) {
        if (status == RequestStatus.SUBMITTED) {
            return "MANAGER_PENDING";
        }

        if (status == RequestStatus.MANAGER_APPROVED) {
            return "FINANCE_PENDING";
        }

        return status.name();
    }
}
