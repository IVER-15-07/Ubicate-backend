package com.tulocal.backend.modules.menu.api.response;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@Data

public class MenuResponse {
    private UUID id;
    private UUID ownerUserId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    private List<MenuItemResponse> items;

}
