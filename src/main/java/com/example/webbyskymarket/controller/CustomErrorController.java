package com.example.webbyskymarket.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = Integer.parseInt(status.toString());
        String errorTitle, errorMessage;

        switch (statusCode) {
            case 403:
                errorTitle = "Forbidden";
                errorMessage = "You don't have permission to access this resource.";
                break;
            case 404:
                errorTitle = "Page Not Found";
                errorMessage = "The page you're looking for doesn't exist or has been moved.";
                break;
            case 500:
                errorTitle = "Internal Server Error";
                errorMessage = "We're sorry, but something went wrong on our end. Our team has been notified and we'll fix it as soon as possible.";
                break;
            default:
                errorTitle = "Error";
                errorMessage = "An unexpected error occurred.";
        }

        model.addAttribute("status", statusCode);
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);

        return "error/error";
    }

}