package com.tulocal.backend.modules.User.domain.repository;

import com.tulocal.backend.modules.User.domain.model.User;

public interface UserRepository {
    boolean existsByEmail(String email);
    User save(User user);
}