package com.tulocal.backend.modules.menu.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "menus", schema = "public")
@Data
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_user_id")
    private UUID ownerUserId;

    private String nombre;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
    
}
