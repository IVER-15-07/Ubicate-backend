package com.tulocal.backend.modules.superAdmin.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMapCategoryRequest {

    @NotBlank
    private String nombre;

    private String icono;
}
