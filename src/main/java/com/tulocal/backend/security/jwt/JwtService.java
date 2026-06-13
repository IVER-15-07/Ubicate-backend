package com.tulocal.backend.security.jwt;

import com.tulocal.backend.common.exception.AppException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Equivalente a JwtService de Node.js
 * Genera y valida access token + refresh token
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    // 7 días para apps móviles (igual que Node)
    private static final long ACCESS_TOKEN_EXPIRY  = 7L  * 24 * 60 * 60 * 1000;
    // 30 días para refresh token
    private static final long REFRESH_TOKEN_EXPIRY = 30L * 24 * 60 * 60 * 1000;

    // ── Generar ambos tokens ──────────────────────────────────────────
    public TokenResponse generateTokens(TokenPayload payload) {
        String accessToken  = buildToken(payload, accessSecret,  ACCESS_TOKEN_EXPIRY);
        String refreshToken = buildToken(payload, refreshSecret, REFRESH_TOKEN_EXPIRY);
        return new TokenResponse(accessToken, refreshToken);
    }

    public String generateAccessToken(TokenPayload payload) {
        return buildToken(payload, accessSecret, ACCESS_TOKEN_EXPIRY);
    }

    public String generateRefreshToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(getKey(refreshSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Verificar access token ────────────────────────────────────────
    public TokenPayload verifyAccessToken(String token) {
        return extractPayload(token, accessSecret);
    }

    // ── Verificar refresh token ───────────────────────────────────────
    public TokenPayload verifyRefreshToken(String token) {
        return extractPayload(token, refreshSecret);
    }

    // ── Helpers privados ──────────────────────────────────────────────
    private String buildToken(TokenPayload payload, String secret, long expiry) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", payload.getUserId());
        claims.put("email",  payload.getEmail());
        claims.put("role",   payload.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    private TokenPayload extractPayload(String token, String secret) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new TokenPayload(
                    claims.get("userId", String.class),
                    claims.get("email",  String.class),
                    claims.get("role",   String.class)
            );
        } catch (JwtException e) {
            throw AppException.unauthorized("Token inválido o expirado");
        }
    }

    private Key getKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}