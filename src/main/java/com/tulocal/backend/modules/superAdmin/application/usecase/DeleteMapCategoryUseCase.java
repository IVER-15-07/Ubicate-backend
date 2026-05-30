package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.superAdmin.domain.repository.MapCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteMapCategoryUseCase {

    private final MapCategoryRepository mapCategoryRepository;

    public void execute(Integer categoryId) {
        if (!mapCategoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("No existe la categoría indicada");
        }
        mapCategoryRepository.deleteById(categoryId);
    }
}
