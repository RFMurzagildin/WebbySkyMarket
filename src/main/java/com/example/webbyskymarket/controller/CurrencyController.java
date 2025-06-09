package com.example.webbyskymarket.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CurrencyController {
    @GetMapping("/set-currency")
    public String setCurrency(@RequestParam String currency, HttpServletResponse response, @RequestHeader("Referer") String referer) {
        Cookie cookie = new Cookie("currency", currency);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 дней
        response.addCookie(cookie);
        return "redirect:" + referer;
    }
} 