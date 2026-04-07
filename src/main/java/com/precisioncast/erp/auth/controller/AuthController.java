package com.precisioncast.erp.auth.controller;

import com.precisioncast.erp.auth.dto.request.ForgotPasswordRequest;
import com.precisioncast.erp.auth.dto.request.LoginRequest;
import com.precisioncast.erp.auth.dto.request.ResetPasswordRequest;
import com.precisioncast.erp.auth.dto.response.CurrentUserResponse;
import com.precisioncast.erp.auth.dto.response.LoginResponse;
import com.precisioncast.erp.auth.dto.response.LogoutResponse;
import com.precisioncast.erp.auth.dto.response.MessageResponse;
import com.precisioncast.erp.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Auth Module",
        description = "API for login, password recovery, logout, and current authentication details."
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/me")
    public CurrentUserResponse getCurrentUser(Authentication authentication) {
        return  authService.getCurrentUser(authentication.getName());
    }

    @PostMapping("/forgot-password")
    public MessageResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        return authService.resetPassword(request);
    }

    @PostMapping("/logout")
    public LogoutResponse logout(){
        return authService.logout();
    }
}