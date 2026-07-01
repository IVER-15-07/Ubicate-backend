package com.tulocal.backend.modules.auth.infrastructure.persistence;

import com.tulocal.backend.modules.auth.domain.model.AuthUser;
import com.tulocal.backend.modules.auth.domain.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUserRepositoryImpl implements AuthUserRepository {

    private final SpringDataAuthUserRepository springDataRepository;

    @Override
    public Optional<AuthUser> findByEmailIgnoreCase(String email) {
        return springDataRepository.findByEmailIgnoreCase(email)
                .map(this::mapToDomain);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return springDataRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public AuthUser save(AuthUser user) {
        AuthUserEntity entity = mapToEntity(user);
        AuthUserEntity savedEntity = springDataRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private AuthUser mapToDomain(AuthUserEntity entity) {
        if (entity == null) return null;
        AuthUser user = new AuthUser();
        user.setId(entity.getId());
        user.setNombre(entity.getNombre());
        user.setEmail(entity.getEmail());
        user.setPasswordHash(entity.getPasswordHash());
        user.setRoleId(entity.getRoleId());
        user.setCreadoEn(entity.getCreadoEn());
        return user;
    }

    private AuthUserEntity mapToEntity(AuthUser user) {
        if (user == null) return null;
        AuthUserEntity entity = new AuthUserEntity();
        entity.setId(user.getId());
        entity.setNombre(user.getNombre());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRoleId(user.getRoleId());
        entity.setCreadoEn(user.getCreadoEn());
        return entity;
    }
}