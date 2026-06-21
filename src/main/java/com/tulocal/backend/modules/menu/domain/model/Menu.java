package com.tulocal.backend.modules.menu.domain.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data

public class Menu {
    private UUID id;
    private UUID ownerUserId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
}
