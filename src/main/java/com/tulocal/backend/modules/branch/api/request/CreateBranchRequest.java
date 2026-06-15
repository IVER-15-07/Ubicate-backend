package com.tulocal.backend.modules.branch.api.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class CreateBranchRequest {

    @NotNull(message = "El id de categoría es obligatorio")
    private Integer categoryId;
    @NotNull(message = "El id del propietario es obligatorio")
    private UUID ownerUserId;
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;
    private String descripcion;
    private String logoUrl;
    private String bannerUrl;
    @NotNull(message = "La latitud es obligatoria")
    private Double lat;
    @NotNull(message = "La longitud es obligatoria")
    private Double lng;
    private String direccion;
    private String telefono;
    private Boolean isActive = false;
    private LocalDateTime creadoEn = LocalDateTime.now();

}