package com.tulocal.backend.modules.details.api.response;

import lombok.Data;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

@Data
public class BranchDetailResponse {
    private UUID id;
    private String nombre;
    private LocalDateTime creadoEn;
    private LocationDetailResponse location;
    
    private List<MenuDetailResponse> menus;
    
}
