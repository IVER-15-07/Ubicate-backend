package com.tulocal.backend.modules.branch.api.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UpdateBranchRequest {

    @NotNull(message = "El id de categoría es obligatorio")
    private Integer categoryId;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    private String descripcion;
    private String direccion;
    private String telefono;

    @NotNull(message = "La latitud es obligatoria")
    private Double lat;

    @NotNull(message = "La longitud es obligatoria")
    private Double lng;

    private Boolean isActive;
}