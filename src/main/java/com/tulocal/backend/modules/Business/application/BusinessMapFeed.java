package com.tulocal.backend.modules.Business.application;

import java.time.Instant;
import java.util.List;

public record BusinessMapFeed(
        BusinessMapCenter center,
        List<BusinessMapPlace> places,
        String source,
        Instant updatedAt
) {
    public record BusinessMapCenter(
            String label,
            double lat,
            double lon
    ) {
    }

    public record BusinessMapPlace(
            String id,
            String name,
            String category,
            String categoryLabel,
            double lat,
            double lon,
            double score,
            int etaMinutes,
            int distanceMeters,
            String priceLabel,
            String accent
    ) {
    }
}