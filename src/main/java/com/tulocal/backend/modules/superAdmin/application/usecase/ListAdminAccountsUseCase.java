package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.User.domain.model.User;
import com.tulocal.backend.modules.User.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListAdminAccountsUseCase {

    public static final int ADMIN_ROLE_ID = 2;

    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.findByRoleIdOrderByCreadoEnDesc(ADMIN_ROLE_ID);
    }
}
