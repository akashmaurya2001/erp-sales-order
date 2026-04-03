package com.precisioncast.erp.auth.service;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String fullName, String resetToken);
}
