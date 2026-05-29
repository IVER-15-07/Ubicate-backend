package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Menu {
    private UUID id;
    private UUID businessId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<UUID> branchIds;
}
