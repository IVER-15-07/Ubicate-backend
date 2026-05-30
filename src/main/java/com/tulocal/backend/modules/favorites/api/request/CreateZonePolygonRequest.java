package com.tulocal.backend.modules.favorites.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateZonePolygonRequest {
    @NotNull
    private UUID userId;

    @NotBlank
    private String nombre;

    @NotNull
    private List<List<Double>> coordinates;
}
