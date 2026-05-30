package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Business.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.Business.api.request.UpdateBranchRequest;
import com.tulocal.backend.modules.Business.api.response.BranchResponse;
import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.CreateBranchUseCase;
import com.tulocal.backend.modules.Business.application.usecase.DeleteBranchUseCase;
import com.tulocal.backend.modules.Business.application.usecase.UpdateBranchUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final UpdateBranchUseCase updateBranchUseCase;
    private final DeleteBranchUseCase deleteBranchUseCase;
    private final BusinessMapper businessMapper;

    @PostMapping("/{businessId}/branches")
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @PathVariable UUID businessId,
            @Valid @RequestBody CreateBranchRequest request) {
        BranchResponse response = businessMapper.toBranchResponse(createBranchUseCase.execute(businessId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal creada correctamente", response));
    }

    @PutMapping("/{businessId}/branches/{branchId}")
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranch(
            @PathVariable UUID businessId,
            @PathVariable UUID branchId,
            @Valid @RequestBody UpdateBranchRequest request) {
        BranchResponse response = businessMapper.toBranchResponse(updateBranchUseCase.execute(businessId, branchId, request));
        return ResponseEntity.ok(ApiResponse.ok("Sucursal actualizada correctamente", response));
    }

    @DeleteMapping("/{businessId}/branches/{branchId}")
    public ResponseEntity<ApiResponse<Void>> deleteBranch(@PathVariable UUID businessId,
                                                          @PathVariable UUID branchId) {
        deleteBranchUseCase.execute(branchId);
        return ResponseEntity.ok(ApiResponse.ok("Sucursal dada de baja correctamente", null));
    }
}
