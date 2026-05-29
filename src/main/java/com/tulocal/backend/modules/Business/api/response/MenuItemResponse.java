package com.tulocal.backend.modules.Business.api.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MenuItemResponse {
    private UUID id;
    private UUID menuId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String photoUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<MenuImageResponse> images;
}
