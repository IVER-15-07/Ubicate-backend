package com.tulocal.backend.modules.menu.domain.model;

import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    
}
