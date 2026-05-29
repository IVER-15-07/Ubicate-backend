package com.tulocal.backend.modules.User.api.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String nombre;
    private String email;
    private Integer roleId;
    private LocalDateTime creadoEn;
}