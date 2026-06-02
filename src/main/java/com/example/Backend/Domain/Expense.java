package com.example.Backend.Domain;


import jakarta.persistence.*;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="expenses",indexes = {
        @Index(name="idx_edp_request",columnList = "travel_request_id"),
        @Index(name="idx_exp_status",columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="travel_request_id",nullable = false)
    private TravelRequest travelRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private Category category;

    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal claimedAmount;

    @Column(precision = 12,scale = 2)
    private BigDecimal approvedAmount;

    @Column(nullable = false,length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(length = 500)
    private String receiptPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    @Builder.Default
    private ExpenseStatus status = ExpenseStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="approved_by_id")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @Column(length = 1000)
    private String financeComments;

    @OneToOne(mappedBy = "expense",cascade = CascadeType.ALL,orphanRemoval = true,
    fetch =FetchType.LAZY)
    private Reimbursement reimbursement;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Category{
        FOOD,STAY,TRANSPORT,MISCELLANEOUS
    }





}
