package com.tulocal.backend.modules.menu.application.mapper;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.api.response.MenuItemResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    
    public MenuItemResponse toResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setMenuId(menuItem.getMenuId());
        response.setNombre(menuItem.getNombre());
        response.setDescripcion(menuItem.getDescripcion());
        response.setPrecio(menuItem.getPrecio());
        response.setPhotoUrl(menuItem.getPhotoUrl());
        response.setIsActive(menuItem.getIsActive());
        response.setCreadoEn(menuItem.getCreadoEn());
        return response;
    }
}
