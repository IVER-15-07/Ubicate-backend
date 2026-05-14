package com.tulocal.backend.modules.Category.application.mapper;

import com.tulocal.backend.modules.Category.api.response.CategoryResponse;
import com.tulocal.backend.modules.Category.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setNombre(category.getNombre());
        response.setIcono(category.getIcono());
        return response;
    }
}
