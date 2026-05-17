package com.tulocal.backend.modules.details.domain.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class BusinessDetail {
    private UUID id;
    private UUID ownerUserId;
    private String  nombre;
    private String descripcion;
    private Integer categoryId;
    private String categoryNombre;
    private String LogoUrl;
    private String bannerUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    
    private List<BranchDetail> branches;

    
}
