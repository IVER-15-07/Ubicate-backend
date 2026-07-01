package com.tulocal.backend.security.config;

import com.tulocal.backend.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Equivalente a la configuración de rutas en Express/Node
 * Define qué rutas son públicas y cuáles requieren JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // habilita @PreAuthorize en controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Sin CSRF (API REST stateless, igual que Node)
                .csrf(AbstractHttpConfigurer::disable)

                // Sin sesión HTTP (usamos JWT)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ── RUTAS PÚBLICAS (equivalente a rutas sin authMiddleware en Node) ──
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/branches/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/branches/public/all-active").permitAll()


                        // ── RUTAS PROTEGIDAS (requieren JWT válido) ──

                        .requestMatchers(HttpMethod.POST, "/api/branches/create").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/branch-menus/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/branch-menus/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/menus/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/menus/**").authenticated()

                        .anyRequest().authenticated())

                // Agregar el filtro JWT antes del filtro de autenticación de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
