package com.tulocal.backend.modules.details.api.response;

import lombok.Data;
import java.util.UUID;
import java.util.List;


@Data

public class MenuItemDetailResponse {
    private UUID id;
    private String nombre;
    private String descripcion;
    private Double precio;
    
    
    private List<MenuImageDetailResponse> images;



    
}
