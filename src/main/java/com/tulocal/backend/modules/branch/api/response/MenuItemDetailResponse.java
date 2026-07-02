package com.tulocal.backend.modules.branch.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuItemDetailResponse {
    private UUID id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String photoUrl;
}