package com.plantastic.backend.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
    @NonNull
    private String usernameOrEmail;
    @NonNull
    private String password;
}
