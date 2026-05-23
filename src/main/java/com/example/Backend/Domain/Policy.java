package com.example.Backend.Domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="policies",indexes = {
        @Index(name = "idx_policy_role",columnList = "applies_to_role"),
        @Index(name="idx_policy_active",columnList = "active")
})

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "applies_to_role",nullable = false,length = 20)
    private Role appliesToRole;

    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal maxBudget;

    @Column(length = 50)
    private String maxTravelClass;

    @Column(nullable = false)
    @Builder.Default
    private Boolean hardBlockOnViolation = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;


}


