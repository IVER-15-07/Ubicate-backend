package com.tulocal.backend.modules.branch.infrastructure.persistence;

import com.tulocal.backend.modules.branch.api.response.BranchResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcFavoriteRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<BranchResponse> findFavoriteBranches(UUID userId) {
        String sql = "SELECT b.id, b.owner_user_id, b.category_id, b.nombre, b.descripcion, " +
                "b.logo_url, b.banner_url, b.lat, b.lng, b.direccion, b.telefono, b.is_active, b.creado_en " +
                "FROM public.favorites f " +
                "INNER JOIN public.branch b ON b.id = f.branch_id " +
                "WHERE f.user_id = ? AND b.is_active = true " +
                "ORDER BY f.creado_en DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BranchResponse r = new BranchResponse();
            r.setId(UUID.fromString(rs.getString("id")));

            String ownerIdStr = rs.getString("owner_user_id");
            if (ownerIdStr != null) {
                r.setOwnerUserId(UUID.fromString(ownerIdStr));
            }

            r.setCategoryId(rs.getObject("category_id", Integer.class));
            r.setNombre(rs.getString("nombre"));
            r.setDescripcion(rs.getString("descripcion"));
            r.setLogoUrl(rs.getString("logo_url"));
            r.setBannerUrl(rs.getString("banner_url"));
            r.setLat(rs.getObject("lat", Double.class));
            r.setLng(rs.getObject("lng", Double.class));
            r.setDireccion(rs.getString("direccion"));
            r.setTelefono(rs.getString("telefono"));
            r.setIsActive(rs.getBoolean("is_active"));
            if (rs.getTimestamp("creado_en") != null) {
                r.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            }
            return r;
        }, userId);
    }
}