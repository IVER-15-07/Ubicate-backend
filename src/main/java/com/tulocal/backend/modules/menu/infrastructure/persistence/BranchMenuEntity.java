package com.tulocal.backend.modules.menu.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch_menus", schema = "public")
@Data

public class BranchMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "menu_id")
    private UUID menuId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
    
}
