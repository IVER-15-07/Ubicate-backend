package com.tulocal.backend.modules.menu.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.menu.api.request.AssignMenuToBranchRequest;
import com.tulocal.backend.modules.menu.api.response.BranchMenuResponse;
import com.tulocal.backend.modules.menu.application.mapper.BranchMenuMapper;
import com.tulocal.backend.modules.menu.application.usecase.AssignMenuToBranchUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetMenusByBranchUseCase;
import com.tulocal.backend.modules.menu.application.usecase.ToggleBranchMenuUseCase;
import com.tulocal.backend.security.jwt.TokenPayload;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branch-menus")
@RequiredArgsConstructor
public class BranchMenuController {

    private final AssignMenuToBranchUseCase assignMenuToBranchUseCase;
    private final GetMenusByBranchUseCase getMenusByBranchUseCase;
    private final ToggleBranchMenuUseCase toggleBranchMenuUseCase;
    private final BranchMenuMapper branchMenuMapper;

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<BranchMenuResponse>> assign(
            @Valid @RequestBody AssignMenuToBranchRequest request,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var branchMenu = assignMenuToBranchUseCase.execute(request, ownerUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menú asignado a la sucursal", branchMenuMapper.toResponse(branchMenu)));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<BranchMenuResponse>>> getByBranch(
            @PathVariable UUID branchId) {

        var response = getMenusByBranchUseCase.execute(branchId)
                .stream()
                .map(branchMenuMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Menús de la sucursal", response));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<BranchMenuResponse>> toggle(
            @PathVariable UUID id,
            @RequestParam boolean isActive) {

        var branchMenu = toggleBranchMenuUseCase.execute(id, isActive);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado", branchMenuMapper.toResponse(branchMenu)));
    }
}