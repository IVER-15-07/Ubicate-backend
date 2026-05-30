package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.User.domain.model.User;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import com.tulocal.backend.modules.superAdmin.api.request.CreateAdminAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateAdminAccountUseCase {

    public static final int ADMIN_ROLE_ID = 1;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(CreateAdminAccountRequest request) {
        String email = request.getEmail().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }

        User user = new User();
        user.setNombre(request.getNombre().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(request.getRoleId() != null ? request.getRoleId() : ADMIN_ROLE_ID);
        user.setCreadoEn(LocalDateTime.now());

        return userRepository.save(user);
    }
}
