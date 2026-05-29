package com.tulocal.backend.modules.Business.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateMenuRequest {
    @NotBlank
    private String nombre;

    @NotEmpty
    private List<@NotNull UUID> branchIds;

    private Boolean isActive;
}
