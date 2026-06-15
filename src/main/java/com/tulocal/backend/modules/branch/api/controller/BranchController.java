package com.tulocal.backend.modules.branch.api.controller;

import com.tulocal.backend.common.response.ApiResponse;

import com.tulocal.backend.modules.branch.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import com.tulocal.backend.modules.branch.application.mapper.BranchMapper;
import com.tulocal.backend.modules.branch.application.usecase.CreateBranchUseCase;
import com.tulocal.backend.security.jwt.TokenPayload;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;
import java.util.UUID;
import java.util.List;
import com.tulocal.backend.modules.branch.application.usecase.GetMyBranchesUseCase;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final BranchMapper branchMapper;
    private final GetMyBranchesUseCase getMyBranchesUseCase;
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(@Valid @RequestBody CreateBranchRequest request,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var branchCreated = createBranchUseCase.execute(request, ownerUserId);

        BranchResponse response = branchMapper.toResponse(branchCreated);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal creada correctamente", response));
    }

    @GetMapping("/my-branches")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getMyBranches(
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        List<BranchResponse> response = getMyBranchesUseCase.execute(ownerUserId)
                .stream()
                .map(branchMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Tus sucursales", response));
    }
}
