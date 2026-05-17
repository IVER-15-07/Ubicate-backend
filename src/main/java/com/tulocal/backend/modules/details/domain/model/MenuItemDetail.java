package com.tulocal.backend.modules.details.domain.model;

import lombok.Data;
import java.util.UUID;
import java.util.List;
@Data

public class MenuItemDetail {
    private UUID id;
    private UUID menuId;
    private String nombre;
    private String descripcion;
    private Double precio;

    private List<MenuImageDetail> imagenes;

    
}
