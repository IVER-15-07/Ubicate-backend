package com.tulocal.backend.modules.Business.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBusinessRequest {
    @NotBlank
    private String nombre;

    private String descripcion;
    private Integer categoryId;
    private String logoUrl;
    private String bannerUrl;
}
