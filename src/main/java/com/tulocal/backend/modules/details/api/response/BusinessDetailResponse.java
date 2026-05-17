package com.tulocal.backend.modules.details.api.response;

import lombok.Data;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

@Data
public class BusinessDetailResponse {

    private UUID id;
    private String nombre;
    private String descripcion;
    private String logoUrl;
    private String bannerUrl;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private CategoryDetailResponse category;
    private List<BranchDetailResponse> branches;


}
