package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteAdminAccountUseCase {

    private final UserRepository userRepository;

    public void execute(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("No existe la cuenta indicada");
        }
        userRepository.deleteById(userId);
    }
}
