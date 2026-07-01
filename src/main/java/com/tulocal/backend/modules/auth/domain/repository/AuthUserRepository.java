package com.tulocal.backend.modules.auth.domain.repository;

import com.tulocal.backend.modules.auth.domain.model.AuthUser;
import java.util.Optional;


public interface AuthUserRepository {
    Optional<AuthUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    AuthUser save(AuthUser user);
}