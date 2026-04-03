package com.precisioncast.erp.auth.service.impl;

import com.precisioncast.erp.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPasswordResetEmail(String toEmail, String fullName, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request - Precision Cast ERP");
        message.setText(
                "Hello " + fullName + ",\n\n" +
                        "We received a request to reset your password.\n\n" +
                        "Use the following reset token:\n" +
                        resetToken + "\n\n" +
                        "This token will expire in 15 minutes.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "Precision Cast ERP Team"
        );

        javaMailSender.send(message);
    }
}