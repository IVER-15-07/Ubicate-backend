package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.superAdmin.api.request.CreateMapCategoryRequest;
import com.tulocal.backend.modules.superAdmin.domain.model.MapCategory;
import com.tulocal.backend.modules.superAdmin.domain.repository.MapCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMapCategoryUseCase {

    private final MapCategoryRepository mapCategoryRepository;

    public MapCategory execute(CreateMapCategoryRequest request) {
        MapCategory category = new MapCategory();
        category.setNombre(request.getNombre().trim());
        category.setIcono(request.getIcono());
        return mapCategoryRepository.save(category);
    }
}
