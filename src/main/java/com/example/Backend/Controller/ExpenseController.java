package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.CreateExpenseRequest;
import com.example.Backend.DTO.Request.UpdateExpenseRequest;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.ExpenseResponse;
import com.example.Backend.Service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping(value = "/{requestId}/expenses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(
            @PathVariable Long requestId,
            @Valid @ModelAttribute CreateExpenseRequest request,
            @RequestParam("file") MultipartFile file
    )  {
        ExpenseResponse response = expenseService.addExpense(requestId, request, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Expense successfully added"));
    }

    @GetMapping("/{requestId}/expenses")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getExpenses(
            @PathVariable Long requestId
    ) {
        List<ExpenseResponse> response = expenseService.getExpenses(requestId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Expenses successfully retrieved")
        );
    }

    @PutMapping("/{requestId}/expenses/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long requestId,
            @PathVariable Long expenseId,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {
        ExpenseResponse response = expenseService.updateExpense(requestId, expenseId, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Expense successfully updated")
        );
    }

    @DeleteMapping("/{requestId}/expenses/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> deleteExpense(
            @PathVariable Long requestId,
            @PathVariable Long expenseId
    ) {
        ExpenseResponse response = expenseService.deleteExpense(requestId, expenseId);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Expense successfully deleted")
        );
    }
    @GetMapping("/expenses/pending")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getPendingExpenses(){
        List<ExpenseResponse> response = expenseService.getPendingExpenses();

        return ResponseEntity.ok(
                ApiResponse.success(response, " pending request successfully retrieved"));
    }
}
