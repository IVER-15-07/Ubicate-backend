package com.tulocal.backend.modules.menu.api.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class AssignMenuToBranchRequest {
    @NotNull(message = "El id de la sucursal es obligatorio")
    private UUID branchId;

    @NotNull(message = "El id del menú es obligatorio")
    private UUID menuId;
    
}
