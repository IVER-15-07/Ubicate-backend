package com.tulocal.backend.modules.menu.api.request;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class CreateMenuRequest {

    @NotBlank(message = "El nombre del menú es obligatorio")
    private String nombre;

    @Valid
    @Size(max = 3, message = "Puedes agregar máximo 3 items al crear el menú")
    private List<CreateMenuItemRequest> items;

}
