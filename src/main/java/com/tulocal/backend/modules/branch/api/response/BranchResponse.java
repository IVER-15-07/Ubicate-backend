package com.tulocal.backend.modules.branch.api.response;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;


@Data
public class BranchResponse {
    private UUID id;
    private UUID ownerUserId;
    private Integer categoryId;
    private String nombre;
    private String descripcion;
    private String logoUrl;
    private String bannerUrl;
    private Double lat;
    private Double lng;
    private String direccion;
    private String telefono;
    private Boolean isActive;
    private LocalDateTime creadoEn;

    
    
}
