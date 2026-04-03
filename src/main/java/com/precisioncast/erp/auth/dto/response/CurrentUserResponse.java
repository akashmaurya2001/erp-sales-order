package com.precisioncast.erp.auth.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CurrentUserResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String role;
}
