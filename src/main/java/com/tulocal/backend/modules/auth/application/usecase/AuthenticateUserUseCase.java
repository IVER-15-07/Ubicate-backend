package com.tulocal.backend.modules.auth.application.usecase;

import com.tulocal.backend.modules.auth.api.response.AuthResponse;
import com.tulocal.backend.modules.auth.domain.model.AuthUser;
import com.tulocal.backend.modules.auth.domain.repository.AuthUserRepository;
import com.tulocal.backend.modules.auth.application.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final AuthUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse execute(String email, String password) {
        Optional<AuthUser> opt = repository.findByEmailIgnoreCase(email);
        if (opt.isEmpty()) return null;
        AuthUser user = opt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) return null;

        String token = jwtService.generateToken(user.getId() != null ? user.getId().toString() : user.getEmail());

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setRoleId(user.getRoleId());
        resp.setEmail(user.getEmail());
        resp.setNombre(user.getNombre());
        return resp;
    }
}
