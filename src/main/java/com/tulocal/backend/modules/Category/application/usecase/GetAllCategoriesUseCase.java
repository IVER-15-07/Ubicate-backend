package com.tulocal.backend.modules.Category.application.usecase;

import com.tulocal.backend.modules.Category.domain.model.Category;
import com.tulocal.backend.modules.Category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public List<Category> execute() {
        return categoryRepository.findAll();
    }
}
