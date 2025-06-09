package com.example.webbyskymarket.controller;

import com.example.webbyskymarket.models.SupportTicket;
import com.example.webbyskymarket.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {
    private final SupportTicketService supportTicketService;

    @GetMapping
    public String supportPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("tickets", supportTicketService.getUserTickets(userDetails.getUsername()));
        }
        return "support/support";
    }

    @PostMapping("/create")
    public String createTicket(@RequestParam String subject,
                             @RequestParam String message,
                             @AuthenticationPrincipal UserDetails userDetails) {
        supportTicketService.createTicket(userDetails.getUsername(), subject, message);
        return "redirect:/support";
    }
} 