package com.tulocal.backend.modules.menu.api.controller;

import com.tulocal.backend.common.response.ApiResponse;
import com.tulocal.backend.modules.menu.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.menu.api.request.AddMenuItemRequest;
import com.tulocal.backend.modules.menu.api.response.MenuResponse;
import com.tulocal.backend.modules.menu.api.response.MenuItemResponse;
import com.tulocal.backend.modules.menu.application.mapper.MenuMapper;
import com.tulocal.backend.modules.menu.application.mapper.MenuItemMapper;
import com.tulocal.backend.modules.menu.application.usecase.CreateMenuUseCase;
import com.tulocal.backend.modules.menu.application.usecase.AddMenuItemUseCase;
import com.tulocal.backend.modules.menu.application.usecase.GetMyMenusUseCase;
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
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final CreateMenuUseCase createMenuUseCase;
    private final AddMenuItemUseCase addMenuItemUseCase;
    private final GetMyMenusUseCase getMyMenusUseCase;
    private final MenuMapper menuMapper;
    private final MenuItemMapper menuItemMapper;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody CreateMenuRequest request,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var result = createMenuUseCase.execute(request, ownerUserId);

        var response = menuMapper.toResponse(result.menu(), result.items());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menú creado correctamente", response));
    }

    @PostMapping("/items/add")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> addItems(
            @Valid @RequestBody AddMenuItemRequest request,
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());
        var items = addMenuItemUseCase.execute(request, ownerUserId);

        var response = items.stream().map(menuItemMapper::toResponse).toList();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Platillos agregados correctamente", response));
    }

    @GetMapping("/my-menus")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMyMenus(
            @AuthenticationPrincipal TokenPayload tokenPayload) {

        UUID ownerUserId = UUID.fromString(tokenPayload.getUserId());

        List<MenuResponse> response = getMyMenusUseCase.execute(ownerUserId)
                .stream()
                .map(r -> menuMapper.toResponse(r.menu(), r.items()))
                .toList();

        return ResponseEntity.ok(ApiResponse.ok("Tus menús", response));
    }
}