package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.FinanceApprovalRequestDto;
import com.example.Backend.DTO.Request.FinanceExpenseActionRequest;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.ExpenseResponse;
import com.example.Backend.Service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/expenses/pending")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getPendingExpenses() {

        List<ExpenseResponse> response = expenseService.getPendingExpenses();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Pending expenses successfully retrieved")
        );
    }

    @PutMapping("/expenses/{expenseId}/approve")
    public ResponseEntity<ApiResponse<ExpenseResponse>> approveExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody FinanceExpenseActionRequest request
    ) {
        ExpenseResponse response = expenseService.approveExpense(expenseId, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Expense approved successfully")
        );
    }
    @PutMapping("/expenses/{expenseId}/reject")
    public ResponseEntity<ApiResponse<ExpenseResponse>> rejectExpense(
            @PathVariable Long expenseId,
            @Valid @RequestBody FinanceExpenseActionRequest request
    ){
        ExpenseResponse response =expenseService.rejectExpense(expenseId,request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Expense rejected successfully"));
    }
}
