package com.tulocal.backend.modules.Business.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BusinessMapService {

    private static final double DEFAULT_CENTER_LAT = -17.3895;
    private static final double DEFAULT_CENTER_LON = -66.1568;
    private static final String DEFAULT_CENTER_LABEL = "Cochabamba, Bolivia";

    private final JdbcTemplate jdbcTemplate;

    public BusinessMapService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BusinessMapFeed getMapFeed(Double centerLat, Double centerLon, String centerLabel, Integer radiusMeters, Integer limit) {
        double resolvedLat = centerLat != null ? centerLat : DEFAULT_CENTER_LAT;
        double resolvedLon = centerLon != null ? centerLon : DEFAULT_CENTER_LON;
        String resolvedLabel = hasText(centerLabel) ? centerLabel : DEFAULT_CENTER_LABEL;
        int resolvedRadius = radiusMeters != null && radiusMeters > 0 ? radiusMeters : 5000;
        int resolvedLimit = limit != null && limit > 0 ? Math.min(limit, 100) : 24;

        List<BusinessMapFeed.BusinessMapPlace> places = fetchBusinesses(resolvedLat, resolvedLon, resolvedRadius, resolvedLimit);

        return new BusinessMapFeed(
                new BusinessMapFeed.BusinessMapCenter(resolvedLabel, resolvedLat, resolvedLon),
                places,
                "backend",
                Instant.now());
    }

    private List<BusinessMapFeed.BusinessMapPlace> fetchBusinesses(double centerLat, double centerLon, int radiusMeters, int limit) {
        String sql = """
                SELECT
                    b.id,
                    b.nombre,
                    COALESCE(c.nombre, 'Negocio') AS category_label,
                    COALESCE(branch.nombre, b.nombre) AS branch_name,
                    COALESCE(branch.direccion, loc.direccion) AS direccion,
                    loc.lat,
                    loc.lng,
                    b.logo_url
                FROM business b
                LEFT JOIN category c ON c.id = b.category_id
                LEFT JOIN LATERAL (
                    SELECT br.nombre, br.direccion
                    FROM branch br
                    WHERE br.business_id = b.id
                    ORDER BY br.creado_en DESC
                    LIMIT 1
                ) branch ON TRUE
                LEFT JOIN LATERAL (
                    SELECT l.lat, l.lng, l.direccion
                    FROM locations l
                    WHERE l.business_id = b.id
                    ORDER BY l.creado_en DESC
                    LIMIT 1
                ) loc ON TRUE
                WHERE b.is_active = TRUE
                  AND loc.lat IS NOT NULL
                  AND loc.lng IS NOT NULL
                ORDER BY b.creado_en DESC
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> mapPlace(resultSet, centerLat, centerLon))
                .stream()
                .filter(place -> place.distanceMeters() <= radiusMeters)
                .sorted(Comparator.comparingInt(BusinessMapFeed.BusinessMapPlace::distanceMeters))
                .limit(limit)
                .toList();
    }

    private BusinessMapFeed.BusinessMapPlace mapPlace(ResultSet resultSet, double centerLat, double centerLon) throws SQLException {
        String id = resultSet.getString("id");
        String businessName = firstText(resultSet.getString("branch_name"), resultSet.getString("nombre"));
        String categoryLabel = firstText(resultSet.getString("category_label"), "Negocio");
        double lat = resultSet.getDouble("lat");
        double lon = resultSet.getDouble("lng");

        int distanceMeters = (int) Math.round(getDistanceMeters(centerLat, centerLon, lat, lon));
        String category = normalizeCategory(categoryLabel, businessName);

        return new BusinessMapFeed.BusinessMapPlace(
                id,
                businessName,
                category,
                categoryLabel,
                lat,
                lon,
                computeScore(businessName, category),
                Math.max(5, Math.round(distanceMeters / 95.0f)),
                distanceMeters,
                getPriceLabel(category, categoryLabel),
                getAccent(category)
        );
    }

    private static String normalizeCategory(String categoryLabel, String businessName) {
        String value = (categoryLabel + " " + businessName).toLowerCase(Locale.ROOT);

        if (value.contains("cafe") || value.contains("coffee") || value.contains("cafeter")) {
            return "cafes";
        }

        if (value.contains("bar") || value.contains("drink") || value.contains("pub") || value.contains("copas")) {
            return "drinks";
        }

        return "food";
    }

    private static String getPriceLabel(String category, String categoryLabel) {
        String value = (categoryLabel + " " + category).toLowerCase(Locale.ROOT);

        if (category.equals("cafes")) {
            return value.contains("bakery") || value.contains("pan") ? "Bakery" : "Coffee";
        }

        if (category.equals("drinks")) {
            return value.contains("cocktail") ? "Cocktails" : "Night out";
        }

        return value.contains("fast") ? "Fast Food" : "Casual";
    }

    private static String getAccent(String category) {
        if (category.equals("cafes")) {
            return "#a56812";
        }

        if (category.equals("drinks")) {
            return "#7c4dff";
        }

        return "#b72a32";
    }

    private static double computeScore(String name, String category) {
        String seed = name + ":" + category;
        int hash = 0;

        for (int index = 0; index < seed.length(); index++) {
            hash += seed.charAt(index);
        }

        return Double.parseDouble(String.format(Locale.US, "%.1f", 4.1 + (hash % 8) * 0.1));
    }

    private static double getDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return 2 * earthRadius * Math.asin(Math.sqrt(a));
    }

    private static String firstText(String first, String fallback) {
        return hasText(first) ? first : fallback;
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}