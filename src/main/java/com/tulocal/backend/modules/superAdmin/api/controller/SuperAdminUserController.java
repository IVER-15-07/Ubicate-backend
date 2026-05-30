package com.tulocal.backend.modules.superAdmin.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.User.api.response.UserResponse;
import com.tulocal.backend.modules.User.application.mapper.UserMapper;
import com.tulocal.backend.modules.superAdmin.api.request.CreateAdminAccountRequest;
import com.tulocal.backend.modules.superAdmin.api.request.UpdateAdminAccountRequest;
import com.tulocal.backend.modules.superAdmin.application.usecase.CreateAdminAccountUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.DeleteAdminAccountUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.ListAdminAccountsUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.UpdateAdminAccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/super-admin/admins")
@RequiredArgsConstructor
public class SuperAdminUserController {

    private final ListAdminAccountsUseCase listAdminAccountsUseCase;
    private final CreateAdminAccountUseCase createAdminAccountUseCase;
    private final UpdateAdminAccountUseCase updateAdminAccountUseCase;
    private final DeleteAdminAccountUseCase deleteAdminAccountUseCase;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> listAdmins() {
        List<UserResponse> response = listAdminAccountsUseCase.execute().stream()
                .map(userMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Cuentas de administrador obtenidas correctamente", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createAdmin(@Valid @RequestBody CreateAdminAccountRequest request) {
        UserResponse response = userMapper.toResponse(createAdminAccountUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cuenta de administrador creada correctamente", response));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateAdmin(@PathVariable UUID userId,
                                                                 @Valid @RequestBody UpdateAdminAccountRequest request) {
        UserResponse response = userMapper.toResponse(updateAdminAccountUseCase.execute(userId, request));
        return ResponseEntity.ok(ApiResponse.ok("Cuenta de administrador actualizada correctamente", response));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable UUID userId) {
        deleteAdminAccountUseCase.execute(userId);
        return ResponseEntity.ok(ApiResponse.ok("Cuenta de administrador eliminada correctamente", null));
    }
}
