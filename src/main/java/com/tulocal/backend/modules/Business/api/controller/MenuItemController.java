package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Business.api.request.CreateMenuItemRequest;
import com.tulocal.backend.modules.Business.api.response.MenuItemResponse;
import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.CreateMenuItemUseCase;
import com.tulocal.backend.modules.Business.application.usecase.DeleteMenuItemUseCase;
import com.tulocal.backend.modules.Business.application.usecase.UpdateMenuItemUseCase;
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
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final BusinessMapper businessMapper;

    @PostMapping("/{businessId}/menus/{menuId}/items")
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(
            @PathVariable UUID businessId,
            @PathVariable UUID menuId,
            @Valid @RequestBody CreateMenuItemRequest request) {
        request.setMenuId(menuId);
        MenuItemResponse response = businessMapper
                .toMenuItemResponse(createMenuItemUseCase.execute(businessId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menu item creado correctamente", response));
    }

    @PutMapping("/{businessId}/menus/{menuId}/items/{itemId}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(
            @PathVariable UUID businessId,
            @PathVariable UUID menuId,
            @PathVariable UUID itemId,
            @Valid @RequestBody CreateMenuItemRequest request) {
        request.setMenuId(menuId);
        MenuItemResponse response = businessMapper
                .toMenuItemResponse(updateMenuItemUseCase.execute(businessId, menuId, itemId, request));
        return ResponseEntity.ok(ApiResponse.ok("Menu item actualizado correctamente", response));
    }

    @DeleteMapping("/{businessId}/menus/{menuId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable UUID businessId,
            @PathVariable UUID menuId,
            @PathVariable UUID itemId) {
        deleteMenuItemUseCase.execute(itemId);
        return ResponseEntity.ok(ApiResponse.ok("Menu item dado de baja correctamente", null));
    }
}
