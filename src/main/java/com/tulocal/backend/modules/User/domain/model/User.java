package com.tulocal.backend.modules.User.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String nombre;
    private String email;
    private String passwordHash;
    private Integer roleId;
    private LocalDateTime creadoEn;
}