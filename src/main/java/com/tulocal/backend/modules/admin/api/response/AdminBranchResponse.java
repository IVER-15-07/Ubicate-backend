package com.tulocal.backend.modules.admin.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AdminBranchResponse {
    private UUID id;
    private UUID businessId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
}