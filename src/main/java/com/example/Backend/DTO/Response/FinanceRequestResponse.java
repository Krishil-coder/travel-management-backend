package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.RequestStatus;
import com.example.Backend.Domain.TravelRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class FinanceRequestResponse {
    private Long requestId;
    private String employeeName;
    private String department;
    private String destination;
    private String travelClass;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal estimatedCost;
    private String purpose;
    private String managerApproverName;
    private String managerComments;
    private String status;

    public static FinanceRequestResponse from(TravelRequest travelRequest) {
        return FinanceRequestResponse.builder()
                .requestId(travelRequest.getId())
                .employeeName(travelRequest.getEmployee().getFirstName() +" "+travelRequest.getEmployee().getLastName())
                .department(travelRequest.getEmployee().getDepartment())
                .destination(travelRequest.getDestination())
                .travelClass(travelRequest.getTravelClass())
                .startDate(travelRequest.getStartDate())
                .endDate(travelRequest.getEndDate())
                .estimatedCost(travelRequest.getEstimatedCost())
                .purpose(travelRequest.getPurpose())
                .managerApproverName(travelRequest.getManagerApprover() == null? null :travelRequest.getManagerApprover().getFirstName() + " " +travelRequest.getManagerApprover().getLastName())
                .managerComments(travelRequest.getManagerComments())
                .status(FinanceStatus(travelRequest.getStatus()))
                .build();
    }
    public static String FinanceStatus(RequestStatus status) {
        if (status == RequestStatus.MANAGER_APPROVED) {
            return "FINANCE_PENDING";
        }
        if(status == RequestStatus.FINANCE_APPROVED){
            return "FINANCE_APPROVED";
        }
        return status.name();
    }
}
