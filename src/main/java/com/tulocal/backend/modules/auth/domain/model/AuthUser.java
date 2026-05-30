package com.tulocal.backend.modules.auth.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class AuthUser {

    @Id
    @Column(name = "id", updatable = true)
    private UUID id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

}
