package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.Menu;
import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMenuItemByIdUseCase {

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuItem execute(UUID itemId, UUID ownerUserId) {

        MenuItem item = menuItemRepository.findById(itemId);

        if (item == null) {
            throw new IllegalArgumentException("El platillo no existe");
        }

        Menu menu = menuRepository.findById(item.getMenuId());

        if (menu == null) {
            throw new IllegalArgumentException("El menú no existe");
        }

        if (!menu.getOwnerUserId().equals(ownerUserId)) {
            throw new IllegalArgumentException("No tienes permiso sobre este platillo");
        }

        return item;
    }
}