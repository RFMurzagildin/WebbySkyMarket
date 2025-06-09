package com.example.webbyskymarket.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendBlockedAccountEmail(String email, String username) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Your Account Has Been Blocked");
        Context context = new Context();
        context.setVariable("username", username);

        String htmlContent = templateEngine.process("admin/account-blocked", context);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    public void sendUnblockedAccountEmail(String email, String username) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Your Account Has Been Unblocked");
        Context context = new Context();
        context.setVariable("username", username);

        String htmlContent = templateEngine.process("admin/account-unblocked", context);

        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}