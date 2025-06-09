package com.example.webbyskymarket.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = switch (exception) {
            case BadCredentialsException ignored -> "Invalid username or password.";
            case DisabledException ignored -> "Your account is blocked.";
            case LockedException ignored -> "Your account has been temporarily blocked.";
            case null, default -> "Login error. Try again.";
        };

        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect("/login");
    }
}