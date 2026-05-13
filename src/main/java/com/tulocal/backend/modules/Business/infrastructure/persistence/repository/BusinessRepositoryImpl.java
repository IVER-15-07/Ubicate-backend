package com.tulocal.backend.modules.Business.infrastructure.persistence.repository;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class BusinessRepositoryImpl implements BusinessRepository{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Business> businessRowMapper = (rs, rowNum) -> {
        Business business = new Business();
        business.setId(UUID.fromString(rs.getString("id")));
        business.setOwnerUserId(UUID.fromString(rs.getString("owner_user_id")));
        business.setNombre(rs.getString("nombre"));
        business.setDescripcion(rs.getString("descripcion"));
        business.setCategoryId(rs.getInt("category_id"));
        business.setLogoUrl(rs.getString("logo_url"));
        business.setBannerUrl(rs.getString("banner_url"));
        business.setIsActive(rs.getBoolean("is_active"));
        business.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
        return business;
    };

    @Override
    public List<Business> findAll() {
         String sql = "SELECT * FROM business WHERE is_active = true";
        return jdbcTemplate.query(sql, businessRowMapper);
    }

    @Override
    public Business findById(UUID id) {
        String sql = "SELECT * FROM business WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, businessRowMapper, id);
    }

    @Override
    public List<Business> findByNombreContaining(String nombre) {
        String sql = "SELECT * FROM business WHERE is_active = true AND LOWER(nombre) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, businessRowMapper, "%" + nombre + "%");
    }

    @Override
    public List<Business> searchByNombreOrDescripcion(String searchTerm) {
        String sql = "SELECT * FROM business WHERE is_active = true AND " +
                "(unaccent(LOWER(nombre)) LIKE unaccent(LOWER(?)) OR " +
                "unaccent(LOWER(descripcion)) LIKE unaccent(LOWER(?)))";
        String searchPattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, businessRowMapper, searchPattern, searchPattern);
    }

    @Override
    public List<Business> findByCategoryId(Integer categoryId) {
        String sql = "SELECT * FROM business WHERE is_active = true AND category_id = ?";
        return jdbcTemplate.query(sql, businessRowMapper, categoryId);
    }
    
}
    

