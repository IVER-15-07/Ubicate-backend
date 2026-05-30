package com.tulocal.backend.modules.Business.api.controller;

import com.tulocal.backend.modules.Business.api.request.BusinessRequest;
import com.tulocal.backend.modules.Business.api.request.CreateBranchRequest;
import com.tulocal.backend.modules.Business.api.request.CreateMenuRequest;
import com.tulocal.backend.modules.Business.application.mapper.BusinessMapper;
import com.tulocal.backend.modules.Business.application.usecase.CreateBranchUseCase;
import com.tulocal.backend.modules.Business.application.usecase.CreateBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.CreateMenuItemUseCase;
import com.tulocal.backend.modules.Business.application.usecase.CreateMenuUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessByIdUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetAllBusinessUseCase;
import com.tulocal.backend.modules.Business.application.usecase.SearchBusinessByNameUseCase;
import com.tulocal.backend.modules.Business.application.usecase.GetBusinessByCategoryUseCase;
import com.tulocal.backend.modules.Business.api.response.BranchResponse;
import com.tulocal.backend.modules.Business.api.response.BusinessResponse;
import com.tulocal.backend.modules.Business.api.response.MenuResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tulocal.backend.common.ApiResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final CreateBusinessUseCase createBusinessUseCase;
    private final CreateBranchUseCase createBranchUseCase;
    private final CreateMenuUseCase createMenuUseCase;
    private final GetAllBusinessUseCase getAllBusinessUseCase;
    private final GetAllBusinessByIdUseCase getAllBusinessByIdUseCase;
    private final SearchBusinessByNameUseCase searchBusinessByNameUseCase;
    private final GetBusinessByCategoryUseCase getBusinessByCategoryUseCase;
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final BusinessMapper businessMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessResponse>> createBusiness(@Valid @RequestBody BusinessRequest request) {
        BusinessResponse response = businessMapper.toResponse(createBusinessUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Negocio creado correctamente", response));
    }

    @PostMapping("/{businessId}/branches")
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @PathVariable UUID businessId,
            @Valid @RequestBody CreateBranchRequest request) {
        BranchResponse response = businessMapper.toBranchResponse(createBranchUseCase.execute(businessId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sucursal creada correctamente", response));
    }

    @PostMapping("/{businessId}/menus/{menuId}/items")
    public ResponseEntity<ApiResponse<com.tulocal.backend.modules.Business.api.response.MenuItemResponse>> createMenuItem(
            @PathVariable UUID businessId,
            @PathVariable UUID menuId,
            @Valid @RequestBody com.tulocal.backend.modules.Business.api.request.CreateMenuItemRequest request) {
        request.setMenuId(menuId);
        com.tulocal.backend.modules.Business.domain.model.MenuItem created = createMenuItemUseCase.execute(businessId,
                request);
        com.tulocal.backend.modules.Business.api.response.MenuItemResponse response = businessMapper
                .toMenuItemResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menu item creado correctamente", response));
    }

    @PostMapping("/{businessId}/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @PathVariable UUID businessId,
            @Valid @RequestBody CreateMenuRequest request) {
        MenuResponse response = businessMapper.toMenuResponse(createMenuUseCase.execute(businessId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Menu creado correctamente", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> getAllBusinesses() {
        List<BusinessResponse> responses = getAllBusinessUseCase.execute()
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Negocios obtenidos correctamente", responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessResponse>> getBusinessById(@PathVariable UUID id) {
        BusinessResponse response = businessMapper.toResponse(getAllBusinessByIdUseCase.execute(id));
        return ResponseEntity.ok(ApiResponse.ok("Negocio obtenido correctamente", response));
    }

    @GetMapping("/Buscar")
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> searchBusinessByName(@RequestParam String nombre) {
        List<BusinessResponse> responses = searchBusinessByNameUseCase.execute(nombre)
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Negocios encontrados", responses));
    }

    @GetMapping("/categoria/{categoryId}")
    public ResponseEntity<ApiResponse<List<BusinessResponse>>> getBusinessByCategory(@PathVariable Integer categoryId) {
        List<BusinessResponse> responses = getBusinessByCategoryUseCase.execute(categoryId)
                .stream()
                .map(businessMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Negocios de la categoría obtenidos correctamente", responses));
    }

}
