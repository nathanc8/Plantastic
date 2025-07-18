package com.plantastic.backend.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String jwt;
    private String message;

    private LoginResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }

    public static LoginResponse success(String jwt) {
        return new LoginResponse(jwt, "Successful authentication.");
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(null, message);
    }
}
