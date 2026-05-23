package com.example.Backend.Domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="notifications",indexes = {
        @Index(name="idx_notif_recipient",columnList = "recipient_id,read_flag")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="recipient_id",nullable = false)
    private User recipient;

    @Column(nullable = false,length = 200)
    private String title;

    @Column(nullable = false,length = 1000)
    private String message;

    @Column(length =500)
    private String linkUrl;

    @Column(name="read_flag",nullable = false)
    @Builder.Default
    private Boolean readFlag = false;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;



}
