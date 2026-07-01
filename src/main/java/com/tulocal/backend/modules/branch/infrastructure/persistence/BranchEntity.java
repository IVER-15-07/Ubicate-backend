package com.tulocal.backend.modules.branch.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch", schema = "public")
@Data
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_user_id")
    private UUID ownerUserId;

    @Column(name = "category_id")
    private Integer categoryId;

    private String nombre;
    
    @Transient
    private String categoryName;
    private String descripcion;
    private String telefono;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    private Double lat;
    private Double lng;
    private String direccion;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();
}