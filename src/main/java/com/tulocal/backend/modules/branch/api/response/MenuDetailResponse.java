package com.tulocal.backend.modules.branch.api.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuDetailResponse {
    private UUID id;
    private String nombre;
    private Boolean isActive;
    private List<MenuItemDetailResponse> items;
}