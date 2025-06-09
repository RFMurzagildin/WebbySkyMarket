package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.enams.TicketStatus;
import com.example.webbyskymarket.models.SupportTicket;
import com.example.webbyskymarket.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/support-tickets")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSupportController {

    private final SupportTicketService supportTicketService;

    @GetMapping
    public String getSupportTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {
        
        List<SupportTicket> tickets;
        if (status != null && !status.isEmpty()) {
            tickets = supportTicketService.findByStatus(TicketStatus.valueOf(status));
        } else if (search != null && !search.isEmpty()) {
            tickets = supportTicketService.searchTickets(search);
        } else {
            tickets = supportTicketService.findAll();
        }
        
        model.addAttribute("tickets", tickets);
        return "admin/support-tickets";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SupportTicket> getTicketDetails(@PathVariable Long id) {
        return supportTicketService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<Void> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody TicketStatusUpdateRequest request) {
        
        if (supportTicketService.updateStatus(id, request.getStatus())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class TicketStatusUpdateRequest {
        private TicketStatus status;

        public TicketStatus getStatus() {
            return status;
        }

        public void setStatus(TicketStatus status) {
            this.status = status;
        }
    }
} 