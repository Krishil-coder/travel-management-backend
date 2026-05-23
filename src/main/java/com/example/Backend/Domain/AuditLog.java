package com.example.Backend.Domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="audit_logs",indexes = {
        @Index(name="idx_audit_timestamp",columnList = "timestamp"),
        @Index(name="idx_audit_actor",columnList = "actor_id"),
        @Index(name = "idx_audit_entity",columnList = "entity_type,entity_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "actor_id",nullable = false)
    private Long actorId;

    @Column(nullable = false,length =200)
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name="entity_type",nullable = false,length = 20)
    private AuditEntityType entityType;

    @Column(name = "entity_id",nullable = false)
    private Long entityId;

    @Column(length = 1000)
    private String comments;



}
