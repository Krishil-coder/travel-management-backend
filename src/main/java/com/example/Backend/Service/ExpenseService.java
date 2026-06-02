package com.example.Backend.Service;

import com.example.Backend.DTO.Request.CreateExpenseRequest;
import com.example.Backend.DTO.Request.FinanceApprovalRequestDto;
import com.example.Backend.DTO.Request.FinanceExpenseActionRequest;
import com.example.Backend.DTO.Request.UpdateExpenseRequest;
import com.example.Backend.DTO.Response.ExpenseResponse;
import com.example.Backend.Domain.Expense;
import com.example.Backend.Domain.ExpenseStatus;
import com.example.Backend.Domain.Role;
import com.example.Backend.Domain.TravelRequest;
import com.example.Backend.Domain.User;
import com.example.Backend.Exception.ResourceConflictException;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Repository.ExpenseRepository;
import com.example.Backend.Repository.TravelRequestRepository;
import com.example.Backend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;

    @Transactional
//    public ExpenseResponse addExpense(Long requestId, CreateExpenseRequest request) {
//        TravelRequest travelRequest = findTravelRequest(requestId);
//
//        Expense expense = Expense.builder()
//                .travelRequest(travelRequest)
//                .category(request.getCategory())
//                .claimedAmount(request.getClaimedAmount())
//                .description(request.getDescription())
//                .expenseDate(request.getExpenseDate())
//                .receiptPath(request.getReceiptPath())
//                .build();
//
//        Expense savedExpense = expenseRepository.save(expense);
//
//        return ExpenseResponse.from(savedExpense);
//    }

    public List<ExpenseResponse> getExpenses(Long requestId) {
        findTravelRequest(requestId);

        return expenseRepository.findByTravelRequestId(requestId)
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    @Transactional
    public ExpenseResponse updateExpense(Long requestId, Long expenseId, UpdateExpenseRequest request) {
        findTravelRequest(requestId);

        Expense expense = findExpense(requestId, expenseId);
        expense.setCategory(request.getCategory());
        expense.setClaimedAmount(request.getClaimedAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setReceiptPath(request.getReceiptPath());

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseResponse.from(savedExpense);
    }

    @Transactional
    public ExpenseResponse deleteExpense(Long requestId, Long expenseId) {
        findTravelRequest(requestId);

        Expense expense = findExpense(requestId, expenseId);
        ExpenseResponse response = ExpenseResponse.from(expense);

        expenseRepository.delete(expense);

        return response;
    }

    private TravelRequest findTravelRequest(Long requestId) {
        return travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel Request Not Found " + requestId));
    }

    private Expense findExpense(Long requestId, Long expenseId) {
        return expenseRepository.findByIdAndTravelRequestId(expenseId, requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found " + expenseId));
    }
    public ExpenseResponse addExpense(
            Long requestId,
            CreateExpenseRequest request,
            MultipartFile file
    ) {
        TravelRequest travelRequest = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Travel request not found"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Receipt file is required");
        }

        String contentType = file.getContentType();

        boolean isPdf = "application/pdf".equals(contentType);
        boolean isImage =
                "image/jpeg".equals(contentType) ||
                        "image/png".equals(contentType) ||
                        "image/jpg".equals(contentType);

        if (!isPdf && !isImage) {
            throw new RuntimeException("Only PDF, JPG, JPEG and PNG files are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size must be less than 5MB");
        }

        try {
            String uploadDir = "uploads/expense-receipts/";

            String originalFileName = file.getOriginalFilename();

            String extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );

            String fileName = "expense_" + System.currentTimeMillis() + extension;

            Path filePath = Paths.get(uploadDir, fileName);

            Files.write(filePath, file.getBytes());

            Expense expense = Expense.builder()
                    .travelRequest(travelRequest)
                    .category(request.getCategory())
                    .claimedAmount(request.getClaimedAmount())
                    .description(request.getDescription())
                    .expenseDate(request.getExpenseDate())
                    .receiptPath(filePath.toString())
                    .status(ExpenseStatus.PENDING)
                    .build();

            Expense saved = expenseRepository.save(expense);

            return ExpenseResponse.from(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload receipt file");
        }
    }
    public List<ExpenseResponse> getPendingExpenses() {

        List<Expense> expenses = expenseRepository.findByStatus(ExpenseStatus.PENDING);

        return expenses.stream()
                .map(ExpenseResponse::from)
                .toList();

    }

    @Transactional
    public ExpenseResponse approveExpense(Long expenseId, FinanceExpenseActionRequest request) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found " + expenseId));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new ResourceConflictException("Only pending expenses can be approved");
        }

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User financer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Finance user not found"));

        expense.setStatus(ExpenseStatus.APPROVED);
        expense.setApprovedAmount(request.getApprovedAmount());
        expense.setApprovedBy(financer);
        expense.setApprovedAt(LocalDateTime.now());
        expense.setFinanceComments(request.getComments());

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseResponse.from(savedExpense);
    }

    @Transactional
    public ExpenseResponse rejectExpense(Long expenseId, FinanceExpenseActionRequest request) {

        Expense expense =expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found " + expenseId));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new ResourceConflictException("Only pending  can be rejected");
        }
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User financer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Finance user not found"));

        expense.setStatus(ExpenseStatus.REJECTED);
        expense.setApprovedAmount(null);
        expense.setApprovedBy(financer);
        expense.setApprovedAt(LocalDateTime.now());
        expense.setFinanceComments(request.getComments());

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseResponse.from(savedExpense);
    }

    private User findFinancer(Long financeId) {
        User financer = userRepository.findById(financeId)
                .orElseThrow(() -> new ResourceNotFoundException("Finance Approver Not Found " + financeId));

        if (financer.getRole() != Role.FINANCER) {
            throw new ResourceConflictException("User is not a financer");
        }

        return financer;
    }
}
