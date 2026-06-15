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

    /**
     * Recuperación masiva optimizada con SQL Nativo.
     * Ideal para cargar miles de sucursales en un mapa sin sobrecargar la memoria.
     */
    public List<BranchEntity> findAllActiveBranchesRaw() {
        String sql = "SELECT id, owner_user_id, category_id, nombre, descripcion, lat, lng, direccion, telefono, is_active, creado_en FROM public.branch WHERE is_active = true";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BranchEntity entity = new BranchEntity();
            entity.setId(UUID.fromString(rs.getString("id")));
            
            String ownerIdStr = rs.getString("owner_user_id");
            if (ownerIdStr != null) {
                entity.setOwnerUserId(UUID.fromString(ownerIdStr));
            }
            
            entity.setCategoryId(rs.getInt("category_id"));
            entity.setNombre(rs.getString("nombre"));
            entity.setDescripcion(rs.getString("descripcion"));
            entity.setLat(rs.getDouble("lat"));
            entity.setLng(rs.getDouble("lng"));
            entity.setDireccion(rs.getString("direccion"));
            entity.setTelefono(rs.getString("telefono"));
            entity.setIsActive(rs.getBoolean("is_active"));
            
            if (rs.getTimestamp("creado_en") != null) {
                entity.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
            }
            
            return entity;
        });
    }
}