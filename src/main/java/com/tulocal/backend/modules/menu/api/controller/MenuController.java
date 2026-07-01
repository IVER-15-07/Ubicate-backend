package com.tulocal.backend.modules.menu.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.menu.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.menu.api.response.MenuResponse;
import com.tulocal.backend.modules.menu.application.mapper.MenuMapper;
import com.tulocal.backend.modules.menu.application.usecase.CreateMenuUseCase;
import com.tulocal.backend.modules.menu.application.usecase.DeleteMenuUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetMyMenusUseCase;
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
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final CreateMenuUseCase createMenuUseCase;
    private final GetMyMenusUseCase getMyMenusUseCase;
    private final DeleteMenuUseCase deleteMenuUseCase;
    private final MenuMapper menuMapper;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @ModelAttribute CreateMenuRequest request,
            @RequestParam(required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        var result = createMenuUseCase.execute(request, ownerUserId, photos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Menú creado correctamente",
                        menuMapper.toResponse(result.menu(), result.items())));
    }

    @GetMapping("/my-menus")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMyMenus(
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        List<MenuResponse> response = getMyMenusUseCase.execute(ownerUserId)
                .stream()
                .map(menu -> menuMapper.toResponse(menu.menu(), menu.items()))
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Tus menús", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @PathVariable UUID id,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        deleteMenuUseCase.execute(id, ownerUserId);

        return ResponseEntity.ok(
                ApiResponse.ok("Menú eliminado correctamente", null));
    }
}