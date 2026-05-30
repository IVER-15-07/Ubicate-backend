package com.tulocal.backend.modules.User.domain.repository;

import com.tulocal.backend.modules.User.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailIgnoreCase(String email);
}