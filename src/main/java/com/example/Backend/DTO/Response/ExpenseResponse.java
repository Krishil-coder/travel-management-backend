package com.example.Backend.DTO.Response;

import com.example.Backend.Domain.Expense;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ExpenseResponse {

    private Long id;
    private Long travelRequestId;
    private String category;
    private BigDecimal claimedAmount;
    private BigDecimal approvedAmount;
    private String description;
    private LocalDate expenseDate;
    private String receiptPath;
    private String status;
    private String financeComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ExpenseResponse from(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .travelRequestId(expense.getTravelRequest().getId())
                .category(expense.getCategory().name())
                .claimedAmount(expense.getClaimedAmount())
                .approvedAmount(expense.getApprovedAmount())
                .description(expense.getDescription())
                .expenseDate(expense.getExpenseDate())
                .receiptPath(expense.getReceiptPath())
                .status(expense.getStatus().name())
                .financeComments(expense.getFinanceComments())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}
