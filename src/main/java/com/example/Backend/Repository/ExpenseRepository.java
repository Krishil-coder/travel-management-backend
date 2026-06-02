package com.example.Backend.Repository;

import com.example.Backend.Domain.Expense;
import com.example.Backend.Domain.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByTravelRequestId(Long travelRequestId);

    Optional<Expense> findByIdAndTravelRequestId(Long id, Long travelRequestId);

    List<Expense> findByStatus(ExpenseStatus status);
}
