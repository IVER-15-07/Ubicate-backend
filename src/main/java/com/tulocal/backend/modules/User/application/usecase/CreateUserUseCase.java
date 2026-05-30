package com.tulocal.backend.modules.User.application.usecase;

import com.tulocal.backend.modules.User.api.request.CreateUserRequest;
import com.tulocal.backend.modules.User.domain.model.User;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(CreateUserRequest request) {
        String email = request.getEmail().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        User user = new User();
        user.setNombre(request.getNombre().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(request.getRoleId() != null ? request.getRoleId() : 2);
        user.setCreadoEn(LocalDateTime.now());

        return userRepository.save(user);
    }
}