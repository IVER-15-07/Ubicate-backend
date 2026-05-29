package com.tulocal.backend.modules.Business.infrastructure.persistence.repository;

import com.tulocal.backend.modules.Business.domain.model.Business;
import com.tulocal.backend.modules.Business.domain.model.Branch;
import com.tulocal.backend.modules.Business.domain.model.Location;
import com.tulocal.backend.modules.Business.domain.model.Menu;
import com.tulocal.backend.modules.Business.domain.model.MenuImage;
import com.tulocal.backend.modules.Business.domain.model.MenuItem;
import com.tulocal.backend.modules.Business.domain.repository.BusinessRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.text.Normalizer;
import java.util.ArrayList;
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

        private static final String ACCENT_SOURCE = "ÁÀÄÂÃáàäâãÉÈËÊéèëêÍÌÏÎíìïîÓÒÖÔÕóòöôõÚÙÜÛúùüûÑñÇç";
        private static final String ACCENT_TARGET = "AAAAAaaaaaEEEEeeeeIIIIiiiiOOOOOoooooUUUUuuuuNnCc";

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
        String normalizedSearchTerm = normalizeSearchTerm(searchTerm);
        String searchPattern = "%" + normalizedSearchTerm + "%";
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true AND " +
            "(" + normalizedTextExpression("b.nombre") + " LIKE ? OR " +
            normalizedTextExpression("b.descripcion") + " LIKE ?)";
        return queryBusinessesWithBranches(sql, searchPattern, searchPattern);
    }

    @Override
    public List<Business> findByCategoryId(Integer categoryId) {
        String sql = SELECT_WITH_BRANCHES + "WHERE b.is_active = true AND b.category_id = ?";
        return queryBusinessesWithBranches(sql, categoryId);
    }

    @Override
    public Business save(Business business) {
        String sql = "INSERT INTO business (owner_user_id, nombre, descripcion, category_id, logo_url, banner_url, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, true)) " +
                "RETURNING id, owner_user_id, nombre, descripcion, category_id, logo_url, banner_url, is_active, creado_en";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Business result = new Business();
            result.setId(UUID.fromString(rs.getString("id")));
            String owner = rs.getString("owner_user_id");
            if (owner != null) {
                result.setOwnerUserId(UUID.fromString(owner));
            }
            result.setNombre(rs.getString("nombre"));
            result.setDescripcion(rs.getString("descripcion"));
            result.setCategoryId(rs.getObject("category_id") == null ? null : rs.getInt("category_id"));
            result.setLogoUrl(rs.getString("logo_url"));
            result.setBannerUrl(rs.getString("banner_url"));
            result.setIsActive(rs.getBoolean("is_active"));
            java.sql.Timestamp ts = rs.getTimestamp("creado_en");
            if (ts != null) {
                result.setCreadoEn(ts.toLocalDateTime());
            }
            result.setBranches(new java.util.ArrayList<>());
            return result;
        },
                business.getOwnerUserId(),
                business.getNombre(),
                business.getDescripcion(),
                business.getCategoryId(),
                business.getLogoUrl(),
                business.getBannerUrl(),
                business.getIsActive());
    }

    @Override
    public Branch saveBranch(Branch branch) {
        String sql = "INSERT INTO branch (business_id, nombre) VALUES (?, ?) " +
                "RETURNING id, business_id, nombre, creado_en";
        Branch result = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Branch createdBranch = new Branch();
            createdBranch.setId(UUID.fromString(rs.getString("id")));
            String businessId = rs.getString("business_id");
            if (businessId != null) {
                createdBranch.setBusinessId(UUID.fromString(businessId));
            }
            createdBranch.setNombre(rs.getString("nombre"));
            java.sql.Timestamp ts = rs.getTimestamp("creado_en");
            if (ts != null) {
                createdBranch.setCreadoEn(ts.toLocalDateTime());
            }
            createdBranch.setLocations(new java.util.ArrayList<>());
            return createdBranch;
        }, branch.getBusinessId(), branch.getNombre());

        if (result != null && branch.getLocations() != null) {
            for (Location locationInput : branch.getLocations()) {
                String locationSql = "INSERT INTO locations (branch_id, lat, lng, direccion) VALUES (?, ?, ?, ?) " +
                        "RETURNING id, branch_id, lat, lng, direccion, creado_en";

                Location savedLocation = jdbcTemplate.queryForObject(locationSql, (rs, rowNum) -> {
                    Location location = new Location();
                    location.setId(UUID.fromString(rs.getString("id")));
                    location.setBranchId(UUID.fromString(rs.getString("branch_id")));
                    location.setLat(rs.getDouble("lat"));
                    location.setLng(rs.getDouble("lng"));
                    location.setDireccion(rs.getString("direccion"));
                    java.sql.Timestamp ts = rs.getTimestamp("creado_en");
                    if (ts != null) {
                        location.setCreadoEn(ts.toLocalDateTime());
                    }
                    return location;
                }, result.getId(), locationInput.getLat(), locationInput.getLng(), locationInput.getDireccion());

                if (savedLocation != null) {
                    result.getLocations().add(savedLocation);
                }
            }
        }

        return result;
    }

    @Override
    public Menu saveMenu(Menu menu) {
        String sql = "INSERT INTO menus (business_id, nombre, is_active) VALUES (?, ?, COALESCE(?, true)) " +
                "RETURNING id, business_id, nombre, is_active, creado_en";

        Menu savedMenu = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Menu createdMenu = new Menu();
            createdMenu.setId(UUID.fromString(rs.getString("id")));
            createdMenu.setBusinessId(UUID.fromString(rs.getString("business_id")));
            createdMenu.setNombre(rs.getString("nombre"));
            createdMenu.setIsActive(rs.getBoolean("is_active"));
            java.sql.Timestamp ts = rs.getTimestamp("creado_en");
            if (ts != null) {
                createdMenu.setCreadoEn(ts.toLocalDateTime());
            }
            createdMenu.setBranchIds(new ArrayList<>());
            return createdMenu;
        }, menu.getBusinessId(), menu.getNombre(), menu.getIsActive());

        if (savedMenu != null && menu.getBranchIds() != null) {
            String branchMenuSql = "INSERT INTO branch_menu (branch_id, menu_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
            for (UUID branchId : menu.getBranchIds()) {
                jdbcTemplate.update(branchMenuSql, branchId, savedMenu.getId());
                savedMenu.getBranchIds().add(branchId);
            }
        }

        return savedMenu;
    }

    @Override
    public Menu findMenuById(UUID id) {
        String sql = "SELECT id, business_id, nombre, is_active, creado_en FROM menus WHERE id = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (!rs.next()) return null;
            Menu m = new Menu();
            m.setId(UUID.fromString(rs.getString("id")));
            m.setBusinessId(UUID.fromString(rs.getString("business_id")));
            m.setNombre(rs.getString("nombre"));
            m.setIsActive(rs.getBoolean("is_active"));
            java.sql.Timestamp ts = rs.getTimestamp("creado_en");
            if (ts != null) m.setCreadoEn(ts.toLocalDateTime());
            return m;
        }, id);
    }

    @Override
    public MenuItem saveMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu_items (menu_id, nombre, descripcion, precio, photo_url, is_active) VALUES (?, ?, ?, ?, ?, COALESCE(?, true)) " +
                "RETURNING id, menu_id, nombre, descripcion, precio, photo_url, is_active, creado_en";

        MenuItem savedItem = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            MenuItem created = new MenuItem();
            created.setId(UUID.fromString(rs.getString("id")));
            created.setMenuId(UUID.fromString(rs.getString("menu_id")));
            created.setNombre(rs.getString("nombre"));
            created.setDescripcion(rs.getString("descripcion"));
            java.math.BigDecimal price = rs.getBigDecimal("precio");
            if (price != null) created.setPrecio(price);
            created.setPhotoUrl(rs.getString("photo_url"));
            created.setIsActive(rs.getBoolean("is_active"));
            java.sql.Timestamp ts = rs.getTimestamp("creado_en");
            if (ts != null) created.setCreadoEn(ts.toLocalDateTime());
            created.setImages(new ArrayList<>());
            return created;
        }, item.getMenuId(), item.getNombre(), item.getDescripcion(), item.getPrecio(), item.getPhotoUrl(), item.getIsActive());

        if (savedItem != null && item.getImages() != null) {
            String imageSql = "INSERT INTO menu_images (menu_item_id, url, orden) VALUES (?, ?, ?) " +
                    "RETURNING id, menu_item_id, url, orden, creado_en";

            for (MenuImage imageInput : item.getImages()) {
                MenuImage savedImage = jdbcTemplate.queryForObject(imageSql, (rs, rowNum) -> {
                    MenuImage image = new MenuImage();
                    image.setId(UUID.fromString(rs.getString("id")));
                    image.setMenuItemId(UUID.fromString(rs.getString("menu_item_id")));
                    image.setUrl(rs.getString("url"));
                    image.setOrden(rs.getInt("orden"));
                    java.sql.Timestamp ts = rs.getTimestamp("creado_en");
                    if (ts != null) {
                        image.setCreadoEn(ts.toLocalDateTime());
                    }
                    return image;
                }, savedItem.getId(), imageInput.getUrl(), imageInput.getOrden());

                if (savedImage != null) {
                    savedItem.getImages().add(savedImage);
                }
            }
        }

        return savedItem;
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

    private static String normalizedTextExpression(String column) {
        return "LOWER(TRANSLATE(COALESCE(" + column + ", ''), '" + ACCENT_SOURCE + "', '" + ACCENT_TARGET + "'))";
    }

    private static String normalizeSearchTerm(String searchTerm) {
        if (searchTerm == null) {
            return "";
        }

        String normalized = Normalizer.normalize(searchTerm, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase();

        return normalized
                .replace('ñ', 'n')
                .replace('ç', 'c');
    }

}
