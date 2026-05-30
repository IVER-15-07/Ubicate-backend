package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.User.domain.model.User;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import com.tulocal.backend.modules.superAdmin.api.request.UpdateAdminAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateAdminAccountUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User execute(UUID userId, UpdateAdminAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la cuenta indicada"));

        String email = request.getEmail().trim();
        boolean emailChanged = !user.getEmail().equalsIgnoreCase(email);
        if (emailChanged && userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }

        user.setNombre(request.getNombre().trim());
        user.setEmail(email);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRoleId() != null) {
            user.setRoleId(request.getRoleId());
        }

        return userRepository.save(user);
    }
}
