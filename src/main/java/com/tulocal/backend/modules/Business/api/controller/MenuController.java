package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Business.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.Business.api.response.MenuResponse;
import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.CreateMenuUseCase;
import com.tulocal.backend.modules.Business.application.usecase.DeleteMenuUseCase;
import com.tulocal.backend.modules.Business.application.usecase.UpdateMenuUseCase;
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
public class MenuController {

    private final CreateMenuUseCase createMenuUseCase;
    private final UpdateMenuUseCase updateMenuUseCase;
    private final DeleteMenuUseCase deleteMenuUseCase;
    private final BusinessMapper businessMapper;

    @PostMapping("/{businessId}/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @PathVariable UUID businessId,
            @Valid @RequestBody CreateMenuRequest request) {
        MenuResponse response = businessMapper.toMenuResponse(createMenuUseCase.execute(businessId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menu creado correctamente", response));
    }

    @PutMapping("/{businessId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable UUID businessId,
            @PathVariable UUID menuId,
            @Valid @RequestBody CreateMenuRequest request) {
        MenuResponse response = businessMapper.toMenuResponse(updateMenuUseCase.execute(businessId, menuId, request));
        return ResponseEntity.ok(ApiResponse.ok("Menu actualizado correctamente", response));
    }

    @DeleteMapping("/{businessId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable UUID businessId,
            @PathVariable UUID menuId) {
        deleteMenuUseCase.execute(menuId);
        return ResponseEntity.ok(ApiResponse.ok("Menu dado de baja correctamente", null));
    }
    
}
