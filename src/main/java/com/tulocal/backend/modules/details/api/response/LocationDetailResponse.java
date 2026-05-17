package com.tulocal.backend.modules.details.api.response;

import  lombok.Data;

@Data
public class LocationDetailResponse {
    private Double lat;
    private Double lng;
    private String direccion;
    
}

