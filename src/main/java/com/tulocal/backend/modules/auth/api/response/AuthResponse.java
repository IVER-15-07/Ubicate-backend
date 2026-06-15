package com.tulocal.backend.modules.auth.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UUID userId;
    private Integer roleId;
    private String email;
    private String nombre;
}
