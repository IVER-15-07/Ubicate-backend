package com.tulocal.backend.modules.User.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.User.api.request.CreateUserRequest;
import com.tulocal.backend.modules.User.api.response.UserResponse;
import com.tulocal.backend.modules.User.application.mapper.UserMapper;
import com.tulocal.backend.modules.User.application.usecase.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userMapper.toResponse(createUserUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario creado correctamente", response));
    }
}