package com.tulocal.backend.modules.Category.domain.repository;

import com.tulocal.backend.modules.Category.domain.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
}
