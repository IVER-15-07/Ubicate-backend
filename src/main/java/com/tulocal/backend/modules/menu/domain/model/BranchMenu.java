package com.tulocal.backend.modules.menu.domain.model;
import lombok.Data;
import java.util.UUID;

import java.time.LocalDateTime;

@Data
public class BranchMenu {

     private UUID id;
    private UUID branchId;
    private UUID menuId;
    private Boolean isActive;
    private LocalDateTime creadoEn;
    
}
