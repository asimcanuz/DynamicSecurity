package org.asodev.dynamicsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private  final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> testMail() {
        emailService.sendMail("uzasim2@gmail.com", "Test Email", "This is a test email from Dynamic Security application.");
        return ResponseEntity.ok().build();
    }
}
