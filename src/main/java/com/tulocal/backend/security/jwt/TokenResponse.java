package com.tulocal.backend.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Equivalente a TokenResponse de Node.js
 */
@Getter
@AllArgsConstructor
public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;
}