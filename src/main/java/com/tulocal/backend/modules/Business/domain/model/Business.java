package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;


@Data
public class Business {
    private UUID id;
    private UUID ownerUserId;
    private String  nombre;
    private String descripcion;
    private Integer categoryId;
    private String LogoUrl;
    private String bannerUrl;
    private Double lat;
    private Double lng;
    private String direccion;
    private Boolean isActive;
    private LocalDateTime creadoEn;

}
