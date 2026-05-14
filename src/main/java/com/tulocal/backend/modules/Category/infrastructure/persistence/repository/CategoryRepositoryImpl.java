package com.tulocal.backend.modules.Category.infrastructure.persistence.repository;

import com.tulocal.backend.modules.Category.domain.model.Category;
import com.tulocal.backend.modules.Category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setNombre(rs.getString("nombre"));
        category.setIcono(rs.getString("icono"));
        return category;
    };

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM category ORDER BY nombre";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }
}
