package com.tulocal.backend.modules.Business.api.request;
import lombok.Data;


@Data

public class BusinessRequest {
    private String  nombre;
    private String descripcion;
    private Integer categoryId;
    private String LogoUrl;
    private String bannerUrl;
    
}
