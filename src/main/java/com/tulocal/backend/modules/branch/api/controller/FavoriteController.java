package com.tulocal.backend.modules.branch.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import com.tulocal.backend.modules.branch.application.usecase.AddFavoriteUseCase;
import com.tulocal.backend.modules.branch.application.usecase.GetMyFavoritesUseCase;
import com.tulocal.backend.modules.branch.application.usecase.RemoveFavoriteUseCase;
import com.tulocal.backend.security.jwt.TokenPayload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final AddFavoriteUseCase addFavoriteUseCase;
    private final RemoveFavoriteUseCase removeFavoriteUseCase;
    private final GetMyFavoritesUseCase getMyFavoritesUseCase;

    // AÑADIR A FAVORITOS
    @PostMapping("/{branchId}")
    public ResponseEntity<ApiResponse<Void>> add(
            @AuthenticationPrincipal TokenPayload tokenPayload, 
            @PathVariable UUID branchId) {
        
        UUID userId = UUID.fromString(tokenPayload.getUserId());
        addFavoriteUseCase.execute(userId, branchId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal añadida a favoritos correctamente", null));
    }

    // ELIMINAR DE FAVORITOS
    @DeleteMapping("/{branchId}")
    public ResponseEntity<ApiResponse<Void>> remove(
            @AuthenticationPrincipal TokenPayload tokenPayload, 
            @PathVariable UUID branchId) {
        
        UUID userId = UUID.fromString(tokenPayload.getUserId());
        removeFavoriteUseCase.execute(userId, branchId);
        
        return ResponseEntity.ok(ApiResponse.ok("Sucursal eliminada de favoritos correctamente", null));
    }

    // RECUPERAR MIS FAVORITOS
    @GetMapping("my-favorites")
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getMyFavorites(
            @AuthenticationPrincipal TokenPayload tokenPayload) {
        
        UUID userId = UUID.fromString(tokenPayload.getUserId());
        List<BranchResponse> response = getMyFavoritesUseCase.execute(userId);
        
        return ResponseEntity.ok(ApiResponse.ok("Tus sucursales favoritas", response));
    }
}