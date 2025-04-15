package se2.group6.librarymanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se2.group6.librarymanagement.service.EmailService;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping("/send-email")
    public String sendEmail() {
        emailService.sendEmail("test@example.com", "Test", "Test");
        return "Email sent";
    }
}
