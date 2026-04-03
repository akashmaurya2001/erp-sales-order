package com.precisioncast.erp.auth.service;

import com.precisioncast.erp.auth.dto.request.ForgotPasswordRequest;
import com.precisioncast.erp.auth.dto.request.LoginRequest;
import com.precisioncast.erp.auth.dto.request.ResetPasswordRequest;
import com.precisioncast.erp.auth.dto.response.CurrentUserResponse;
import com.precisioncast.erp.auth.dto.response.LoginResponse;
import com.precisioncast.erp.auth.dto.response.LogoutResponse;
import com.precisioncast.erp.auth.dto.response.MessageResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    CurrentUserResponse getCurrentUser(String email);

    MessageResponse forgotPassword(ForgotPasswordRequest request);

    MessageResponse resetPassword(ResetPasswordRequest request);

    LogoutResponse logout();
}
