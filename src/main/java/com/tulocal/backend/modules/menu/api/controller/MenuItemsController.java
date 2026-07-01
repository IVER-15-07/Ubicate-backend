package com.tulocal.backend.modules.menu.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.menu.api.request.AddMenuItemRequest;
import com.tulocal.backend.modules.menu.api.request.UpdateMenuItemRequest;
import com.tulocal.backend.modules.menu.api.response.MenuItemResponse;
import com.tulocal.backend.modules.menu.application.mapper.MenuItemMapper;
import com.tulocal.backend.modules.menu.application.usecase.AddMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.DeleteMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetMenuItemByIdUseCase;
import com.tulocal.backend.modules.menu.application.usecase.UpdateMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetItemsByMenuUseCase;
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
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemsController {

    private final AddMenuItemUseCase addMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final GetItemsByMenuUseCase getItemsByMenuUseCase;
    private final MenuItemMapper menuItemMapper;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> addItems(
            @Valid @ModelAttribute AddMenuItemRequest request,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        var items = addMenuItemUseCase.execute(request, ownerUserId, photos);

        List<MenuItemResponse> response = items.stream()
                .map(menuItemMapper::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Platillos agregados correctamente",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItem(
            @PathVariable UUID id,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        var item = getMenuItemByIdUseCase.execute(id, ownerUserId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Platillo encontrado",
                        menuItemMapper.toResponse(item)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateItem(
            @PathVariable UUID id,
            @Valid @ModelAttribute UpdateMenuItemRequest request,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @AuthenticationPrincipal TokenPayload tokenPayload) throws Exception {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        var item = updateMenuItemUseCase.execute(id, request, photo, ownerUserId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Platillo actualizado correctamente",
                        menuItemMapper.toResponse(item)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable UUID id,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        deleteMenuItemUseCase.execute(id, ownerUserId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Platillo eliminado correctamente",
                        null));
    }

    @GetMapping("/by-menu/{menuId}")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByMenu(
            @PathVariable UUID menuId,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        List<MenuItemResponse> response = getItemsByMenuUseCase.execute(menuId, ownerUserId)
                .stream()
                .map(menuItemMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Items del menú", response));
    }
}