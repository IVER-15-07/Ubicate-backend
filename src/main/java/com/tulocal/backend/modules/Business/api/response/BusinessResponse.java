package com.tulocal.backend.modules.Business.api.response;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;


@Data
public class BusinessResponse {
    
    private UUID id;
    private UUID ownerUserId;
    private String nombre;
    private String descripcion;


    private CategoryResponse category;


    private String logoUrl;
    private String bannerUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;

    private LocationResponse location;
}
