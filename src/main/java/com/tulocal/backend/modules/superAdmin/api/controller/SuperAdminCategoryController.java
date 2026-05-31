package com.tulocal.backend.modules.superAdmin.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Category.api.response.CategoryResponse;
import com.tulocal.backend.modules.superAdmin.api.request.CreateMapCategoryRequest;
import com.tulocal.backend.modules.superAdmin.api.request.UpdateMapCategoryRequest;
import com.tulocal.backend.modules.superAdmin.application.usecase.CreateMapCategoryUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.DeleteMapCategoryUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.ListMapCategoriesUseCase;
import com.tulocal.backend.modules.superAdmin.application.usecase.UpdateMapCategoryUseCase;
import com.tulocal.backend.modules.superAdmin.domain.model.MapCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin/categories")
@RequiredArgsConstructor
public class SuperAdminCategoryController {

    private final ListMapCategoriesUseCase listMapCategoriesUseCase;
    private final CreateMapCategoryUseCase createMapCategoryUseCase;
    private final UpdateMapCategoryUseCase updateMapCategoryUseCase;
    private final DeleteMapCategoryUseCase deleteMapCategoryUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories() {
        List<CategoryResponse> response = listMapCategoriesUseCase.execute().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Categorías de mapa obtenidas correctamente", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateMapCategoryRequest request) {
        CategoryResponse response = toResponse(createMapCategoryUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Categoría de mapa creada correctamente", response));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Integer categoryId,
            @Valid @RequestBody UpdateMapCategoryRequest request) {
        CategoryResponse response = toResponse(updateMapCategoryUseCase.execute(categoryId, request));
        return ResponseEntity.ok(ApiResponse.ok("Categoría de mapa actualizada correctamente", response));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer categoryId) {
        deleteMapCategoryUseCase.execute(categoryId);
        return ResponseEntity.ok(ApiResponse.ok("Categoría de mapa eliminada correctamente", null));
    }

    private CategoryResponse toResponse(MapCategory category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setNombre(category.getNombre());
        response.setIcono(category.getIcono());
        return response;
    }
}
