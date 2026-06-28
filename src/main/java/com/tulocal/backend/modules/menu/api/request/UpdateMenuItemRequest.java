package com.tulocal.backend.modules.menu.api.request;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class UpdateMenuItemRequest {

    private String nombre; // opcional — si viene, se actualiza
    private String descripcion; // opcional

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precio; // opcional — si viene, se actualiza

  

}
