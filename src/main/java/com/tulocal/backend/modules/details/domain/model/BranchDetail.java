package com.tulocal.backend.modules.details.domain.model;


import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;


@Data

public class BranchDetail {
    private UUID id;
    private UUID businessId;
    private String nombre;
    private LocalDateTime creadoEn;
    private LocationDetail location;
    private List<MenuDetail> menus;

    
}
