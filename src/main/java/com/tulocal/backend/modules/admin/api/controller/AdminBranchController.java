package com.tulocal.backend.modules.admin.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.admin.api.response.AdminBranchResponse;
import com.tulocal.backend.modules.admin.application.usecase.ApproveBranchUseCase;
import com.tulocal.backend.modules.admin.application.usecase.GetActiveBranchesUseCase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminBranchController {

    private final ApproveBranchUseCase approveBranchUseCase;
    private final GetActiveBranchesUseCase getActiveBranchesUseCase;

    @GetMapping("/branches/active")
    public ResponseEntity<ApiResponse<List<AdminBranchResponse>>> getActiveBranches() {
        List<AdminBranchResponse> response = getActiveBranchesUseCase.execute().stream()
                .map(branch -> {
                    AdminBranchResponse item = new AdminBranchResponse();
                    item.setId(branch.getId());
                    item.setBusinessId(branch.getBusinessId());
                    item.setNombre(branch.getNombre());
                    item.setIsActive(branch.getIsActive());
                    item.setCreadoEn(branch.getCreadoEn());
                    return item;
                })
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Sucursales activas", response));
    }

    @PostMapping("/branches/{branchId}/approve")
    public ResponseEntity<ApiResponse<AdminBranchResponse>> approveBranch(@PathVariable UUID branchId) {
        var branch = approveBranchUseCase.execute(branchId);

        AdminBranchResponse response = new AdminBranchResponse();
        response.setId(branch.getId());
        response.setBusinessId(branch.getBusinessId());
        response.setNombre(branch.getNombre());
        response.setIsActive(branch.getIsActive());
        response.setCreadoEn(branch.getCreadoEn());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("Sucursal aprobada correctamente", response));
    }
}
