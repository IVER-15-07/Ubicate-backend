package com.tulocal.backend.modules.Business.infrastructure.persistence.repository;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.domain.model.Location;
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

    private static final String BASE_SELECT = "SELECT DISTINCT b.id, b.owner_user_id, b.nombre, b.descripcion, " +
            "b.category_id, b.logo_url, b.banner_url, b.is_active, b.creado_en, " +
            "c.nombre AS category_nombre " +
            "FROM business b " +
            "LEFT JOIN category c ON c.id = b.category_id ";

        private static final String SELECT_WITH_BRANCHES = "SELECT b.id, b.owner_user_id, b.nombre, b.descripcion, " +
            "b.category_id, b.logo_url, b.banner_url, b.is_active, b.creado_en, " +
            "c.nombre AS category_nombre, " +
            "br.id AS branch_id, br.business_id AS branch_business_id, br.nombre AS branch_nombre, br.creado_en AS branch_creado_en, " +
            "l.id AS location_id, l.lat, l.lng, l.direccion, l.creado_en AS location_creado_en " +
            "FROM business b " +
            "LEFT JOIN category c ON c.id = b.category_id " +
            "LEFT JOIN branch br ON br.business_id = b.id " +
            "LEFT JOIN locations l ON l.branch_id = br.id ";

    private final RowMapper<Business> businessRowMapper = (rs, rowNum) -> {
        Business business = new Business();
        business.setId(UUID.fromString(rs.getString("id")));
        business.setOwnerUserId(UUID.fromString(rs.getString("owner_user_id")));
        business.setNombre(rs.getString("nombre"));
        business.setDescripcion(rs.getString("descripcion"));
        business.setCategoryId(rs.getInt("category_id"));
        business.setCategoryNombre(rs.getString("category_nombre"));
        business.setLogoUrl(rs.getString("logo_url"));
        business.setBannerUrl(rs.getString("banner_url"));
        business.setIsActive(rs.getBoolean("is_active"));
        business.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
        return business;
    };
    
    private Branch mapRowToBranch(java.sql.ResultSet rs) throws java.sql.SQLException {
        Branch branch = new Branch();
        branch.setId(UUID.fromString(rs.getString("branch_id")));
        branch.setBusinessId(UUID.fromString(rs.getString("business_id")));
        branch.setNombre(rs.getString("branch_nombre"));
        branch.setCreadoEn(rs.getTimestamp("branch_creado_en").toLocalDateTime());
        return branch;
    }
    
    private Location mapRowToLocation(java.sql.ResultSet rs) throws java.sql.SQLException {
        Location location = new Location();
        location.setId(UUID.fromString(rs.getString("location_id")));
        location.setLat(rs.getDouble("lat"));
        location.setLng(rs.getDouble("lng"));
        location.setDireccion(rs.getString("direccion"));
        location.setBranchId(UUID.fromString(rs.getString("branch_id")));
        location.setCreadoEn(rs.getTimestamp("location_creado_en").toLocalDateTime());
        return location;
    }

    @Override
    public List<Business> findAll() {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true";
        return queryBusinessesWithBranches(sql);
    }

    @Override
    public Business findById(UUID id) {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.id = ?";
        List<Business> list = queryBusinessesWithBranches(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Business> findByNombreContaining(String nombre) {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true AND LOWER(b.nombre) LIKE LOWER(?)";
        return queryBusinessesWithBranches(sql, "%" + nombre + "%");
    }

    @Override
    public List<Business> searchByNombreOrDescripcion(String searchTerm) {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true AND " +
            "(unaccent(LOWER(b.nombre)) LIKE unaccent(LOWER(?)) OR " +
            "unaccent(LOWER(b.descripcion)) LIKE unaccent(LOWER(?)))";
        String searchPattern = "%" + searchTerm + "%";
        return queryBusinessesWithBranches(sql, searchPattern, searchPattern);
    }

    @Override
    public List<Business> findByCategoryId(Integer categoryId) {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true AND b.category_id = ?";
        return queryBusinessesWithBranches(sql, categoryId);
    }

    private List<Business> queryBusinessesWithBranches(String sql, Object... args) {
        java.util.Map<UUID, Business> map = new java.util.LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            UUID businessId = UUID.fromString(rs.getString("id"));
            Business business = map.get(businessId);
            if (business == null) {
                business = new Business();
                business.setId(businessId);
                String owner = rs.getString("owner_user_id");
                if (owner != null) business.setOwnerUserId(UUID.fromString(owner));
                business.setNombre(rs.getString("nombre"));
                business.setDescripcion(rs.getString("descripcion"));
                business.setCategoryId(rs.getObject("category_id") == null ? null : rs.getInt("category_id"));
                business.setCategoryNombre(rs.getString("category_nombre"));
                business.setLogoUrl(rs.getString("logo_url"));
                business.setBannerUrl(rs.getString("banner_url"));
                business.setIsActive(rs.getBoolean("is_active"));
                java.sql.Timestamp ts = rs.getTimestamp("creado_en");
                if (ts != null) business.setCreadoEn(ts.toLocalDateTime());
                business.setBranches(new java.util.ArrayList<>());
                map.put(businessId, business);
            }

            String branchIdStr = rs.getString("branch_id");
            if (branchIdStr != null) {
                UUID branchId = UUID.fromString(branchIdStr);
                // check if branch already added
                boolean exists = business.getBranches().stream().anyMatch(b -> b.getId().equals(branchId));
                if (!exists) {
                    Branch branch = new Branch();
                    branch.setId(branchId);
                    String bBusinessId = rs.getString("branch_business_id");
                    if (bBusinessId != null) branch.setBusinessId(UUID.fromString(bBusinessId));
                    branch.setNombre(rs.getString("branch_nombre"));
                    java.sql.Timestamp bts = rs.getTimestamp("branch_creado_en");
                    if (bts != null) branch.setCreadoEn(bts.toLocalDateTime());
                    branch.setLocations(new java.util.ArrayList<>());
                    business.getBranches().add(branch);

                    String locationIdStr = rs.getString("location_id");
                    if (locationIdStr != null) {
                        Location location = new Location();
                        location.setId(UUID.fromString(locationIdStr));
                        location.setLat(rs.getDouble("lat"));
                        location.setLng(rs.getDouble("lng"));
                        location.setDireccion(rs.getString("direccion"));
                        location.setBranchId(branch.getId());
                        java.sql.Timestamp lts = rs.getTimestamp("location_creado_en");
                        if (lts != null) location.setCreadoEn(lts.toLocalDateTime());
                        branch.getLocations().add(location);
                    }
                } else {
                    // branch exists, maybe add location
                    String locationIdStr = rs.getString("location_id");
                    if (locationIdStr != null) {
                        UUID locId = UUID.fromString(locationIdStr);
                        Branch existing = business.getBranches().stream().filter(b -> b.getId().equals(branchId)).findFirst().get();
                        boolean locExists = existing.getLocations().stream().anyMatch(l -> l.getId().equals(locId));
                        if (!locExists) {
                            Location location = new Location();
                            location.setId(locId);
                            location.setLat(rs.getDouble("lat"));
                            location.setLng(rs.getDouble("lng"));
                            location.setDireccion(rs.getString("direccion"));
                            location.setBranchId(existing.getId());
                            java.sql.Timestamp lts = rs.getTimestamp("location_creado_en");
                            if (lts != null) location.setCreadoEn(lts.toLocalDateTime());
                            existing.getLocations().add(location);
                        }
                    }
                }
            }
        }, args);

        return new java.util.ArrayList<>(map.values());
    }

}
