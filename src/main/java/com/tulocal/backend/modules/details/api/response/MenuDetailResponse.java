package com.tulocal.backend.modules.details.api.response;

import lombok.Data;
import java.util.UUID;
import java.util.List;

@Data
public class MenuDetailResponse {

    private UUID id;
    private String nombre;
    private Boolean isActive;
    
    private List<MenuItemDetailResponse> items;
    
}
