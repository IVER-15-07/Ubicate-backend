package com.tulocal.backend.modules.favorites.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ZonePolygon {
    private UUID id;
    private UUID userId;
    private String nombre;
    private List<List<Double>> coordinates;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<FavoriteBusiness> businesses = new ArrayList<>();
}