package com.tulocal.backend.modules.superAdmin.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMapCategoryRequest {

    @NotBlank
    private String nombre;

    private String icono;
}
