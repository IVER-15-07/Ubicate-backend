package com.tulocal.backend.modules.Business.api.response;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class BranchResponse {
    
    private UUID id;
    private UUID businessId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    
    private List<LocationResponse> locations;
}
