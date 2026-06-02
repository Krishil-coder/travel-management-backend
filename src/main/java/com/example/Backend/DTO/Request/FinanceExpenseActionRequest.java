package com.example.Backend.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinanceExpenseActionRequest {



    private BigDecimal approvedAmount;

    private String comments;
}