    package com.tulocal.backend.modules.Business.api.request;

    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import lombok.Data;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.UUID;

    @Data
    public class CreateMenuItemRequest {
        @NotNull
        private UUID menuId;

        @NotBlank
        private String nombre;

        private String descripcion;

        @NotNull
        private BigDecimal precio;

        private String photoUrl;

        @Size(max = 3)
        private List<String> imageUrls;

        private Boolean isActive;
    }
