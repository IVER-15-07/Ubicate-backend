package com.tulocal.backend.modules.favorites.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ZonePolygonResponse {
    private UUID id;
    private UUID userId;
    private String nombre;
    private List<List<Double>> coordinates;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<FavoriteBusinessResponse> businesses;
}
