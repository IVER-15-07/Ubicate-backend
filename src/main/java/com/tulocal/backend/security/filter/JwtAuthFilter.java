package com.tulocal.backend.security.filter;

import com.tulocal.backend.security.jwt.JwtService;
import com.tulocal.backend.security.jwt.TokenPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Equivalente al AuthMiddleware de Node.js
 * Intercepta cada request, extrae y valida el JWT
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Sin token → continúa (Spring Security decide si la ruta es pública o no)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            TokenPayload payload = jwtService.verifyAccessToken(token);

            // Convertir role a authority para Spring Security
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + payload.getRole().toUpperCase()));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(payload, null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Token inválido → Spring Security rechazará si la ruta es protegida
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}