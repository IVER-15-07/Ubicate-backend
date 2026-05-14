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
public class BusinessRepositoryImpl implements BusinessRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT = "SELECT b.*, loc.lat AS location_lat, loc.lng AS location_lng, loc.direccion AS location_direccion "
            +
            "FROM business b " +
            "LEFT JOIN LATERAL (" +
            "  SELECT l.lat, l.lng, l.direccion " +
            "  FROM locations l " +
            "  WHERE l.business_id = b.id " +
            "  ORDER BY l.creado_en DESC " +
            "  LIMIT 1" +
            ") loc ON true ";

    private final RowMapper<Business> businessRowMapper = (rs, rowNum) -> {
        Business business = new Business();
        business.setId(UUID.fromString(rs.getString("id")));
        business.setOwnerUserId(UUID.fromString(rs.getString("owner_user_id")));
        business.setNombre(rs.getString("nombre"));
        business.setDescripcion(rs.getString("descripcion"));
        business.setCategoryId(rs.getInt("category_id"));
        business.setLogoUrl(rs.getString("logo_url"));
        business.setBannerUrl(rs.getString("banner_url"));

        business.setLat(
                rs.getDouble("location_lat"));

        business.setLng(
                rs.getDouble("location_lng"));

        business.setDireccion(
                rs.getString("location_direccion"));

        business.setIsActive(rs.getBoolean("is_active"));
        business.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
        return business;
    };

    @Override
    public List<Business> findAll() {
        String sql = BASE_SELECT + "WHERE b.is_active = true";
        return jdbcTemplate.query(sql, businessRowMapper);
    }

    @Override
    public Business findById(UUID id) {
        String sql = BASE_SELECT + "WHERE b.id = ?";
        return jdbcTemplate.queryForObject(sql, businessRowMapper, id);
    }

    @Override
    public List<Business> findByNombreContaining(String nombre) {
        String sql = BASE_SELECT + "WHERE b.is_active = true AND LOWER(b.nombre) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, businessRowMapper, "%" + nombre + "%");
    }

    @Override
    public List<Business> searchByNombreOrDescripcion(String searchTerm) {
        String sql = BASE_SELECT + "WHERE b.is_active = true AND " +
                "(unaccent(LOWER(b.nombre)) LIKE unaccent(LOWER(?)) OR " +
                "unaccent(LOWER(b.descripcion)) LIKE unaccent(LOWER(?)))";
        String searchPattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, businessRowMapper, searchPattern, searchPattern);
    }

    @Override
    public List<Business> findByCategoryId(Integer categoryId) {
        String sql = BASE_SELECT + "WHERE b.is_active = true AND b.category_id = ?";
        return jdbcTemplate.query(sql, businessRowMapper, categoryId);
    }

}
