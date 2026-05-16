package com.tulocal.backend.modules.Business.domain.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;


@Data
public class Location {
    private UUID id;
    private Double lat;
    private Double lng;
    private String direccion;
    private UUID branchId;
    private LocalDateTime creadoEn;
}
