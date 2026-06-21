package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.api.request.AddMenuItemRequest;
import com.tulocal.backend.modules.menu.api.request.CreateMenuItemRequest;
import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddMenuItemUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public List<MenuItem> execute(AddMenuItemRequest request, UUID ownerUserId) {

        Menu menu = menuRepository.findById(request.getMenuId());
        if (menu == null) {
            throw new IllegalArgumentException("El menú indicado no existe");
        }
        if (!menu.getOwnerUserId().equals(ownerUserId)) {
            throw new IllegalArgumentException("No tienes permiso sobre este menú");
        }

        List<MenuItem> savedItems = new ArrayList<>();

        for (CreateMenuItemRequest itemRequest : request.getItems()) {
            MenuItem item = new MenuItem();
            item.setMenuId(menu.getId());
            item.setNombre(itemRequest.getNombre().trim());
            item.setDescripcion(itemRequest.getDescripcion());
            item.setPrecio(itemRequest.getPrecio());
            item.setPhotoUrl(itemRequest.getPhotoUrl());
            item.setIsActive(true);
            item.setCreadoEn(LocalDateTime.now());

            savedItems.add(menuItemRepository.save(item));
        }

        return savedItems;
    }
}