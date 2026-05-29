package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MenuItem {
    private UUID id;
    private UUID menuId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String photoUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<MenuImage> images = new ArrayList<>();
}
