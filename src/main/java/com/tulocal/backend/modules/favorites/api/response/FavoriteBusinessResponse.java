package com.tulocal.backend.modules.favorites.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FavoriteBusinessResponse {
    private UUID id;
    private String nombre;
    private String descripcion;
    private Integer categoryId;
    private String logoUrl;
    private String bannerUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;
}
