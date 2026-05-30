package com.tulocal.backend.modules.superAdmin.application.usecase;

import com.tulocal.backend.modules.superAdmin.domain.model.MapCategory;
import com.tulocal.backend.modules.superAdmin.domain.repository.MapCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListMapCategoriesUseCase {

    private final MapCategoryRepository mapCategoryRepository;

    public List<MapCategory> execute() {
        return mapCategoryRepository.findAllByOrderByNombreAsc();
    }
}
