package com.tulocal.backend.modules.User.application.mapper;

import com.tulocal.backend.modules.User.api.response.UserResponse;
import com.tulocal.backend.modules.User.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRoleId(user.getRoleId());
        response.setCreadoEn(user.getCreadoEn());
        return response;
    }
}