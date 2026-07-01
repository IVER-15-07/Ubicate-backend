package com.tulocal.backend.modules.branch.infrastructure.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

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
}