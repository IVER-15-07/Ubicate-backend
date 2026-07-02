package com.tulocal.backend.modules.branch.infrastructure.persistence;

import com.tulocal.backend.modules.branch.api.response.BranchDetailResponse;
import com.tulocal.backend.modules.branch.api.response.CategoryDetailResponse;
import com.tulocal.backend.modules.branch.api.response.MenuDetailResponse;
import com.tulocal.backend.modules.branch.api.response.MenuItemDetailResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class JdbcBranchRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<BranchEntity> findAllActiveBranchesRaw() {
        // CORREGIDO: Se cambió 'public.categoria' por 'public.category'
        String sql = "SELECT b.id, b.owner_user_id, b.category_id, c.nombre as category_name, " +
                "b.nombre, b.descripcion, b.logo_url, b.banner_url, b.lat, b.lng, b.direccion, b.telefono, b.is_active, b.creado_en " +
                "FROM public.branch b " +
                "LEFT JOIN public.category c ON b.category_id = c.id " +
                "WHERE b.is_active = true";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BranchEntity entity = new BranchEntity();
            entity.setId(UUID.fromString(rs.getString("id")));

            String ownerIdStr = rs.getString("owner_user_id");
            if (ownerIdStr != null) {
                entity.setOwnerUserId(UUID.fromString(ownerIdStr));
            }

            entity.setCategoryId(rs.getObject("category_id", Integer.class));
            entity.setCategoryName(rs.getString("category_name"));

            entity.setNombre(rs.getString("nombre"));
            entity.setDescripcion(rs.getString("descripcion"));
            entity.setLogoUrl(rs.getString("logo_url"));
            entity.setBannerUrl(rs.getString("banner_url"));
            entity.setLat(rs.getObject("lat", Double.class));
            entity.setLng(rs.getObject("lng", Double.class));
            entity.setDireccion(rs.getString("direccion"));
            entity.setTelefono(rs.getString("telefono"));
            entity.setIsActive(rs.getBoolean("is_active"));

            if (rs.getTimestamp("creado_en") != null) {
                entity.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            }

            return entity;
        });
    }

    public List<BranchEntity> searchBranches(String query) {
        String sql = """
                  SELECT b.id, b.owner_user_id, b.category_id, c.nombre as category_name,
                      b.nombre, b.descripcion, b.logo_url, b.banner_url,
                      b.lat, b.lng, b.direccion, b.telefono, b.is_active, b.creado_en
                    FROM public.branch b
                    LEFT JOIN public.category c ON c.id = b.category_id
                    WHERE b.is_active = true
                      AND (
                        LOWER(b.nombre)      LIKE LOWER(CONCAT('%', ?, '%'))
                        OR LOWER(b.descripcion) LIKE LOWER(CONCAT('%', ?, '%'))
                        OR LOWER(b.direccion)   LIKE LOWER(CONCAT('%', ?, '%'))
                        OR LOWER(c.nombre)      LIKE LOWER(CONCAT('%', ?, '%'))
                      )
                    ORDER BY b.nombre
                    LIMIT 30
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BranchEntity entity = new BranchEntity();
            entity.setId(UUID.fromString(rs.getString("id")));

            String ownerIdStr = rs.getString("owner_user_id");
            if (ownerIdStr != null)
                entity.setOwnerUserId(UUID.fromString(ownerIdStr));

            entity.setCategoryId(rs.getObject("category_id", Integer.class));
            entity.setCategoryName(rs.getString("category_name"));
            entity.setNombre(rs.getString("nombre"));
            entity.setDescripcion(rs.getString("descripcion"));
            entity.setLogoUrl(rs.getString("logo_url"));
            entity.setBannerUrl(rs.getString("banner_url"));
            entity.setLat(rs.getObject("lat", Double.class));
            entity.setLng(rs.getObject("lng", Double.class));
            entity.setDireccion(rs.getString("direccion"));
            entity.setTelefono(rs.getString("telefono"));
            entity.setIsActive(rs.getBoolean("is_active"));

            if (rs.getTimestamp("creado_en") != null)
                entity.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());

            return entity;
        }, query, query, query, query); // 4 parámetros, uno por cada ?
    }

    public BranchDetailResponse getBranchDetail(UUID branchId) {
        String branchSql = "SELECT b.id, b.category_id, c.nombre AS category_name, " +
                "b.nombre, b.descripcion, b.logo_url, b.banner_url, b.lat, b.lng, b.direccion, b.telefono, b.is_active, b.creado_en " +
                "FROM public.branch b " +
                "LEFT JOIN public.category c ON c.id = b.category_id " +
                "WHERE b.id = ? AND b.is_active = true";

        List<BranchDetailResponse> branchResult = jdbcTemplate.query(branchSql, (rs, rowNum) -> {
            BranchDetailResponse branch = new BranchDetailResponse();
            branch.setId(UUID.fromString(rs.getString("id")));
            branch.setNombre(rs.getString("nombre"));
            branch.setDescripcion(rs.getString("descripcion"));
            branch.setLogoUrl(rs.getString("logo_url"));
            branch.setBannerUrl(rs.getString("banner_url"));
            branch.setTelefono(rs.getString("telefono"));
            branch.setLat(rs.getObject("lat", Double.class));
            branch.setLng(rs.getObject("lng", Double.class));
            branch.setDireccion(rs.getString("direccion"));
            branch.setIsActive(rs.getBoolean("is_active"));
            if (rs.getTimestamp("creado_en") != null) {
                branch.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            }

            Integer categoryId = rs.getObject("category_id", Integer.class);
            String categoryName = rs.getString("category_name");
            if (categoryId != null || categoryName != null) {
                CategoryDetailResponse category = new CategoryDetailResponse();
                category.setId(categoryId);
                category.setNombre(categoryName);
                branch.setCategory(category);
            }

            return branch;
        }, branchId);

        if (branchResult.isEmpty()) {
            return null;
        }

        BranchDetailResponse branch = branchResult.get(0);
        branch.setMenus(findMenusForBranch(branchId));
        return branch;
    }

    private List<MenuDetailResponse> findMenusForBranch(UUID branchId) {
        String sql = "SELECT m.id, m.nombre, m.is_active " +
                "FROM public.branch_menus bm " +
                "INNER JOIN public.menus m ON m.id = bm.menu_id " +
                "WHERE bm.branch_id = ? AND bm.is_active = true AND m.is_active = true " +
                "ORDER BY m.nombre";

        List<MenuDetailResponse> menus = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MenuDetailResponse menu = new MenuDetailResponse();
            menu.setId(UUID.fromString(rs.getString("id")));
            menu.setNombre(rs.getString("nombre"));
            menu.setIsActive(rs.getBoolean("is_active"));
            return menu;
        }, branchId);

        menus.forEach(menu -> menu.setItems(findItemsForMenu(menu.getId())));
        return menus;
    }

    private List<MenuItemDetailResponse> findItemsForMenu(UUID menuId) {
        String sql = "SELECT id, nombre, descripcion, precio, photo_url " +
                "FROM public.menu_items " +
                "WHERE menu_id = ? AND is_active = true " +
                "ORDER BY nombre";

        List<MenuItemDetailResponse> items = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MenuItemDetailResponse item = new MenuItemDetailResponse();
            item.setId(UUID.fromString(rs.getString("id")));
            item.setNombre(rs.getString("nombre"));
            item.setDescripcion(rs.getString("descripcion"));
            BigDecimal price = rs.getBigDecimal("precio");
            item.setPrecio(price != null ? price.doubleValue() : null);
            item.setPhotoUrl(rs.getString("photo_url"));
            return item;
        }, menuId);

        return items;
    }
}