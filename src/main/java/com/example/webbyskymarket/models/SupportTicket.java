package com.example.webbyskymarket.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.webbyskymarket.enams.TicketStatus;

@Data
@Entity
@Table(name = "support_tickets")
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = TicketStatus.OPEN;
    }
} 