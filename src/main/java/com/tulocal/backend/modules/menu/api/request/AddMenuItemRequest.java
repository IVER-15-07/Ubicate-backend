package com.tulocal.backend.modules.menu.api.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.List;

@Data
public class AddMenuItemRequest {
    @NotNull(message = "El ID del menú es obligatorio")
    private UUID menuId;

   @Valid
    @NotNull
    @Size(min = 1, max = 3, message = "Puedes agregar entre 1 y 3 items por vez")
    private List<CreateMenuItemRequest> items;
    
}
