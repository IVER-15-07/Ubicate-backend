package com.tulocal.backend.modules.auth.application.usecase;

import com.tulocal.backend.modules.auth.api.request.RegisterRequest;
import com.tulocal.backend.modules.auth.api.response.AuthResponse;
import com.tulocal.backend.modules.auth.application.mapper.AuthMapper;
import com.tulocal.backend.modules.auth.domain.model.AuthUser;
import com.tulocal.backend.modules.auth.domain.repository.AuthUserRepository;
import com.tulocal.backend.security.jwt.JwtService;
import com.tulocal.backend.security.jwt.TokenPayload;
import com.tulocal.backend.security.jwt.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final AuthUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    public AuthResponse execute(RegisterRequest request) {
        if (repository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        AuthUser user = new AuthUser();
        user.setId(UUID.randomUUID());
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(request.getRoleId());
        user.setCreadoEn(LocalDateTime.now());

        AuthUser savedUser = repository.save(user);

        TokenPayload payload = new TokenPayload(
                savedUser.getId().toString(),
                savedUser.getEmail(),
                savedUser.getRoleId().toString()
        );

        TokenResponse tokens = jwtService.generateTokens(payload);

        return authMapper.toResponse(savedUser, tokens);
    }
}