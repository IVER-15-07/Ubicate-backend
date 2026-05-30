package com.tulocal.backend.modules.favorites.domain.repository;

import com.tulocal.backend.modules.favorites.domain.model.FavoriteBusiness;
import com.tulocal.backend.modules.favorites.domain.model.ZonePolygon;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository {
    void addBusinessFavorite(UUID userId, UUID businessId, Boolean notifyOffers, Boolean notifyNewBranch);
    void removeBusinessFavorite(UUID userId, UUID businessId);
    List<FavoriteBusiness> findFavoriteBusinesses(UUID userId);
    ZonePolygon createZone(UUID userId, String nombre, List<List<Double>> coordinates);
    ZonePolygon findZoneById(UUID zoneId);
    List<ZonePolygon> findZonesByUser(UUID userId);
    List<FavoriteBusiness> findBusinessesByZoneId(UUID zoneId);
    void deleteZone(UUID zoneId);
}