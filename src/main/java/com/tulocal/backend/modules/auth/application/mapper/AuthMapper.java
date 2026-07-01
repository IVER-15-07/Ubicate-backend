package com.tulocal.backend.modules.auth.application.mapper;

import com.tulocal.backend.modules.auth.api.response.AuthResponse;
import com.tulocal.backend.modules.auth.domain.model.AuthUser;
import com.tulocal.backend.security.jwt.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthResponse toResponse(AuthUser user, TokenResponse tokens) {
        if (user == null) {
            return null;
        }

        AuthResponse response = new AuthResponse();
        
        if (tokens != null) {
            response.setToken(tokens.getAccessToken());
            response.setRefreshToken(tokens.getRefreshToken());
        }

        response.setUserId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRoleId(user.getRoleId());

        return response;
    }
}