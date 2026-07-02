package com.tulocal.backend.modules.branch.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BranchDetailResponse {
    private UUID id;
    private String nombre;
    private String descripcion;
    private String logoUrl;
    private String bannerUrl;
    private String telefono;
    private Double lat;
    private Double lng;
    private String direccion;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private CategoryDetailResponse category;
    private List<MenuDetailResponse> menus;
}