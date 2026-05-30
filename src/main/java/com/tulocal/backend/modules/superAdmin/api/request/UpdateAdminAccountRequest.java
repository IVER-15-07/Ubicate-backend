package com.tulocal.backend.modules.superAdmin.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAdminAccountRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    @Email
    private String email;

    private String password;

    private Integer roleId;
}
