package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


@Data
public class Branch {
    private UUID id;
    private UUID businessId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<Location> locations = new ArrayList<>();
}
