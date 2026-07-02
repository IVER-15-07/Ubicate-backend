package com.tulocal.backend.modules.branch.api.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BranchMapPointResponse {
    private UUID id;
    private String nombre;
    private Integer categoryId;
    private String categoryName;
    private String direccion;
    private String logoUrl;
    private String bannerUrl;
    private Double distanceKm;
    
    private Double lat;
    private Double lng;
}
