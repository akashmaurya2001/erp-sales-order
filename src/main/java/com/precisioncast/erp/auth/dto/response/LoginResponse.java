package com.precisioncast.erp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String token;
    private String tokenType;
    private Long expiresIn;
    private String message;
}
