package com.plantastic.backend.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String usernameOrEmail;
    private String password;

    public LoginRequest(String username, String password) {
        this.usernameOrEmail = username;
        this.password = password;
    }
}
