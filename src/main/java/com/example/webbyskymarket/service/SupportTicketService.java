package com.example.webbyskymarket.service;

import com.example.webbyskymarket.enams.TicketStatus;
import com.example.webbyskymarket.models.SupportTicket;
import com.example.webbyskymarket.models.User;
import com.example.webbyskymarket.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupportTicketService {
    private final SupportTicketRepository supportTicketRepository;
    private final UserService userService;

    public SupportTicket createTicket(String username, String subject, String message) {
        User user = userService.getUserByUsername(username);
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSubject(subject);
        ticket.setMessage(message);
        return supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getUserTickets(String username) {
        User user = userService.getUserByUsername(username);
        return supportTicketRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAllByOrderByCreatedAtDesc();
    }

    public SupportTicket getTicketById(Long id) {
        return supportTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<SupportTicket> findAll() {
        return supportTicketRepository.findAll();
    }

    public List<SupportTicket> findByStatus(TicketStatus status) {
        return supportTicketRepository.findByStatus(status);
    }

    public List<SupportTicket> searchTickets(String query) {
        return supportTicketRepository.findBySubjectContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(query, query);
    }

    public Optional<SupportTicket> findById(Long id) {
        return supportTicketRepository.findById(id);
    }

    @Transactional
    public boolean updateStatus(Long id, TicketStatus newStatus) {
        return supportTicketRepository.findById(id)
                .map(ticket -> {
                    ticket.setStatus(newStatus);
                    return supportTicketRepository.save(ticket);
                })
                .isPresent();
    }
} 