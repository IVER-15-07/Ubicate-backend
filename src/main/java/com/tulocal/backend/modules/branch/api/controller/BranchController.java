package com.tulocal.backend.modules.branch.api.controller;
import com.tulocal.backend.common.response.ApiResponse;

import com.tulocal.backend.modules.branch.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import com.tulocal.backend.modules.branch.application.mapper.BranchMapper;
import com.tulocal.backend.modules.branch.application.usecase.CreateBranchUseCase;




import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {
    
    private final CreateBranchUseCase createBranchUseCase;
    private final BranchMapper branchMapper;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        BranchResponse response = branchMapper.toResponse(createBranchUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal creada correctamente", response));
    }
}
