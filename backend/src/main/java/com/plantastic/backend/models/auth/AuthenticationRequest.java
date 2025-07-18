package com.plantastic.backend.models.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String email;
    private String password;

    public AuthenticationRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
