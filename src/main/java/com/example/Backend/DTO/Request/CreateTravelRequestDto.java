package com.example.Backend.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class CreateTravelRequestDto {

    @NotNull(message = "Employee is is required")
    private Long employeeId;

    @NotBlank(message = "destination is required")
    @Size(message = "Destination must not exceed 200 characters")
    private String destination;

    @Size(max = 50,message = "travel class must not exceed 50 characters ")
    private String travelClass;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "Estimated cost must be greater than 0")
    private BigDecimal estimatedCost;

    @NotBlank(message = "Purpose can't be null")
    @Size(max = 500,message = "Purpose not exceed 500 words")
    private String purpose;




}
