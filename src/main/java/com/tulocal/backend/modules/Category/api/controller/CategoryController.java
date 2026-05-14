package com.tulocal.backend.modules.Category.api.controller;

import com.tulocal.backend.common.ApiResponse;
import com.tulocal.backend.modules.Category.api.response.CategoryResponse;
import com.tulocal.backend.modules.Category.application.mapper.CategoryMapper;
import com.tulocal.backend.modules.Category.application.usecase.GetAllCategoriesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final GetAllCategoriesUseCase getAllCategoriesUseCase;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> responses = getAllCategoriesUseCase.execute()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Categorías obtenidas correctamente", responses));
    }
}
