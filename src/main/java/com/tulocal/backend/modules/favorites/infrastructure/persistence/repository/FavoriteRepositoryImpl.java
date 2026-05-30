package com.tulocal.backend.modules.favorites.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulocal.backend.modules.favorites.domain.model.FavoriteBusiness;
import com.tulocal.backend.modules.favorites.domain.model.ZonePolygon;
import com.tulocal.backend.modules.favorites.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void addBusinessFavorite(UUID userId, UUID businessId, Boolean notifyOffers, Boolean notifyNewBranch) {
        jdbcTemplate.update("DELETE FROM favorites WHERE user_id = ? AND business_id = ?", userId, businessId);
        jdbcTemplate.update(
                "INSERT INTO favorites (user_id, business_id, notify_offers, notify_new_branch) VALUES (?, ?, COALESCE(?, true), COALESCE(?, true))",
                userId, businessId, notifyOffers, notifyNewBranch);
    }

    @Override
    public void removeBusinessFavorite(UUID userId, UUID businessId) {
        jdbcTemplate.update("DELETE FROM favorites WHERE user_id = ? AND business_id = ?", userId, businessId);
    }

    @Override
    public List<FavoriteBusiness> findFavoriteBusinesses(UUID userId) {
        String sql = "SELECT b.id, b.nombre, b.descripcion, b.category_id, b.logo_url, b.banner_url, b.is_active, b.creado_en " +
                "FROM favorites f INNER JOIN business b ON b.id = f.business_id " +
                "WHERE f.user_id = ? ORDER BY b.nombre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapBusinessRow(rs), userId);
    }

    @Override
    public ZonePolygon createZone(UUID userId, String nombre, List<List<Double>> coordinates) {
        try {
            String coordinatesJson = objectMapper.writeValueAsString(coordinates);
            return jdbcTemplate.queryForObject(
                    "INSERT INTO zone_polygons (user_id, nombre, coordinates, is_active) VALUES (?, ?, CAST(? AS jsonb), true) " +
                            "RETURNING id, user_id, nombre, coordinates, is_active, creado_en",
                    (rs, rowNum) -> mapZoneRow(rs, false),
                    userId, nombre, coordinatesJson);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("No se pudieron serializar las coordenadas del poligono", ex);
        }
    }

    @Override
    public ZonePolygon findZoneById(UUID zoneId) {
        String sql = "SELECT id, user_id, nombre, coordinates, is_active, creado_en FROM zone_polygons WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapZoneRow(rs, true), zoneId);
    }

    @Override
    public List<ZonePolygon> findZonesByUser(UUID userId) {
        String sql = "SELECT id, user_id, nombre, coordinates, is_active, creado_en FROM zone_polygons WHERE user_id = ? AND is_active = true ORDER BY creado_en DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapZoneRow(rs, true), userId);
    }

    @Override
    public List<FavoriteBusiness> findBusinessesByZoneId(UUID zoneId) {
        ZonePolygon zone = findZoneById(zoneId);
        return zone == null || zone.getCoordinates() == null ? Collections.emptyList() : findBusinessesByPolygon(zone.getCoordinates());
    }

    @Override
    public void deleteZone(UUID zoneId) {
        jdbcTemplate.update("UPDATE zone_polygons SET is_active = false WHERE id = ?", zoneId);
    }

    private ZonePolygon mapZoneRow(java.sql.ResultSet rs, boolean loadBusinesses) throws java.sql.SQLException {
        ZonePolygon response = new ZonePolygon();
        response.setId(UUID.fromString(rs.getString("id")));
        response.setUserId(UUID.fromString(rs.getString("user_id")));
        response.setNombre(rs.getString("nombre"));
        response.setCoordinates(readCoordinates(rs.getString("coordinates")));
        response.setIsActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) {
            response.setCreadoEn(ts.toLocalDateTime());
        }
        if (loadBusinesses) {
            response.setBusinesses(findBusinessesByPolygon(response.getCoordinates()));
        }
        return response;
    }

    private FavoriteBusiness mapBusinessRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        FavoriteBusiness response = new FavoriteBusiness();
        response.setId(UUID.fromString(rs.getString("id")));
        response.setNombre(rs.getString("nombre"));
        response.setDescripcion(rs.getString("descripcion"));
        response.setCategoryId(rs.getObject("category_id") == null ? null : rs.getInt("category_id"));
        response.setLogoUrl(rs.getString("logo_url"));
        response.setBannerUrl(rs.getString("banner_url"));
        response.setIsActive(rs.getBoolean("is_active"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) {
            response.setCreadoEn(ts.toLocalDateTime());
        }
        return response;
    }

    private List<FavoriteBusiness> findBusinessesByPolygon(List<List<Double>> coordinates) {
        if (coordinates == null || coordinates.size() < 3) {
            return Collections.emptyList();
        }

        String sql = "SELECT DISTINCT b.id, b.nombre, b.descripcion, b.category_id, b.logo_url, b.banner_url, b.is_active, b.creado_en, " +
                "l.lat, l.lng " +
                "FROM business b " +
                "INNER JOIN branch br ON br.business_id = b.id AND br.is_active = true " +
                "INNER JOIN locations l ON l.branch_id = br.id " +
                "WHERE b.is_active = true " +
                "ORDER BY b.nombre";

        Map<UUID, FavoriteBusiness> result = new LinkedHashMap<>();
        jdbcTemplate.query(sql, rs -> {
            UUID businessId = UUID.fromString(rs.getString("id"));
            Double lat = rs.getObject("lat") == null ? null : rs.getDouble("lat");
            Double lng = rs.getObject("lng") == null ? null : rs.getDouble("lng");
            if (lat == null || lng == null || !isPointInsidePolygon(lat, lng, coordinates)) {
                return;
            }

            if (!result.containsKey(businessId)) {
                result.put(businessId, mapBusinessRow(rs));
            }
        });

        return List.copyOf(result.values());
    }

    private List<List<Double>> readCoordinates(String coordinatesJson) {
        try {
            return objectMapper.readValue(coordinatesJson, new TypeReference<List<List<Double>>>() {
            });
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("No se pudieron leer las coordenadas del poligono", ex);
        }
    }

    private boolean isPointInsidePolygon(double lat, double lng, List<List<Double>> polygon) {
        boolean inside = false;
        int j = polygon.size() - 1;
        for (int i = 0; i < polygon.size(); i++) {
            List<Double> current = polygon.get(i);
            List<Double> previous = polygon.get(j);
            if (current.size() < 2 || previous.size() < 2) {
                j = i;
                continue;
            }

            double currentLat = current.get(0);
            double currentLng = current.get(1);
            double previousLat = previous.get(0);
            double previousLng = previous.get(1);

            boolean intersects = ((currentLng > lng) != (previousLng > lng))
                    && (lat < (previousLat - currentLat) * (lng - currentLng) / (previousLng - currentLng + 0.0) + currentLat);
            if (intersects) {
                inside = !inside;
            }
            j = i;
        }
        return inside;
    }
}