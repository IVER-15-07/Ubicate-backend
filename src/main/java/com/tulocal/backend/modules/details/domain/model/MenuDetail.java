package com.tulocal.backend.modules.details.domain.model;
import lombok.Data;
import java.util.UUID;
import java.util.List;


@Data

public class MenuDetail {
    private UUID id;
    private UUID branchId;
    private String nombre;
    private List<MenuItemDetail> items;
    
}
