package com.tulocal.backend.modules.favorites.application.mapper;

import com.tulocal.backend.modules.favorites.api.response.FavoriteBusinessResponse;
import com.tulocal.backend.modules.favorites.api.response.ZonePolygonResponse;
import com.tulocal.backend.modules.favorites.domain.model.FavoriteBusiness;
import com.tulocal.backend.modules.favorites.domain.model.ZonePolygon;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FavoriteMapper {

    public FavoriteBusinessResponse toBusinessResponse(FavoriteBusiness business) {
        FavoriteBusinessResponse response = new FavoriteBusinessResponse();
        response.setId(business.getId());
        response.setNombre(business.getNombre());
        response.setDescripcion(business.getDescripcion());
        response.setCategoryId(business.getCategoryId());
        response.setLogoUrl(business.getLogoUrl());
        response.setBannerUrl(business.getBannerUrl());
        response.setIsActive(business.getIsActive());
        response.setCreadoEn(business.getCreadoEn());
        return response;
    }

    public ZonePolygonResponse toZoneResponse(ZonePolygon zone) {
        ZonePolygonResponse response = new ZonePolygonResponse();
        response.setId(zone.getId());
        response.setUserId(zone.getUserId());
        response.setNombre(zone.getNombre());
        response.setCoordinates(zone.getCoordinates());
        response.setIsActive(zone.getIsActive());
        response.setCreadoEn(zone.getCreadoEn());
        response.setBusinesses(mapBusinesses(zone.getBusinesses()));
        return response;
    }

    public List<FavoriteBusinessResponse> mapBusinesses(List<FavoriteBusiness> businesses) {
        return businesses == null ? List.of() : businesses.stream().map(this::toBusinessResponse).collect(Collectors.toList());
    }

    public List<ZonePolygonResponse> mapZones(List<ZonePolygon> zones) {
        return zones == null ? List.of() : zones.stream().map(this::toZoneResponse).collect(Collectors.toList());
    }
}