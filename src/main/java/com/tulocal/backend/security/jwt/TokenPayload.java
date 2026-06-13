package com.tulocal.backend.security.jwt;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
 
/**
 * Equivalente a la interfaz TokenPayload de Node.js
 * Datos que van dentro del JWT
 */
@Getter
@AllArgsConstructor
public class TokenPayload {
    private final String userId;
    private final String email;
    private final String role;
}
 