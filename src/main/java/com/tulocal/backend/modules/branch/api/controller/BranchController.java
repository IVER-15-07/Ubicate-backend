package com.tulocal.backend.modules.branch.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.branch.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.branch.api.request.UpdateBranchRequest;
import com.tulocal.backend.modules.branch.api.response.BranchMapPointResponse;
import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import com.tulocal.backend.modules.branch.application.mapper.BranchMapper;
import com.tulocal.backend.modules.branch.application.usecase.CreateBranchUseCase;
import com.tulocal.backend.modules.branch.application.usecase.DeleteBranchUseCase;
import com.tulocal.backend.modules.branch.application.usecase.GetMyBranchesUseCase;
import com.tulocal.backend.modules.branch.application.usecase.SearchBranchesUseCase;
import com.tulocal.backend.modules.branch.application.usecase.UpdateBranchUseCase;
import com.tulocal.backend.modules.branch.application.usecase.GetAllActiveBranchesUseCase;
import com.tulocal.backend.security.jwt.TokenPayload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final GetMyBranchesUseCase getMyBranchesUseCase;
    private final UpdateBranchUseCase updateBranchUseCase;
    private final DeleteBranchUseCase deleteBranchUseCase;
    private final BranchMapper branchMapper;
    private final GetAllActiveBranchesUseCase getAllActiveBranchesUseCase;
    private final SearchBranchesUseCase searchBranchesUseCase;

    // multipart/form-data: campos del form + archivos en la misma llamada
    // CREAR SUCURSAL
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @Valid @ModelAttribute CreateBranchRequest request,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile banner,
            @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var branch = createBranchUseCase.execute(request, ownerUserId, logo, banner);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal creada correctamente", branchMapper.toResponse(branch)));
    }

    // RECUPERAR MIS SUCURSALES DEL USUARIO LOGUEADO
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

    // ACTUALIZAR SUCURSAL

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranch(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateBranchRequest request,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile banner,
            @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var updated = updateBranchUseCase.execute(id, request, ownerUserId, logo, banner);

        return ResponseEntity
                .ok(ApiResponse.ok("Sucursal actualizada correctamente", branchMapper.toResponse(updated)));
    }

    // ELIMINAR SUCURSAL
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBranch(
            @PathVariable UUID id,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        deleteBranchUseCase.execute(id, ownerUserId);

        return ResponseEntity.ok(ApiResponse.ok("Sucursal eliminada correctamente", null));
    }

    // OBTENER TODAS LAS SUCURSALES ACTIVAS (para el mapa)
    @GetMapping("/public/all-active")
    public ResponseEntity<ApiResponse<List<BranchMapPointResponse>>> getAllActiveBranches() {
        List<BranchMapPointResponse> response = getAllActiveBranchesUseCase.execute();

        return ResponseEntity.ok(ApiResponse.ok("Sucursales activas", response));
    }

    // BUSCAR SUCURSALES POR NOMBRE (para el mapa)

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<List<BranchMapPointResponse>>> searchBranches(
            @RequestParam(name = "q") String query) {
        List<BranchMapPointResponse> response = searchBranchesUseCase.execute(query);
        return ResponseEntity.ok(ApiResponse.ok("Resultados de búsqueda", response));
    }
}