package com.example.webbyskymarket.repository;

import com.example.webbyskymarket.enams.TicketStatus;
import com.example.webbyskymarket.models.SupportTicket;
import com.example.webbyskymarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
    List<SupportTicket> findByStatus(TicketStatus status);
    List<SupportTicket> findBySubjectContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(String subject, String username);
} 