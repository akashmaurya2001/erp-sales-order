package com.precisioncast.erp.auth.service.impl;

import com.precisioncast.erp.auth.dto.request.ForgotPasswordRequest;
import com.precisioncast.erp.auth.dto.request.LoginRequest;
import com.precisioncast.erp.auth.dto.request.ResetPasswordRequest;
import com.precisioncast.erp.auth.dto.response.CurrentUserResponse;
import com.precisioncast.erp.auth.dto.response.LoginResponse;
import com.precisioncast.erp.auth.dto.response.LogoutResponse;
import com.precisioncast.erp.auth.dto.response.MessageResponse;
import com.precisioncast.erp.auth.entity.PasswordResetToken;
import com.precisioncast.erp.auth.entity.User;
import com.precisioncast.erp.auth.repository.PasswordResetTokenRepository;
import com.precisioncast.erp.auth.repository.UserRepository;
import com.precisioncast.erp.auth.service.AuthService;
import com.precisioncast.erp.auth.service.EmailService;
import com.precisioncast.erp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadCredentialsException("User account is inactive");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getRoleName().name(),
                loginRequest.getRememberMe()
        );

        long expiresIn = Boolean.TRUE.equals(loginRequest.getRememberMe())
                ? jwtService.getRememberMeJwtExpiration()
                : jwtService.getNormalJwtExpiration();

        return LoginResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .message("Login successful")
                .build();
    }

    @Override
    public LogoutResponse logout() {
        return LogoutResponse.builder()
                .message("Logout successful. please remove token from client side.")
                .build();
    }

    @Override
    public CurrentUserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return CurrentUserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .build();
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found with this email"));

        PasswordResetToken resetToken = passwordResetTokenRepository.findByUser(user)
                .orElseGet(PasswordResetToken::new);

        resetToken.setUser(user);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getFullName(),
                resetToken.getToken()
        );

        return MessageResponse.builder()
                .message("Password reset token has been sent to your email")
                .build();
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadCredentialsException("New password and confirm password do not match");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid reset token"));

        if (Boolean.TRUE.equals(resetToken.getUsed())) {
            throw new BadCredentialsException("Reset token has already been used");
        }

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return MessageResponse.builder()
                .message("Password reset successful")
                .build();
    }
}