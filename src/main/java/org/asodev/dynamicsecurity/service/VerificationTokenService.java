package org.asodev.dynamicsecurity.service;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.model.User;
import org.asodev.dynamicsecurity.model.VerificationToken;
import org.asodev.dynamicsecurity.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;


    public String createVerificationToken(User user) {
        // Generate a 6-digit code
        String code = generateSixDigitCode();

        // Create and save verification token
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15)); // Short expiry for numeric codes
        verificationTokenRepository.save(verificationToken);

        return code;
    }

    public String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(code);
    }

    public String verifyCode(String code) {
        VerificationToken token = verificationTokenRepository.findByToken(code);

        if (token == null) {
            return "invalid";
        }

        if (LocalDateTime.now().isAfter(token.getExpiryDate())) {
            verificationTokenRepository.delete(token);
            return "expired";
        }

        User user = token.getUser();
        user.setEnabled(true);

        verificationTokenRepository.delete(token);
        return "valid";
    }

    public void sendVerificationEmail(User user) {
        String code = createVerificationToken(user);
        emailService.sendVerificationEmail(user, code);
    }


}
