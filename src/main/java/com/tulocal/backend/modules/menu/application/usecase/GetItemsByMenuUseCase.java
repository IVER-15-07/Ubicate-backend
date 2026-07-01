package com.tulocal.backend.modules.menu.application.usecase;

import com.tulocal.backend.modules.menu.domain.model.MenuItem;
import com.tulocal.backend.modules.menu.domain.repository.MenuItemRepository;
import com.tulocal.backend.modules.menu.domain.repository.MenuRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetItemsByMenuUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    public List<MenuItem> execute(UUID menuId, UUID ownerUserId) {

        var menu = menuRepository.findById(menuId);
        if (menu == null)
            throw new IllegalArgumentException("El menú no existe");
        if (!menu.getOwnerUserId().equals(ownerUserId))
            throw new IllegalArgumentException("No tienes permiso sobre este menú");

        return menuItemRepository.findByMenuId(menuId);
    }
}