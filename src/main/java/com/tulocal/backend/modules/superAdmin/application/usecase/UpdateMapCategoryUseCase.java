package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.superAdmin.api.request.UpdateMapCategoryRequest;
import com.tulocal.backend.modules.superAdmin.domain.model.MapCategory;
import com.tulocal.backend.modules.superAdmin.domain.repository.MapCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateMapCategoryUseCase {

    private final MapCategoryRepository mapCategoryRepository;

    @Transactional
    public MapCategory execute(Integer categoryId, UpdateMapCategoryRequest request) {
        MapCategory category = mapCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la categoría indicada"));

        category.setNombre(request.getNombre().trim());
        category.setIcono(request.getIcono());
        return mapCategoryRepository.save(category);
    }
}
