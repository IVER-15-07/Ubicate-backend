package com.tulocal.backend.modules.auth.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.auth.api.request.LoginRequest;
import com.tulocal.backend.modules.auth.api.response.AuthResponse;
import com.tulocal.backend.modules.auth.application.usecase.AuthenticateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticateUserUseCase authenticateUserUseCase;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse resp = authenticateUserUseCase.execute(request.getEmail(), request.getPassword());
		if (resp == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Credenciales inválidas"));
		return ResponseEntity.ok(ApiResponse.ok("Autenticado", resp));
	}

}
