package com.tulocal.backend.modules.admin.domain.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "branch")
@Data
public class AdminBranch {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;
}