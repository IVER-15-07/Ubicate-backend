package com.tulocal.backend.modules.menu.application.mapper;

import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.api.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuMapper {

    private final MenuItemMapper menuItemMapper;

    public MenuResponse toResponse(Menu menu) {
        return toResponse(menu, Collections.emptyList());
    }

    public MenuResponse toResponse(Menu menu, List<MenuItem> items) {
        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setOwnerUserId(menu.getOwnerUserId());
        response.setNombre(menu.getNombre());
        response.setIsActive(menu.getIsActive());
        response.setCreadoEn(menu.getCreadoEn());
        response.setItems(items.stream().map(menuItemMapper::toResponse).toList());
        return response;
    }
    
}
