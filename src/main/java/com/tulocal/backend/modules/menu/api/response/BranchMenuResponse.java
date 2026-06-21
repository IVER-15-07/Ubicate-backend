package com.tulocal.backend.modules.menu.api.response;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;
@Data
public class BranchMenuResponse {
    private UUID id;
    private UUID branchId;
    private UUID menuId;
    private String nombre;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    
}
