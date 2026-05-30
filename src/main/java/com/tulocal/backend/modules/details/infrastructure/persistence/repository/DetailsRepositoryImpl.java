package com.tulocal.backend.modules.details.infrastructure.persistence.repository;


import com.tulocal.backend.modules.details.domain.model.*;
import com.tulocal.backend.modules.details.domain.repository.DetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DetailsRepositoryImpl implements DetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public BusinessDetail getBusinessDetails(UUID businessId) {

        String sql = "SELECT b.id, b.nombre, b.descripcion, b.logo_url, b.banner_url, " +
                "b.is_active, b.creado_en, " +
                "c.id AS category_id, c.nombre AS category_nombre, c.icono AS category_icono, " +
                "COALESCE(AVG(r.rating), 0) AS review_promedio, " +
                "COUNT(r.id) AS review_total " +
                "FROM business b " +
                "LEFT JOIN category c ON c.id = b.category_id " +
                "LEFT JOIN reviews r ON r.business_id = b.id " +
                "WHERE b.id = ? " +
                "GROUP BY b.id, c.id";

        BusinessDetail detail = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BusinessDetail d = new BusinessDetail();
            d.setId(UUID.fromString(rs.getString("id")));
            d.setNombre(rs.getString("nombre"));
            d.setDescripcion(rs.getString("descripcion"));
            d.setLogoUrl(rs.getString("logo_url"));
            d.setBannerUrl(rs.getString("banner_url"));
            d.setIsActive(rs.getBoolean("is_active"));
            d.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            d.setCategoryId(rs.getInt("category_id"));
            d.setCategoryNombre(rs.getString("category_nombre"));

            return d;
        }, businessId);

        if (detail != null) {
            detail.setBranches(findBranches(businessId));
        }

        return detail;

    }

    @Override
    public BusinessDetail getBusinessDetailsByBranchId(UUID branchId) {

        String sql = "SELECT b.id, b.nombre, b.descripcion, b.logo_url, b.banner_url, " +
                "b.is_active, b.creado_en, " +
                "c.id AS category_id, c.nombre AS category_nombre, c.icono AS category_icono, " +
                "COALESCE(AVG(r.rating), 0) AS review_promedio, " +
                "COUNT(r.id) AS review_total " +
                "FROM business b " +
                "INNER JOIN branch br ON br.business_id = b.id " +
                "LEFT JOIN category c ON c.id = b.category_id " +
                "LEFT JOIN reviews r ON r.business_id = b.id " +
                "WHERE br.id = ? " +
                "GROUP BY b.id, c.id";

        BusinessDetail detail = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BusinessDetail d = new BusinessDetail();
            d.setId(UUID.fromString(rs.getString("id")));
            d.setNombre(rs.getString("nombre"));
            d.setDescripcion(rs.getString("descripcion"));
            d.setLogoUrl(rs.getString("logo_url"));
            d.setBannerUrl(rs.getString("banner_url"));
            d.setIsActive(rs.getBoolean("is_active"));
            d.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            d.setCategoryId(rs.getInt("category_id"));
            d.setCategoryNombre(rs.getString("category_nombre"));

            return d;
        }, branchId);

        if (detail != null) {
            BranchDetail branch = findBranchById(branchId);
            detail.setBranches(List.of(branch));
        }

        return detail;

    }

    private List<BranchDetail> findBranches(UUID businessId) {
        String sql = "SELECT br.id, br.business_id, br.nombre, br.is_active, br.creado_en, " +
                "l.lat, l.lng, l.direccion " +
                "FROM branch br " +
                "LEFT JOIN locations l ON l.branch_id = br.id " +
            "WHERE br.business_id = ? AND br.is_active = true";

        List<BranchDetail> branches = jdbcTemplate.query(sql, (rs, rowNum) -> {
            BranchDetail branch = new BranchDetail();
            branch.setId(UUID.fromString(rs.getString("id")));
            branch.setBusinessId(UUID.fromString(rs.getString("business_id")));
            branch.setNombre(rs.getString("nombre"));
            branch.setIsActive(rs.getBoolean("is_active"));
            branch.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());

            String lat = rs.getString("lat");
            if (lat != null) {
                LocationDetail loc = new LocationDetail();
                loc.setLat(rs.getDouble("lat"));
                loc.setLng(rs.getDouble("lng"));
                loc.setDireccion(rs.getString("direccion"));
                branch.setLocation(loc);
            }

            return branch;
        }, businessId);

        branches.forEach(branch -> branch.setMenus(findMenus(branch.getId())));

        return branches;
    }

    private BranchDetail findBranchById(UUID branchId) {
        String sql = "SELECT br.id, br.business_id, br.nombre, br.is_active, br.creado_en, " +
                "l.lat, l.lng, l.direccion " +
                "FROM branch br " +
                "LEFT JOIN locations l ON l.branch_id = br.id " +
            "WHERE br.id = ? AND br.is_active = true";

        BranchDetail branch = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BranchDetail item = new BranchDetail();
            item.setId(UUID.fromString(rs.getString("id")));
            item.setBusinessId(UUID.fromString(rs.getString("business_id")));
            item.setNombre(rs.getString("nombre"));
            item.setIsActive(rs.getBoolean("is_active"));
            item.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());

            String lat = rs.getString("lat");
            if (lat != null) {
                LocationDetail loc = new LocationDetail();
                loc.setLat(rs.getDouble("lat"));
                loc.setLng(rs.getDouble("lng"));
                loc.setDireccion(rs.getString("direccion"));
                item.setLocation(loc);
            }

            return item;
        }, branchId);

        if (branch != null) {
            branch.setMenus(findMenus(branch.getId()));
        }

        return branch;
    }

    private List<MenuDetail> findMenus(UUID branchId) {
        String sql = "SELECT m.id, m.nombre, m.is_active " +
                "FROM menus m " +
                "INNER JOIN branch_menu bm ON bm.menu_id = m.id " +
                "WHERE bm.branch_id = ? AND m.is_active = true";

        List<MenuDetail> menus = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MenuDetail menu = new MenuDetail();
            menu.setId(UUID.fromString(rs.getString("id")));
            menu.setNombre(rs.getString("nombre"));

            return menu;
        }, branchId);

        menus.forEach(menu -> menu.setItems(findMenuItems(menu.getId())));

        return menus;
    }

    private List<MenuItemDetail> findMenuItems(UUID menuId) {
        String sql = "SELECT id, nombre, descripcion, precio FROM menu_items " +
            "WHERE menu_id = ?";

        List<MenuItemDetail> items = jdbcTemplate.query(sql, (rs, rowNum) -> {
            MenuItemDetail item = new MenuItemDetail();
            item.setId(UUID.fromString(rs.getString("id")));
            item.setNombre(rs.getString("nombre"));
            item.setDescripcion(rs.getString("descripcion"));
            item.setPrecio(rs.getDouble("precio"));

            return item;
        }, menuId);

        items.forEach(item -> item.setImagenes(findMenuImages(item.getId())));

        return items;
    }

    private List<MenuImageDetail> findMenuImages(UUID menuItemId) {
        String sql = "SELECT id, url FROM menu_images " +
                "WHERE menu_item_id = ? ORDER BY orden ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MenuImageDetail image = new MenuImageDetail();
            image.setId(UUID.fromString(rs.getString("id")));
            image.setUrl(rs.getString("url"));

            return image;
        }, menuItemId);
    }

}
